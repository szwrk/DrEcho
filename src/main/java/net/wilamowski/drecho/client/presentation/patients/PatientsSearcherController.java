package net.wilamowski.drecho.client.presentation.patients;

import atlantafx.base.controls.Popover;
import atlantafx.base.controls.ToggleSwitch;
import atlantafx.base.util.Animations;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import lombok.ToString;
import net.wilamowski.drecho.client.application.infra.GeneralViewHandler;
import net.wilamowski.drecho.client.application.infra.ViewModels;
import net.wilamowski.drecho.client.application.infra.ViewModelsInitializer;
import net.wilamowski.drecho.client.application.infra.controler_init.PostInitializable;
import net.wilamowski.drecho.client.presentation.customs.PopoverFactory;
import net.wilamowski.drecho.client.presentation.customs.animations.AnimationsUtil;
import net.wilamowski.drecho.client.presentation.customs.modals.ExceptionAlert;
import net.wilamowski.drecho.client.presentation.main.ViewHandlerInitializer;
import net.wilamowski.drecho.client.properties.ClientPropertyReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Arkadiusz Wilamowski
 *     <p></><a href="https://github.com/szwrk">GitHub</a>
 *     <p>For questions or inquiries, at contact arek@wilamowski.net
 */
@ToString
public class PatientsSearcherController
    implements Initializable, ViewModelsInitializer, PostInitializable, ViewHandlerInitializer {
  public static final String SEARCH_TEXTFIELD_TOGGLE_TIP =
      "Wpisz pierwsze znaki nazwiska LUB numeru pesel pacjenta";
  public static final String AUTOSEARCHER_TOGGLE_TIP =
      "Włącz autowyszukiwanie pacjenta po wpisanych kilku pierwszych znakach w polu wyszukiwania";
  public static final String SELECTED_PATIENT_NO_RESULT = "No patient selected";
  public static final String PROPERTY_KEY_AUTOSEARCH_DEFAULT_VALUE =
      "user.ui.patient-view.autosearch-toggle.defaultstate";
  public static final int AUTOSEARCH_STRING_LENGTH_TRIGGER = 4;
  public static final String PATTERN_PESEL_CODE = "\\d{11}";
  private static final Logger logger = LogManager.getLogger(PatientsSearcherController.class);
  @FXML private Button newPatientButton;

  @FXML private TableView<PatientFx> resultTable;
  @FXML private TableColumn<PatientFx, Long> idColumn;
  @FXML private TableColumn<PatientFx, String> nameColumn;
  @FXML private TableColumn<PatientFx, String> lastNameColumn;
  @FXML private TableColumn<PatientFx, String> peselColumn;

  @FXML private ToggleSwitch autosearchToggle;

  @FXML private Button searchButton;

  @FXML private TextField searcherTextField;

  @FXML private VBox patientSummary;

  @FXML private Label nameValueLabel;

  @FXML private Label lastNameValueLabel;

  @FXML private Label peselValueLabel;
  @FXML private VBox patientsSearcherRoot;
  @FXML private Pagination patientPagination;

  @FXML private GridPane searcherGrid;
  @FXML private Button editPatientButton;
  @FXML private Button previewPatientButton;
  private PatientSearcherViewModel patientSearcherViewModel;
  private ResourceBundle bundle;
  private Stage owner;
  private GeneralViewHandler viewHandler;
  private int paginationPageRowNumber;

  public PatientsSearcherController() {}

  @FXML
  void onActionSearch(ActionEvent event) {
    logger.debug("Clicked on search button...");
    logger.debug("Controller table view before {}", resultTable.getItems().size());
    Platform.runLater(
        () -> {
          if (searcherTextField.getText() != null) {
            patientSearcherViewModel.searchPatientByFullName(searcherTextField.getText());
            logger.debug("Controller table view after {}", resultTable.getItems().size());
          }
        });
  }

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    this.bundle = resourceBundle;
  }

  @FXML
  void onActionAddNewPatient(ActionEvent event) {
    openNewPatientModal();
  }

  private void openNewPatientModal() {
    setupOwner();
    Stage modal = new Stage();

    PatientRegisterController patientRegisterController =
        (PatientRegisterController)
            viewHandler.switchSceneForStage("patient/PatientRegister", modal);

    PatientRegisterViewModel viewModel = patientRegisterController.getViewModel();
    viewModel.turnOnAddPatientMode();
    viewModel.configureListenersForPreventingUnsavedChanges();

    patientRegisterController.setTitle("Add patient");
    GeneralViewHandler.setupAsBlurModal(modal, owner);
    GeneralViewHandler.setupStageTitle(modal, "New patient registration");
    modal.showAndWait();
    GeneralViewHandler.disableBlur(owner);
  }

  private void setupOwner() {
    owner = (Stage) patientsSearcherRoot.getScene().getWindow();
  }

  public PatientSearcherViewModel getPatientSearcherViewModel() {
    return patientSearcherViewModel;
  }

  @Override
  public void initializeViewModels(ViewModels viewModelsFactory) {
    this.patientSearcherViewModel = viewModelsFactory.patientViewModel();
  }

  @Override
  public void postInitialize() {
    setupSearchResultsTable();
    onSelectItemUpdateCurrentPatient();
    bindTableWithViewModel();
    updateCurrentPatientLabel();
    initAutoSearch();
    focusOnRootWhenPressEscape();
    focusResultTableWhenPressDownOnSearchField();
    setupPlayAnimationOnNewPatient();
    initializePatientPagination();
    bindSearchTextFieldWithViewModel();
    playAnimationFocusUserOnSearchFieldAndMoveCaret();
    enableButtonWhenPatientIsSelected(editPatientButton);
    enableButtonWhenPatientIsSelected(previewPatientButton);
  }

  private void enableButtonWhenPatientIsSelected(Button button) {
    button
        .disableProperty()
        .bind(patientSearcherViewModel.getSelectedPatientAsObjectProperty().isNull());
    var animation = AnimationsUtil.userCallToActionAnimation(button);

    button
        .disableProperty()
        .addListener(
            (obs, oldVal, newVal) -> {
              if (!newVal) {
                Timeline timeline =
                    new Timeline(
                        new KeyFrame(
                            Duration.millis(300),
                            event -> {
                              animation.playFromStart();
                            }));
                timeline.play();
              }
            });
  }

  private void playAnimationFocusUserOnSearchFieldAndMoveCaret() {
    Platform.runLater(
        () -> {
          Timeline timeline =
              new Timeline(
                  new KeyFrame(
                      Duration.seconds(0.5),
                      event -> {
                        var animation = Animations.shakeX(searcherTextField, 10);
                        animation.playFromStart();
                        Platform.runLater(
                            () -> {
                              searcherTextField.requestFocus();
                                moveCaretToEndOfSearchField( );
                            });
                      }));
          timeline.play();
        });

  }

    private void moveCaretToEndOfSearchField() {
        if (searcherTextField.getText()!=null){
            searcherTextField.positionCaret( searcherTextField.getText().length());
        }
    }

    private void bindSearchTextFieldWithViewModel() {
    searcherTextField
        .textProperty()
        .bindBidirectional(patientSearcherViewModel.searchTextProperty());
  }

  private void initializePatientPagination() {
    patientPagination.setVisible(false);
    patientPagination.setDisable(true);
    paginationPageRowNumber =
        ClientPropertyReader.getInt("admin.ui.patient-view.searcher.result-table-rows-on-page");
    patientPagination.setMaxPageIndicatorCount(3);
    patientPagination.getStyleClass().add(Pagination.STYLE_CLASS_BULLET);
    patientPagination
        .currentPageIndexProperty()
        .addListener(
            (obs, oldvalue, newValue) -> {
              logger.debug("[CONTROLLER] Pagination, click on item: ", newValue);
            });

    patientPagination.setPageFactory(
        (pageIndex) -> {
            String text = searcherTextField.getText( );
            logger.debug(
              "[CONTROLLER] Page factory. Input:"
                  + text
                  + " Page index: "
                  + pageIndex);
          updatePaginationCounter();

          patientSearcherViewModel.searchPatientByFullName( text );

          var label = new Label("#" + (pageIndex + 1));
          label.setStyle("-fx-font-size: 1em;");
          return new BorderPane(label);
        });
  }

  private void setupSearchResultsTable() {
    Platform.runLater(
        () -> {
          idColumn.setCellValueFactory(cellData -> cellData.getValue().getId().asObject());
          nameColumn.setCellValueFactory(cellData -> cellData.getValue().getName());
          lastNameColumn.setCellValueFactory(cellData -> cellData.getValue().getLastName());
          peselColumn.setCellValueFactory(cellData -> cellData.getValue().getPesel());
          searcherTextField.setTooltip(new Tooltip(SEARCH_TEXTFIELD_TOGGLE_TIP));
          resultTable.setPlaceholder(new Label(SELECTED_PATIENT_NO_RESULT));
        });
  }

  private void setupPlayAnimationOnNewPatient() {
    var timeline = AnimationsUtil.animateDataRefresh(nameValueLabel);
    var timeline2 = AnimationsUtil.animateDataRefresh(lastNameValueLabel);
    var timeline3 = AnimationsUtil.animateDataRefresh(peselValueLabel);
    patientSearcherViewModel
        .selectedPatientProperty()
        .addListener(
            (observable, oldValue, newValue) -> {
              timeline.playFromStart();
              timeline2.playFromStart();
              timeline3.playFromStart();
            });
  }

  private void focusResultTableWhenPressDownOnSearchField() {
    searcherTextField.setOnKeyPressed(
        e -> {
          if (searcherTextField.isFocused() && e.getCode() == KeyCode.DOWN) {
            if (!resultTable.getItems().isEmpty()) {
              resultTable.getSelectionModel().selectFirst();
              resultTable.requestFocus();
            }
          }
        });
  }

  private void focusOnRootWhenPressEscape() {
    patientsSearcherRoot.setOnKeyPressed(
        e -> {
          if (new KeyCodeCombination(KeyCode.ESCAPE).match(e)) {
            logger.debug("User pressed key combination for escape search field");
            patientsSearcherRoot.setFocusTraversable(true);
          }
        });
  }

  private void initAutoSearch() {
    configBindingsAndDefaultValues();
    configTriggerNewPatientWithPopOver();
  }

  private void configTriggerNewPatientWithPopOver() {
    autosearchToggle.setTooltip(new Tooltip(AUTOSEARCHER_TOGGLE_TIP));
    searcherTextField
        .textProperty()
        .addListener(
            (obs, oldVal, newVal) -> {
              if (newVal != null) {
                if (!newVal.isEmpty()) {
                  if (autosearchToggle.isSelected()
                      && newVal.length() > AUTOSEARCH_STRING_LENGTH_TRIGGER) {
                    Platform.runLater(
                        () -> {
                          int searchResultsCounter =
                              patientSearcherViewModel.searchPatientByFullName(newVal);
                          turnOnPaginationUiElement();
                          updatePaginationCounter();
                          if (searchResultsCounter == 0
                              && searcherTextField.getText().matches(PATTERN_PESEL_CODE)) {
                            logger.debug("Open register window with search field value");
                            var popoverBox =
                                PopoverFactory.createActionPopover(
                                    bundle.getString(
                                        "ui.patient.searcher.no-patient-warn-popover-title"),
                                    bundle.getString(
                                        "ui.patient.searcher.no-patient-warn-popover-content-text"),
                                    bundle.getString(
                                        "ui.patient.searcher.no-patient-warn-popover-open-registy-button"),
                                    bundle.getString(
                                        "ui.patient.searcher.no-patient-warn-popover-open-registy-tooltip"),
                                    event -> openNewPatientModal(),
                                    Popover.ArrowLocation.TOP_RIGHT);
                            popoverBox.show(searcherTextField);
                            searcherTextField.positionCaret(newVal.length());
                          }
                        });
                  }
                } else {
                  patientSearcherViewModel.patientsProperty().clear();
                }
              } else {
                logger.debug("New value is null. No action.");
              }
            });
  }

  private void updatePaginationCounter() {
    Platform.runLater(
        () -> {
          int patientCounter =
              patientSearcherViewModel.countPatientByAnyInput(searcherTextField.getText());
          patientPagination.setPageCount(patientCounter / paginationPageRowNumber);
        });
  }

  private void turnOnPaginationUiElement() {
    if (!patientSearcherViewModel.getPatients().isEmpty()) {
      patientPagination.setVisible(true);
      patientPagination.setDisable(false);
    }
  }

  private void configBindingsAndDefaultValues() {
    boolean isAutoSearchIsOn =
        Boolean.parseBoolean(ClientPropertyReader.getString(PROPERTY_KEY_AUTOSEARCH_DEFAULT_VALUE));
    autosearchToggle.setSelected(isAutoSearchIsOn);
    searchButton.setVisible(!isAutoSearchIsOn);
    searchButton.setDisable(isAutoSearchIsOn);
    searchButton.visibleProperty().bind(autosearchToggle.selectedProperty().not());
    searchButton.disableProperty().bind(autosearchToggle.selectedProperty());
  }

  private void updateCurrentPatientLabel() {
    ObjectProperty<PatientFx> selectedPatientProperty =
        patientSearcherViewModel.selectedPatientProperty();
    selectedPatientProperty.addListener(
        (obs, oldVal, newVal) -> {
          if (newVal != null) {
            logger.debug("Selected patient: {}", newVal.getId());
            nameValueLabel.textProperty().set(newVal.getName().get());
            lastNameValueLabel.textProperty().set(newVal.getLastName().get());
            peselValueLabel.textProperty().set(newVal.getPesel().get());
          }
        });
  }

  private void onSelectItemUpdateCurrentPatient() {
    resultTable
        .getSelectionModel()
        .selectedItemProperty()
        .addListener(
            (observable, old, newValue) -> {
              if (newValue != null) {
                patientSearcherViewModel.setCurrentPatient(newValue);
              }
            });
  }

  private void bindTableWithViewModel() {
    resultTable.itemsProperty().bindBidirectional(patientSearcherViewModel.patientsProperty());
  }

  @FXML
  void onActionEditPatient(ActionEvent event) {
    setupOwner();
    Stage modal = new Stage();

    PatientRegisterController patientRegisterController =
        (PatientRegisterController)
            viewHandler.switchSceneForStage("patient/PatientRegister", modal);
    PatientRegisterViewModel viewModel = patientRegisterController.getViewModel();
    viewModel.selectPatientForEdit(patientSearcherViewModel.getSelectedPatient());
    viewModel.turnOnEditingPatientMode();
    patientRegisterController.viewModelReBindings();

    patientRegisterController.setTitle("Edit patient");
    GeneralViewHandler.setupAsBlurModal(modal, owner);
    GeneralViewHandler.setupStageTitle(modal, "Edit patient");
    modal.showAndWait();
    GeneralViewHandler.disableBlur(owner);
  }

  @FXML
  void onActionReadPatient(ActionEvent event) {
    setupOwner();
    Stage modal = new Stage();

    PatientRegisterController patientRegisterController =
        (PatientRegisterController)
            viewHandler.switchSceneForStage("patient/PatientRegister", modal);
    PatientRegisterViewModel viewModel = patientRegisterController.getViewModel();
    viewModel.selectPatientForEdit(patientSearcherViewModel.getSelectedPatient());
    patientRegisterController.viewModelReBindings();
    patientRegisterController.blockFields();
    patientRegisterController.setTitle("Preview patient details");
    GeneralViewHandler.setupAsBlurModal(modal, owner);
    GeneralViewHandler.setupStageTitle(modal, "Preview patient details");
    modal.showAndWait();
    GeneralViewHandler.disableBlur(owner);
  }

  public void searcherReguestFocus() {
    searcherTextField.requestFocus();
  }

  private void handleError(Exception e, String headerKey, String contentKey) {
    logger.error("An error occurred:", e.getMessage(), e);
    String header = bundle.getString(headerKey);
    String msg = bundle.getString(contentKey);
    ExceptionAlert.create().showError(e, header, msg);
  }

  public void inititalizeSearchValue(String searchValue) {
    searcherTextField.setText(searchValue);
  }



  @Override
  public void initializeViewHandler(GeneralViewHandler viewHandler) {
    this.viewHandler = viewHandler;
  }
}
