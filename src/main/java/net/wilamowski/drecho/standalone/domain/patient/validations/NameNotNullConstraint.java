package net.wilamowski.drecho.standalone.domain.patient.validations;

import net.wilamowski.drecho.standalone.domain.patient.Patient;

class NameNotNullConstraint implements Constraint {
  private final Patient patient;

  public NameNotNullConstraint(Patient patient) {
    this.patient = patient;
  }

  @Override
  public boolean validate() {
    return patient.getName()!=null;
  }

  @Override
  public String errorMessage() {
    return "Name are required fields.";
  }
}
