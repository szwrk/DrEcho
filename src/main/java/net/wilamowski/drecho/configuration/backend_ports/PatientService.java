package net.wilamowski.drecho.configuration.backend_ports;

import java.util.List;
import java.util.Optional;
import net.wilamowski.drecho.app.dto.PatientDto;
import net.wilamowski.drecho.standalone.domain.patient.Patient;
import net.wilamowski.drecho.standalone.domain.patient.validations.ValidationExceptions;

public interface PatientService {

  List<PatientDto> findByCitizenCode(String peselCode, int page);

  List<PatientDto> findByFullName(String lastName, int page);

  int counterByFullName(String lastName);

  int counterByCitizenCode(String code);

  Optional<PatientDto> createPatientRecord(PatientDto patient) throws ValidationExceptions;

  Optional<PatientDto> updatePatient(PatientDto patient) throws ValidationExceptions;

  Optional<Patient> findById(Long patientId);

  List<PatientDto> findRecentlyPatients();

  PatientDto findByCitizenCodeFirstRecord(String par);
}
