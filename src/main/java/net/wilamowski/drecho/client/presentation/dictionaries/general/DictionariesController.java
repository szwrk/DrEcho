package net.wilamowski.drecho.client.presentation.dictionaries.general;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.VBox;
import javafx.util.converter.DefaultStringConverter;
import javafx.util.converter.IntegerStringConverter;
import lombok.ToString;
import net.wilamowski.drecho.client.application.infra.GeneralViewHandler;
import net.wilamowski.drecho.client.application.infra.ViewModelConfiguration;
import net.wilamowski.drecho.client.application.infra.ViewModelsInitializer;
import net.wilamowski.drecho.client.application.infra.controler_init.KeyEventDebugInitializer;
import net.wilamowski.drecho.client.application.infra.controler_init.PostInitializable;
import net.wilamowski.drecho.client.application.infra.controler_init.Tooltipable;
import net.wilamowski.drecho.client.presentation.customs.modals.ExceptionAlert;
import net.wilamowski.drecho.client.presentation.customs.modals.UserAlert;
import net.wilamowski.drecho.client.presentation.debugger.DebugHandler;
import net.wilamowski.drecho.client.presentation.debugger.KeyDebugHandlerGui;
import net.wilamowski.drecho.client.presentation.examinations.chooser.ExaminationDefinitionFx;
import net.wilamowski.drecho.client.presentation.main.ViewHandlerInitializer;
import net.wilamowski.drecho.shared.bundle.Lang;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@ToString
public class DictionariesController
    implements Initializable,
        KeyEventDebugInitializer,
        PostInitializable,
        Tooltipable,
        ViewModelsInitializer,
        ViewHandlerInitializer {

  public static final String NO_DATA = "Brak danych";
  private static final Logger logger = LogManager.getLogger(DictionariesController.class);
  @FXML private VBox root;

  @FXML private Button confirmButton;

  @FXML private ResourceBundle resourceBundle;

  @FXML private URL location;

  @FXML private TableView<DictionaryFx> dictionaryTable;
  @FXML private TableColumn<DictionaryFx, String> dictionaryCodeColumn;

  @FXML private TableColumn<DictionaryFx, String> dictionaryDescriptionColumn;

  @FXML private TableColumn<DictionaryFx, String> dictionaryNameColumn;

  @FXML private TableView<PositionFx> positionsTable;

  @FXML private TableColumn<PositionFx, String> positionCodeColumn;

  @FXML private TableColumn<PositionFx, Boolean> positionIsActiveColumn;

  @FXML private TableColumn<PositionFx, String> positionNameColumn;

  @FXML private TableColumn<PositionFx, Integer> positionOrderColumn;
  private TableColumn<PositionFx, ExaminationDefinitionFx> positionActionColumn;
  private GeneralViewHandler viewHandler;
  private DictionaryViewModel dictionaryViewModel;

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    this.resourceBundle = resourceBundle;
    // dict table
    setupDictionaryColumns();
    // position table
    positionsTable.setPlaceholder(new Label(NO_DATA));
    positionsTable.setEditable(true);
    setupReadOnlyColumns();
    setupEditablePositionIsActiveColumn();
    setupEditPositionName();
  }

  private void setupReadOnlyColumns() {
    // CellValueFactory
    positionCodeColumn.setCellValueFactory(cell -> cell.getValue().codeProperty());
    positionOrderColumn.setCellValueFactory(cell -> cell.getValue().orderProperty().asObject());
    // CellFactory
    positionCodeColumn.setCellFactory(
        TextFieldTableCell.forTableColumn(new DefaultStringConverter()));
    positionOrderColumn.setCellFactory(
        TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
  }

  private void setupDictionaryColumns() {
    dictionaryTable.setPlaceholder(new Label(NO_DATA));
    /// CellValueFactory
    dictionaryCodeColumn.setCellValueFactory(cell -> cell.getValue().codeProperty());
    dictionaryNameColumn.setCellValueFactory(cell -> cell.getValue().nameProperty());
    dictionaryDescriptionColumn.setCellValueFactory(cell -> cell.getValue().descriptionProperty());
    // CellFactory
    dictionaryNameColumn.setCellFactory(
        TextFieldTableCell.forTableColumn(new DefaultStringConverter()));
    dictionaryDescriptionColumn.setCellFactory(
        TextFieldTableCell.forTableColumn(new DefaultStringConverter()));
  }

  private void setupEditPositionName() {
    positionNameColumn.setCellFactory(
        TextFieldTableCell.forTableColumn(new DefaultStringConverter()));
    positionNameColumn.setCellValueFactory(cell -> cell.getValue().nameProperty());
    positionNameColumn.setEditable(true);

    positionNameColumn.setOnEditCommit(
        new EventHandler<TableColumn.CellEditEvent<PositionFx, String>>() {
          @Override
          public void handle(TableColumn.CellEditEvent<PositionFx, String> editEvent) {
            try {
              logger.debug("Edit Commit");
              PositionFx rowValue = editEvent.getRowValue();
              rowValue.setName(editEvent.getNewValue());
              // Commit the edit
              positionsTable.getSelectionModel().clearSelection();
              positionsTable.edit(-1, null);
              dictionaryViewModel.disableDictionary();
            } catch (Exception e) {
              handleError(e, "e.999.header", "e.999.msg");
            }
          }
        });

    positionNameColumn.setOnEditCancel(
        event -> {
          logger.debug("Edit cancel");
        });
  }

  private void handleError(Exception e, String headerKey, String contentKey) {
    logger.error("An error occurred:", e.getMessage(), e);
    String header = resourceBundle.getString(headerKey);
    String msg = resourceBundle.getString(contentKey);
    ExceptionAlert.create().showError(e, header, msg);
  }

  private void setupEditablePositionIsActiveColumn() {
    positionIsActiveColumn.setCellFactory(
        position -> {
          CheckBox checkBox = new CheckBox();
          TableCell<PositionFx, Boolean> cell =
              new TableCell<>() {
                @Override
                public void updateItem(Boolean item, boolean empty) {
                  if (empty) {
                    setGraphic(null);
                  } else {
                    checkBox.setSelected(item);
                    setGraphic(checkBox);
                  }
                }
              };

          onClickIsActiveDictionaryPosition(checkBox, cell);

          cell.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
          cell.setAlignment(Pos.CENTER);
          return cell;
        });

    positionIsActiveColumn.setCellValueFactory(cell -> cell.getValue().activeProperty());
    positionIsActiveColumn.setEditable(true);
  }

  private void onClickIsActiveDictionaryPosition(
      CheckBox checkBox, TableCell<PositionFx, Boolean> cell) {
    checkBox
        .selectedProperty()
        .addListener(
            (obs, wasSelected, isSelected) -> {
              PositionFx positionFx = cell.getTableRow().getItem();
              if (positionFx != null) {
                positionFx.setActive(isSelected);
              }
            });
    checkBox.setOnAction(event -> dictionaryViewModel.disableDictionary());
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
  public void initializeViewHandler(GeneralViewHandler viewHandler) {
    this.viewHandler = viewHandler;
  }

  @Override
  public void postInitialize() {
    dictionaryTable.itemsProperty().bindBidirectional(dictionaryViewModel.dictionariesFxProperty());
    initOnClickRefreshTablePositions();
    initListenerChooseDictionary();
    setupBlockDictionaryTableWhenNotSaved();
  }

  private void setupBlockDictionaryTableWhenNotSaved() {
    dictionaryTable
        .disableProperty()
        .bindBidirectional(dictionaryViewModel.getIsDictionaryNowEdited());
  }

  private void initListenerChooseDictionary() {
    dictionaryTable
        .getSelectionModel()
        .selectedItemProperty()
        .addListener(
            (observable, selectedDictionary, previousSelection) -> {
              if (previousSelection != null) {
                dictionaryViewModel.chooseDictionary(previousSelection);
              }
            });
  }

  private void initOnClickRefreshTablePositions() {
    dictionaryTable
        .getSelectionModel()
        .selectedItemProperty()
        .addListener(
            (obs, oldSelection, newSelection) -> {
              if (newSelection != null) {

                newSelection
                    .getPositions()
                    .forEach(
                        position -> {
                          dictionaryViewModel.removeListeners(position);
                          dictionaryViewModel.addListeners(position);
                        });
                positionsTable.getItems().clear();
                positionsTable.getItems().setAll(newSelection.getPositions());
              }
            });
  }

  @FXML
  void onActionConfirmDictionaryChanges(ActionEvent event) {
    logger.info("User clicked on the confirm button in the Dictionary view");
    logger.debug("View model {}", dictionaryViewModel.getDictionariesFx());
    dictionaryViewModel.saveChangesDictionary();
    UserAlert.simpleInfo( Lang.getString("u.002.header"), Lang.getString("u.002.msg") )
            .showAndWait();
    dictionaryViewModel.enableDictionary();
  }

  @Override
  public Node getRootUiNode() {
    logger.warn("Root node is not impl yet");
    return null;
  }

  @Override
  public void initializeViewModels(ViewModelConfiguration factory) {
    this.dictionaryViewModel = factory.dictionariesViewModel();
  }
}
