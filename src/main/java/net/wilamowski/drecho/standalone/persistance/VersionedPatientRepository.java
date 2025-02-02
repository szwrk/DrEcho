package net.wilamowski.drecho.standalone.persistance;

import java.util.Optional;
import net.wilamowski.drecho.standalone.domain.patient.Patient;

public interface VersionedPatientRepository {
  Optional<Patient> addNew(Patient patient);
}
