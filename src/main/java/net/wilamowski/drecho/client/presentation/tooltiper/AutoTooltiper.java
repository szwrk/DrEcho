package net.wilamowski.drecho.client.presentation.tooltiper;

import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import net.wilamowski.drecho.shared.bundle.Lang;
import net.wilamowski.drecho.client.properties.ClientPropertyReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Arkadiusz Wilamowski
 *     <p></><a href="https://github.com/szwrk">GitHub</a>
 *     <p>For questions or inquiries, at contact arek@wilamowski.net
 */
public class AutoTooltiper {
  private static final Logger logger = LogManager.getLogger(AutoTooltiper.class);

  private static void logIgnoredControlNode(Node root) {
    logger.debug("Ignored item: {}", root);
  }

  public void runTree(Node root) {
    String s = ClientPropertyReader.getString("admin.debug.autotooltiper.ison");
    if (Boolean.parseBoolean(s)) {
      createElementsRecursively(root);
    } else {
      logger.debug("Autotooltiper is off");
    }
  }

  private void createElementsRecursively(Node root) {
    if (root instanceof javafx.scene.control.Control) {
      String tooltipText = "";
      if (root instanceof Label label) {
        logger.traceEntry();
        tooltipText = createTooltipText(label.getId(), label.getText(), "");
        setTooltip(root, tooltipText);
      } else if (root instanceof TextField textField) {
        logger.traceEntry();
        tooltipText =
            createTooltipText(textField.getId(), textField.getText(), textField.getPromptText());
        setTooltip(root, tooltipText);
      } else if (root instanceof ComboBox) {
        logger.traceEntry();
        ComboBox combo = (ComboBox) root;
        tooltipText = createTooltipText(combo.getId(), "", combo.getPromptText());
        setTooltip(root, tooltipText);
      } else if (root instanceof TextArea) {
        logger.traceEntry();
        TextArea combo = (TextArea) root;
        tooltipText = createTooltipText(combo.getId(), combo.getText(), combo.getPromptText());
        setTooltip(root, tooltipText);
      } else if (root instanceof Separator) {
      } else {
        logIgnoredControlNode(root);
      }
    } else {
      if (root instanceof Region region) {
        logger.traceEntry("Creating tooltips for next Region: {}", root);
        region.getChildrenUnmodifiable().forEach(this::createElementsRecursively);
      }
    }
  }

  private String createTooltipText(String nodeId, String nodeText, String nodePromptText) {
    nodeId = (nodeId != null) ? nodeId : " - ";
    nodeText = (nodeText != null) ? nodeText : " - ";
    nodePromptText = (nodePromptText != null) ? nodePromptText : " - ";
    return String.format(
        "id: %s\ntext: %s\nprompt: %s\nbundle matched values: %s",
        nodeId, nodeText, nodePromptText, Lang.getKeysByValue(nodeText));
  }

  private void setTooltip(Node node, String tooltipText) {
    Tooltip tooltip = new Tooltip(tooltipText);
    Tooltip.install(node, tooltip);
  }
}
