package net.wilamowski.drecho.client.presentation.examinations.chooser;

import atlantafx.base.theme.Styles;
import java.net.URL;
import java.util.Iterator;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import lombok.ToString;
import net.wilamowski.drecho.client.application.infra.GeneralViewHandler;
import net.wilamowski.drecho.client.application.infra.ViewModels;
import net.wilamowski.drecho.client.application.infra.ViewModelsInitializer;
import net.wilamowski.drecho.client.application.infra.controler_init.PostInitializable;
import net.wilamowski.drecho.client.presentation.customs.modals.ExceptionAlert;
import net.wilamowski.drecho.client.presentation.customs.modals.UserAlert;
import net.wilamowski.drecho.client.presentation.examinations.echo.EchoExaminationDefinition;
import net.wilamowski.drecho.client.presentation.examinations.echo.EchoTteExaminationInstance;
import net.wilamowski.drecho.client.presentation.examinations.pacemaker.PacemakerExaminationDefinitionFx;
import net.wilamowski.drecho.client.presentation.main.ViewHandlerInitializer;
import net.wilamowski.drecho.shared.bundle.Lang;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@ToString
public class ExaminationsChooserController
    implements ViewHandlerInitializer, Initializable, PostInitializable, ViewModelsInitializer {
  public static final String DICT_BTN_IMG = "/assets/add-circle.png";
  public static final String SELECTED_EXAMS_EDIT_BTN_IMG = "/assets/edit.png";
  public static final String SELECTED_EXAMS_PRINT_BTN_IMG = "/assets/printer.png";
  public static final String SELECTED_EXAMS_DELETE_BTN_IMG = "/assets/delete.png";
  private static final Logger logger = LogManager.getLogger(ExaminationsChooserController.class);
  // Dictionary table
  @FXML private TableView<ExaminationDefinitionFx> examinationsDictionaryTable;
  @FXML private TableColumn<ExaminationDefinitionFx, String> codeDictionaryColumn;
  @FXML private TableColumn<ExaminationDefinitionFx, String> nameDictionaryColumn;

  @FXML
  private TableColumn<ExaminationDefinitionFx, ExaminationDefinitionFx> definitionActionColumn;

  // Selected item table
  @FXML private TableView<ExaminationInstance> selectedExhaminationsTable;
  @FXML private TableColumn<ExaminationInstance, Integer> selectedExhaminationTempIdColumn;

  @FXML private TableColumn<ExaminationInstance, ExaminationInstance> selectedExaminationEditColumn;

  @FXML
  private TableColumn<ExaminationInstance, ExaminationInstance> selectedExaminationPrintColumn;

  @FXML
  private TableColumn<ExaminationInstance, ExaminationInstance> selectedExaminationDeleteColumn;

  @FXML private TableColumn<ExaminationInstance, String> selectedExhaminationCodeColumn;
  @FXML private TableColumn<ExaminationInstance, String> selectedExhaminationNameColumn;
  // End Selected item table

  private GeneralViewHandler viewHandler;

  private ExaminationsChooserViewModel examinationsChooserViewModel;
  private ViewModels factory;
  private Iterator<Integer> tempIdIterator;

  public ExaminationsChooserController() {
    initIterator();
  }

  private void initIterator() {
    this.tempIdIterator =
        new Iterator<>() {
          Integer id = 1;

          @Override
          public boolean hasNext() {
            return true;
          }

          @Override
          public Integer next() {
            return id++;
          }
        };
  }

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    logger.traceEntry();
    configSelectedExaminationsPlaceholder();
    configDictionaryExaminationsPlaceholder();
    logger.traceExit();
  }

  private void configSelectedExaminationsPlaceholder() {
    Label label = new Label(Lang.getString("ui.examinations.searcher.selected.nodata"));
    label.getStyleClass().add(Styles.TEXT_SUBTLE);
    selectedExhaminationsTable.setPlaceholder(label);
  }

  private void configDictionaryExaminationsPlaceholder() {
    Label label = new Label(Lang.getString("ui.examinations.searcher.selected.nodata"));
    label.getStyleClass().add(Styles.TEXT_SUBTLE);
    examinationsDictionaryTable.setPlaceholder(label);
  }

  private void initExamDictionary() {
    examinationsDictionaryTable.setPlaceholder(
        new Label(Lang.getString("ui.examinations.catalogue.nodata.lbl")));
    codeDictionaryColumn.setCellValueFactory(cellData -> cellData.getValue().codeProperty());
    nameDictionaryColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());

    definitionActionColumn.setCellValueFactory(
        cellData -> new SimpleObjectProperty<>(cellData.getValue()));
    definitionActionColumn.setCellFactory(
        column ->
            new TableCell<>() {

              @Override
              protected void updateItem(ExaminationDefinitionFx item, boolean empty) {
                super.updateItem(item, empty);
                if (empty
                    || item == null
                    || getIndex() < 0
                    || getIndex() >= getTableView().getItems().size()) {
                  setGraphic(null);
                } else {
                  Button addExaminationButton =
                      createAddNewInstanceOfExaminationButton(
                          getTableView().getItems().get(getIndex()));
                  setGraphic(addExaminationButton);
                }
                logger.debug("Dictionary table index: " + getIndex());
              }
            });

    ObservableList<ExaminationDefinitionFx> definitions = fetchExamCatalog();
    examinationsDictionaryTable.setItems(definitions);
  }

  private Button createAddNewInstanceOfExaminationButton(ExaminationDefinitionFx definition) {
    Button btn = new Button();
    Tooltip tooltip = new Tooltip(Lang.getString("ui.examinations.catalogue.additem"));
    btn.setTooltip(tooltip);
    Image image = new Image(getClass().getResourceAsStream(DICT_BTN_IMG));
    ImageView imageView = new ImageView(image);
    btn.setGraphic(imageView);
    btn.setOnAction(
        event -> {
          addNewExaminationTodo(definition);
        });
    return btn;
  }

  private void addNewExaminationTodo(ExaminationDefinitionFx definition) {
    if (isPatientSelected()) {
      Objects.requireNonNull(viewHandler);
      logger.debug("Selected examination: {}", definition);
      try {
        EchoTteExaminationInstance echoInstance =
            new EchoTteExaminationInstance(
                tempIdIterator.next(),
                definition,
                examinationsChooserViewModel.selectedPatientProperty(),
                viewHandler);

        examinationsChooserViewModel.getChosenExamination().getValue().add(echoInstance);

      } catch (NotImplementedException notImplementedException) {
        handleError(notImplementedException, "e.006.header", "e.006.msg");
      } catch (Exception exception) {
        handleError(exception, "e.999.header", "e.999.msg");
      }
    } else {
      UserAlert userAlert = new UserAlert();
      userAlert.showWarn(Lang.getString("u.001.header"), Lang.getString("u.001.msg"));
    }
  }

  private boolean isPatientSelected() {
    return examinationsChooserViewModel.selectedPatientProperty().getValue() != null;
  }

  private ObservableList<ExaminationDefinitionFx>
      fetchExamCatalog() { // todo hardcoded exam catalogue, create view model + servic, dictionary?
                           // factory? etc?
    ExaminationDefinitionFx tte =
        new EchoExaminationDefinition(
            new SimpleStringProperty("ETTE"),
            new SimpleStringProperty(Lang.getString("ui.examinations.service.echotte")));
    ExaminationDefinitionFx ks =
        new PacemakerExaminationDefinitionFx(
            new SimpleStringProperty("PM"),
            new SimpleStringProperty(Lang.getString("ui.examinations.service.pacemaker")));
    return FXCollections.observableArrayList(tte, ks);
  }

  @Override
  public void initializeViewHandler(GeneralViewHandler viewHandler) {
    this.viewHandler = viewHandler;
  }

  @Override
  public void postInitialize() {
    selectedExhaminationsTable
        .itemsProperty()
        .bindBidirectional(examinationsChooserViewModel.getChosenExamination());
    configureSelectedTableColumns();
    configureSelectedEditColumn();
    configureSelectedPrintColumn();
    configureSelectedDeleteColumn();
    initExamDictionary();
  }

  private void configureSelectedDeleteColumn() {
    examinationColumnConfigurator(
        selectedExaminationDeleteColumn,
        Lang.getString("ui.examinations.searcher.selected.delete"),
        SELECTED_EXAMS_DELETE_BTN_IMG,
        item -> {
          logger.debug("Delete {} {}.... ", item.getDefinition().getCode(), item.getTempId());
          selectedExhaminationsTable.itemsProperty().getValue().remove(item);
        });
  }

  private void configureSelectedPrintColumn() {
    examinationColumnConfigurator(
        selectedExaminationPrintColumn,
        Lang.getString("ui.examinations.searcher.selected.print"),
        SELECTED_EXAMS_PRINT_BTN_IMG,
        item ->
            logger.debug(
                "Printing {} {}.... ",
                item.getDefinition().getName(),
                item.getDefinition().getCode()));
  }

  private void configureSelectedEditColumn() {
    examinationColumnConfigurator(
        selectedExaminationEditColumn,
        "Describe examination",
        SELECTED_EXAMS_EDIT_BTN_IMG,
        ExaminationInstance::showForm);
  }

  private void configureSelectedTableColumns() {
    selectedExhaminationsTable.setItems(
        examinationsChooserViewModel.getChosenExamination().getValue());
    selectedExhaminationCodeColumn.setCellValueFactory(
        cellData -> cellData.getValue().getDefinition().codeProperty());
    selectedExhaminationNameColumn.setCellValueFactory(
        cellData -> cellData.getValue().getDefinition().nameProperty());
    selectedExhaminationTempIdColumn.setCellValueFactory(
        cellData -> cellData.getValue().getTempId().asObject());
  }

  private Button createButton(
      String tooltipText, String imageResource, EventHandler<ActionEvent> eventHandler) {
    Button button = new Button();
    Tooltip tooltip = new Tooltip(tooltipText);
    Image image = new Image(getClass().getResourceAsStream(imageResource));
    ImageView imageView = new ImageView(image);

    button.setTooltip(tooltip);
    button.setGraphic(imageView);
    button.setOnAction(eventHandler);

    return button;
  }

  private void examinationColumnConfigurator(
      TableColumn<ExaminationInstance, ExaminationInstance> column,
      String tooltipText,
      String imageResource,
      Consumer<ExaminationInstance> action) {
    column.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue()));
    column.setCellFactory(
        col ->
            new TableCell<>() {
              final Button button =
                  createButton(
                      tooltipText,
                      imageResource,
                      event -> action.accept(getTableView().getItems().get(getIndex())));

              @Override
              protected void updateItem(ExaminationInstance item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : button);
              }
            });
  }

  public ExaminationsChooserViewModel getExaminationViewModel() {
    return examinationsChooserViewModel;
  }

  @Override
  public void initializeViewModels(ViewModels factory) {
    this.factory = factory;
    this.examinationsChooserViewModel = factory.examinationsChooserViewModel();
  }

  private void handleError(Exception e, String headerKey, String contentKey) {
    logger.error("An error occurred:", e.getMessage(), e);
    String header = Lang.getString(headerKey);
    String msg = Lang.getString(contentKey);
    ExceptionAlert.create().showError(e, header, msg);
  }
}
