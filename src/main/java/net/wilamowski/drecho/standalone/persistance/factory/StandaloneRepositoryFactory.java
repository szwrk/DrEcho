package net.wilamowski.drecho.standalone.persistance.factory;

import net.wilamowski.drecho.standalone.persistance.ConfigurationRepository;
import net.wilamowski.drecho.standalone.persistance.EchoTteRepository;
import net.wilamowski.drecho.standalone.persistance.PatientRepository;
import net.wilamowski.drecho.standalone.persistance.UserRepository;
import net.wilamowski.drecho.standalone.persistance.VersionedPatientRepository;
import net.wilamowski.drecho.standalone.persistance.VisitRepository;

public interface StandaloneRepositoryFactory {

  EchoTteRepository instanceEchoTteRepository();

  ConfigurationRepository instanceConfigurationRepository();

  UserRepository instanceUserRepository();

  VisitRepository instanceVisitRepository();

  PatientRepository instancePatientRepository();

  VersionedPatientRepository instanceVersionedPatientRepository();
}
