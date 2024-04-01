package net.wilamowski.drecho.client.presentation.customs.modals;

import javafx.application.Platform;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import net.wilamowski.drecho.shared.bundle.Lang;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Dedicated for users as readable alerts with solution. Non typically losw level system error.
 * Should contain internatiolization.
 */
public class UserAlert {
  private static final Logger log = LogManager.getLogger(UserAlert.class);

  public void showInfo(String header, String content) {
    String title = Lang.getString("ui.alert.info.title");
    Alert alert = createBaseAlert(Alert.AlertType.INFORMATION, title, header, content, "");
    showAndWaitWithLogging(alert, header, content);
  }

  public void showInfo(String header, String content, String hiddenDetails) {
    String title = Lang.getString("ui.alert.info.title");
    Alert alert = createBaseAlert(Alert.AlertType.INFORMATION, title, header, content, hiddenDetails);
    showAndWaitWithLogging(alert, header, content);
  }

  private void showAndWaitWithLogging(Alert alert, String header, String content) {
    alert
        .showAndWait()
        .filter(buttonType -> buttonType == ButtonType.OK)
        .ifPresent(result -> log.debug("User accepted alert: {}, {}", header, content));
  }

  private Alert createBaseAlert(
    Alert.AlertType alertType, String title, String header, String bodyText, String detailsText) {

    Alert alert = new Alert(alertType);

        Button button = new Button();
    button.setText("OK");
    button.setOnAction(x -> alert.getOwner().hide());
    alert.setTitle(title);
    alert.setHeaderText(header);
    alert.setContentText(bodyText);
    alert.getDialogPane().getChildren().add(button);

    String detailsLabelName = "Details";
    javafx.scene.control.Label stackTraceLabel = new javafx.scene.control.Label(detailsLabelName);
    TextArea                   detailsTextArea        = new TextArea(detailsText);
    detailsTextArea.setEditable(false);
    detailsTextArea.setWrapText(true);
    detailsTextArea.setMaxWidth(Double.MAX_VALUE);
    detailsTextArea.setMaxHeight(Double.MAX_VALUE);
    GridPane.setVgrow(detailsTextArea, Priority.ALWAYS);
    GridPane.setHgrow(detailsTextArea, Priority.ALWAYS);
    GridPane expContent = new GridPane();
    expContent.add(stackTraceLabel, 0, 0);
    expContent.add(detailsTextArea, 0, 1);
    alert.getDialogPane().setExpandableContent(expContent);
    return alert;
  }

  public void showWarn(String header, String content) {
    String title = Lang.getString("ui.alert.warn.title");
    Alert alert = createBaseAlert(Alert.AlertType.WARNING, title, header, content, "");
    Platform.runLater(() -> showAndWaitWithLogging(alert, header, content));
  }

  public void showWarnConfirmOrLeave(
      String header,
      String content,
      String confirmButton,
      Runnable confirmAction,
      String cancelButtonName,
      Runnable cancelAction) {
    String title = Lang.getString("ui.alert.warn.title");
    Alert alert = createBaseAlert(Alert.AlertType.WARNING, title, header, content, "");
    Platform.runLater(() -> showAndWaitWithLogging(alert, header, content));

    ButtonType confirmButtonType = new ButtonType(confirmButton, ButtonBar.ButtonData.OK_DONE);
    ButtonType leaveButtonType =
        new ButtonType(cancelButtonName, ButtonBar.ButtonData.CANCEL_CLOSE);
    alert.getButtonTypes().setAll(confirmButtonType, leaveButtonType);

    alert
        .showAndWait()
        .ifPresent(
            buttonType -> {
              if (buttonType == confirmButtonType) {
                confirmAction.run();
              } else if (buttonType == leaveButtonType) {
                cancelAction.run();
              }
            });
  }
}
