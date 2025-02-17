package net.wilamowski.drecho.client.presentation.settings;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.VBox;
import javafx.util.converter.DefaultStringConverter;
import lombok.ToString;
import net.wilamowski.drecho.client.application.infra.controler_init.KeyEventDebugInitializer;
import net.wilamowski.drecho.client.application.infra.controler_init.Tooltipable;
import net.wilamowski.drecho.client.presentation.debugger.DebugHandler;
import net.wilamowski.drecho.client.presentation.debugger.KeyDebugHandlerGui;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Arkadiusz Wilamowski
 *     <p></><a href="https://github.com/szwrk">GitHub</a>
 *     <p>For questions or inquiries, at contact arek@wilamowski.net
 */
@ToString
public class SettingsController
    implements Initializable,
        KeyEventDebugInitializer,
        Tooltipable
//        ,
//        ViewModelsInitializer,
//        PostInitializable 
{
  private static final Logger logger = LogManager.getLogger(SettingsController.class);
  @FXML private TableView<SettingPropertyFx> table;

  @FXML private VBox root;

  private SettingsViewModel viewModel;
  private ResourceBundle bundle;
  private TableColumn<SettingPropertyFx, String> keyColumn;
  private TableColumn<SettingPropertyFx, String> valueColumn;

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    this.bundle = resourceBundle;

    keyColumn = new TableColumn<>("Key");
    keyColumn.setCellValueFactory(cellData -> cellData.getValue().keyProperty());
    keyColumn.setMinWidth(275);

    valueColumn = new TableColumn<>("Value");
    valueColumn.setCellValueFactory(cellData -> cellData.getValue().valueProperty());
    valueColumn.setMinWidth(275);
    setupEditOfValue();

    table.getColumns().addAll(keyColumn, valueColumn);
    table.setItems(viewModel.getSettingsFx());
  }

  public SettingsController(SettingsViewModel viewModel) {
    this.viewModel = viewModel;
  }

  @Override
  public void initializeKeyEventDebugging() {
    logger.traceEntry();
    DebugHandler debugHandler = new KeyDebugHandlerGui();
    debugHandler.initNode(root);
    debugHandler.watch(this);
    logger.traceExit();
  }

  @Override
  public Node getRootUiNode() {
    return root;
  }



  private void setupEditOfValue() {
    valueColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DefaultStringConverter()));
    valueColumn.setCellValueFactory(cell -> cell.getValue().getValue());
    valueColumn.setEditable(true);

    valueColumn.setOnEditCommit(
        new EventHandler<TableColumn.CellEditEvent<SettingPropertyFx, String>>() {
          @Override
          public void handle(TableColumn.CellEditEvent<SettingPropertyFx, String> editEvent) {
            try {
              logger.debug("Edit Commit");
              SettingPropertyFx rowValue = editEvent.getRowValue();
              rowValue.setValue(new SimpleStringProperty(editEvent.getNewValue()));
              // Commit the edit
              table.getSelectionModel().clearSelection();
              table.edit(-1, null);
              viewModel.update(rowValue);
              //                            viewModel.disableDictionary();
            } catch (Exception e) {
              logger.error(e.getMessage(), e);
              //                            handleError(e, "e.999.header", "e.999.msg");
            }
          }
        });

    keyColumn.setOnEditCancel(
        event -> {
          logger.debug("Edit cancel");
        });
  }
}
