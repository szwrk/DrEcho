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
import net.wilamowski.drecho.client.application.infra.ViewModelConfiguration;
import net.wilamowski.drecho.client.presentation.customs.PopoverFactory;
import net.wilamowski.drecho.client.presentation.customs.animations.AnimationsUtil;
import net.wilamowski.drecho.client.presentation.customs.modals.ExceptionAlert;
import net.wilamowski.drecho.client.properties.ClientPropertyReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Arkadiusz Wilamowski
 *     <p></><a href="https://github.com/szwrk">GitHub</a>
 *     <p>For questions or inquiries, at contact arek@wilamowski.net
 */
@ToString
public class PatientsSearcherController implements Initializable {
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

  @FXML private TableView<PatientVM> resultTable;
  @FXML private TableColumn<PatientVM, Long> idColumn;
  @FXML private TableColumn<PatientVM, String> nameColumn;
  @FXML private TableColumn<PatientVM, String> lastNameColumn;
  @FXML private TableColumn<PatientVM, String> peselColumn;

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

  public PatientsSearcherController(
      ViewModelConfiguration viewModelConfiguration, GeneralViewHandler viewHandler) {
    this.patientSearcherViewModel = viewModelConfiguration.patientViewModel();
    this.viewHandler = viewHandler;
  }

  @FXML
  void onActionSearch(ActionEvent event) {
    logger.debug("Clicked on search button...");
    logger.debug("Controller table view before {}", resultTable.getItems().size());
    Platform.runLater(
        () -> {
          if (searcherTextField.getText() != null) {
            patientSearcherViewModel.searchPatientByFullName(searcherTextField.getText());
            logger.debug("Controller table view after {}", resultTable.getItems().size());
          } else {
              patientSearcherViewModel.findRecentPatients();
          }
        });
  }

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    this.bundle = resourceBundle;
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

  private void setupOwner() {
    if (owner == null) {
      owner = (Stage) patientsSearcherRoot.getScene().getWindow();
    }
  }

    private void enableButtonWhenPatientIsSelected(Button button) {
    button.disableProperty().bind(patientSearcherViewModel.isPatientNull());
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
                              moveCaretToEndOfSearchField();
                            });
                      }));
          timeline.play();
        });
  }

  private void moveCaretToEndOfSearchField() {
    if (searcherTextField.getText() != null) {
      searcherTextField.positionCaret(searcherTextField.getText().length());
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
          String text = searcherTextField.getText();
          logger.debug("[CONTROLLER] Page factory. Input:" + text + " Page index: " + pageIndex);
          updatePaginationCounter();

          patientSearcherViewModel.searchPatientByFullName(text);

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

  private void openNewPatientModal() {
    setupOwner();
    viewHandler.openNewPatientView(owner);
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
      patientSearcherViewModel.isAutosearchProperty().bindBidirectional( autosearchToggle.selectedProperty() );

    boolean isAutoSearchIsOn =
        Boolean.parseBoolean(ClientPropertyReader.getString(PROPERTY_KEY_AUTOSEARCH_DEFAULT_VALUE));
    patientSearcherViewModel.isAutosearchProperty().set( isAutoSearchIsOn );
    autosearchToggle.setSelected(isAutoSearchIsOn);
    searchButton.visibleProperty().bind(patientSearcherViewModel.isAutosearchProperty().not());
    searchButton.disableProperty().bind(patientSearcherViewModel.isAutosearchProperty());
  }

  private void updateCurrentPatientLabel() {
    ObjectProperty<PatientVM> selectedPatientProperty =
        patientSearcherViewModel.selectedPatientProperty();
    selectedPatientProperty.addListener(
        (obs, oldVal, newVal) -> {
          if (newVal != null) {
            logger.debug("[CONTROLLER] Update patient labels. Patient id: {}", newVal.getId());
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
                logger.debug("[CONTROLLER] User choose patient: {}", newValue.getId());
                patientSearcherViewModel.setCurrentPatient(newValue);
                logger.debug(
                    "[CONTROLLER] Patient searcher - onSelectItemUpdateCurrentPatient {}",
                    patientSearcherViewModel.getSelectedPatient());
              } else {
                logger.debug("[CONTROLLER] User choose patient... new value is null");
              }
            });
  }

  private void bindTableWithViewModel() {
    resultTable.itemsProperty().bindBidirectional(patientSearcherViewModel.patientsProperty());
  }

  @FXML
  void onActionAddNewPatient(ActionEvent event) {
    openNewPatientModal();
  }

  public PatientSearcherViewModel getPatientSearcherViewModel() {
    return patientSearcherViewModel;
  }

  @FXML
  void onActionEditSelectedPatient(ActionEvent event) {
   setupOwner();
   viewHandler.openPatientEditView(owner, patientSearcherViewModel.getSelectedPatient());
  }

  @FXML
  void onActionPreviewSelectedPatient(ActionEvent event) {
   setupOwner();
   viewHandler.openPatientReadOnlyView(owner, patientSearcherViewModel.getSelectedPatient());
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

  public void disableSearcher() {
    searcherTextField.disableProperty().set(true);
  }

  public void animateForUserFocusWhenNoPatient() {
    if (searcherTextField.getText() == null || searcherTextField.getText().isEmpty()) {
      var timeline = AnimationsUtil.userCallToActionAnimation(searcherTextField);
      timeline.play();
    } else {
      var timeline = AnimationsUtil.userCallToActionAnimation(resultTable);
      timeline.play();
    }
  }

  public void animateSearcherForUserFocus() {
    var timeline = AnimationsUtil.userCallToActionAnimation(searcherTextField);
    timeline.play();
  }
}
