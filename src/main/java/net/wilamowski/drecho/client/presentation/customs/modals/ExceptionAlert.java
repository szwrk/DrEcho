package net.wilamowski.drecho.client.presentation.customs.modals;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import net.wilamowski.drecho.client.properties.ClientPropertyReader;
import net.wilamowski.drecho.client.application.infra.util.screenshot.Screenshoter;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/** Dedicated for support. Typicaly for low level problems. Handle errors. */
public class ExceptionAlert {
  public static final String BUNDLE_UI_ALERT_ERROR_HEADER = "ui.alert.error.header";
  public static final String BUNDLE_UI_ALERT_ERROR_TITLE = "ui.alert.error.title";
  public static final String BUNDLE_UI_DETAILS_LABEL = "ui.alert.error.details";
  private static final Logger log = LogManager.getLogger(ExceptionAlert.class);
  private static final String DEFAULT_TITLE = "Error";
  private static final String DEFAULT_HEADER = "Found error";

  public ExceptionAlert() {}

  public static ExceptionAlert create() {
    return new ExceptionAlert();
  }

  public void showError(Throwable e, String header, String content) {
    log.error(e.getMessage());
    if ( ClientPropertyReader.getString( "user.ui.debug.screenshoot-when-error" ).equals( Boolean.TRUE)){
      Screenshoter.shot();
    }
    String title;

    title = "Error";

    Alert alert = createBaseAlert(Alert.AlertType.ERROR, title, header, content);
    String stackTrace = ExceptionUtils.getStackTrace(e);
    setExpandableDetailsContent(alert, stackTrace);

    Platform.runLater(
        () ->
            alert
                .showAndWait()
                .filter(buttonType -> buttonType == ButtonType.OK)
                .ifPresent(result -> log.debug("User accepted error: {}", e.getMessage())));
  }

  private Alert createBaseAlert(
      Alert.AlertType alertType, String title, String header, String content) {
    Alert alert = new Alert(alertType);
    Button button = new Button();

    button.setText("OK");
    button.setOnAction(x -> alert.getOwner().hide());
    alert.setTitle(title);
    alert.setHeaderText(header);
    alert.setContentText(content);

    alert.getDialogPane().getChildren().add(button);
    return alert;
  }

  private void setExpandableDetailsContent(Alert alert, String content) {
    String detailsLabelName = "Details";
    javafx.scene.control.Label stackTraceLabel = new javafx.scene.control.Label(detailsLabelName);
    TextArea textArea = new TextArea(content);
    textArea.setEditable(false);
    textArea.setWrapText(true);
    textArea.setMaxWidth(Double.MAX_VALUE);
    textArea.setMaxHeight(Double.MAX_VALUE);
    GridPane.setVgrow(textArea, Priority.ALWAYS);
    GridPane.setHgrow(textArea, Priority.ALWAYS);
    GridPane expContent = new GridPane();
    expContent.add(stackTraceLabel, 0, 0);
    expContent.add(textArea, 0, 1);

    alert.getDialogPane().setExpandableContent(expContent);
  }
}
