package net.wilamowski.drecho.connectors.model.standalone.infra;
import net.wilamowski.drecho.client.presentation.main.ConnectorMainModel;
import net.wilamowski.drecho.connectors.model.*;

import net.wilamowski.drecho.connectors.model.standalone.domain.user.ConnectorUser;
import net.wilamowski.drecho.connectors.model.standalone.persistance.factory.StandaloneRepositoryFactory;

/**
 * @author Arkadiusz Wilamowski
 *     <p></><a href="https://github.com/szwrk">GitHub</a>
 *     <p>For questions or inquiries, at contact arek@wilamowski.net
 */
public abstract class ConnectorLayer {
  public static ConnectorLayer createServiceLayerAsStandalone(
      StandaloneRepositoryFactory standaloneRepositoryFactory) {
    return new ConnectorLayerLocal(standaloneRepositoryFactory);
  }

  public static ConnectorLayer createServiceLayerAsProxy(
      StandaloneRepositoryFactory standaloneRepositoryFactory) {
    return new ConnectorLayerLocal(standaloneRepositoryFactory);
  }

  public abstract ConnectorEchoTte echoTteModel();

  public abstract ConnectorConfiguration settingsModel();

  public abstract ConnectorUser userModel();

  public abstract ConnectorAuthenticator authenticatorModel();

  public abstract ConnectorMainModel mainDataModel();

  public abstract ConnectorPatient patientModel();

  public abstract ConnectorSimpleDictionaries dictionariesService();

  public abstract ConnectorExamination examinationsModel();

  public abstract ConnectorVisit visitModel();
  public abstract ConnectorNotes notesModel() ;
}
