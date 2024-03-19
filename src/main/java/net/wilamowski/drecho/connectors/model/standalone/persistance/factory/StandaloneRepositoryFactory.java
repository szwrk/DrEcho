package net.wilamowski.drecho.connectors.model.standalone.persistance.factory;

import net.wilamowski.drecho.connectors.model.standalone.persistance.ConfigurationRepository;
import net.wilamowski.drecho.connectors.model.standalone.persistance.EchoTteRepository;
import net.wilamowski.drecho.connectors.model.standalone.persistance.PatientRepository;
import net.wilamowski.drecho.connectors.model.standalone.persistance.UserRepository;
import net.wilamowski.drecho.connectors.model.standalone.persistance.VersionedPatientRepository;
import net.wilamowski.drecho.connectors.model.standalone.persistance.VisitRepository;

public interface StandaloneRepositoryFactory {

  EchoTteRepository instanceEchoTteRepository();

  ConfigurationRepository instanceConfigurationRepository();

  UserRepository instanceUserRepository();

  VisitRepository instanceVisitRepository();

  PatientRepository instancePatientRepository();

  VersionedPatientRepository instanceVersionedPatientRepository();
}
