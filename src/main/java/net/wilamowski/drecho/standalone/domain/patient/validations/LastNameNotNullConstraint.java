package net.wilamowski.drecho.standalone.domain.patient.validations;

import net.wilamowski.drecho.standalone.domain.patient.Patient;

class LastNameNotNullConstraint implements Constraint {
  private final Patient patient;

  public LastNameNotNullConstraint(Patient patient) {
    this.patient = patient;
  }

  @Override
  public boolean validate() {
    return patient!=null;
  }

  @Override
  public String errorMessage() {
    return "Last name are required fields.";
  }
}
