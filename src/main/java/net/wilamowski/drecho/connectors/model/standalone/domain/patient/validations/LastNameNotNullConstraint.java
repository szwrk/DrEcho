package net.wilamowski.drecho.connectors.model.standalone.domain.patient.validations;

import net.wilamowski.drecho.connectors.model.standalone.domain.patient.Patient;

class LastNameNotNullConstraint implements Constraint {
  private final Patient patient;

  public LastNameNotNullConstraint(Patient patient) {
    this.patient = patient;
  }

  @Override
  public boolean validate() {
    if (!patient.getLastName().isEmpty()
    ) {
      return true;
    }
    return false;
  }

  @Override
  public String errorMessage() {
    return "Last name are required fields.";
  }
}