package net.wilamowski.drecho.client.presentation.login;

import atlantafx.base.theme.Styles;
import java.net.URL;
import java.util.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.StringConverter;
import net.wilamowski.drecho.client.ApplicationRoot;
import net.wilamowski.drecho.client.application.infra.GeneralViewHandler;
import net.wilamowski.drecho.client.application.infra.ViewModelConfiguration;
import net.wilamowski.drecho.client.application.infra.ViewModelsInitializer;
import net.wilamowski.drecho.client.application.infra.controler_init.PostInitializable;
import net.wilamowski.drecho.client.presentation.customs.modals.UserAlert;
import net.wilamowski.drecho.client.presentation.main.ViewHandlerInitializer;
import net.wilamowski.drecho.gateway.infra.DeploymentType;
import net.wilamowski.drecho.app.auth.AuthenticationResults;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoginController
    implements Initializable, ViewHandlerInitializer, ViewModelsInitializer, PostInitializable {
  private static final Logger logger = LogManager.getLogger(LoginController.class);
  private final Tooltip loginTooltip =
      new Tooltip("Znaki zostaly automatycznie powiekszone. Login wpisujemy wielkimi literami!");
  private final List<Scene> scenes = new ArrayList<>();
  @FXML private ImageView art;
  @FXML private TextField loginField;
  @FXML private PasswordField passwordField;
  @FXML private Pane rootPane;
  @FXML private Label loginInfo;
  @FXML private Label passwordInfo;
  private Stage loginStage;
  private LoginViewModel loginViewModel;
  private Point2D loginPoint;
  private ApplicationRoot applicationRoot;
  private GeneralViewHandler viewHandler;

  public LoginController() {
    logger.debug("New LoginController");
  }

  @FXML
  void setOnActionLogin(ActionEvent event) {
    logger.debug("[CONTROLLER] Clicked on login button...");
    Platform.runLater(
        () -> {
          login();
        });
  }

  private void login() {
    try {
      if (loginViewModel.validateLoginInput()) {
        AuthenticationResults authenticationResults = loginViewModel.performAuthenticate();
        if (authenticationResults.isAuthenticated()) {
          handleValidCredentials();
        }
      } else {
        handleInvalidCredentials();
      }
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
    }
  }

  private void handleInvalidCredentials() {
    logger.warn("Invalid user credentials!");
    boolean emptyLogin = loginField.getText().isEmpty();
    boolean emptyPassword = passwordField.getText().isEmpty();

    if (emptyLogin && emptyPassword) {
      highlightInvalidInput(loginField);
      highlightInvalidInput(passwordField);
      UserAlert
              .simpleInfo( "Empty Credentials","Please provide both login and password data."  )
              .showAndWait();
    } else {
      logger.debug("Authentication failed. Please check your credentials.");
    }
  }

  private void highlightInvalidInput(TextField textField) {
    textField.pseudoClassStateChanged(Styles.STATE_DANGER, true);
  }

  private void handleValidCredentials() {
    logger.debug("[CONTROLLER] User is authenticated. Context up...");
    try {
      getStageByRoot();
      loginStage.close();
      viewHandler.openMain();
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
      throw new RuntimeException(e);
    }
  }

  public void getStageByRoot() {
    loginStage = (Stage) rootPane.getScene().getWindow();
  }

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    logger.debug("Initializing LoginController...");
  }

  @FXML
  void onClickOpenSettings(MouseEvent event) {
    logger.debug("Clicked on Settings...");
    showAppSettings();
  }

  private void showAppSettings() {
    logger.debug("Open settings");
    //        List<String> backendValues =
    // EnumUtils.getEnumList(BackendType.class).stream().map(BackendType::name).collect(Collectors.toList());
    List<DeploymentType> deploymentTypes = Arrays.asList(DeploymentType.values());
    ChoiceBox<DeploymentType> backendTypeChoiceBox = new ChoiceBox<>();
    backendTypeChoiceBox.getItems().addAll(deploymentTypes);
    backendTypeChoiceBox.setConverter(
        new StringConverter<DeploymentType>() {
          @Override
          public String toString(DeploymentType deploymentType) {
            return deploymentType.getLabel() + " (" + deploymentType.getDescription() + ")";
          }

          @Override
          public DeploymentType fromString(String s) {
            return DeploymentType.valueOf(s);
          }
        });
  }

  @Override
  public void initializeViewHandler(GeneralViewHandler viewHandler) {
    this.viewHandler = viewHandler;
  }

  @Override
  public void initializeViewModels(ViewModelConfiguration factory) {
    loginViewModel = factory.loginViewModel();
  }

  @Override
  public void postInitialize() {
    bindingFieldWithProperty();
    initTooltip();
    convertLoginToUpperCaseIfContainsLowerChars();
    emptyFieldListener();
  }

  private void emptyFieldListener() {
    loginField
        .textProperty()
        .addListener(
            (obs, oldv, newv) -> {
              loginField.pseudoClassStateChanged(Styles.STATE_DANGER, newv.isEmpty());
            });
    passwordField
        .textProperty()
        .addListener(
            (obs, oldv, newv) -> {
              passwordField.pseudoClassStateChanged(Styles.STATE_DANGER, newv.isEmpty());
            });
  }

  private void bindingFieldWithProperty() {
    loginField.textProperty().bindBidirectional(loginViewModel.loginTextProperty());
    passwordField.textProperty().bindBidirectional(loginViewModel.passwordProperty());
    loginInfo.textProperty().bind(loginViewModel.loginMsgProperty());
    passwordInfo.textProperty().bind(loginViewModel.passwordMsgProperty());
  }

  private void initTooltip() {
    loginTooltip.setAutoHide(true);
    loginTooltip.setShowDelay(Duration.seconds(3));
    loginField.setTooltip(loginTooltip);
    loginPoint = loginField.localToScene(0.0, 0.0);
  }

  private void convertLoginToUpperCaseIfContainsLowerChars() {
    loginField
        .textProperty()
        .addListener(
            (observable, oldValue, newValue) -> {
              if (newValue.isBlank()) {
                loginTooltip.hide();
              }
              if (containsAnyLowerChar(newValue)) {
                loginField.setText(newValue.toUpperCase());
                loginTooltip.show(loginField, calculateX(), calculateY());
              }
            });
  }

  private static boolean containsAnyLowerChar(String text) {
    if (text == null) {
      return false;
    }

    for (char ch : text.toCharArray()) {
      if (Character.isLowerCase(ch)) {
        return true;
      }
    }

    return false;
  }

  private double calculateY() {
    return loginPoint.getY()
        + loginField.getScene().getY()
        + loginField.getScene().getWindow().getY()
        + loginField.getHeight()
        + 2;
  }

  private double calculateX() {
    return loginPoint.getX()
        + loginField.getScene().getX()
        + loginField.getScene().getWindow().getX();
  }
}
