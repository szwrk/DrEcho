package net.wilamowski.drecho.client.presentation.examinations.general;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import lombok.ToString;
import net.wilamowski.drecho.client.presentation.patients.PatientVM;

@ToString
public class GeneralExaminatiomViewModel {
  private final ObjectProperty<PatientVM> patient = new SimpleObjectProperty<>();

  public GeneralExaminatiomViewModel() {}

  public ObjectProperty<PatientVM> patientProperty() {
    return patient;
  }
}
