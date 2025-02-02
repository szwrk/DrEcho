package net.wilamowski.drecho.standalone.persistance.factory;

import net.wilamowski.drecho.standalone.persistance.ConfigurationRepository;
import net.wilamowski.drecho.standalone.persistance.EchoTteRepository;
import net.wilamowski.drecho.standalone.persistance.PatientRepository;
import net.wilamowski.drecho.standalone.persistance.UserRepository;
import net.wilamowski.drecho.standalone.persistance.VersionedPatientRepository;
import net.wilamowski.drecho.standalone.persistance.VisitRepository;
import net.wilamowski.drecho.standalone.persistance.demo.DemoDataGeneratorInMemory;
import net.wilamowski.drecho.standalone.persistance.impl.inmemory.ConfigurationFileRepository;
import net.wilamowski.drecho.standalone.persistance.impl.inmemory.InMemoryEchoTteRepository;
import net.wilamowski.drecho.standalone.persistance.impl.inmemory.PatientRepositoryInMemory;
import net.wilamowski.drecho.standalone.persistance.impl.inmemory.UserRepositoryInMemory;
import net.wilamowski.drecho.standalone.persistance.impl.inmemory.VersionedPatientRepositoryInMemory;
import net.wilamowski.drecho.standalone.persistance.impl.inmemory.VisitRepositoryInMemory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class InMemoryRepositoryFactory implements StandaloneRepositoryFactory {
  private static final Logger logger = LogManager.getLogger(InMemoryRepositoryFactory.class);
  private final EchoTteRepository repositoryInMemoryEchoTte;
  private final ConfigurationRepository configurationRepository;
  private final UserRepositoryInMemory userRepositoryInMemory;
  private final PatientRepositoryInMemory patientRepositoryInMemory;
  private final VisitRepositoryInMemory visitRepositoryInMemory;
  private final VersionedPatientRepositoryInMemory versionedPatientRepositoryInMemory;

  public InMemoryRepositoryFactory() {
    DemoDataGeneratorInMemory demoDataGeneratorInMemory = DemoDataGeneratorInMemory.instance();
    repositoryInMemoryEchoTte = InMemoryEchoTteRepository.createRepositoryInMemoryEchoTte();
    configurationRepository = ConfigurationFileRepository.defaultConfigurationFileRepository();
    userRepositoryInMemory = UserRepositoryInMemory.createUserRepositoryInMemory();
    patientRepositoryInMemory =
        PatientRepositoryInMemory.createPatientRepositoryInMemory(demoDataGeneratorInMemory);
    visitRepositoryInMemory =
        VisitRepositoryInMemory.createVisitRepositoryInMemory(demoDataGeneratorInMemory);
    versionedPatientRepositoryInMemory = new VersionedPatientRepositoryInMemory();
  }

  public static InMemoryRepositoryFactory instance() {
    logger.info("[CONTEXT] Initializing singleton instance of InMemoryRepositoryFactory...");

    return new InMemoryRepositoryFactory();
  }

  @Override
  public EchoTteRepository instanceEchoTteRepository() {
    return repositoryInMemoryEchoTte;
  }

  @Override
  public ConfigurationRepository instanceConfigurationRepository() {
    return configurationRepository;
  }

  @Override
  public UserRepository instanceUserRepository() {
    return userRepositoryInMemory;
  }

  @Override
  public VisitRepository instanceVisitRepository() {
    return visitRepositoryInMemory;
  }

  @Override
  public PatientRepository instancePatientRepository() {
    return patientRepositoryInMemory;
  }

  @Override
  public VersionedPatientRepository instanceVersionedPatientRepository() {
    return versionedPatientRepositoryInMemory;
  }
}
