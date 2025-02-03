package net.wilamowski.drecho.configuration;

import net.wilamowski.drecho.client.presentation.examinations.chooser.ExaminationModel;
import net.wilamowski.drecho.client.presentation.main.MainService;
import net.wilamowski.drecho.configuration.backend_ports.*;
import net.wilamowski.drecho.standalone.domain.user.UserService;
import org.apache.commons.lang.NotImplementedException;

/**
 * @author Arkadiusz Wilamowski
 *     <p></><a href="https://github.com/szwrk">GitHub</a>
 *     <p>For questions or inquiries, at contact arek@wilamowski.net
 */
public class RestClientConfiguration extends BackendConfigurationFactory {

  public RestClientConfiguration() {
    throw new NotImplementedException();
  }

  @Override
  public EchoTteExaminationService echoTteModel() {
    return null;
  }

  @Override
  public SettingsService settingsModel() {
    return null;
  }

  @Override
  public UserService userModel() {
    return null;
  }

  @Override
  public AuthenticatorService authenticatorModel() {
    return null;
  }

  @Override
  public MainService mainDataModel() {
    return null;
  }

  @Override
  public PatientService patientModel() {
    return null;
  }

  @Override
  public DictionariesService dictionariesService() {
    return null;
  }

  @Override
  public ExaminationModel examinationsModel() {
    return null;
  }

  @Override
  public VisitService visitModel() {
    return null;
  }

  @Override
  public NotesService notesModel() {
    return null;
  }
}
