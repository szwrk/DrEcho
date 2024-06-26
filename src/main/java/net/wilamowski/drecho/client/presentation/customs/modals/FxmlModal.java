package net.wilamowski.drecho.client.presentation.customs.modals;

import javafx.scene.Parent;
import javafx.stage.Stage;
import net.wilamowski.drecho.client.application.infra.GeneralViewHandler;
import net.wilamowski.drecho.client.presentation.customs.ModalController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FxmlModal {
  private static final Logger logger = LogManager.getLogger( FxmlModal.class);
  private static final String MODAL_NAME_PATIENT_SEARCHER = "Modal";
  private final GeneralViewHandler viewHandler;
  private final Stage modal;
  private final Parent modalRoot;
  private final String subviewPath;
  //    private final Object modalController;
  //    private final ResourceBundle bundle;
  //    private Node includedNode;
  //    private Object includedController;
  private Stage owner;
  private ModalController modalController;

  private FxmlModal(GeneralViewHandler handler, Parent modalRoot, String pathToSubview) {
    this.modalRoot = modalRoot;
    this.viewHandler = handler;
    this.modal = new Stage();
    this.subviewPath = pathToSubview;
    setupModal();
    //        this.bundle = bundle;
    initializeOwnerStage(modalRoot);
  }

  private void setupModal() {
    GeneralViewHandler.setupStageTitle(modal, MODAL_NAME_PATIENT_SEARCHER);
    Object controller = viewHandler.switchSceneForStage("modal/SimpleModal", modal);
    modalController = (ModalController) controller;
    modalController.loadNestedExamByFxmlPath(subviewPath);

    modalController.addConfirmButton(
        event -> {
          GeneralViewHandler.disableBlur(owner);
          modal.close();
        });
  }

  private void initializeOwnerStage(Parent modalRoot) {
    this.owner = (Stage) modalRoot.getScene().getWindow();
  }

  public static FxmlModal setupPatientSearcherView(
      GeneralViewHandler handler, Parent windowOwner) {
    final String pathToSubview = "patient/PatientsSearcherView.fxml";
    return new FxmlModal(handler, windowOwner, pathToSubview);
  }

  public void showWithBlur() {
    GeneralViewHandler.setupAsBlurModal(modal, owner);
    modal.show();
  }

  public void showAndWaitWithBlur() {
    GeneralViewHandler.setupAsBlurModal(modal, owner);
    modal.showAndWait();
  }

  public void close() {
    GeneralViewHandler.disableBlur(owner);
    modal.close();
  }

  public Object getModalController() {
    return modalController;
  }
}
