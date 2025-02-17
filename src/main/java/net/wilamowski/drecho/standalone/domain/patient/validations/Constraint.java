package net.wilamowski.drecho.standalone.domain.patient.validations;

import net.wilamowski.drecho.configuration.backend_ports.PatientService;
import net.wilamowski.drecho.standalone.domain.patient.Patient;

public interface Constraint {
  static Constraint lastNameNotNullConstraint(Patient patient) {
    return new LastNameNotNullConstraint(patient);
  }

  static Constraint nameNotNullConstraint(Patient patient) {
    return new NameNotNullConstraint(patient);
  }

  static Constraint patientPeselUniqueConstraint(PatientService patient, Object validatedObject) {
    return new PatientPeselUniqueConstraint(patient, validatedObject);
  }

  static Constraint patientRequiredFields(Patient patient) {
    return new PatientRequiredFields(patient);
  }

  static Constraint peselCodeLengthConstraint(String pesel) {
    return new PeselCodeLengthConstraint(pesel);
  }

  static Constraint peselNotNullConstraint(String pesel) {
    return new PeselNotNullConstraint(pesel);
  }

  boolean validate();

  String errorMessage();
}
