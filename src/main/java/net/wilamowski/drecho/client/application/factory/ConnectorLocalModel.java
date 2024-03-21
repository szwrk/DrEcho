package net.wilamowski.drecho.client.application.factory;

import java.util.Objects;
import lombok.ToString;
import net.wilamowski.drecho.client.presentation.examinations.chooser.ExaminationsStandaloneModel;
import net.wilamowski.drecho.client.presentation.main.MainModel;
import net.wilamowski.drecho.client.presentation.main.MainModelStandalone;
import net.wilamowski.drecho.connectors.model.*;
import net.wilamowski.drecho.connectors.model.standalone.domain.echotte.EchoTteService;
import net.wilamowski.drecho.connectors.model.standalone.domain.patient.PatientStandaloneService;
import net.wilamowski.drecho.connectors.model.standalone.domain.user.UserService;
import net.wilamowski.drecho.connectors.model.standalone.domain.visit.VisitStandaloneModel;
import net.wilamowski.drecho.connectors.model.standalone.persistance.factory.StandaloneRepositoryFactory;
import net.wilamowski.drecho.connectors.model.standalone.persistance.impl.inmemory.SimpleDictionariesServiceInMemory;
import net.wilamowski.drecho.connectors.model.standalone.service.authenticator.AuthenticatorServiceImpl;
import net.wilamowski.drecho.connectors.model.standalone.service.authenticator.ConfigurationService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Arkadiusz Wilamowski
 *     <p></><a href="https://github.com/szwrk">GitHub</a>
 *     <p>For questions or inquiries, at contact arek@wilamowski.net
 */
@ToString
public class ConnectorLocalModel extends ConnectorLayer {
  private static final Logger logger = LogManager.getLogger(ConnectorLocalModel.class);
  private final StandaloneRepositoryFactory repoFactory;
  private MainModel mainDataModel;
  private ModelEchoTte modelEchoTte;
  private Configuration configuration;
  private AuthenticatorServiceImpl authenticatorService;
  private UserService userService;
  private PatientService patientService;

  private ExaminationsModel examinationsModel;
  private VisitModel visitModel;
  private SimpleDictionariesServiceInMemory dictionariesService;

  ConnectorLocalModel(StandaloneRepositoryFactory repoFactory) {
    Objects.requireNonNull(repoFactory);
    logger.debug(
        "[CORE] Initializing ModelLayer - StandaloneModel, {} ...",
        repoFactory.getClass().getSimpleName());
    this.repoFactory = repoFactory;
  }

  public static ConnectorLocalModel createServiceLayerAsStandalone(
      StandaloneRepositoryFactory repoFactory) {
    return new ConnectorLocalModel(repoFactory);
  }

  public ModelEchoTte echoTteModel() {
    if (modelEchoTte == null) {
      modelEchoTte = new EchoTteService(repoFactory.instanceEchoTteRepository());
    }
    return Objects.requireNonNull(modelEchoTte);
  }

  @Override
  public Configuration settingsModel() {
    if (configuration == null) {
      configuration = new ConfigurationService(repoFactory.instanceConfigurationRepository());
    }
    return Objects.requireNonNull(configuration);
  }

  @Override
  public UserService userFacadeModel() {
    if (userService == null) {
      userService = new UserService(repoFactory.instanceUserRepository());
    }
    return Objects.requireNonNull(userService);
  }

  @Override
  public AuthenticatorService authenticatorModel() {
    if (authenticatorService == null) {
      authenticatorService =
          new AuthenticatorServiceImpl(new UserService(repoFactory.instanceUserRepository()));
    }
    return Objects.requireNonNull(authenticatorService);
  }

  @Override
  public MainModel mainDataModel() {
    if (mainDataModel == null) {
      mainDataModel = new MainModelStandalone();
    }
    return Objects.requireNonNull(mainDataModel);
  }

  @Override
  public PatientService patientModel() {
    if (patientService == null) {
      patientService =
          new PatientStandaloneService(
              repoFactory.instancePatientRepository(),
              repoFactory.instanceVersionedPatientRepository());
    }
    return Objects.requireNonNull(patientService);
  }

  public SimpleDictionariesService dictionariesService() {
    if (dictionariesService == null) {
      dictionariesService = new SimpleDictionariesServiceInMemory();
    }
    return Objects.requireNonNull(dictionariesService);
  }

  @Override
  public ExaminationsModel examinationsModel() {
    if (examinationsModel == null) {
      examinationsModel = new ExaminationsStandaloneModel();
    }
    return Objects.requireNonNull(examinationsModel);
  }

  @Override
  public VisitModel visitModel() {
    if (visitModel == null) {
      logger.trace("Visit model is null. Creating new instances start...");
      visitModel =
          new VisitStandaloneModel(
              repoFactory.instanceVisitRepository(),
              repoFactory.instanceUserRepository(),
              repoFactory.instancePatientRepository());
    } else {
      logger.trace("Visit model is not null");
    }
    return Objects.requireNonNull(visitModel);
  }
}
