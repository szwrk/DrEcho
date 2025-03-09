package net.wilamowski.drecho.client.application.infra;

import static javafx.application.Application.STYLESHEET_MODENA;

import atlantafx.base.util.Animations;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.fxml.LoadException;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TitledPane;
import javafx.scene.effect.BoxBlur;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import lombok.ToString;
import net.wilamowski.drecho.ModalWrapper;
import net.wilamowski.drecho.app.bundle.Lang;
import net.wilamowski.drecho.client.ApplicationRoot;
import net.wilamowski.drecho.client.presentation.complex.quickvisit.QuickVisitController;
import net.wilamowski.drecho.client.presentation.customs.modals.ExceptionAlert;
import net.wilamowski.drecho.client.presentation.patients.*;
import net.wilamowski.drecho.client.properties.ClientPropertyReader;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Arkadiusz Wilamowski
 *     <p></><a href="https://github.com/szwrk">GitHub</a>
 *     <p>For questions or inquiries, at contact arek@wilamowski.net
 */
@ToString
public class GeneralViewHandler {
  public static final String VIEW_PATIENT_SEARCHER = "patient/PatientsSearcherView.fxml";
  public static final String VIEW_PATIENT_DETAILS = "patient/PatientRegisterView.fxml";
  public static final String VIEW_MAIN = "MainView.fxml";
  public static final String VIEW_QUICK_VISIT = "QuickVisit.fxml";
  public static final String VIEW_LOGIN = "LoginView.fxml";
  private static final Logger logger = LogManager.getLogger(GeneralViewHandler.class);
  private static final int WIDTH = 1920;
  public static int ANIMATION_CHANGE_SCANE_DURATION = 200;
  private static ControllerInitializer controllerInitializer;
  private final ApplicationRoot root;
  private final ViewModelConfiguration viewModelConfiguration;
  private String applicationStyle;
  private BorderPane mainView;
  private ControllerFactory controllerFactory;
  private GeneralViewHandler(
          String styleName, ViewModelConfiguration viewModelConfiguration , ApplicationRoot applicationRoot) {
    ANIMATION_CHANGE_SCANE_DURATION = ClientPropertyReader.getInt("admin.switch-scene.duration");
    this.root = applicationRoot;
    this.initGlobalStyle(styleName);
    controllerInitializer = new ControllerInitializerImpl( viewModelConfiguration );
    this.viewModelConfiguration = viewModelConfiguration;
    this.controllerFactory = new ControllerFactory( viewModelConfiguration , this );
  }

  private void initGlobalStyle(String styleName) {
    String passedStyleName = styleName.toLowerCase();
    String styles = ClientPropertyReader.getString("admin.style.list");
    List<String> listOfValue = Arrays.asList(styles.split(","));

    if (!listOfValue.contains(passedStyleName)) {
      logger.warn(
          "Wrong configuration. Style name does not exists on style list properties. Set default style.");
      setBuiltInStyle(STYLESHEET_MODENA);
      return;
    }

    if (isBuitlInStyle(passedStyleName)) {
      setBuiltInStyle(passedStyleName);
    } else {
      setCssStyle(passedStyleName);
    }
  }

  private static boolean isBuitlInStyle(String passedStyle) {
    return passedStyle.equals("modena") || passedStyle.equals("kaspian");
  }

  private static void setBuiltInStyle(String javaFxStyleConstant) {
    logger.info("Style: {}", javaFxStyleConstant);
    ApplicationRoot.setUserAgentStylesheet(null);
    ApplicationRoot.setUserAgentStylesheet(javaFxStyleConstant);
  }

  private void setCssStyle(String styleName) {
    logger.info("Apply style: {}", styleName);
    String format = ClientPropertyReader.getString("admin.style.path-format");
    String path = String.format(format, styleName);
    applicationStyle = path;
    ApplicationRoot.setUserAgentStylesheet(null);
    ApplicationRoot.setUserAgentStylesheet(path);
    logger.debug("Current path stylesheet: {}", applicationStyle);
  }

  public static GeneralViewHandler instance(
          String styleName, ViewModelConfiguration viewModelConfiguration , ApplicationRoot applicationRoot) {
    return new GeneralViewHandler(styleName, viewModelConfiguration , applicationRoot);
  }

  public static ControllerInitializer initializer() {
    return controllerInitializer;
  }

  /**
   * Sets up the provided Stage as a simple modal window. Simple modals are autonomous windows like
   * login windows without a parent.
   *
   * @param modalStage The Stage to be set up as a simple modal window.
   */
  public static void setupAsSimpleModal(Stage modalStage) {
    modalStage.addEventFilter(
        KeyEvent.KEY_PRESSED,
        event -> {
          if (event.getCode() == KeyCode.ESCAPE) {
            modalStage.close();
          }
        });
    modalStage.setFullScreenExitKeyCombination(new KeyCodeCombination(KeyCode.ESCAPE));
    modalStage.initModality(Modality.APPLICATION_MODAL);
    modalStage.initStyle(StageStyle.UNDECORATED);
    modalStage.setMaximized(false);
  }

  public <T extends Parent> Object switchSceneForParent(String viewToOpen) {
    BorderPane root = getMainView( );
    Objects.requireNonNull(root,"Main view root node is not set");
    Objects.requireNonNull(viewToOpen);

    logger.debug("Switch scene for parent, view name: {} ", viewToOpen);
    FXMLLoader loader = instanceLoaderFxml( );
    Parent newNode = loaderLoad( setupLoader( viewToOpen , loader ) );
    Objects.requireNonNull(newNode, "Fxml loading error. Node is null");

    Timeline timeline =
            Animations.fadeIn(newNode, Duration.millis(ANIMATION_CHANGE_SCANE_DURATION));
    embedNodeInContainer(root, newNode);
    applyCurrentStyleForParent(newNode);
    Object controller = loader.getController();
    timeline.play();
    return controller;
  }
  
//  public FXMLLoader loaderFxmlInstance(String subView) {
//    FXMLLoader loader = new FXMLLoader(GeneralViewHandler.class.getResource(subView));
//    loader.setControllerFactory( controllerFactory );
//    loader.setResources(Lang.getBundle());
//
//    return loader;
//  }

  private FXMLLoader instanceLoaderFxml() {
    final FXMLLoader loader= new FXMLLoader( );
    loader.setControllerFactory(controllerFactory);
    return loader;
  }

  public BorderPane getMainView() {
    return mainView;
  }

  public void setMainView(BorderPane mainView) {
    this.mainView = mainView;
  }

  private FXMLLoader setupLoader(String viewName, FXMLLoader loader) {
    Parent parent = null;
    String path = "/views/" + viewName;
    logger.debug("Load fxml with path: {} ", path);

    URL resourceUrl = getClass().getResource(path);
    if (resourceUrl == null) {
      String s = "FXML resource not found: " + path;
      logger.error(s);
      throw new IllegalStateException(s);
    }
    loader.setLocation(resourceUrl);
    loader.setResources(Lang.getBundle());
    return loader;
  }

  private void applyCurrentStyleForParent(Parent newNode) {
    if (applicationStyle != null) {
      URL resource = getClass().getResource(applicationStyle);
      String styleUrl = resource.toExternalForm();
      if (styleUrl != null) {
        newNode.getStylesheets().clear();
        newNode.getStylesheets().add(styleUrl);
      } else {
        logger.warn("Failed to get resource URL for style: {}", applicationStyle);
      }
    } else {
      logger.warn("Global style is not initialized");
    }
  }

  private <T extends Parent> void embedNodeInContainer(T container, Parent parent)
      throws NotImplementedException {
    if (container instanceof BorderPane) {
      Platform.runLater(() -> ((BorderPane) container).setCenter(parent));
    } else if (container instanceof VBox) {
      Platform.runLater(() -> ((VBox) container).getChildren().add(parent));
    } else if (container instanceof TitledPane ) {
      Platform.runLater(() -> ((TitledPane) container).setContent(null));
      Platform.runLater(() -> ((TitledPane) container).setContent(parent));
    } else {
      logger.warn( "Unhandled parent type " + container.getClass() );
    }
  }

  private Parent loaderLoad(FXMLLoader l) {
    Parent parent = null;
    try {
      parent =  l.load();
    } catch (LoadException e) {
      showLocalizedError(e, "e.018.header", "e.018.msg", "-");
    } catch (IOException e) {
      showLocalizedError(e, "e.001.header", "e.001.msg", "-");
    } catch (IllegalArgumentException e) {
      showLocalizedError(e, "e.010.header", "e.010.msg", "-");
    } catch (IllegalStateException e) {
      throw new RuntimeException(e);
    } catch (Exception e) {
      showLocalizedError(e, "e.999.header", "e.999.msg", "-");
    }
      return parent;
  }

  private void showLocalizedError(Exception e, String headerKey, String contentKey, String path) {
    logger.error("An error occurred while loading FXML view '{}': {}", path, e.getMessage(), e);
    String header = Lang.getString(headerKey);
    String msg = Lang.getString(contentKey);
    ExceptionAlert.create().showError(e, header, msg);
  }

  public <T extends Parent> Object switchSceneForParent(T container, String viewToOpen) {
    Objects.requireNonNull(container);
    Objects.requireNonNull(viewToOpen);

    logger.debug("Switch scene for parent, view name: {} ", viewToOpen);
    FXMLLoader loader = instanceLoaderFxml( );
    Parent newNode = loaderLoad( setupLoader( viewToOpen , loader ) );
    Objects.requireNonNull(newNode, "Fxml loading error. Node is null");

    Timeline timeline =
        Animations.fadeIn(newNode, Duration.millis(ANIMATION_CHANGE_SCANE_DURATION));
    embedNodeInContainer(container, newNode);
    applyCurrentStyleForParent(newNode);
    Object controller = loader.getController();
    timeline.play();
    return controller;
  }

  public <T extends  Parent> Object switchSceneForQuickVisit(T container) {
    Objects.requireNonNull(container, "Container cannot be null");

    logger.debug("Switch scene for QuickVisit view");
    FXMLLoader loader = instanceLoaderFxml( );
    loader.setControllerFactory(controllerFactory);

    Parent newNode;
    try {
      newNode = loader.load();
    } catch (IOException e) {
      logger.error("Failed to load QuickVisit.fxml", e);
      throw new RuntimeException("Failed to load QuickVisit.fxml", e);
    }

    QuickVisitController quickVisitController = loader.getController();

    loader.getNamespace().put("visitController", quickVisitController.getVisitController());
    loader.getNamespace().put("patientController", quickVisitController.getPatientController());
    loader.getNamespace().put("examinationController", quickVisitController.getExaminationController());
    loader.getNamespace().put("notesController", quickVisitController.getNotesController());

    embedNodeInContainer(container, newNode);
    applyCurrentStyleForParent(newNode);

    return quickVisitController;
  }

  public ApplicationRoot getRoot() {
    return root;
  }

  public void openLogin() {
    logger.info("Login window lauching...");
    try {
      Stage           newStage   = new Stage();
      switchSceneForStage( VIEW_LOGIN , newStage);

      newStage.show();
    } catch (IllegalStateException e) {
      showLocalizedError(e, "e.001.header", "e.001.msg","-");
    } catch (Exception e) {
      showLocalizedError(e, "e.999.header", "e.999.msg","-");
}
  }

  public Object switchSceneForStage(String viewToOpen, Stage stage) {
    try {
      Objects.requireNonNull(viewToOpen);
      Objects.requireNonNull(stage);
      logger.debug("Switch scene for stage, view name: {} ", viewToOpen);
      FXMLLoader loader = instanceLoaderFxml( );
      Parent parent = loaderLoad( setupLoader( viewToOpen , loader ) );

      if (parent == null) {
        showLocalizedError(
                new IllegalStateException("Failed to load FXML"), "e.001.header", "e.001.msg", viewToOpen);
        return null;
      }
      Scene scene = new Scene(parent);

      stage.setTitle(viewToOpen);
      stage.setScene(scene);
      applyCurrentStyleForParent(parent);

      Object controller = loader.getController();
//      Objects.requireNonNull(controller);
      return controller;
    } catch (IllegalStateException stateException) {
      showLocalizedError(stateException, "e.011.header","e.011.msg", viewToOpen);
      return null;
    } catch (IllegalArgumentException illegalArgumentException) {
      showLocalizedError(illegalArgumentException, "e.999.header", "e.999.msg",viewToOpen);
      return null;
    } catch (Exception unexceptedException) {
      showLocalizedError(unexceptedException, "e.999.header", "e.999.msg",viewToOpen);
      return null;
    }
  }

  //    return includedController.getPatientSearcherViewModel().getSelectedPatient();
  public PatientVM openModalPatientChooser(String par) {
    FXMLLoader loader = instanceLoaderFxml();
    FXMLLoader setupedLoader = setupLoader(VIEW_PATIENT_SEARCHER, loader);
    Parent parent = loaderLoad(setupedLoader);

    PatientsSearcherController controller = setupedLoader.getController();
    controller.inititalizeSearchValue(par);

    ModalWrapper<PatientVM> modal = new ModalWrapper<>(parent,
            () -> controller.getPatientSearcherViewModel().getSelectedPatient()
    );

//    GeneralViewHandler.setupAsBlurModal(modal, owner);
//    GeneralViewHandler.setupStageTitle(patientModal, "New patient registration");

    Optional<PatientVM> result = modal.showAndWait();
    return result.orElse(null);
  }

  public PatientsSearcherController openNewPatientView(Stage owner){
  Stage patientModal = new Stage();

  PatientsSearcherController patientRegisterController = //todo factory methods
          (PatientsSearcherController)
                  this.switchSceneForStage( VIEW_PATIENT_SEARCHER , patientModal);

  PatientSearcherViewModel viewModel = patientRegisterController.getPatientSearcherViewModel();
//  patientRegisterController.bindControlsWithCurrentPatient();
//  viewModel.enableWriteMode();
//  patientRegisterController.setTitle("Add patient");
  GeneralViewHandler.setupAsBlurModal(patientModal, owner);
  GeneralViewHandler.setupStageTitle(patientModal, "New patient registration");
  patientModal.showAndWait();
return patientRegisterController;
}

  /**
   * Sets up the provided Stage as a blur modal window. Blur modals are used for popups, modal
   * dialogs, and business logic with a blurred background effect.
   *
   * @param modalStage The Stage to be set up as a blur modal window.
   * @param ownerStage The owner Stage of the modal, used for blurring the background.
   */
  public static void setupAsBlurModal(Stage modalStage, Stage ownerStage) {
    modalStage.addEventFilter(
        KeyEvent.KEY_PRESSED,
        event -> {
          if (event.getCode() == KeyCode.ESCAPE) {
            modalStage.close();
            GeneralViewHandler.disableBlur(ownerStage);
          }
        });

    modalStage.setOnCloseRequest(
        e -> {
          ownerStage.getScene().getRoot().setEffect(null);
        });
    modalStage.setFullScreenExitKeyCombination(new KeyCodeCombination(KeyCode.ESCAPE));
    modalStage.initModality(Modality.APPLICATION_MODAL);
    modalStage.initStyle(StageStyle.UNDECORATED);
    modalStage.setMaximized(false);
    modalStage.initOwner(ownerStage);

    BoxBlur blur = new BoxBlur(10, 10, 3);
    ownerStage.getScene().getRoot().setEffect(blur);
  }

  public static void disableBlur(Stage ownerStage) {
    ownerStage.getScene().getRoot().setEffect(null);
  }

  public static void setupStageTitle(Stage stage, String name) {
    stage.setTitle(name);
  }
  
  public void openPatientReadOnlyView(Stage owner , PatientVM selectedPatient) {
    Stage patientModal = new Stage();

    PatientRegisterController patientRegisterController = //todo factory methods
            (PatientRegisterController)
                    this.switchSceneForStage( VIEW_PATIENT_DETAILS , patientModal);
    PatientRegisterViewModel viewModel = patientRegisterController.getViewModel();
    viewModel.selectPatientForEdit(selectedPatient);
    patientRegisterController.bindControlsWithCurrentPatient();
    patientRegisterController.blockFields();
    patientRegisterController.setTitle("Preview patient details");
    this.setupAsBlurModal(patientModal, owner);
    this.setupStageTitle(patientModal, "Preview patient details");
    patientModal.showAndWait();
    GeneralViewHandler.disableBlur(owner);
  }

  public void openPatientEditView(Stage owner , PatientVM selectedPatient) {
    Stage modal = new Stage();

    PatientRegisterController patientRegisterController =
            (PatientRegisterController)
                    this.switchSceneForStage(VIEW_PATIENT_DETAILS, modal);
    PatientRegisterViewModel viewModel = patientRegisterController.getViewModel();
    viewModel.selectPatientForEdit(selectedPatient);
    viewModel.turnOnEditingPatientMode();
    patientRegisterController.bindControlsWithCurrentPatient();
    patientRegisterController.setTitle("Edit patient");
    this.setupAsBlurModal(modal, owner);
    this.setupStageTitle(modal, "Edit patient");
    modal.showAndWait();
    GeneralViewHandler.disableBlur(owner);
  }

  public void openPatientAddView(Stage owner) {
    Stage modal = new Stage();

    PatientRegisterController patientRegisterController =
            (PatientRegisterController)
                    this.switchSceneForStage(VIEW_PATIENT_DETAILS, modal);
    PatientRegisterViewModel viewModel = patientRegisterController.getViewModel();
    patientRegisterController.setTitle("New patient");
    viewModel.turnOnInsertMode();
    this.setupAsBlurModal(modal, owner);
    this.setupStageTitle(modal, "New patient");
    modal.showAndWait();
    GeneralViewHandler.disableBlur(owner);
  }

  public void openMain() {
    try {
      Stage newStage = new Stage();
      switchSceneForStage( VIEW_MAIN , newStage);
      GeneralViewHandler.setupAsFullScreenModal(newStage);
      newStage.show();
    } catch (IllegalStateException e) {
      showLocalizedError(e, "e.001.header", "e.001.msg","-");
    } catch (Exception e) {
      showLocalizedError(e, "e.999.header", "e.999.msg","-");
    }
  }

  public static void setupAsFullScreenModal(Stage stage) {
    stage.setMaximized(true);
    stage.initModality(Modality.APPLICATION_MODAL);
    stage.setWidth(WIDTH);
    stage.setHeight(calculateAvailableScreenHeight());
    stage.setFullScreenExitKeyCombination(new KeyCodeCombination(KeyCode.ESCAPE));
    stage.fullScreenExitHintProperty().setValue("Press ESC to exit full screen");
  }

  private static double calculateAvailableScreenHeight() {
    Screen screen = Screen.getPrimary();
    Rectangle2D bounds = screen.getVisualBounds();
    double taskbarHeight = bounds.getHeight() - screen.getBounds().getHeight();
    return bounds.getHeight() - taskbarHeight;
  }

//  public Parent loadNestedView(String subView) {
//    FXMLLoader loader = instanceLoaderFxml( );
//    return setupLoader(subView, loader);
//   }
}
