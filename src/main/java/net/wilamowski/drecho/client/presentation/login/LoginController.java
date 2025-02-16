package net.wilamowski.drecho.client.presentation.login;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import net.wilamowski.drecho.app.auth.AuthenticationResults;
import net.wilamowski.drecho.client.application.infra.GeneralViewHandler;
import net.wilamowski.drecho.client.properties.ClientPropertyReader;
import net.wilamowski.drecho.configuration.BackendType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/*Author:
Arkadiusz Wilamowski
https://github.com/szwrk
*/

public class LoginController implements Initializable {
  private static final Logger logger = LogManager.getLogger(LoginController.class);
  public static final String PROPERTY_BACKEND_CONNECT_MODE = "admin.backend-connect-mode";

  @FXML private TextField loginField;

  @FXML private Label loginInfo;

  @FXML private PasswordField passwordField;

  @FXML private Label passwordInfo;

  @FXML private Pane rootPane;
  private ObjectProperty<BackendType> deployMode;
  private LoginViewModel loginViewModel;
  private final GeneralViewHandler viewHandler;

  public LoginController(LoginViewModel loginViewModel, GeneralViewHandler viewHandler) {
    this.loginViewModel = loginViewModel;
    this.viewHandler = viewHandler;
  }

  @FXML
  void onClickOpenSettings(MouseEvent event) {}

  @FXML
  void setOnActionLogin(ActionEvent event) {
    AuthenticationResults authenticationResults = loginViewModel.performAuthenticate( );
    if (authenticationResults.isAuthenticated()){
      viewHandler.openMain();
    } else {
      logger.debug( "Authentication failed" );
    }

  }

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    initBackendMode();
    loginViewModel.loginTextProperty().bindBidirectional(loginField.textProperty());
    loginViewModel.passwordProperty().bindBidirectional(passwordField.textProperty());

    loginViewModel.loginMsgProperty().bindBidirectional( loginInfo.textProperty() );
    loginViewModel.passwordMsgProperty().bindBidirectional(passwordInfo.textProperty()) ;
  }

  private void initBackendMode() {
    deployMode = new SimpleObjectProperty<>(  );
    BackendType backendConnectModeInitValue = null;
    backendConnectModeInitValue =
            BackendType.of( ClientPropertyReader.getString(PROPERTY_BACKEND_CONNECT_MODE));
    deployMode.set(backendConnectModeInitValue);
  }
}
