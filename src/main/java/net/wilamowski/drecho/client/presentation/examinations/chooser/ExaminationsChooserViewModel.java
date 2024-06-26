package net.wilamowski.drecho.client.presentation.examinations.chooser;

import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.ToString;
import net.wilamowski.drecho.client.presentation.patients.PatientVM;

@ToString
public class ExaminationsChooserViewModel {
  private final Object examinationsModel;
  private final ObjectProperty<PatientVM> patient = new SimpleObjectProperty<>();

  private final Property<ObservableList<ExaminationInstance>> chosenExams =
      new SimpleObjectProperty<>(FXCollections.observableArrayList());

  public ExaminationsChooserViewModel(Object examinationsModel) {
    this.examinationsModel = examinationsModel;
  }

  public ObjectProperty<PatientVM> selectedPatientProperty() {
    return patient;
  }

  public Property<ObservableList<ExaminationInstance>> getChosenExamination() {
    return chosenExams;
  }

   public List<ExaminationInstance> getVisitExaminationsInstances(){
     ObservableList<ExaminationInstance> chosenExamsList = chosenExams.getValue();
     return new ArrayList<>(chosenExamsList);
   }
}
