package net.wilamowski.drecho.client.presentation.customs.modals;

import javafx.scene.Parent;
import javafx.stage.Stage;
import net.wilamowski.drecho.client.presentation.customs.SimpleModalController;
import net.wilamowski.drecho.client.application.infra.GeneralViewHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SimpleModal {
private static final Logger logger = LogManager.getLogger(SimpleModal.class);
    private final GeneralViewHandler viewHandler;
    private final Stage modal;
    private static final String MODAL_NAME_PATIENT_SEARCHER = "Modal";
    private final Parent modalRoot;
      private final String subviewPath;
//    private final Object modalController;
//    private final ResourceBundle bundle;
//    private Node includedNode;
//    private Object includedController;
    private Stage owner;
    private SimpleModalController simpleModalController;

    private SimpleModal(GeneralViewHandler handler, Parent modalRoot, String pathToSubview) {
        this.modalRoot = modalRoot;
        this.viewHandler = handler;
        this.modal = new Stage(  );
        this.subviewPath = pathToSubview;
        setupModal( );
//        this.bundle = bundle;
        initializeOwnerStage( modalRoot );
    }

    public static SimpleModal setupPatientSearcherView(GeneralViewHandler handler, Parent windowOwner){
               final String pathToSubview = "patient/PatientsSearcherView.fxml";
        return new SimpleModal( handler , windowOwner , pathToSubview );
    }

    private void setupModal() {
        GeneralViewHandler.setupStageTitle( modal , MODAL_NAME_PATIENT_SEARCHER);
        Object                controller            = viewHandler.switchSceneForStage("modal/SimpleModal", modal );
        simpleModalController = (SimpleModalController) controller;
        simpleModalController.loadNestedExamByFxmlPath(subviewPath);

        simpleModalController.addConfirmButton(
                event -> {
                    GeneralViewHandler.disableBlur( owner );
                    modal.close();
                });
    }


    public void showWithBlur(){
        GeneralViewHandler.setupAsBlurModal( modal , owner);
        modal.show();
    }

    public void close(){
        GeneralViewHandler.disableBlur(owner  );
        modal.close();
    }

    public Object getModalController(){
        return simpleModalController;
    }

    private void initializeOwnerStage(Parent modalRoot) {
        this.owner = (Stage) modalRoot.getScene().getWindow();
    }
}
