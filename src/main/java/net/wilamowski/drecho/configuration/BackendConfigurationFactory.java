package net.wilamowski.drecho.configuration;
import java.util.Arrays;
import java.util.Objects;
import net.wilamowski.drecho.client.presentation.main.MainService;
import net.wilamowski.drecho.configuration.backend_ports.*;
import net.wilamowski.drecho.standalone.domain.user.UserService;
import net.wilamowski.drecho.standalone.persistance.factory.InMemoryRepositoryFactory;
import net.wilamowski.drecho.standalone.persistance.factory.StandaloneRepositoryFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Arkadiusz Wilamowski
 *     <p></><a href="https://github.com/szwrk">GitHub</a>
 *     <p>For questions or inquiries, at contact arek@wilamowski.net
 */
public abstract class BackendConfigurationFactory {
  private static final Logger log = LogManager.getLogger( BackendConfigurationFactory.class);

  static BackendConfigurationFactory standaloneBackend(
      StandaloneRepositoryFactory standaloneRepositoryFactory) {
    return new StandaloneBackendConfiguration(standaloneRepositoryFactory);
  }

  static BackendConfigurationFactory networkBackend() {
    return new RestClientConfiguration();
  }

  public static BackendConfigurationFactory getBackendByName(BackendType deployType)
          throws IllegalArgumentException {
    Objects.requireNonNull(deployType);
    //    DeploymentType passedType = stringToEnum( DeploymentType.class, deployType);
    switch (deployType) {
      case STANDALONE_INMEMORY:
        return BackendConfigurationFactory.standaloneBackend( InMemoryRepositoryFactory.instance());
      case STANDALONE_LOCALDB:
      {
        String errorMsg =
                "STANDALONE_LOCALDB is not implemented yet. Parameter: " + deployType.name();
        log.error(errorMsg);
        throw new IllegalArgumentException(errorMsg);
      }
      case STANDALONE_REMOTEDB:
      {
        String errorMsg =
                "STANDALONE_REMOTEDB is not implemented yet. Parameter: " + deployType.name();
        log.error(errorMsg);
        throw new IllegalArgumentException(errorMsg);
      }
      case CLIENTSERVER:
      {
        String errorMsg = "CLIENTSERVER is not implemented yet. Parameter: " + deployType.name();
        log.error(errorMsg);
        return new RestClientConfiguration();
      }
      default:
      {
        String errorMsg =
                String.format(
                        "Unsupported deploy argument: %1$s. Available implementations: %2$s",
                        deployType, Arrays.toString( BackendType.values()));
        log.error(errorMsg);
        throw new IllegalArgumentException(errorMsg);
      }
    }
  }

  public abstract EchoTteExaminationService echoTteModel();

  public abstract SettingsService settingsModel();

  public abstract UserService userModel();

  public abstract AuthenticatorService authenticatorModel();

  public abstract MainService mainDataModel(); //todo ?

  public abstract PatientService patientModel();

  public abstract DictionariesService dictionariesService();

  public abstract ExaminationModel examinationsModel();

  public abstract VisitService visitModel();
  public abstract NotesService notesModel() ;
}
