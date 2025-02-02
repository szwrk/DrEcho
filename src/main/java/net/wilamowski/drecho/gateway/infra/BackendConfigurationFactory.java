package net.wilamowski.drecho.gateway.infra;
import net.wilamowski.drecho.client.presentation.main.MainService;
import net.wilamowski.drecho.gateway.*;
import net.wilamowski.drecho.standalone.domain.user.UserService;
import net.wilamowski.drecho.standalone.persistance.factory.StandaloneRepositoryFactory;

/**
 * @author Arkadiusz Wilamowski
 *     <p></><a href="https://github.com/szwrk">GitHub</a>
 *     <p>For questions or inquiries, at contact arek@wilamowski.net
 */
public abstract class BackendConfigurationFactory {
  public static BackendConfigurationFactory standaloneBackend(
      StandaloneRepositoryFactory standaloneRepositoryFactory) {
    return new StandaloneBackendConfiguration(standaloneRepositoryFactory);
  }

  public static BackendConfigurationFactory networkBackend() {
    return new RestClientConfiguration();
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
