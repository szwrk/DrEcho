package net.wilamowski.drecho.connectors.model.standalone.persistance.impl.inmemory;

import java.util.Optional;

import net.wilamowski.drecho.connectors.model.standalone.domain.patient.Patient;
import net.wilamowski.drecho.connectors.model.standalone.persistance.VersionedPatientRepository;

public class VersionedPatientRepositoryInMemory implements VersionedPatientRepository {
    @Override
    public Optional<Patient> addNew(Patient patient) {
        return Optional.empty( );
    }
}
