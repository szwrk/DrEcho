package net.wilamowski.drecho.connectors.model.standalone.domain.patient.validations;


import net.wilamowski.drecho.connectors.model.standalone.domain.patient.Patient;

class NameNotNullConstraint implements Constraint {
    private final Patient patient;

    public NameNotNullConstraint(Patient patient) {
      this.patient = patient;
    }

    @Override
    public boolean validate() {
      if (!patient.getName().isEmpty()
             ) {
        return true;
      }
      return false;
    }

    @Override
    public String errorMessage() {
      return "Name are required fields.";
    }
  }
