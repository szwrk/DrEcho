package net.wilamowski.drecho.client.presentation.examinations.general;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.util.Objects;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;
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
import net.wilamowski.drecho.client.presentation.main.ViewHandlerInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@ToString
public class GeneralExaminationController
    implements ViewHandlerInitializer,
        KeyEventDebugInitializer,
        ViewModelsInitializer,
        Tooltipable,
        PostInitializable {
  private static final Logger logger = LogManager.getLogger(GeneralExaminationController.class);
  @FXML private ScrollPane root;
  @FXML private Label detailVisitLabel;
  @FXML private Label doctorNameLabel;
  @FXML private Label patientLabel;

  private Parent includedNode;
  private Object includedController;
  private GeneralViewHandler handler;

  private GeneralExaminatiomViewModel generalExaminatiomViewModel;
  private ViewModels factory;

  public GeneralExaminationController() {}

  public static int calculateAge(LocalDate birthdate, LocalDate currentDate) {
    if ((birthdate != null) && (currentDate != null)) {
      return Period.between(birthdate, currentDate).getYears();
    } else {
      throw new IllegalArgumentException("Birthdate and current date cannot be null.");
    }
  }

  public void loadNestedExamByFxmlPath(String subView) throws IOException {
    logger.debug("Loading nested examination. Param: {}", subView);
    FXMLLoader loader = GeneralViewHandler.createFxmlLoader( subView );
    includedNode = loader.load();
    includedController = loader.getController();
    ControllerInitializer initializer = GeneralViewHandler.initializer();
    initializer.initControllers(includedController, handler);
    this.root.setContent(includedNode);
  }

  @Override
  public void initializeKeyEventDebugging() {
    logger.traceEntry();
    DebugHandler debugHandler = new KeyDebugHandlerGui();
    debugHandler.initNode(Objects.requireNonNull(root));
    debugHandler.watch(this, includedController);
    logger.traceExit();
  }

  @Override
  public void initializeViewHandler(GeneralViewHandler viewHandler) {
    this.handler = viewHandler;
  }

  @Override
  public void initializeViewModels(ViewModels factory) {
    this.factory = factory;
    generalExaminatiomViewModel = factory.generalExaminatiomViewModel();
  }

  public GeneralExaminatiomViewModel viewModel() {
    return generalExaminatiomViewModel;
  }

  @Override
  public Node getRootUiNode() {
    return root;
  }

  public void onActionSave(ActionEvent event) {
    logger.debug("Clicked on Save button");
  }

  public void onActionHideWindow(ActionEvent event) {
    logger.debug("Clicked on Hide button");
    Stage stage = (Stage) root.getScene().getWindow();
    stage.close();
  }

  public Object getFormController() {
    return includedController;
  }

  public void onActionClearForm(ActionEvent event) {}

  @Override
  public void postInitialize() {}

  public void initPatientLabelBindigs() {
    if (generalExaminatiomViewModel.patientProperty().getValue() != null) {
      patientLabel
          .textProperty()
          .bind(
              Bindings.concat(
                  generalExaminatiomViewModel.patientProperty().getValue().getName(),
                  " ",
                  generalExaminatiomViewModel.patientProperty().getValue().getLastName()));
    } else {
      logger.debug("User dont choose patient before choosing examination so patient is null now");
    }
    //todo calculate age, add label
//    if (generalExaminatiomViewModel.patientProperty().getValue().getDateBirth() != null) {
//      calculateAge(
//          generalExaminatiomViewModel.patientProperty().getValue().getDateBirth().getValue(),
//          LocalDate.now());
//    }
  }
}
