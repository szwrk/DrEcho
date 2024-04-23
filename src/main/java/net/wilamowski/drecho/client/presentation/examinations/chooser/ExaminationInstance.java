package net.wilamowski.drecho.client.presentation.examinations.chooser;

import java.util.Objects;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import lombok.ToString;
import net.wilamowski.drecho.client.application.infra.GeneralViewHandler;
import net.wilamowski.drecho.client.presentation.patients.PatientVM;

@ToString
public abstract class ExaminationInstance {
  protected final SimpleIntegerProperty tempId;
  protected final ExaminationDefinitionFx definition;

  @ToString.Exclude
  protected final GeneralViewHandler viewHandler;
  protected final ObjectProperty<PatientVM> patient;

  public ExaminationInstance(
      Integer tempId,
      ExaminationDefinitionFx definition,
      ObjectProperty<PatientVM> patientFxObject,
      GeneralViewHandler viewHandler) {
    this.tempId = new SimpleIntegerProperty(tempId);
    this.patient = Objects.requireNonNull(patientFxObject);
    this.definition = definition;
    this.viewHandler = viewHandler;
  }

  public abstract void showForm();

  public SimpleIntegerProperty getTempId() {
    return tempId;
  }

  public ExaminationDefinitionFx getDefinition() {
    return definition;
  }
}
