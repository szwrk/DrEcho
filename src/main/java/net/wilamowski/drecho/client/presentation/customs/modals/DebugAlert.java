package net.wilamowski.drecho.client.presentation.customs.modals;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.scene.control.Alert;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.fxmisc.richtext.InlineCssTextArea;

public class DebugAlert {
  private static final Logger log = LogManager.getLogger(DebugAlert.class);
  private final String title;
  private final String header;
  private final int height;
  private final int width;
  private final String content;

  private DebugAlert(String title, String header, int height, int width, String content) {
    this.title = title;
    this.header = header;
    this.height = height;
    this.width = width;
    this.content = content;
  }

  public static DebugAlert createWithContents(String content) throws NullPointerException {
    return new DebugAlert("Debug Information", "Scene state", 600, 600, content);
  }

  public void show() {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle(title);
    alert.setHeaderText(header);
    alert.setResizable(true);

    InlineCssTextArea area = new InlineCssTextArea(content);

    highlightsWords(area, "\\w+=null", "-fx-font-weight: bold; -fx-fill: red;");
    highlightsWords(area, "\\b[A-Z]\\w*ViewModel\\b", "-fx-font-weight: bold;  -fx-fill: green;");
    highlightsWords(
        area,
        "\\b(?!\\w*ViewModel$)\\[A-Z]\\w*Model\\b",
        "-fx-font-weight: bold;  -fx-fill: blue;");
    highlightsWords(area, "@@@@@", "-fx-font-weight: bold;  -fx-fill: orange;");

    area.setEditable(false);
    area.setWrapText(true);
    area.setPrefSize(800, 600);
    alert.getDialogPane().setContent(area);
    alert.showAndWait();
  }

  private static void highlightsWords(InlineCssTextArea area, String regex, String style) {
    int startPos = 0;
    int endPos;

    Pattern pattern = Pattern.compile(regex); // "\\bnull\\b"
    Matcher matcher = pattern.matcher(area.getText());

    while (matcher.find()) {
      startPos = matcher.start();
      endPos = matcher.end();
      log.debug(
          "Matched nulls: "
              + startPos
              + " "
              + endPos
              + " "
              + matcher
              + " "
              + area.getText().substring(startPos, endPos));
      area.clearStyle(startPos, endPos);
      area.setStyle(startPos, endPos, style); //
    }
  }
}
