package net.wilamowski.drecho.connectors.model.standalone.persistance;

import java.util.Optional;
import net.wilamowski.drecho.connectors.model.standalone.domain.patient.Patient;

public interface VersionedPatientRepository {
  Optional<Patient> addNew(Patient patient);
}
