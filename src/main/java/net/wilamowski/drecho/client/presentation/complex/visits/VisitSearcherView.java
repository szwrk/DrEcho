package net.wilamowski.drecho.client.presentation.complex.visits;

import atlantafx.base.controls.Popover;
import atlantafx.base.theme.Styles;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import lombok.ToString;
import net.wilamowski.drecho.client.application.infra.GeneralViewHandler;
import net.wilamowski.drecho.client.application.infra.ViewModelConfiguration;
import net.wilamowski.drecho.client.application.infra.ViewModelsInitializer;
import net.wilamowski.drecho.client.application.infra.controler_init.KeyEventDebugInitializer;
import net.wilamowski.drecho.client.application.infra.controler_init.PostInitializable;
import net.wilamowski.drecho.client.presentation.customs.ModalController;
import net.wilamowski.drecho.client.presentation.customs.PopoverFactory;
import net.wilamowski.drecho.client.presentation.customs.modals.Modal;
import net.wilamowski.drecho.client.presentation.debugger.DebugHandler;
import net.wilamowski.drecho.client.presentation.debugger.KeyDebugHandlerGui;
import net.wilamowski.drecho.client.presentation.main.ViewHandlerInitializer;
import net.wilamowski.drecho.client.presentation.patients.PatientVM;
import net.wilamowski.drecho.client.presentation.patients.PatientSearcherViewModel;
import net.wilamowski.drecho.client.presentation.patients.PatientsSearcherController;
import net.wilamowski.drecho.client.presentation.user.UserVM;
import net.wilamowski.drecho.client.presentation.visit.VisitVM;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Arkadiusz Wilamowski
 *     <p></><a href="https://github.com/szwrk">GitHub</a>
 *     <p>For questions or inquiries, at contact arek@wilamowski.net
 */
@ToString
public class VisitSearcherView
    implements ViewHandlerInitializer,
        Initializable,
        ViewModelsInitializer,
        KeyEventDebugInitializer,
        PostInitializable {
  private static final Logger logger = LogManager.getLogger(VisitSearcherView.class);
  private final ObservableList<VisitVM> visits = FXCollections.observableArrayList();
  private final DateTimeFormatter dataTimeFormatter =
      DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
  private Stage owner;
  @FXML private DatePicker saveDatePicker;
  @FXML private Button searchByDateButton;
  @FXML private TextField searchByPatientTextField;
  @FXML private Button searchByPatientButton;
  @FXML private TitledPane root;
  private GeneralViewHandler viewHandler;
  private ResourceBundle bundle;
  @FXML private TableView<VisitVM> visitTable;
  @FXML private TableColumn<VisitVM, UserVM> performerColumn;
  @FXML private TableColumn<VisitVM, LocalDateTime> realizationDateTimeColumn;
  @FXML private TableColumn<VisitVM, UserVM> registrantColumn;
  @FXML private TableColumn<VisitVM, PatientVM> patientCodePeselColumn;
  @FXML private TableColumn<VisitVM, PatientVM> patientColumn;
  private VisitDashboardViewModel visitDashboardViewModel;
  private PatientSearcherViewModel includedPatientSearcherViewModel;
  private Modal modal;
  private PatientsSearcherController includedPatientController;

  @FXML
  void onActionSearchByDate(ActionEvent event) {
    assert visitDashboardViewModel == null
        : "visitDashboardViewModel is unexpectedly null during user click on search by date in the visit view";
    if (saveDatePicker.valueProperty().get() != null) {
      visitDashboardViewModel.searchByDate(saveDatePicker.getValue(), 0);
    } else {
      showSearchByDateTipPopup();
      animateSearchByDateError();
    }
  }

  private void showSearchByDateTipPopup() {
    var searchDateTipPopup =
        PopoverFactory.createSimplePopover(
            "Information Required", "Tip: Select a date before searching");
    searchDateTipPopup.setArrowLocation(Popover.ArrowLocation.TOP_LEFT);
    searchDateTipPopup.show(saveDatePicker);

    PauseTransition pause = new PauseTransition(Duration.millis(3000));
    pause.setOnFinished(
        e -> {
          searchDateTipPopup.hide();
        });
    pause.play();
  }

  private void animateSearchByDateError() {
    saveDatePicker.pseudoClassStateChanged(Styles.STATE_DANGER, true);
    PauseTransition pause = new PauseTransition(Duration.millis(3000));
    pause.setOnFinished(
        e -> {
          saveDatePicker.pseudoClassStateChanged(Styles.STATE_DANGER, false);
        });
    pause.play();
  }

  @FXML
  void onActionSearchVisitsByPatient(ActionEvent event) {
    logger.debug("Clicked on search by patient");
    initializePatientModal( );
    String passedSearchValue = searchByPatientTextField.getText( );
    includedPatientController.inititalizeSearchValue( passedSearchValue );

    int findedMatchedPatients =
        includedPatientSearcherViewModel.searchPatientByFullName(
                passedSearchValue );
    logger.debug("foundMatchedPatients {}", findedMatchedPatients);

    if (findedMatchedPatients == 1) {
      visitDashboardViewModel.searchByPatient(
          includedPatientSearcherViewModel.getSelectedPatient(), 0);
    } else if (findedMatchedPatients == 0) {
      logger.debug("Found 0. Open and wait.");
      modal.showAndWaitWithBlur();

      visitDashboardViewModel.searchByPatient(
          includedPatientSearcherViewModel.getSelectedPatient(), 0);

    } else {
      logger.debug("Clearing search results and showing modal with blur");
      visitDashboardViewModel.clearSearchResults();
      modal.showAndWaitWithBlur();

      visitDashboardViewModel.searchByPatient(
          includedPatientSearcherViewModel.getSelectedPatient(), 0);
    }
  }

  private void initializePatientModal() {
    modal = Modal.setupPatientSearcherView(viewHandler, root);
    includedPatientController = getIncludedController(modal);
    includedPatientSearcherViewModel = getIncludedViewModel( includedPatientController );
  }


  private static PatientSearcherViewModel getIncludedViewModel(
      PatientsSearcherController includedPatientController) {
    return includedPatientController.getPatientSearcherViewModel();
  }

  private PatientsSearcherController getIncludedController(Modal modal) {
    ModalController rootModalController = (ModalController) modal.getModalController();
    PatientsSearcherController includedPatientController =
        (PatientsSearcherController) rootModalController.getIncludedController();
    return includedPatientController;
  }

  @Override
  public void initializeViewHandler(GeneralViewHandler viewHandler) {
    this.viewHandler = viewHandler;
  }

  @Override
  public void initializeViewModels(ViewModelConfiguration factory) {
    this.visitDashboardViewModel = factory.visitDashboardViewModel();
  }

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    this.bundle = resourceBundle;
  }

  @Override
  public void initializeKeyEventDebugging() {
    DebugHandler debugHandler = new KeyDebugHandlerGui();
    debugHandler.initNode(root);
    debugHandler.watch(this);
  }

  @Override
  public void postInitialize() {
    logger.traceEntry("VisitDashboardControler postinitialize()...");
    bindTableViewModel();
    configColumnValues();
    configColumnDisplay();
  }

  private void configColumnDisplay() {
    realizationDateTimeColumn.setCellFactory(
        column ->
            new TableCell<VisitVM, LocalDateTime>() {
              @Override
              protected void updateItem(LocalDateTime dateTime, boolean empty) {
                super.updateItem(dateTime, empty);

                if (dateTime == null || empty) {
                  setText(null);
                } else {
                  setText(dateTime.format(dataTimeFormatter));
                }
              }
            });
    performerColumn.setCellFactory(
        column ->
            new TableCell<VisitVM, UserVM>() {
              @Override
              protected void updateItem(UserVM user, boolean empty) {
                super.updateItem(user, empty);
                if (user == null || empty) {
                  setText(null);
                } else {
                  setText(user.getFullName().get());
                }
              }
            });

    registrantColumn.setCellFactory(
        column ->
            new TableCell<VisitVM, UserVM>() {
              @Override
              protected void updateItem(UserVM user, boolean empty) {
                super.updateItem(user, empty);
                if (user == null || empty) {
                  setText(null);
                } else {
                  setText(user.getFullName().get());
                }
              }
            });

    patientColumn.setCellFactory(
        column ->
            new TableCell<VisitVM, PatientVM>() {
              @Override
              protected void updateItem(PatientVM patient, boolean empty) {
                super.updateItem(patient, empty);
                if (patient == null || empty) {
                  setText(null);
                } else {
                  setText(patient.getLastName().getValue() + " " + patient.getName().getValue());
                }
              }
            });

    patientCodePeselColumn.setCellFactory(
        column ->
            new TableCell<VisitVM, PatientVM>() {
              @Override
              protected void updateItem(PatientVM patient, boolean empty) {
                super.updateItem(patient, empty);
                if (patient == null || empty) {
                  setText(null);
                } else {
                  setText(patient.getPesel().get());
                }
              }
            });
  }

  private void configColumnValues() {
    performerColumn.setCellValueFactory(cellData -> cellData.getValue().getSelectedPerformer());
    realizationDateTimeColumn.setCellValueFactory(
        cellData -> cellData.getValue().getRealizationDateTimeProperty());
    registrantColumn.setCellValueFactory(cellData -> cellData.getValue().getSelectedRegistrant());
    patientColumn.setCellValueFactory(cellData -> cellData.getValue().getSelectedPatient());
    patientCodePeselColumn.setCellValueFactory(
        cellData -> cellData.getValue().getSelectedPatient());
    visitTable.setPlaceholder(new Label("Sorry, no visits match your criteria."));
  }

  private void bindTableViewModel() {
    visitTable.setItems(visitDashboardViewModel.getVisits());
  }
}
