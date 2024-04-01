package net.wilamowski.drecho.connectors.model.standalone.infra;

import java.util.Objects;
import lombok.ToString;
import net.wilamowski.drecho.client.presentation.examinations.chooser.ConnectorExamination;
import net.wilamowski.drecho.client.presentation.main.ConnectorMainModel;
import net.wilamowski.drecho.client.presentation.main.ConnectorMainModelStandalone;
import net.wilamowski.drecho.connectors.model.*;
import net.wilamowski.drecho.connectors.model.standalone.domain.echotte.EchoTteService;
import net.wilamowski.drecho.connectors.model.standalone.domain.patient.ConnectorPatientStandalone;
import net.wilamowski.drecho.connectors.model.standalone.domain.user.ConnectorUser;
import net.wilamowski.drecho.connectors.model.standalone.domain.visit.VisitService;
import net.wilamowski.drecho.connectors.model.standalone.persistance.factory.StandaloneRepositoryFactory;
import net.wilamowski.drecho.connectors.model.standalone.persistance.impl.inmemory.ConnectorSimpleDictionariesInMemory;
import net.wilamowski.drecho.connectors.model.standalone.service.authenticator.ConnectorAuthenticatorImpl;
import net.wilamowski.drecho.connectors.model.standalone.service.authenticator.ConnectorConfigurationService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Arkadiusz Wilamowski
 *     <p></><a href="https://github.com/szwrk">GitHub</a>
 *     <p>For questions or inquiries, at contact arek@wilamowski.net
 */
@ToString
public class ConnectorLayerLocal extends ConnectorLayer {
  private static final Logger logger = LogManager.getLogger( ConnectorLayerLocal.class);
  private final StandaloneRepositoryFactory repoFactory;
  private ConnectorMainModel mainDataModel;
  private ConnectorEchoTte modelEchoTte;
  private ConnectorConfiguration connectorConfiguration;
  private ConnectorAuthenticatorImpl authenticatorService;
  private ConnectorUser connectorUser;
  private ConnectorPatient connectorPatient;

  private ConnectorExamination connectorExamination;
  private ConnectorVisit connectorVisit;
  private ConnectorSimpleDictionariesInMemory dictionaries;

  ConnectorLayerLocal(StandaloneRepositoryFactory repoFactory) {
    Objects.requireNonNull(repoFactory);
    logger.debug(
        "[CORE] Initializing ModelLayer - StandaloneModel, {} ...",
        repoFactory.getClass().getSimpleName());
    this.repoFactory = repoFactory;
  }

  public static ConnectorLayerLocal createServiceLayerAsStandalone(
      StandaloneRepositoryFactory repoFactory) {
    return new ConnectorLayerLocal(repoFactory);
  }

  public ConnectorEchoTte echoTteModel() {
    if (modelEchoTte == null) {
      modelEchoTte = new EchoTteService(repoFactory.instanceEchoTteRepository());
    }
    return Objects.requireNonNull(modelEchoTte);
  }

  @Override
  public ConnectorConfiguration settingsModel() {
    if ( connectorConfiguration == null) {
      connectorConfiguration = new ConnectorConfigurationService(repoFactory.instanceConfigurationRepository());
    }
    return Objects.requireNonNull( connectorConfiguration );
  }

  @Override
  public ConnectorUser userModel() {
    if ( connectorUser == null) {
      connectorUser = new ConnectorUser(repoFactory.instanceUserRepository());
    }
    return Objects.requireNonNull( connectorUser );
  }

  @Override
  public ConnectorAuthenticator authenticatorModel() {
    if (authenticatorService == null) {
      authenticatorService =
          new ConnectorAuthenticatorImpl(new ConnectorUser(repoFactory.instanceUserRepository()));
    }
    return Objects.requireNonNull(authenticatorService);
  }

  @Override
  public ConnectorMainModel mainDataModel() {
    if (mainDataModel == null) {
      mainDataModel = new ConnectorMainModelStandalone();
    }
    return Objects.requireNonNull(mainDataModel);
  }

  @Override
  public ConnectorPatient patientModel() {
    if ( connectorPatient == null) {
      connectorPatient =
          new ConnectorPatientStandalone(
              repoFactory.instancePatientRepository(),
              repoFactory.instanceVersionedPatientRepository());
    }
    return Objects.requireNonNull( connectorPatient );
  }

  public ConnectorSimpleDictionaries dictionariesService() {
    if ( dictionaries == null) {
      dictionaries = new ConnectorSimpleDictionariesInMemory();
    }
    return Objects.requireNonNull( dictionaries );
  }

  @Override
  public ConnectorExamination examinationsModel() {
    if ( connectorExamination == null) {
      connectorExamination = new ConnectorExamination();
    }
    return Objects.requireNonNull( connectorExamination );
  }

  @Override
  public ConnectorVisit visitModel() {
    if ( connectorVisit == null) {
      logger.trace("Visit model is null. Creating new instances start...");
      connectorVisit =
          new VisitService(
              repoFactory.instanceVisitRepository(),
              userModel(),
              patientModel());
    } else {
      logger.trace("Visit model is not null");
    }
    return Objects.requireNonNull( connectorVisit );
  }
}
