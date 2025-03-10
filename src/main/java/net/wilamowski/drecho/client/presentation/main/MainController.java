package net.wilamowski.drecho.client.presentation.main;

import atlantafx.base.controls.Popover;
import atlantafx.base.controls.Tile;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import lombok.ToString;
import net.wilamowski.drecho.app.auth.Session;
import net.wilamowski.drecho.client.application.infra.GeneralViewHandler;
import net.wilamowski.drecho.client.application.infra.ViewModelConfiguration;
import net.wilamowski.drecho.client.application.infra.controler_init.KeyEventDebugInitializer;
import net.wilamowski.drecho.client.presentation.customs.ImageViewUtil;
import net.wilamowski.drecho.client.presentation.customs.PopoverFactory;
import net.wilamowski.drecho.client.presentation.debugger.DebugHandler;
import net.wilamowski.drecho.client.presentation.debugger.KeyDebugHandlerGui;
import net.wilamowski.drecho.client.presentation.settings.SettingsViewModel;
import net.wilamowski.drecho.client.properties.ClientPropertyReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@ToString
public class MainController
    implements Initializable,
        KeyEventDebugInitializer         {
  private static final Logger logger = LogManager.getLogger(MainController.class);
  public static final String VIEW_PATIENT_SEARCHER = "patient/PatientsSearcherView.fxml";
  public static final String VIEW_PREFERENCE = "PreferenceView.fxml";
  public static final String VIEW_SETTINGS = "SettingsView.fxml";
  public static final String VIEW_VISIT_SEARCHER = "VisitSearcherView.fxml";
  public static final String VIEW_DICTIONARIES = "DictionariesView.fxml";
  public static final String VIEW_WELCOME = "WelcomeView.fxml";
  private final String loggedInUser;
  private final String sessionStartDateTime;
  // ViewModels
  private MainViewModel mainViewModel = null;
  private SettingsViewModel settingsViewModel = null;
  // BUNDLE
  private ResourceBundle bundle;
  @FXML private BorderPane root;
  @FXML private HBox borderTop;
  @FXML private Button settingsButton;
  @FXML private HBox borderCenter;
  @FXML private Pane borderBottom;
  @FXML private Pane borderLeft;
  @FXML private Accordion mainAccordion;
  @FXML private Pane borderRight;
  @FXML private Button saveButton;
  @FXML private ProgressIndicator progressIndicator;
  @FXML private HBox logoutBox;
  private GeneralViewHandler viewHandler;
  @FXML private Button quickVisitButton;
  private Stage owner;

  public MainController(ViewModelConfiguration viewModelConfiguration, GeneralViewHandler viewHandler) {
    this.viewHandler = viewHandler;
    this.mainViewModel = viewModelConfiguration.mainViewModel();
    this.settingsViewModel = viewModelConfiguration.settingsViewModel();
    Session instance = Session.instance();
    loggedInUser = instance.getUserLogin();
    LocalDateTime sessionStartAt = instance.sessionStartAt();
    String propertiesLanguage = ClientPropertyReader.getString("user.ui.language");
    Locale locale = new Locale.Builder().setLanguage(propertiesLanguage).build();
    sessionStartDateTime =
        sessionStartAt.format(DateTimeFormatter.ofPattern("d MMMM, HH:mm", locale));
  }

  @Override
  public void initialize(URL url, java.util.ResourceBundle resourceBundle) {
    logger.traceEntry();
    loadWelcomeScreen();
    passGlobalRootNode();
    setupOwner();
    setupLogoutTile();

    this.bundle = resourceBundle;
    quickVisitButton.requestFocus();
    logger.traceExit();
  }
  
  @FXML
  public void onActionOpenPatientsView(ActionEvent actionEvent) {
    logger.debug("Clicked on Patients...");
    Platform.runLater(
        () -> {
          viewHandler.switchSceneForParent(root, VIEW_PATIENT_SEARCHER );
        });
  }

  @FXML
  public void onActionOpenUserPreference(ActionEvent actionEvent) {
    logger.debug("Clicked on user preference...");
    viewHandler.switchSceneForParent(root, VIEW_PREFERENCE );
  }

  @FXML
  public void onActionOpenAdminSettings(ActionEvent actionEvent) {
    logger.debug("Clicked on admin settings...");
    Platform.runLater(
        () -> {
          viewHandler.switchSceneForParent(root, VIEW_SETTINGS );
        });
  }

  @FXML
  public void onActionOpenVisits(ActionEvent actionEvent) {
    logger.debug("Clicked on open Visits...");
    Platform.runLater(
        () -> {
          viewHandler.switchSceneForParent(root, VIEW_VISIT_SEARCHER );
        });
  }

  @FXML
  public void onActionOpenDictionaries(ActionEvent actionEvent) {
    logger.debug("Clicked on Dictionaries...");
    Platform.runLater(
        () -> {
          viewHandler.switchSceneForParent(root, VIEW_DICTIONARIES );
        });
  }

  @FXML
  void onMouseClickedNewExhamination(ActionEvent event) {
    logger.debug("Clicked on label new document...");
  }

  @FXML
  void onMouseClickedExhList(ActionEvent event) {
    logger.debug("Clicked on label list document...");
  }

  @FXML
  void onClickHomeScreen(MouseEvent event) {
    logger.debug("Click onClickHomeScreen...");
    loadWelcomeScreen();
  }

  public void loadWelcomeScreen() {
    logger.traceEntry();
    runWithIndicatior(
        new Task<>() {
          @Override
          protected Void call() throws Exception {
            viewHandler.switchSceneForParent(root, VIEW_WELCOME );
            return null;
          }
        });
    logger.traceExit();
  }

  private void runWithIndicatior(Task<Void> task) {
    progressIndicator.visibleProperty().bind(task.runningProperty());
    task.setOnRunning(event -> updateProgress(-1));
    task.setOnSucceeded(event -> updateProgress(1));
    task.setOnFailed(
        event -> {
          updateProgress(0);
        });
    new Thread(task).start();
  }

  private void updateProgress(double value) {
    progressIndicator.setProgress(value);
    if (value == 1) {
      try {
        TimeUnit.MILLISECONDS.sleep(500);
      } catch (InterruptedException e) {
        logger.debug(e.getMessage());
      } finally {
        progressIndicator.visibleProperty().unbind();
      }
    }
  }

  @FXML
  public void onActionPerformQuickVisit(ActionEvent event) {
    logger.debug("Click on quickVisit button.");
    runWithIndicatior(
        new Task<>() {
          @Override
          protected Void call() throws Exception {
//            viewHandler.switchSceneForParent(root, "QuickVisit");
            viewHandler.switchSceneForQuickVisit( root );
            return null;
          }
        });
  }

  @Override
  public void initializeKeyEventDebugging() {
    logger.traceEntry();
    DebugHandler debugHandler = new KeyDebugHandlerGui();
    debugHandler.initNode(root);
    debugHandler.watch(this);
    logger.traceExit();
  }

  private void passGlobalRootNode() {
    viewHandler.setMainView( root );
  }

  private void setupLogoutTile() {
    var logoutButton = new Button();
    logoutButton.setGraphic(ImageViewUtil.getImg("logout"));
    logoutButton.setOnAction(
        e -> {
          confirmLogout(logoutButton);
        });

    var logoutTile = new Tile("Logged as...,", loggedInUser + ", " + sessionStartDateTime);
    logoutTile.setAction(logoutButton);
    logoutTile.setActionHandler(
        new Runnable() {
          @Override
          public void run() {}
        });

    var avatar = ImageViewUtil.getImg("badge");
    avatar.setFitWidth(64);
    avatar.setFitHeight(64);
    logoutTile.setGraphic(avatar);
    logoutBox.getChildren().add(logoutTile);
  }

  private void confirmLogout(Button logoutButton) {
    var lvmPopover =
        PopoverFactory.createActionPopover(
            "Logout",
            "Are you sure you want to logout?",
            "Yes, proceed",
            "Click here to logout",
            popoverButtonEvent -> {
              viewHandler.openLogin();
              owner.close();
            },
            Popover.ArrowLocation.RIGHT_TOP);
    lvmPopover.show(logoutButton);
  }

  private void setupOwner() {
    Platform.runLater(() -> {
      owner = (Stage) root.getScene().getWindow();
    });
  }

}
