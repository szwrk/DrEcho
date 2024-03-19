package net.wilamowski.drecho.connectors.model.standalone.persistance;

import net.wilamowski.drecho.connectors.model.standalone.domain.patient.Patient;

import java.util.Optional;

public interface VersionedPatientRepository {
    Optional<Patient> addNew(Patient patient);
}
