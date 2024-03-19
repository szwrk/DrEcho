package net.wilamowski.drecho.connectors.model.standalone.persistance.factory;

import net.wilamowski.drecho.connectors.model.standalone.persistance.ConfigurationRepository;
import net.wilamowski.drecho.connectors.model.standalone.persistance.EchoTteRepository;
import net.wilamowski.drecho.connectors.model.standalone.persistance.PatientRepository;
import net.wilamowski.drecho.connectors.model.standalone.persistance.UserRepository;
import net.wilamowski.drecho.connectors.model.standalone.persistance.VersionedPatientRepository;
import net.wilamowski.drecho.connectors.model.standalone.persistance.VisitRepository;
import net.wilamowski.drecho.connectors.model.standalone.persistance.impl.inmemory.ConfigurationFileRepository;
import net.wilamowski.drecho.connectors.model.standalone.persistance.impl.inmemory.InMemoryEchoTteRepository;
import net.wilamowski.drecho.connectors.model.standalone.persistance.impl.inmemory.PatientRepositoryInMemory;
import net.wilamowski.drecho.connectors.model.standalone.persistance.impl.inmemory.UserRepositoryInMemory;
import net.wilamowski.drecho.connectors.model.standalone.persistance.impl.inmemory.VersionedPatientRepositoryInMemory;
import net.wilamowski.drecho.connectors.model.standalone.persistance.impl.inmemory.VisitRepositoryInMemory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class InMemoryRepositoryFactory implements StandaloneRepositoryFactory {
  private static final Logger logger = LogManager.getLogger(InMemoryRepositoryFactory.class);

  private InMemoryRepositoryFactory( ) {
  }

  public static InMemoryRepositoryFactory createInMemoryRepositoryFactory() {
    return new InMemoryRepositoryFactory(  );
  }

  @Override
  public EchoTteRepository instanceEchoTteRepository() {
    return InMemoryEchoTteRepository.createRepositoryInMemoryEchoTte();
  }

  @Override
  public ConfigurationRepository instanceConfigurationRepository() {
    logger.warn( "[FACTORY]" );
    return ConfigurationFileRepository
        .defaultConfigurationFileRepository(); // todo in-memory instance should be file vs map?
  }

  @Override
  public UserRepository instanceUserRepository() {
    return UserRepositoryInMemory.createUserRepositoryInMemory();
  }

  @Override
  public VisitRepository instanceVisitRepository() {
    return VisitRepositoryInMemory.createVisitRepositoryInMemory();
  }

  @Override
  public PatientRepository instancePatientRepository() {
    return PatientRepositoryInMemory.createPatientRepositoryInMemory();
  }

  @Override
  public VersionedPatientRepository instanceVersionedPatientRepository() {
    return new VersionedPatientRepositoryInMemory();
  }
}
