package net.wilamowski.drecho.client.presentation.notes;

import javafx.beans.property.SimpleStringProperty;
import net.wilamowski.drecho.connectors.model.ConnectorNotes;
public class NotesViewModel {
   private SimpleStringProperty interviewProperty;
   private SimpleStringProperty recommendationsProperty;
  private final ConnectorNotes notesService;

    public NotesViewModel(ConnectorNotes connectorNotes) {
        interviewProperty =  new SimpleStringProperty(  );
        recommendationsProperty =  new SimpleStringProperty(  );
        this.notesService = connectorNotes;
    }
    public SimpleStringProperty interviewProperty() {
        return interviewProperty;
    }

    public SimpleStringProperty recommendationsProperty() {
        return recommendationsProperty;
    }
}
