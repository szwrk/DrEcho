package net.wilamowski.drecho.client.presentation.main;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;
import net.wilamowski.drecho.shared.auth.Session;

public class WelcomeController implements Initializable {

  @FXML
  private Text welcomeText;

  private final String loggedInUser;

  public WelcomeController() {
    Session instance = Session.instance();
    loggedInUser = instance.getUserLogin();
  }

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    String message = String.format( "Welcome, %s", loggedInUser );
    welcomeText.setText( message );
  }
}
