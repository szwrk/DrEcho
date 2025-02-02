package net.wilamowski.drecho.client.presentation.examinations.echo;

import java.util.Objects;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.stage.Stage;
import net.wilamowski.drecho.client.application.infra.GeneralViewHandler;
import net.wilamowski.drecho.client.presentation.customs.modals.ExceptionAlert;
import net.wilamowski.drecho.client.presentation.examinations.chooser.ExaminationDefinitionFx;
import net.wilamowski.drecho.client.presentation.examinations.chooser.ExaminationInstance;
import net.wilamowski.drecho.client.presentation.examinations.general.GeneralExaminationController;
import net.wilamowski.drecho.client.presentation.patients.PatientVM;
import net.wilamowski.drecho.app.bundle.Lang;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EchoTteExaminationInstance extends ExaminationInstance {
  public static final String VIEW_TO_OPEN_GENERAL_EXAM = "examination/GeneralExamination";
  private static final Logger logger = LogManager.getLogger(EchoTteExaminationInstance.class);
  private ObjectProperty<EchoTte> echoTte;

  public EchoTteExaminationInstance(
      Integer id,
      ExaminationDefinitionFx definition,
      ObjectProperty<PatientVM> patientFxObject,
      GeneralViewHandler viewHandler) {
    super(id, definition, patientFxObject, viewHandler);
  }

  @Override
  public void showForm() {
    logger.debug("Loading form window...");
    if (echoTte == null) {
      logger.debug("EchoTTe is null. Init value...");
      echoTte = new SimpleObjectProperty<>(EchoTte.newInstance());
    }

    logger.debug("EchoTTe is not null");
    GeneralExaminationController generalExaminationController;
    String examPath = null;
    Objects.requireNonNull(viewHandler);
    logger.debug("Selected examination: {}", definition);
    try {
      examPath = definition.getExamPath();
      Stage stage = new Stage();
      GeneralViewHandler.setupAsFullScreenModal(stage);
      stage.setTitle("Examination");
      Object controller = viewHandler.switchSceneForStage(VIEW_TO_OPEN_GENERAL_EXAM, stage);

      generalExaminationController = (GeneralExaminationController) controller;
      generalExaminationController.loadNestedExamByFxmlPath(examPath);
      logger.debug("patient.get() {}", patient.get());
      generalExaminationController.viewModel().patientProperty().set(patient.get());
      generalExaminationController.initPatientLabelBindigs();
      logger.debug(
          "generalExaminationController.viewModel( ).patientProperty( ) {}",
          generalExaminationController.viewModel().patientProperty());

      EchoController echoFormController =
          (EchoController) generalExaminationController.getFormController();
      echoFormController.reinitializeEchoFormProperties(echoTte.get());
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
}
