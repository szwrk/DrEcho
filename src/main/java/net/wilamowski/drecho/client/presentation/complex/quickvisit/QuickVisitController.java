package net.wilamowski.drecho.client.presentation.complex.quickvisit;

import java.util.Objects;
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
import net.wilamowski.drecho.client.application.infra.ControllerInitializer;
import net.wilamowski.drecho.client.application.infra.GeneralViewHandler;
import net.wilamowski.drecho.client.application.infra.ViewModels;
import net.wilamowski.drecho.client.application.infra.ViewModelsInitializer;
import net.wilamowski.drecho.client.application.infra.controler_init.KeyEventDebugInitializer;
import net.wilamowski.drecho.client.application.infra.controler_init.PostInitializable;
import net.wilamowski.drecho.client.application.infra.controler_init.Tooltipable;
import net.wilamowski.drecho.client.presentation.debugger.DebugHandler;
import net.wilamowski.drecho.client.presentation.debugger.KeyDebugHandlerGui;
import net.wilamowski.drecho.client.presentation.examinations.chooser.ExaminationsChooserController;
import net.wilamowski.drecho.client.presentation.main.ViewHandlerInitializer;
import net.wilamowski.drecho.client.presentation.patients.PatientsSearcherController;
import net.wilamowski.drecho.client.presentation.visit.VisitController;
import net.wilamowski.drecho.client.presentation.visit.VisitDetailsViewModel;
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
        ViewModelsInitializer,
        ViewHandlerInitializer,
        PostInitializable,
        Tooltipable {
  private static final Logger logger = LogManager.getLogger(QuickVisitController.class);
  @FXML private RadioButton controlRadio;
  @FXML private TitledPane root;
  @FXML private RadioButton echoTteRadio;
  @FXML private TitledPane visit;
  @FXML private VisitController visitController;
  @FXML private TitledPane patient;
  @FXML private PatientsSearcherController patientController;
  @FXML private VBox examination;
  @FXML private ExaminationsChooserController examinationController;
  @FXML private TabPane tabPane;
  @FXML private Tab visitDetailTab;
  @FXML private Tab examinationsTab;
  @FXML private Tab summaryTab;
  @FXML private Button confirmButton;
  private ResourceBundle bundle;
  private ViewModels factory;
  private GeneralViewHandler handler;
  private VisitDetailsViewModel visitDetailsViewModel;

  public QuickVisitController() {}

  @FXML
  void onActionConfirmVisitDetails(ActionEvent event) {
    logger.debug("Clicked on confirm visit details...");
    tabPane.getSelectionModel().select(examinationsTab);
    visitDetailsViewModel.confirmVisit();
  }

  @Override
  public void initializeKeyEventDebugging() {
    DebugHandler debugHandler = new KeyDebugHandlerGui();
    debugHandler.initNode(root);
    debugHandler.watch(this);
  }

  @Override
  public void initializeViewModels(ViewModels factory) {
    this.visitDetailsViewModel = factory.visitViewModel( );
  }

  @Override
  public void postInitialize() {
    try {
      initializeNestedViewControllers();
      informVisitAboutSelectedPatient();
      informExaminationAboutSelectedPatient();
      requestFocusOnViewStart();
      fireConfirmButtonWhenPressKeyCombination();
      bindExaminationTabDisableToSelectedPatient();
      bindSummaryTabDisableToSelectedPatient( );
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
    Objects.requireNonNull(patientController);
    Objects.requireNonNull(examinationController);
    Objects.requireNonNull(visitController);
    Objects.requireNonNull(handler);

    ControllerInitializer initializer = GeneralViewHandler.initializer();
    initializer.initControllers(patientController, handler);
    initializer.initControllers(examinationController, handler);
    initializer.initControllers(visitController, handler);

    Objects.requireNonNull(visitController.getViewModel());
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
        .bind(visitController.getViewModel().getSelectedPatient());
  }

  private void informVisitAboutSelectedPatient() {
    visitController
        .getViewModel()
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
