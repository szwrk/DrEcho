package net.wilamowski.drecho.client.presentation.customs.modals;
/**
 * @author Arkadiusz Wilamowski
 *     <p></><a href="https://github.com/szwrk">GitHub</a>
 *     <p>For questions or inquiries, at contact arek@wilamowski.net
 */

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextArea;

public class UserDialog {
  private final Dialog<ButtonType> dialog;
  private final Map<String, Runnable> buttonActions;

  private UserDialog(Builder builder) {
    dialog = new Dialog<>();
    dialog.setTitle(builder.title);
    dialog.setHeaderText(builder.header);
    dialog.setContentText(builder.content);

    if (builder.details != null) {
      if (!builder.details.isEmpty()) {
        addHiddenDetailsTextArea(builder);
      }
    }

    buttonActions = builder.buttonActions;
    for (String buttonText : builder.buttonActions.keySet()) {
      ButtonType buttonType = new ButtonType(buttonText);
      dialog.getDialogPane().getButtonTypes().add(buttonType);
    }
  }

  private void addHiddenDetailsTextArea(Builder builder) {
    TextArea extensionTextArea = new TextArea(builder.details );
    dialog.getDialogPane().setExpandableContent(extensionTextArea);
  }

  public static Builder builder() {
    return new Builder();
  }

  public void showAndWait() {
    Optional<ButtonType> result = dialog.showAndWait();
    result.ifPresent(
        buttonType -> {
          String clickedButtonName = buttonType.getText();
          if (buttonActions.containsKey(clickedButtonName)) {
            Runnable runnable = buttonActions.get(clickedButtonName);
            runnable.run();
          }
        });
  }

  public void close() {
    dialog.close();
  }

  public static class Builder {
    private final Map<String, Runnable> buttonActions = new HashMap<>();
    private String title;
    private String header;
    private String content;
    private String details;

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

    public Builder details(String extensionField) {
      this.details = extensionField;
      return this;
    }

    public Builder addButton(String buttonText, Runnable action) {
      buttonActions.put(buttonText, action);
      return this;
    }

    public UserDialog build() {
      return new UserDialog(this);
    }
  }
}
