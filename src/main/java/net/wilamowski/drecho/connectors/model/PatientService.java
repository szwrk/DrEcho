package net.wilamowski.drecho.connectors.model;

import java.util.List;
import java.util.Optional;
import net.wilamowski.drecho.connectors.model.standalone.domain.patient.Patient;
import net.wilamowski.drecho.connectors.model.standalone.domain.patient.validations.ValidationExceptions;

public interface PatientService {
  List<Patient> findByAny(String input);

  List<Patient> findByPesel(String peselCode, int page);

  List<Patient> findByFullName(String lastName, int page);

  int counterByFullName(String lastName);

  Optional<Patient> createPatientRecord(Patient patient) throws ValidationExceptions;

  Optional<Patient> updatePatient(Patient patient) throws ValidationExceptions;

}
