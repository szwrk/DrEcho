package net.wilamowski.drecho.client.application.infra;

import static javafx.application.Application.STYLESHEET_MODENA;

import atlantafx.base.util.Animations;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
import net.wilamowski.drecho.client.ApplicationRoot;
import net.wilamowski.drecho.client.presentation.customs.modals.ExceptionAlert;
import net.wilamowski.drecho.client.presentation.login.LoginController;
import net.wilamowski.drecho.client.properties.ClientPropertyReader;
import net.wilamowski.drecho.shared.bundle.Lang;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Arkadiusz Wilamowski
 * <p></><a href="https://github.com/szwrk">GitHub</a></p>
 * <p> For questions or inquiries, at contact arek@wilamowski.net </p>
 */
@ToString
public class GeneralViewHandler {
  public static int ANIMATION_CHANGE_SCANE_DURATION = 200;
  private static final Logger logger = LogManager.getLogger(GeneralViewHandler.class);
  private static final int WIDTH = 1920;
  private static ControllerInitializer controllerInitializer;
  private final ApplicationRoot root;
  private String applicationStyle;

  private GeneralViewHandler(
      String styleName, ViewModels viewModels, ApplicationRoot applicationRoot) {
    ANIMATION_CHANGE_SCANE_DURATION = ClientPropertyReader.getInt( "admin.switch-scene.duration" );
    this.root = applicationRoot;
    this.initGlobalStyle(styleName);
    controllerInitializer = new ControllerInitializerImpl(viewModels);
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
      String styleName, ViewModels viewModels, ApplicationRoot applicationRoot) {
    return new GeneralViewHandler(styleName, viewModels, applicationRoot);
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

  public static FXMLLoader createFxmlLoader(String subView) {
    FXMLLoader loader = new FXMLLoader(GeneralViewHandler.class.getResource(subView));
    loader.setResources(Lang.getBundle());
    return loader;
  }

  public <T extends Parent> Object switchSceneForParent(T container, String viewToOpen) {
    Objects.requireNonNull(container);
    Objects.requireNonNull(viewToOpen);

    logger.debug("Switch scene for parent, view name: {} ", viewToOpen);
    FXMLLoader loader = new FXMLLoader();
    Parent newNode = loadFxml(viewToOpen, loader);
    Objects.requireNonNull(newNode, "Fxml loading error. Node is null");

    Timeline timeline =
        Animations.fadeIn(newNode, Duration.millis(ANIMATION_CHANGE_SCANE_DURATION));
    embedNodeInContainer(container, newNode);
    applyCurrentStyleForParent(newNode);
    Object controller = loader.getController();
    initializeController(controller);
    timeline.play();
    return controller;
  }

  private void initializeController(Object controller) {
    controllerInitializer.initControllers(controller, this);
  }

  private Parent loadFxml(String viewName, FXMLLoader loader) {
    Parent parent = null;
    String path = "/views/" + viewName + "View.fxml";
    logger.debug("Load fxml with path: {} ", path);

    URL resourceUrl = getClass().getResource(path);
    if (resourceUrl == null) {
      String s = "FXML resource not found: " + path;
      logger.error(s);
      throw new IllegalStateException(s);
    }
    loader.setLocation(resourceUrl);
    loader.setResources(Lang.getBundle());

    try {
      parent = loader.load();
    } catch (IOException ioException) {
      logger.error("View with that name does not exist: {}", path);
      logger.error("An error occurred:", ioException.getMessage(), ioException);
      String header = Lang.getString("e.001.header");
      String msg = Lang.getString("e.001.msg");
      ExceptionAlert.create().showError(ioException, header, String.format(msg, path));
    } catch (IllegalArgumentException illegalArgumentException) {
      logger.error("View with that name does not exist: {}", path);
      logger.error(
          "An error occurred:", illegalArgumentException.getMessage(), illegalArgumentException);
      String header = Lang.getString("e.010.header");
      String msg = Lang.getString("e.010.msg");
      ExceptionAlert.create().showError(illegalArgumentException, header, msg);
    } catch (IllegalStateException illegalStateException) {
      logger.error("An error occurred:", illegalStateException.getMessage(), illegalStateException);
      String header = Lang.getString("e.007.header");
      String msg = Lang.getString("e.007.msg");
      ExceptionAlert.create().showError(illegalStateException, header, String.format(msg, path));
      throw new RuntimeException(illegalStateException);
    } catch (Exception unexceptedException) {
      handleError(unexceptedException, "e.999.header", "e.999.msg");
    }
    Objects.requireNonNull(parent, "View loading failed! Check fxml with name: " + viewName);
    return parent;
  }

  private void handleError(Exception e, String headerKey, String contentKey) {
    logger.error("An error occurred: ", e.getMessage(), e);
    String header = Lang.getString(headerKey);
    String msg = Lang.getString(contentKey);
    ExceptionAlert.create().showError(e, header, msg);
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
    } else {
      throw new NotImplementedException("Unhandled parent type " + container.getClass());
    }
  }

  public ApplicationRoot getRoot() {
    return root;
  }

  public void openLogin() {
    logger.info("Login window lauching...");
    try {
      Stage newStage = new Stage();
      LoginController controller = (LoginController) switchSceneForStage("Login", newStage);
      newStage.show();
    } catch (IllegalStateException e) {
      handleError(e, "e.001.header", "e.001.msg");
    } catch (Exception e) {
      handleError(e, "e.999.header", "e.999.msg");
    }
  }

  public Object switchSceneForStage(String viewToOpenAsStage, Stage stage) {
    try {
      return switchScene(viewToOpenAsStage, stage);
    } catch (IllegalStateException stateException) {
      logger.error("An error occurred:", stateException.getMessage(), stateException);
      String header = Lang.getString("e.011.header");
      String msg = Lang.getString("e.011.msg");
      ExceptionAlert.create()
          .showError(stateException, header, String.format(msg, viewToOpenAsStage));
      return null;
    } catch (IllegalArgumentException illegalArgumentException) {
      handleError(illegalArgumentException, "e.999.header", "e.999.msg");
      return null;
    } catch (Exception unexceptedException) {
      handleError(unexceptedException, "e.999.header", "e.999.msg");
      return null;
    }
  }

  private Object switchScene(String viewToOpenAsStage, Stage newStage) {
    Objects.requireNonNull(viewToOpenAsStage);
    Objects.requireNonNull(newStage);
    logger.debug("Switch scene for stage, view name: {} ", viewToOpenAsStage);

    FXMLLoader loader = new FXMLLoader();
    Parent parent = loadFxml(viewToOpenAsStage, loader);

    if (parent == null) {
      String errorMsg =
          "Failed to load FXML. Please check if the view file name is correct: "
              + viewToOpenAsStage;
      logger.error(errorMsg);
      throw new IllegalStateException(errorMsg);
    }
    Scene scene = new Scene(parent);

    newStage.setTitle(viewToOpenAsStage);
    newStage.setScene(scene);
    applyCurrentStyleForParent(parent);

    Object controller = loader.getController();
    Objects.requireNonNull(controller);
    initializeController(controller);
    return controller;
  }

  public void openMain() {
    try {
      Stage newStage = new Stage();
      switchSceneForStage("Main", newStage);
      GeneralViewHandler.setupAsFullScreenModal(newStage);
      newStage.show();
    } catch (IllegalStateException e) {
      handleError(e, "e.001.header", "e.001.msg");
    } catch (Exception e) {
      handleError(e, "e.999.header", "e.999.msg");
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
}
