package net.wilamowski.drecho.client.presentation.customs.controls;

import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import net.wilamowski.drecho.client.application.infra.GeneralViewHandler;
import net.wilamowski.drecho.client.presentation.customs.modals.ExceptionAlert;
import net.wilamowski.drecho.shared.bundle.Lang;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Arkadiusz Wilamowski
 *     <p></><a href="https://github.com/szwrk">GitHub</a>
 *     <p>For questions or inquiries, at contact arek@wilamowski.net
 */
public class FormContextMenu {
  public static final String DICTIONARIES_VIEW_NAME = "Dictionaries";
  private static final Logger logger = LogManager.getLogger(FormContextMenu.class);
  private final GeneralViewHandler viewHandler;

  private FormContextMenu(GeneralViewHandler viewHandler) {
    this.viewHandler = viewHandler;
  }

  public static FormContextMenu setup(GeneralViewHandler viewHandler) {
    return new FormContextMenu(viewHandler);
  }

  private ContextMenu create() {

    ContextMenu menu = new ContextMenu();
    MenuItem item = new MenuItem(Lang.getString("ui.formcontextmenu.header"));
    item.setDisable(true);
    MenuItem editItem = new MenuItem(Lang.getString("ui.formcontextmenu.item.settings"));
    menu.getItems().addAll(item, editItem);
    editItem.setOnAction(event -> openDictionarySettings());
    return menu;
  }

  private void openDictionarySettings() {
    try {
      Stage stage = new Stage();
      viewHandler.switchSceneForStage(DICTIONARIES_VIEW_NAME, stage);
      GeneralViewHandler.setupAsSimpleModal(stage);
      stage.showAndWait();
    } catch (Exception e) {
      handleError(e, "e.999.header", "e.999.msg");
    }
  }

  private void handleError(Exception e, String headerKey, String contentKey) {
    logger.error("An error occurred:", e.getMessage(), e);
    String header = Lang.getString(headerKey);
    String msg = Lang.getString(contentKey);
    ExceptionAlert.create().showError(e, header, msg);
  }

  public void applyForRootNode(Node root) {
    if (root instanceof ComboBox textField) {
      ContextMenu menu = create();
      MenuItem item = new MenuItem();
      item.setDisable(true);
      item.setText("Id: " + root.getId());
      menu.getItems().add(item);
      textField.setContextMenu(menu);
    }

    if (root instanceof Region region) {
      for (Node child : region.getChildrenUnmodifiable()) {
        applyForRootNode(child);
      }
    }
  }
}
