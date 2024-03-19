package net.wilamowski.drecho.client.presentation.examinations.chooser;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.ToString;
import net.wilamowski.drecho.client.presentation.patients.PatientFx;

@ToString
public class ExaminationsChooserViewModel {
  private final Object examinationsModel;
  private final ObjectProperty<PatientFx> patient = new SimpleObjectProperty<>();

  private final Property<ObservableList<ExaminationInstance>> choosedExams =
      new SimpleObjectProperty<>(FXCollections.observableArrayList());

  public ExaminationsChooserViewModel(Object examinationsModel) {
    this.examinationsModel = examinationsModel;
  }

  public ObjectProperty<PatientFx> selectedPatientProperty() {
    return patient;
  }

  public Property<ObservableList<ExaminationInstance>> getChosenExamination() {
    return choosedExams;
  }
}
