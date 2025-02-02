package net.wilamowski.drecho.standalone.persistance;

import java.util.List;
import java.util.Optional;
import net.wilamowski.drecho.standalone.domain.patient.Patient;

public interface PatientRepository {
  Optional<Patient> addNew(Patient patient);

  Optional<Patient> findById(long id);

  List<Patient> findByFullName(String param, int page);

  int countByFullName(String param);

  List<Patient> findByPeselCode(String param, int page);

  Optional<Patient> update(Patient patient);

  List<Patient> findAll();

  //  List<Patient> getByPage(int pageNumber , int pageSize);
}
