package net.wilamowski.drecho.client.presentation.complex.quickvisit;

import java.util.Objects;
import java.util.Optional;
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
import net.wilamowski.drecho.client.application.exceptions.GeneralUiException;
import net.wilamowski.drecho.client.application.exceptions.VisitVmNoPatientSelected;
import net.wilamowski.drecho.client.application.infra.ControllerInitializer;
import net.wilamowski.drecho.client.application.infra.GeneralViewHandler;
import net.wilamowski.drecho.client.application.infra.controler_init.KeyEventDebugInitializer;
import net.wilamowski.drecho.client.application.infra.controler_init.PostInitializable;
import net.wilamowski.drecho.client.application.infra.controler_init.Tooltipable;
import net.wilamowski.drecho.client.presentation.Views;
import net.wilamowski.drecho.client.presentation.customs.animations.AnimationsUtil;
import net.wilamowski.drecho.client.presentation.customs.modals.ExceptionAlert;
import net.wilamowski.drecho.client.presentation.customs.modals.UserAlert;
import net.wilamowski.drecho.client.presentation.customs.modals.UserDialog;
import net.wilamowski.drecho.client.presentation.debugger.DebugHandler;
import net.wilamowski.drecho.client.presentation.debugger.KeyDebugHandlerGui;
import net.wilamowski.drecho.client.presentation.examinations.chooser.ExaminationsChooserController;
import net.wilamowski.drecho.client.presentation.examinations.chooser.ExaminationsChooserViewModel;
import net.wilamowski.drecho.client.presentation.main.ViewHandlerInitializer;
import net.wilamowski.drecho.client.presentation.notes.NotesController;
import net.wilamowski.drecho.client.presentation.notes.NotesViewModel;
import net.wilamowski.drecho.client.presentation.patients.PatientSearcherViewModel;
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
    implements KeyEventDebugInitializer, ViewHandlerInitializer, PostInitializable, Tooltipable {
  public static final String SAVED = "SAVED";
  private static final Logger logger = LogManager.getLogger(QuickVisitController.class);
  @FXML private RadioButton controlRadio;
  @FXML private TitledPane root;
  @FXML private RadioButton echoTteRadio;
  @FXML private TitledPane visit;

  /** Included Controller */
  @FXML private VisitController visitController;
  @FXML private TitledPane patient;

  /** Included Controller */
  @FXML private PatientsSearcherController patientController;
  @FXML private VBox examination;

  /** Included Controller */
  @FXML private ExaminationsChooserController examinationController;

  /** Included Controller */
  @FXML private NotesController notesController;
  @FXML private VBox notes;

  @FXML private TabPane tabPane;
  @FXML private Tab visitDetailTab;
  @FXML private Tab examinationsTab;
  @FXML private Tab summaryTab;
  @FXML private Tab notesTab;

  @FXML private Button confirmButton;
  private GeneralViewHandler viewHandler;
  @FXML private Label notesTabLabel;
  private UserDialog finalDialog;

  @FXML
  void onActionConfirmVisitDetails(ActionEvent event) {
    logger.debug("[CONTROLLER] Clicked on confirm visit details...");
    loggerDebugControllersMemoryAddresses();
    validateIsPatientExist();
    validateVisitViewModel( );
    Optional<VisitDtoResponse> visitDtoResponseOptional = tryConfirmVisit( );
    visitDtoResponseOptional.ifPresent(this::handleSuccessfulVisitConfirmation);
  }
  private Optional<VisitDtoResponse> tryConfirmVisit() {
    Optional<VisitDtoResponse> visitDtoResponseOptional = Optional.empty();
    try {
      visitDtoResponseOptional = nestedVisitVM().confirmVisit();
    } catch ( IllegalStateException | NullPointerException e) {
      logger.error(e.getMessage(), e);
      ExceptionAlert alert = ExceptionAlert.create();
      alert.showError(e, Lang.getString("e.000.header"), Lang.getString("e.000.msg"));
    } catch (VisitVmNoPatientSelected ise) {
      patientController.animateForUserFocusWhenNoPatient();
    } catch (GeneralUiException vce) {
      logger.error("VisitVmValidationException: " + vce.getMessage(), vce);
      ExceptionAlert alert = ExceptionAlert.create();
      alert.showError(vce, vce.getHeader(), vce.getMessage());
      logger.debug(
          "[CONTROLLER] Visit VM state: \nPatient:\n{};\nPerformer:\n{};\nRegistrant:\n{} ",
          nestedVisitVM().selectedPatientVm(),
          nestedVisitVM().getSelectedPerformer(),
          nestedVisitVM().getSelectedPerformer());
      }
    return visitDtoResponseOptional;
  }

  private void validateVisitViewModel() {
    try {
      Objects.requireNonNull( nestedVisitVM(),"Visit VM is null");
    } catch (NullPointerException e) {
      logger.error(e.getMessage(), e);
      ExceptionAlert alert = ExceptionAlert.create();
      alert.showError(e, Lang.getString("e.000.header"), Lang.getString("e.000.msg"));
    }
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
    UserDialog dialog = null;
    UserDialog finalDialog = dialog;
    dialog =
        UserDialog.builder()
            .title("Information")
            .header(Lang.getString("u.004.header"))
            .content(Lang.getString("u.004.msg") + "\nID: " + visitDto.visitId())
            .details(visitDto.toString())
            .addButton(
                "OK",
                () -> {
                  if (finalDialog != null) {
                    finalDialog.close();
                  }
                  confirmButton.setDisable( true );
                  Tooltip.install(confirmButton, new Tooltip("The visit has been successfully saved. You can now add some visit context or add another new visit. If you need to edit the current visit, use the Update Visit Info button."));
                  var timeline = AnimationsUtil.userCallToActionAnimation(notesTabLabel);
                  timeline.play();
                })
            .addButton("Add another Visit", () -> viewHandler.switchSceneForParent("QuickVisit"))
            .build();
    dialog.showAndWait();
  }

  private void validateIsPatientExist() {
    logger.trace("[CONTROLLER] Enter validate patient exist...");
    if (currentPatient() == null) {
      UserAlert.simpleWarn(
              Lang.getString( "u.005.header" ),
              Lang.getString( "u.005.msg" ))
          .showAndWait();
    }
    logger.trace("[CONTROLLER] Exiting validate patient");
  }

  private PatientVM currentPatient() {
    return nestedPatientVm().selectedPatientProperty().getValue();
  }

  private PatientSearcherViewModel nestedPatientVm() {
    return patientController.getPatientSearcherViewModel();
  }

  private VisitViewModel nestedVisitVM() {
    return visitController.getVisitViewModel();
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
      setupNestedViewControllers();
      updateVisitAboutSelectPatient();
      informExaminationAboutSelectedPatient();
      requestFocusOnViewStart();
      fireConfirmButtonWhenPressKeyCombination();
      bindExaminationTabDisableToSelectedPatient();
      bindSummaryTabDisableToSelectedPatient();
      bindNotesTabDisableToSelectedPatient();

    } catch (Exception e) {
      logger.error(e.getMessage(), e);
    }
  }

  private void bindNotesTabDisableToSelectedPatient() {
    notesTab
            .disableProperty()
            .bind(nestedPatientVm().isPatientNull());
  }

  private void bindSummaryTabDisableToSelectedPatient() {
    summaryTab
        .disableProperty()
        .bind(nestedPatientVm().isPatientNull());
  }

  private void bindExaminationTabDisableToSelectedPatient() {
    examinationsTab
        .disableProperty()
        .bind(nestedPatientVm().isPatientNull());
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

  private void setupNestedViewControllers() {
    validateControllers();
    loggerDebugControllersMemoryAddresses();
    initializeNestedControllers();
    loggerDebugControllersMemoryAddresses();
    validateViewModelsAfterInitControllers();
  }

  /**
   * Initializes nested controllers (View Models etc.)
   * To add additional nested views, extend this method by providing the necessary controller instance.
   * {@code initControllers}
   */
  private void initializeNestedControllers() {
    ControllerInitializer initializer = GeneralViewHandler.initializer();
    initializer.initController(patientController, viewHandler);
    initializer.initController(examinationController, viewHandler);
    initializer.initController(visitController, viewHandler);
    initializer.initController(notesController, viewHandler);
  }

  private void validateViewModelsAfterInitControllers() {
    try {
      Objects.requireNonNull(visitController.getVisitViewModel(),"View model is null. Use ControllerInitializer.");
      Objects.requireNonNull(nestedPatientVm(),"View model is null. Use ControllerInitializer.");
      Objects.requireNonNull( nestedExaminationsVM( ) ,"View model is null. Use ControllerInitializer.");
      Objects.requireNonNull(notesController.getViewModel(),"View model is null. Use ControllerInitializer.");
    } catch (NullPointerException e) {
      logger.error("Failed to initialize Quick Visit view nested components. Application crash.");
      throw new IllegalStateException(
          "Failed to initialize nested components view models. Application crash.", e);
    }
  }

  private ExaminationsChooserViewModel nestedExaminationsVM() {
    return examinationController.getExaminationViewModel( );
  }

  private void validateControllers() {
    try {
      Objects.requireNonNull(patientController, "Patient controller initialization failed!");
      Objects.requireNonNull(
          examinationController, "Examination controller initialization failed!");
      Objects.requireNonNull(visitController, "Visit controller initialization failed!");
      Objects.requireNonNull(notesController, "Notes controller initialization failed!");
      Objects.requireNonNull(viewHandler, "General Handler initialization failed!");
    } catch (NullPointerException e) {
      logger.error("Failed to initialize Quick Visit view nested components. Application crash.");
      throw new IllegalStateException(
          "Failed to initialize nested components controllers. Application crash.", e);
    }
  }

  private void requestFocusOnViewStart() {
    Platform.runLater(
        () -> {
          root.requestFocus();
          patientController.searcherReguestFocus();
        });
  }

  private void informExaminationAboutSelectedPatient() {
    nestedExaminationsVM( )
        .selectedPatientProperty()
        .bind( nestedVisitVM().getSelectedPatient());
  }

  private void updateVisitAboutSelectPatient() {
    nestedVisitVM()
        .getSelectedPatient()
        .bind(nestedPatientVm().selectedPatientProperty());
  }

  @Override
  public void initializeViewHandler(GeneralViewHandler viewHandler) {
    this.viewHandler = viewHandler;
  }

  @Override
  public Node getRootUiNode() {
    return root;
  }

  public void onActionFinishVisit(ActionEvent event) {
    logger.trace("Clicked on finish visit");
    try{
      Objects.requireNonNull(notesController, "NotesController is null");
      Objects.requireNonNull(notesController.getViewModel(), "NotesController viewmodel is null");
      Objects.requireNonNull(nestedVisitVM().getSelectedPatient(), "VisitVM: selected patient is null");
    } catch (NullPointerException npe){
      logger.error( npe.getMessage(),npe );
    }
    disableAllControl( );
    openDialog( );
  }

  private void openDialog() {
    UserDialog dialog      = null;
    finalDialog = dialog;
    dialog = UserDialog.builder( )
              .title( "Message" )
              .header("Successful")
              .content( "Visit was added.\nWhat would you like to do next?" )
              .details( "" )
              .addButton( "Stay here for preview" , () -> {
                  finalDialog.close();
              } )
              .addButton("Leave and add another Visit", ()-> viewHandler.switchSceneForParent( Views.QUICK_VISIT.getFxmlFileName() ) )
              .build();
    dialog.showAndWait();
  }

  private void disableAllControl() {
    visit.setDisable( true );
    patient.setDisable( true );
    examination.setDisable( true );
  }

  private void logDebugNotes() {
    logger.debug(
        "1 {}\n2 {}\n3 {}"
            , nestedExaminationsVM( ).getListOfVisitExamination().toString()
            , nestedNotesVm( ).interviewProperty().get()
            , nestedNotesVm( ).recommendationsProperty().get());
  }

  private NotesViewModel nestedNotesVm() {
    return notesController.getViewModel( );
  }
}
