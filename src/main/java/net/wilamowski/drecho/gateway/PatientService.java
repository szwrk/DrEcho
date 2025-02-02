package net.wilamowski.drecho.gateway;

import java.util.List;
import java.util.Optional;
import net.wilamowski.drecho.standalone.domain.patient.Patient;
import net.wilamowski.drecho.standalone.domain.patient.validations.ValidationExceptions;
import net.wilamowski.drecho.app.dto.PatientDto;

public interface PatientService {
  List<PatientDto> findByAny(String input);

  List<PatientDto> findByPesel(String peselCode, int page);

  List<PatientDto> findByFullName(String lastName, int page);

  int counterByFullName(String lastName);

  Optional<PatientDto> createPatientRecord(PatientDto patient) throws ValidationExceptions;

  Optional<PatientDto> updatePatient(PatientDto patient) throws ValidationExceptions;

  Optional<Patient> findById(Long patientId);
}
