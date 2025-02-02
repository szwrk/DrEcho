package net.wilamowski.drecho.client.application.infra;

import lombok.ToString;
import net.wilamowski.drecho.client.presentation.notes.NotesViewModel;
import net.wilamowski.drecho.gateway.infra.BackendConfigurationFactory;
import net.wilamowski.drecho.client.presentation.complex.visits.VisitDashboardViewModel;
import net.wilamowski.drecho.client.presentation.dictionaries.general.DictionaryViewModel;
import net.wilamowski.drecho.client.presentation.examinations.chooser.ExaminationsChooserViewModel;
import net.wilamowski.drecho.client.presentation.examinations.echo.EchoTteViewModel;
import net.wilamowski.drecho.client.presentation.examinations.general.GeneralExaminatiomViewModel;
import net.wilamowski.drecho.client.presentation.login.LoginViewModel;
import net.wilamowski.drecho.client.presentation.main.MainViewModel;
import net.wilamowski.drecho.client.presentation.patients.PatientRegisterViewModel;
import net.wilamowski.drecho.client.presentation.patients.PatientSearcherViewModel;
import net.wilamowski.drecho.client.presentation.preferences.PreferenceViewModel;
import net.wilamowski.drecho.client.presentation.settings.SettingsViewModel;
import net.wilamowski.drecho.client.presentation.visit.VisitViewModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Arkadiusz Wilamowski
 *     <p></><a href="https://github.com/szwrk">GitHub</a>
 *     <p>For questions or inquiries, at contact arek@wilamowski.net
 */
@ToString
public class ViewModelConfiguration {
  private static final Logger logger = LogManager.getLogger( ViewModelConfiguration.class);
  private final BackendConfigurationFactory connectors;

  public ViewModelConfiguration(BackendConfigurationFactory connectors) {
    this.connectors = connectors;
  }

  public LoginViewModel loginViewModel() {
    return new LoginViewModel( connectors.authenticatorModel());
  }

  public EchoTteViewModel exhaminationTteViewModel() {
    logger.traceEntry();
    return new EchoTteViewModel( connectors.echoTteModel(), connectors.dictionariesService());
  }

  public SettingsViewModel settingsViewModel() {
    return new SettingsViewModel( connectors.settingsModel());
  }

  public PreferenceViewModel preferenceViewModel() {
    return new PreferenceViewModel( connectors.settingsModel());
  }

  public MainViewModel mainViewModel() {
    logger.traceEntry();
    return new MainViewModel( connectors.mainDataModel());
  }

  public PatientSearcherViewModel patientViewModel() {
    logger.traceEntry();
    return new PatientSearcherViewModel( connectors.patientModel());
  }

  public ExaminationsChooserViewModel examinationsChooserViewModel() {
    logger.traceEntry();
    return new ExaminationsChooserViewModel( connectors.examinationsModel());
  }

  public GeneralExaminatiomViewModel generalExaminatiomViewModel() {
    logger.traceEntry();
    return new GeneralExaminatiomViewModel();
  }

  public VisitViewModel visitViewModel() {
    logger.traceEntry();
    return new VisitViewModel(
        connectors.visitModel(), connectors.dictionariesService(), connectors.userModel());
  }

  public DictionaryViewModel dictionariesViewModel() {
    logger.traceEntry();
    return new DictionaryViewModel( connectors.dictionariesService());
  }

  public VisitDashboardViewModel visitDashboardViewModel() {
    logger.traceEntry();
    return new VisitDashboardViewModel( connectors.visitModel());
  }

  public PatientRegisterViewModel patientRegistrationViewModel() {
    logger.traceEntry();
    return new PatientRegisterViewModel( connectors.patientModel());
  }

    public NotesViewModel notesViewModel() {
      logger.traceEntry();
    return new NotesViewModel(connectors.notesModel());
    }
}
