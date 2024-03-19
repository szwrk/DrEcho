package net.wilamowski.drecho.client.application.factory;

import net.wilamowski.drecho.connectors.model.*;
import net.wilamowski.drecho.connectors.model.standalone.persistance.factory.StandaloneRepositoryFactory;
import net.wilamowski.drecho.client.presentation.main.MainModel;
import net.wilamowski.drecho.connectors.model.standalone.domain.user.UserService;

/**
 * @author Arkadiusz Wilamowski
 *     <p></><a href="https://github.com/szwrk">GitHub</a>
 *     <p>For questions or inquiries, at contact arek@wilamowski.net
 */
public abstract class ConnectorLayer {
  public static ConnectorLayer createServiceLayerAsStandalone(
      StandaloneRepositoryFactory standaloneRepositoryFactory) {
    return new ConnectorLocalModel(standaloneRepositoryFactory);
  }

  public static ConnectorLayer createServiceLayerAsProxy(
      StandaloneRepositoryFactory standaloneRepositoryFactory) {
    return new ConnectorLocalModel(standaloneRepositoryFactory);
  }

  public abstract ModelEchoTte echoTteModel();

  public abstract Configuration settingsModel();

  public abstract UserService userFacadeModel();

  public abstract AuthenticatorService authenticatorModel();

  public abstract MainModel mainDataModel();

  public abstract PatientService patientModel();

  public abstract SimpleDictionariesService dictionariesService();

  public abstract ExaminationsModel examinationsModel();

  public abstract VisitModel visitModel();
}
