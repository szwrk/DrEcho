package net.wilamowski.drecho.client.presentation.customs.modals;

import java.util.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import net.wilamowski.drecho.app.bundle.Lang;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Dedicated for users as readable alerts with solution. Non typically losw level system error.
 * Should contain internatiolization.
 */
public class UserAlert {
  public static final String INFO_TITLE = Lang.getString("ui.alert.info.title");
  public static final String WARN_TITLE = Lang.getString("ui.alert.warn.title");
  private static final Logger log = LogManager.getLogger(UserAlert.class);
  private final Alert alert;

  private UserAlert(Builder builder) {
    this.alert = builder.alert;
    this.alert.setTitle(builder.title);
    this.alert.setHeaderText(builder.header);
    this.alert.setContentText(builder.content);

    if (builder.details != null && !builder.details.isEmpty()) {
      addDetailsTextArea(builder.details);
    }
  }

  public static UserAlert simpleWarn(String header, String content) {
    Alert alert = new Alert(Alert.AlertType.WARNING);
    return new Builder(alert)
            .title(INFO_TITLE)
            .header(header)
            .content(content)
            .build();
  }


  public static UserAlert simpleInfo(String header, String content) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    return new Builder(alert)
            .title(INFO_TITLE)
            .header(header)
            .content(content)
            .build();
  }

  public static UserAlert simpleInfo(String header, String content, String details) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    return new Builder(alert)
            .title(INFO_TITLE)
            .header(header)
            .content(content)
            .details(details)
            .build();
  }

  private static EventHandler<ActionEvent> closeAlert(Alert alert) {
    return x -> ((Stage) alert.getDialogPane( ).getScene( ).getWindow( )).close( );
  }

  private void addDetailsTextArea(String detailsText) {
    String detailsLabelName = "Details";
    javafx.scene.control.Label stackTraceLabel = new javafx.scene.control.Label(detailsLabelName);
    TextArea detailsTextArea = new TextArea(detailsText);
    configureDetailsTextArea(detailsTextArea);

    GridPane expContent = new GridPane();
    expContent.add(stackTraceLabel, 0, 0);
    expContent.add(detailsTextArea, 0, 1);
    alert.getDialogPane().setExpandableContent(expContent);
  }

  private void configureDetailsTextArea(TextArea detailsTextArea) {
    detailsTextArea.setEditable(false);
    detailsTextArea.setWrapText(true);
    detailsTextArea.setMaxWidth(Double.MAX_VALUE);
    detailsTextArea.setMaxHeight(Double.MAX_VALUE);
    GridPane.setVgrow(detailsTextArea, Priority.ALWAYS);
    GridPane.setHgrow(detailsTextArea, Priority.ALWAYS);
  }

  public void showAndWait() {
    Platform.runLater(
        () -> {
          Optional<ButtonType> result = alert.showAndWait();
          result.ifPresent(
              buttonType -> {
                if (buttonType == ButtonType.OK) {
                  log.debug(
                      "User accepted alert: {}, {}", alert.getHeaderText(), alert.getContentText());
                }
              });
        });
  }

  public static class Builder {
    private final Alert alert;
    private String title;
    private String header;
    private String content;
    private String details;
    private Map<String, Runnable> buttons = new HashMap<>( );

    public Builder(Alert alert) {
      this.alert = alert;
    }

    public Builder title(String title) {
      this.title = title;
      return this;
    }

    public Builder header(String header) {
      this.header = header;
      return this;
    }

    public Builder content(String content) {
      this.content = content;
      return this;
    }

    public Builder details(String details) {
      this.details = details;
      return this;
    }

    public UserAlert build() {
      return new UserAlert(this);
    }
  }
}
