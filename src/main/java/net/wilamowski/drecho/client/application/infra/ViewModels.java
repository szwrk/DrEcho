package net.wilamowski.drecho.client.application.infra;

import lombok.ToString;
import net.wilamowski.drecho.client.application.factory.ConnectorLayer;
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
import net.wilamowski.drecho.client.presentation.visit.VisitDetailsViewModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Arkadiusz Wilamowski
 *     <p></><a href="https://github.com/szwrk">GitHub</a>
 *     <p>For questions or inquiries, at contact arek@wilamowski.net
 */
@ToString
public class ViewModels {
  private static final Logger logger = LogManager.getLogger(ViewModels.class);
  private final ConnectorLayer modelLayer;

  public ViewModels(ConnectorLayer modelLayer) {
    this.modelLayer = modelLayer;
  }

  public LoginViewModel loginViewModel() {
    return new LoginViewModel(modelLayer.authenticatorModel());
  }

  public EchoTteViewModel exhaminationTteViewModel() {
    logger.traceEntry();
    return new EchoTteViewModel(modelLayer.echoTteModel(), modelLayer.dictionariesService());
  }

  public SettingsViewModel settingsViewModel() {
    return new SettingsViewModel(modelLayer.settingsModel());
  }

  public PreferenceViewModel preferenceViewModel() {
    return new PreferenceViewModel(modelLayer.settingsModel());
  }

  public MainViewModel mainViewModel() {
    logger.traceEntry();
    return new MainViewModel(modelLayer.mainDataModel());
  }

  public PatientSearcherViewModel patientViewModel() {
    logger.traceEntry();
    return new PatientSearcherViewModel(modelLayer.patientModel());
  }

  public ExaminationsChooserViewModel examinationsChooserViewModel() {
    logger.traceEntry();
    return new ExaminationsChooserViewModel(modelLayer.examinationsModel());
  }

  public GeneralExaminatiomViewModel generalExaminatiomViewModel() {
    logger.traceEntry();
    return new GeneralExaminatiomViewModel();
  }

  public VisitDetailsViewModel visitViewModel() {
    logger.traceEntry();
    return new VisitDetailsViewModel(
        modelLayer.visitModel(), modelLayer.dictionariesService(), modelLayer.userFacadeModel());
  }

  public DictionaryViewModel dictionariesViewModel() {
    return new DictionaryViewModel(modelLayer.dictionariesService());
  }

  public VisitDashboardViewModel visitDashboardViewModel() {
    return new VisitDashboardViewModel(modelLayer.visitModel());
  }

  public PatientRegisterViewModel patientRegistrationViewModel() {
    return new PatientRegisterViewModel(modelLayer.patientModel());
  }
}
