package net.wilamowski.drecho.client.presentation.notes;

import javafx.beans.property.SimpleStringProperty;
import net.wilamowski.drecho.gateway.NotesService;
public class NotesViewModel {
   private SimpleStringProperty interviewProperty;
   private SimpleStringProperty recommendationsProperty;
  private final NotesService notesService;

    public NotesViewModel(NotesService notesService) {
        interviewProperty =  new SimpleStringProperty(  );
        recommendationsProperty =  new SimpleStringProperty(  );
        this.notesService = notesService;
    }
    public SimpleStringProperty interviewProperty() {
        return interviewProperty;
    }

    public SimpleStringProperty recommendationsProperty() {
        return recommendationsProperty;
    }
}
