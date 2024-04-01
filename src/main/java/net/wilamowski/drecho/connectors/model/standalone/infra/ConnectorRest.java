package net.wilamowski.drecho.connectors.model.standalone.infra;

import net.wilamowski.drecho.client.presentation.examinations.chooser.ConnectorExamination;
import net.wilamowski.drecho.client.presentation.main.ConnectorMainModel;
import net.wilamowski.drecho.connectors.model.*;
import net.wilamowski.drecho.connectors.model.standalone.domain.user.ConnectorUser;
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
  public ConnectorEchoTte echoTteModel() {
    return null;
  }

  @Override
  public ConnectorConfiguration settingsModel() {
    return null;
  }

  @Override
  public ConnectorUser userModel() {
    return null;
  }

  @Override
  public ConnectorAuthenticator authenticatorModel() {
    return null;
  }

  @Override
  public ConnectorMainModel mainDataModel() {
    return null;
  }

  @Override
  public ConnectorPatient patientModel() {
    return null;
  }

  @Override
  public ConnectorSimpleDictionaries dictionariesService() {
    return null;
  }

  @Override
  public ConnectorExamination examinationsModel() {
    return null;
  }

  @Override
  public ConnectorVisit visitModel() {
    return null;
  }
}
