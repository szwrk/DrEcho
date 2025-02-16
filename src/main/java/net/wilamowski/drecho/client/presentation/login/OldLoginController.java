package net.wilamowski.drecho.client.presentation.login;

import atlantafx.base.theme.Styles;
import java.net.URL;
import java.util.*;
import javafx.application.Platform;
import javafx.beans.property.*;
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
import net.wilamowski.drecho.app.auth.AuthenticationResults;
import net.wilamowski.drecho.app.bundle.Lang;
import net.wilamowski.drecho.client.ApplicationRoot;
import net.wilamowski.drecho.client.application.infra.GeneralViewHandler;
import net.wilamowski.drecho.client.presentation.customs.modals.UserAlert;
import net.wilamowski.drecho.client.properties.ClientPropertyReader;
import net.wilamowski.drecho.configuration.BackendType;
import net.wilamowski.drecho.standalone.service.authenticator.Credentials;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Deprecated
public class OldLoginController
    implements Initializable {
//
  public static final String PROPERTY_BACKEND_CONNECT_MODE = "admin.backend-connect-mode";
  private static final Logger logger = LogManager.getLogger( OldLoginController.class);
  private final Tooltip loginTooltip =
      new Tooltip("Znaki zostaly automatycznie powiekszone. Login wpisujemy wielkimi literami!");
  private final List<Scene> scenes = new ArrayList<>();
  private final LoginViewModel loginViewModel;

  @FXML private ImageView art;
  @FXML private TextField loginField;
  @FXML private PasswordField passwordField;
  @FXML private Pane rootPane;
  @FXML private Label loginInfo;
  @FXML private Label passwordInfo;
  private Stage loginStage;
  private Point2D loginPoint;
  private ApplicationRoot applicationRoot;
  private GeneralViewHandler viewHandler;
  private ObjectProperty<BackendType> deployMode;
  private StringProperty styleMode;
  private StringProperty login;
  private StringProperty password;
  private StringProperty loginMsg;
  private StringProperty passwordMsg;
  private BooleanProperty isCapsLockOn;


//  @Inject
  public OldLoginController(LoginViewModel loginViewModel, GeneralViewHandler viewHandler) {
    this.viewHandler = viewHandler;
    logger.debug("New LoginController");
    this.loginViewModel = loginViewModel;
    this.styleMode = new SimpleStringProperty("");
    this.login = new SimpleStringProperty("");
    this.password = new SimpleStringProperty("");
    this.loginMsg = new SimpleStringProperty("");
    this.passwordMsg = new SimpleStringProperty("");
    this.isCapsLockOn = new SimpleBooleanProperty();
    this.deployMode = new SimpleObjectProperty<>();
    initBackendMode();
  }

  private void initBackendMode() {
    BackendType backendConnectModeInitValue = null;
    backendConnectModeInitValue =
            BackendType.of( ClientPropertyReader.getString(PROPERTY_BACKEND_CONNECT_MODE));
    deployModeProperty().set(backendConnectModeInitValue);
  }

  public ObjectProperty<BackendType> deployModeProperty() {
    return deployMode;
  }

  @FXML
  public void setOnActionLogin(ActionEvent event) {
    logger.debug("[CONTROLLER] Clicked on login button...");
    Platform.runLater(
        () -> {
          login();
        });
  }

  private void login() {
    try {
      logger.debug("Is not null...?");
      if (notNullCheckUi()) {
        logger.debug("Not null check = PASS, PERFORM AUTH...");
        AuthenticationResults authenticationResults = performAuthenticate();
        if (authenticationResults.isAuthenticated()) {
          logger.debug("IS AUTH. HANDLE VALID CREDS");
          handleValidCredentials();
        }
      } else {
        handleInvalidCredentials();
      }
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
    }
  }

  boolean notNullCheckUi() {
    setLoginNotify(null);
    setPasswordNotify(null);

    String login = getLogin();
    String password = getPassword();

    if (login == null || login.isBlank()) {
      setLoginNotify( Lang.getString("ui.login.validation.empty-login"));
    }

    if (password == null || password.isBlank()) {
      setPasswordNotify(Lang.getString("ui.login.validation.empty-password"));
    }
    return isLoginAndPasswordNotBlank();
  }

  private boolean isLoginAndPasswordNotBlank() {
    return !getLogin().isBlank() && !getPassword().isBlank();
  }

  public String getLogin() {
    return login.get();
  }

  public void setLogin(String login) {
    this.login.set(login);
  }

  public String getPassword() {
    return password.get();
  }

  public void setPassword(String password) {
    this.password.set(password);
  }

  public void setLoginNotify(String loginMsg) {
    this.loginMsg.set(loginMsg);
  }

  public void setPasswordNotify(String passwordMsg) {
    this.passwordMsg.set(passwordMsg);
  }

  AuthenticationResults performAuthenticate() { //todo
    try {
      logger.trace("[VM-LOGIN] Perform authenticate...");
      AuthenticationResults authenticationResults =
              loginViewModel.getAuthenticationService().performAuthentication(new Credentials(getLogin(), getPassword()));
      setLoginNotify(authenticationResults.loginMessage());
      setPasswordNotify(authenticationResults.passwordMessage());

      logger.trace("[VM-LOGIN] Perform authenticate... DONE");
      return authenticationResults;
    } catch (Exception e) {
      logger.error("[VM-LOGIN] Authentication failed: {}", e.getMessage());
      throw new RuntimeException("Authentication failed", e);
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

  public BooleanProperty isCapsLockOnProperty() {
    return isCapsLockOn;
  }

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    logger.debug("Initializing LoginController...");
    initTooltip();
    convertLoginToUpperCaseIfContainsLowerChars();
    emptyFieldListener();
    bindingFieldWithProperty();
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

  private void bindingFieldWithProperty() {
    loginField.textProperty().bindBidirectional(loginViewModel.loginTextProperty());
    passwordField.textProperty().bindBidirectional(loginViewModel.passwordProperty());
    loginInfo.textProperty().bind(loginViewModel.loginMsgProperty());
    passwordInfo.textProperty().bind(loginViewModel.passwordMsgProperty());
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

  @FXML
  void onClickOpenSettings(MouseEvent event) {
    logger.debug("Clicked on Settings...");
    showAppSettings();
  }

  private void showAppSettings() {
    logger.debug("Open settings");
    //        List<String> backendValues =
    // EnumUtils.getEnumList(BackendType.class).stream().map(BackendType::name).collect(Collectors.toList());
    List<BackendType>      backendTypes         = Arrays.asList( BackendType.values());
    ChoiceBox<BackendType> backendTypeChoiceBox = new ChoiceBox<>();
    backendTypeChoiceBox.getItems().addAll( backendTypes );
    backendTypeChoiceBox.setConverter(
        new StringConverter<BackendType>() {
          @Override
          public String toString(BackendType backendType) {
            return backendType.getLabel() + " (" + backendType.getDescription() + ")";
          }

          @Override
          public BackendType fromString(String s) {
            return BackendType.valueOf(s);
          }
        });
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
}
