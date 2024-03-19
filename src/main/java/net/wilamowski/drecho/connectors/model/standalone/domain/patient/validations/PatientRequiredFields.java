package net.wilamowski.drecho.connectors.model.standalone.domain.patient.validations;

import net.wilamowski.drecho.connectors.model.standalone.domain.patient.Patient;

class PatientRequiredFields implements Constraint {
  private final Patient patient;

  public PatientRequiredFields(Patient patient) {
    this.patient = patient;
  }

  @Override
  public boolean validate() {
      return !patient.getPesel( ).isEmpty( )
              && !patient.getName( ).isEmpty( )
              && !patient.getLastName( ).isEmpty( )
              && patient.getDateBirth( ) != null;
  }

  @Override
  public String errorMessage() {
    return "Name, last name, PESEL, and date of birth are required fields.";
  }
}
