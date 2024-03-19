package net.wilamowski.drecho.connectors.model.standalone.persistance;

import net.wilamowski.drecho.connectors.model.standalone.domain.patient.Patient;

import java.util.List;
import java.util.Optional;

public interface PatientRepository {
  Optional<Patient> addNew(Patient patient);

  Optional<Patient> findById(long id);

  List<Patient> findByLastName(String param, int page);
  int countByLastName(String param);

  List<Patient> findByPeselCode(String param, int page);

  Optional<Patient>  update(Patient patient);

  List<Patient> findAll();

//  List<Patient> getByPage(int pageNumber , int pageSize);
}
