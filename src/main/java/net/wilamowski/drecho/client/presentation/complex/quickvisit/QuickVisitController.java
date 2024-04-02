package net.wilamowski.drecho.client.presentation.complex.quickvisit;

import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.VBox;
import lombok.ToString;
import net.wilamowski.drecho.client.application.exceptions.VisitVmValidationException;
import net.wilamowski.drecho.client.application.infra.ControllerInitializer;
import net.wilamowski.drecho.client.application.infra.GeneralViewHandler;
import net.wilamowski.drecho.client.application.infra.ViewModelConfiguration;
import net.wilamowski.drecho.client.application.infra.controler_init.KeyEventDebugInitializer;
import net.wilamowski.drecho.client.application.infra.controler_init.PostInitializable;
import net.wilamowski.drecho.client.application.infra.controler_init.Tooltipable;
import net.wilamowski.drecho.client.presentation.customs.modals.ExceptionAlert;
import net.wilamowski.drecho.client.presentation.customs.modals.UserAlert;
import net.wilamowski.drecho.client.presentation.debugger.DebugHandler;
import net.wilamowski.drecho.client.presentation.debugger.KeyDebugHandlerGui;
import net.wilamowski.drecho.client.presentation.examinations.chooser.ExaminationsChooserController;
import net.wilamowski.drecho.client.presentation.main.ViewHandlerInitializer;
import net.wilamowski.drecho.client.presentation.patients.PatientVM;
import net.wilamowski.drecho.client.presentation.patients.PatientsSearcherController;
import net.wilamowski.drecho.client.presentation.visit.VisitController;
import net.wilamowski.drecho.client.presentation.visit.VisitViewModel;
import net.wilamowski.drecho.shared.bundle.Lang;
import net.wilamowski.drecho.shared.dto.VisitDtoResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Arkadiusz Wilamowski
 *     <p></><a href="https://github.com/szwrk">GitHub</a>
 *     <p>For questions or inquiries, at contact arek@wilamowski.net
 */
@ToString
public class QuickVisitController
    implements KeyEventDebugInitializer,
        //        ViewModelsInitializer,
        ViewHandlerInitializer,
        PostInitializable,
        Tooltipable {
  private static final Logger logger = LogManager.getLogger(QuickVisitController.class);
  @FXML private RadioButton controlRadio;
  @FXML private TitledPane root;
  @FXML private RadioButton echoTteRadio;
  @FXML private TitledPane visit;
  /**Included Controller*/
  @FXML private VisitController visitController;
  @FXML private TitledPane patient;
  /**Included Controller*/
  @FXML private PatientsSearcherController patientController;
  @FXML private VBox examination;
  /**Included Controller*/
  @FXML private ExaminationsChooserController examinationController;
  @FXML private TabPane tabPane;
  @FXML private Tab visitDetailTab;
  @FXML private Tab examinationsTab;
  @FXML private Tab summaryTab;
  @FXML private Button confirmButton;
  private ResourceBundle bundle;
  private ViewModelConfiguration factory;
  private GeneralViewHandler handler;

  @FXML
  void onActionConfirmVisitDetails(ActionEvent event) {
    logger.debug("[CONTROLLER] Clicked on confirm visit details...");
    loggerDebugControllersMemoryAddresses();
    validateIsPatientExist();
    Optional<VisitDtoResponse> visitDtoResponseOptional = Optional.empty();
    try {
      Objects.requireNonNull(visitVM());
    } catch (NullPointerException e) {
      logger.error(e.getMessage(), e);
      ExceptionAlert alert = ExceptionAlert.create();
      alert.showError(e, Lang.getString("e.000.header"), Lang.getString("e.000.msg"));
    }

    try {
      visitDtoResponseOptional = visitVM().confirmVisit();
    } catch (IllegalStateException | NullPointerException e) {
      logger.error(e.getMessage(), e);
      ExceptionAlert alert = ExceptionAlert.create();
      alert.showError(e, Lang.getString("e.000.header"), Lang.getString("e.000.msg"));
    } catch (VisitVmValidationException vce) {
      logger.error(vce.getMessage(), vce);
      ExceptionAlert alert = ExceptionAlert.create();
      alert.showError(vce, vce.getHeader(), vce.getContent());
    }
      visitDtoResponseOptional.ifPresent( this::handleSuccessfulVisitConfirmation );
  }

  private VisitViewModel visitVM() {
    return visitController.getVisitViewModel();
  }

  private void loggerDebugControllersMemoryAddresses() {
    logger.debug(
        "[CONTROLLER] Memory addresses of objects: patientController={}, examinationController={}, visitController={}",
        System.identityHashCode(patientController),
        System.identityHashCode(examinationController),
        System.identityHashCode(visitController));
  }

  private void handleSuccessfulVisitConfirmation(VisitDtoResponse visitDto) {
    logger.trace("[CONTROLLER] Handling successful confirmation of visit response...");
    UserAlert alert = new UserAlert();
    alert.showInfo(
        Lang.getString("u.004.header"), Lang.getString("u.004.msg") + "\nID: " + visitDto.visitId(), visitDto.toString());
    tabPane.getSelectionModel().select(examinationsTab);
  }

  private void validateIsPatientExist() {
    logger.trace("[CONTROLLER] Enter validate patient exist...");
    if (currentPatient() == null) {
      UserAlert alert = new UserAlert();
      alert.showWarn(
          "Visit warning", "Patient not selected. Use the patient searcher or add a new patient.");
    }
    logger.trace("[CONTROLLER] Exiting validate patient");
  }

  private PatientVM currentPatient() {
    return patientController.getPatientSearcherViewModel().selectedPatientProperty().getValue();
  }

  @Override
  public void initializeKeyEventDebugging() {
    DebugHandler debugHandler = new KeyDebugHandlerGui();
    debugHandler.initNode(root);
    debugHandler.watch(this);
  }

  @Override
  public void postInitialize() {
    logger.trace("QuickVisitController postInitialize...");
    try {
      initializeNestedViewControllers();
      informExaminationAboutSelectedPatient();
      requestFocusOnViewStart();
      fireConfirmButtonWhenPressKeyCombination();
      bindExaminationTabDisableToSelectedPatient();
      bindSummaryTabDisableToSelectedPatient();
      updateVisitAboutSelectPatient();
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
    }
  }

  private void bindSummaryTabDisableToSelectedPatient() {
    summaryTab
        .disableProperty()
        .bind(patientController.getPatientSearcherViewModel().selectedPatientProperty().isNull());
  }

  private void bindExaminationTabDisableToSelectedPatient() {
    examinationsTab
        .disableProperty()
        .bind(patientController.getPatientSearcherViewModel().selectedPatientProperty().isNull());
  }

  private void fireConfirmButtonWhenPressKeyCombination() {
    root.setOnKeyPressed(
        e -> {
          if (new KeyCodeCombination(KeyCode.ENTER, KeyCombination.SHIFT_DOWN).match(e)) {
            logger.debug("User pressed key combination for fire confirm button");
            confirmButton.fire();
          }
        });
  }

  private void initializeNestedViewControllers() {
    try {
      Objects.requireNonNull(patientController, "Patient controller initialization failed!");
      Objects.requireNonNull(
          examinationController, "Examination controller initialization failed!");
      Objects.requireNonNull(visitController, "Visit controller initialization failed!");
      Objects.requireNonNull(handler, "General Handler initialization failed!");
    } catch (NullPointerException e) {
      logger.error("Failed to initialize Quick Visit view nested components. Application crash.");
      throw new IllegalStateException(
          "Failed to initialize nested components. Application crash.", e);
    }
    loggerDebugControllersMemoryAddresses();
    ControllerInitializer initializer = GeneralViewHandler.initializer();
    initializer.initControllers(patientController, handler);
    initializer.initControllers(examinationController, handler);
    initializer.initControllers(visitController, handler);
    loggerDebugControllersMemoryAddresses();
    Objects.requireNonNull(visitController.getVisitViewModel());
    Objects.requireNonNull(patientController.getPatientSearcherViewModel());
    Objects.requireNonNull(examinationController.getExaminationViewModel());
  }

  private void requestFocusOnViewStart() {
    Platform.runLater(
        () -> {
          root.requestFocus();
          patientController.searcherReguestFocus();
        });
  }

  private void informExaminationAboutSelectedPatient() {
    examinationController
        .getExaminationViewModel()
        .selectedPatientProperty()
        .bind(visitVM().getSelectedPatient());
  }

  private void updateVisitAboutSelectPatient() {
    visitVM()
        .getSelectedPatient()
        .bind(patientController.getPatientSearcherViewModel().selectedPatientProperty());
  }

  @Override
  public void initializeViewHandler(GeneralViewHandler viewHandler) {
    this.handler = viewHandler;
  }

  @Override
  public Node getRootUiNode() {
    return root;
  }

  public void onActionSaveVisit(ActionEvent event) {
    logger.trace("Clicked on save visit");
  }
}
