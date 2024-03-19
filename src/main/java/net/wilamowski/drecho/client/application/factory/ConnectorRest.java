package net.wilamowski.drecho.client.application.factory;

import net.wilamowski.drecho.client.presentation.examinations.chooser.ExaminationsStandaloneModel;
import net.wilamowski.drecho.client.presentation.main.MainModel;
import net.wilamowski.drecho.connectors.model.*;
import net.wilamowski.drecho.connectors.model.standalone.domain.user.UserService;

import org.apache.commons.lang.NotImplementedException;

/**
 * @author Arkadiusz Wilamowski
 *     <p></><a href="https://github.com/szwrk">GitHub</a>
 *     <p>For questions or inquiries, at contact arek@wilamowski.net
 */
public class ConnectorRest extends ConnectorLayer {

  public ConnectorRest() {
    throw new NotImplementedException();
  }

  @Override
  public ModelEchoTte echoTteModel() {
    return null;
  }

  @Override
  public Configuration settingsModel() {
    return null;
  }

  @Override
  public UserService userFacadeModel() {
    return null;
  }

  @Override
  public AuthenticatorService authenticatorModel() {
    return null;
  }

  @Override
  public MainModel mainDataModel() {
    return null;
  }

  @Override
  public PatientService patientModel() {
    return null;
  }

  @Override
  public SimpleDictionariesService dictionariesService() {
    return null;
  }

  @Override
  public ExaminationsStandaloneModel examinationsModel() {
    return null;
  }

  @Override
  public VisitModel visitModel() {
    return null;
  }
}
