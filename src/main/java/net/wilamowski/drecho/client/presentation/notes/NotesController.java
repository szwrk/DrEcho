package net.wilamowski.drecho.client.presentation.notes;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import net.wilamowski.drecho.client.application.infra.ViewModelConfiguration;
import net.wilamowski.drecho.client.application.infra.ViewModelsInitializer;
import net.wilamowski.drecho.client.application.infra.controler_init.PostInitializable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NotesController implements Initializable, PostInitializable, ViewModelsInitializer {
    private static final Logger logger = LogManager.getLogger(NotesController.class);
    @FXML
    private TextArea interviewTextArea;
    @FXML
    private VBox notes;

    @FXML
    private TextArea recommTextArea;
    private NotesViewModel viewModel;

    public NotesController() {
    }

    public NotesViewModel getViewModel() {
        return viewModel;
    }


    @Override
    public void initialize(URL url , ResourceBundle resourceBundle) {
    }

    @Override
    public void postInitialize() {
        Bindings.bindBidirectional( interviewTextArea.textProperty(), viewModel.interviewProperty());
        Bindings.bindBidirectional( recommTextArea.textProperty(),viewModel.recommendationsProperty() );

    interviewTextArea
        .textProperty()
        .addListener(
            (obs, old, newVal) -> {
              System.out.println("Text area: " + newVal);
                System.out.println("binded propery: " + viewModel.interviewProperty());
            });

        recommTextArea
                .textProperty()
                .addListener(
                        (obs, old, newVal) -> {
                            System.out.println("Text area: " + newVal);
                            System.out.println("binded propery: " + viewModel.recommendationsProperty());
                        });
    }

    @Override
    public void initializeViewModels(ViewModelConfiguration factory) {
        NotesViewModel notesViewModel = factory.notesViewModel( );
        logger.debug( "[CONTROLLER] initialize NotesController with {}", notesViewModel );
        this.viewModel = notesViewModel;
    }
}

