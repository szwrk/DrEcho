package net.wilamowski.drecho.client.presentation.customs;

import atlantafx.base.controls.Popover;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Arkadiusz Wilamowski
 *     <p></><a href="https://github.com/szwrk">GitHub</a>
 *     <p>For questions or inquiries, at contact arek@wilamowski.net
 */
public class PopoverFactory {
  private static final Logger logger = LogManager.getLogger(PopoverFactory.class);

  public static Popover createActionPopover(
      String title,
      String contentText,
      String buttonText,
      String tooltip,
      EventHandler<ActionEvent> eventHandler,
      Popover.ArrowLocation location) {
    logger.debug("New action popover");
    var pop = new Popover();
    pop.setArrowLocation(location);
    pop.setTitle(title);
    pop.setHeaderAlwaysVisible(true);
    Button actionButton = new Button(buttonText);
    actionButton.setLineSpacing(10);
    actionButton.setTooltip(new Tooltip(tooltip));

    VBox box = new VBox(10);
    box.setSpacing(10);
    VBox.setMargin(box, new Insets(5, 5, 5, 5));
    box.getChildren().add(actionButton);
    actionButton.setOnAction(eventHandler);
    pop.setContentNode(new VBox(new TextFlow(new Text(contentText)), box));
    return pop;
  }

  public static Popover createSimplePopover(String title, String contentText) {
    logger.debug("New simple popover");
    var pop = new Popover();
    pop.setTitle(title);
    pop.setCornerRadius(10);
    pop.setHeaderAlwaysVisible(true);
    pop.setContentNode(new VBox(new TextFlow(new Text(contentText))));
    return pop;
  }

  public static Popover createSimplePopover(
      String title, String contentText, Popover.ArrowLocation location) {
    logger.debug("New simple popover");
    var pop = new Popover();
    pop.setTitle(title);
    pop.setHeaderAlwaysVisible(true);
    pop.setContentNode(new VBox(new TextFlow(new Text(contentText))));
    pop.setArrowLocation(location);
    return pop;
  }
}
