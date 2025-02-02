package net.wilamowski.drecho.configuration;

import java.util.Objects;
import lombok.ToString;
import net.wilamowski.drecho.client.presentation.examinations.chooser.ExaminationModel;
import net.wilamowski.drecho.client.presentation.main.ConnectorMainModelStandalone;
import net.wilamowski.drecho.client.presentation.main.MainService;
import net.wilamowski.drecho.configuration.backend_ports.*;
import net.wilamowski.drecho.standalone.domain.echo.EchoTteServiceExaminationService;
import net.wilamowski.drecho.standalone.domain.notes.NotesLocalServiceService;
import net.wilamowski.drecho.standalone.domain.patient.PatientServiceStandalone;
import net.wilamowski.drecho.standalone.domain.user.UserService;
import net.wilamowski.drecho.standalone.domain.visit.VisitServiceService;
import net.wilamowski.drecho.standalone.persistance.factory.StandaloneRepositoryFactory;
import net.wilamowski.drecho.standalone.persistance.impl.inmemory.DictionariesServiceInMemory;
import net.wilamowski.drecho.standalone.service.authenticator.AuthenticatorServiceImpl;
import net.wilamowski.drecho.standalone.service.authenticator.SettingsServiceService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Arkadiusz Wilamowski
 *     <p></><a href="https://github.com/szwrk">GitHub</a>
 *     <p>For questions or inquiries, at contact arek@wilamowski.net
 */
@ToString
public class StandaloneBackendConfiguration extends BackendConfigurationFactory {
  private static final Logger logger = LogManager.getLogger( StandaloneBackendConfiguration.class);
  private final StandaloneRepositoryFactory repoFactory;
  private MainService mainDataModel;
  private EchoTteExaminationService modelEchoTte;
  private SettingsService settingsService;
  private AuthenticatorServiceImpl authenticatorService;
  private UserService userService;
  private PatientService patientService;

  private ExaminationModel connectorExamination;
  private VisitService visitService;
  private DictionariesServiceInMemory dictionaries;
  private NotesLocalServiceService notesLocalService;

  StandaloneBackendConfiguration(StandaloneRepositoryFactory repoFactory) {
    Objects.requireNonNull(repoFactory);
    logger.debug(
        "[CORE] Initializing ModelLayer - StandaloneModel, {} ...",
        repoFactory.getClass().getSimpleName());
    this.repoFactory = repoFactory;
  }

  public static StandaloneBackendConfiguration createServiceLayerAsStandalone(
      StandaloneRepositoryFactory repoFactory) {
    return new StandaloneBackendConfiguration(repoFactory);
  }

  public EchoTteExaminationService echoTteModel() {
    if (modelEchoTte == null) {
      modelEchoTte = new EchoTteServiceExaminationService(repoFactory.instanceEchoTteRepository());
    }
    return Objects.requireNonNull(modelEchoTte);
  }

  @Override
  public SettingsService settingsModel() {
    if ( settingsService == null) {
      settingsService = new SettingsServiceService(repoFactory.instanceConfigurationRepository());
    }
    return Objects.requireNonNull( settingsService );
  }

  @Override
  public UserService userModel() {
    if ( userService == null) {
      userService = new UserService(repoFactory.instanceUserRepository());
    }
    return Objects.requireNonNull( userService );
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
  public MainService mainDataModel() {
    if (mainDataModel == null) {
      mainDataModel = new ConnectorMainModelStandalone();
    }
    return Objects.requireNonNull(mainDataModel);
  }

  @Override
  public PatientService patientModel() {
    if ( patientService == null) {
      patientService =
          new PatientServiceStandalone(
              repoFactory.instancePatientRepository(),
              repoFactory.instanceVersionedPatientRepository());
    }
    return Objects.requireNonNull( patientService );
  }

  public DictionariesService dictionariesService() {
    if ( dictionaries == null) {
      dictionaries = new DictionariesServiceInMemory();
    }
    return Objects.requireNonNull( dictionaries );
  }

  @Override
  public ExaminationModel examinationsModel() {
    if ( connectorExamination == null) {
      connectorExamination = new ExaminationModel();
    }
    return Objects.requireNonNull( connectorExamination );
  }

  @Override
  public VisitService visitModel() {
    if ( visitService == null) {
      logger.trace("Visit model is null. Creating new instances start...");
      visitService =
          new VisitServiceService(
              repoFactory.instanceVisitRepository(),
              userModel(),
              patientModel());
    } else {
      logger.trace("Visit model is not null");
    }
    return Objects.requireNonNull( visitService );
  }

  @Override
  public NotesService notesModel() {
    logger.traceEntry();
    if ( notesLocalService == null) {
      notesLocalService = new NotesLocalServiceService();
    }
    return Objects.requireNonNull( notesLocalService );
  }
}
