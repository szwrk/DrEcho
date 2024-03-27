package net.wilamowski.drecho.client.presentation.login;

import javafx.beans.property.*;
import net.wilamowski.drecho.client.application.factory.DeploymentType;
import net.wilamowski.drecho.client.properties.ClientPropertyReader;
import net.wilamowski.drecho.connectors.model.AuthenticatorService;
import net.wilamowski.drecho.connectors.model.standalone.service.authenticator.Credentials;
import net.wilamowski.drecho.shared.auth.AuthenticationResults;
import net.wilamowski.drecho.shared.bundle.Lang;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoginViewModel {
  public static final String PROPERTY_BACKEND_CONNECT_MODE = "admin.backend-connect-mode";
  private static final Logger logger = LogManager.getLogger(LoginViewModel.class);

  private final ObjectProperty<DeploymentType> deployMode;
  private final StringProperty styleMode;
  private final StringProperty login;
  private final StringProperty password;
  private final StringProperty loginMsg;
  private final StringProperty passwordMsg;
  private final BooleanProperty isCapsLockOn;
  private final AuthenticatorService authenticationService;

  public LoginViewModel(AuthenticatorService service) {
    this.authenticationService = service;
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
    DeploymentType backendConnectModeInitValue = null;
    backendConnectModeInitValue =
        DeploymentType.of(ClientPropertyReader.getString(PROPERTY_BACKEND_CONNECT_MODE));
    deployModeProperty().set(backendConnectModeInitValue);
  }

  public ObjectProperty<DeploymentType> deployModeProperty() {
    return deployMode;
  }

  public BooleanProperty isCapsLockOnProperty() {
    return isCapsLockOn;
  }

  boolean validateLoginInput() {
    setLoginNotify(null);
    setPasswordNotify(null);

    String login = getLogin();
    String password = getPassword();

    if (login == null || login.isBlank()) {
      setLoginNotify(Lang.getString("ui.login.validation.empty-login"));
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

  AuthenticationResults performAuthenticate() {
    try {
      logger.trace("[VM-LOGIN] Perform authenticate...");
      AuthenticationResults authenticationResults =
              authenticationService.performAuthentication(new Credentials(getLogin(), getPassword()));

      setLoginNotify(authenticationResults.loginMessage());
      setPasswordNotify(authenticationResults.passwordMessage());

      logger.trace("[VM-LOGIN] Perform authenticate... DONE");
      return authenticationResults;
    } catch (Exception e) {
      logger.error("[VM-LOGIN] Authentication failed: {}", e.getMessage());
      // You might want to throw a more specific exception or return a specific error indication
      throw new RuntimeException("Authentication failed", e);
    }
  }

  public StringProperty loginTextProperty() {
    return login;
  }

  public StringProperty passwordProperty() {
    return password;
  }

  public String getStyleMode() {
    return styleMode.get();
  }

  public StringProperty styleModeProperty() {
    return styleMode;
  }

  @Override
  public String toString() {
    return "LoginModelView{" + "login=" + login + ", password= *******" + '}';
  }

  public String getLoginMsg() {
    return loginMsg.get();
  }

  public StringProperty loginMsgProperty() {
    return loginMsg;
  }

  public String getPasswordMsg() {
    return passwordMsg.get();
  }

  public StringProperty passwordMsgProperty() {
    return passwordMsg;
  }

  public String getDeployMode() {
    return deployModeProperty().get().name();
  }
}
