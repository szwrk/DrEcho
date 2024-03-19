package net.wilamowski.drecho.client.presentation.customs.modals;

import javafx.scene.control.Alert;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NativeAlert {
  private static final Logger log = LogManager.getLogger(NativeAlert.class);

  /** Safe simple alert without bundle class and property dependency for low level errors */
  public static void safeError(Throwable e) {
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle("Error!");
    alert.setHeaderText("An error occurred");
    alert.setContentText("Check log for more information about: {}" + e.getMessage());
    alert.showAndWait();
  }
}
