package net.wilamowski.drecho.gateway.infra;

import java.util.Arrays;
import java.util.Objects;
import net.wilamowski.drecho.standalone.persistance.factory.InMemoryRepositoryFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Arkadiusz Wilamowski
 *     <p></><a href="https://github.com/szwrk">GitHub</a>
 *     <p>For questions or inquiries, at contact arek@wilamowski.net
 */
public class ModelLayerFactory {
  private static final Logger log = LogManager.getLogger(ModelLayerFactory.class);

  public ModelLayerFactory() {}

  public BackendConfigurationFactory createModelLayerByType(DeploymentType deployType)
      throws IllegalArgumentException {
    Objects.requireNonNull(deployType);
    //    DeploymentType passedType = stringToEnum( DeploymentType.class, deployType);
    switch (deployType) {
      case STANDALONE_INMEMORY:
        return BackendConfigurationFactory.standaloneBackend(InMemoryRepositoryFactory.instance());
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
          throw new IllegalArgumentException(errorMsg);
        }
      default:
        {
          String errorMsg =
              String.format(
                  "Unsupported deploy argument: %1$s. Available implementations: %2$s",
                  deployType, Arrays.toString(DeploymentType.values()));
          log.error(errorMsg);
          throw new IllegalArgumentException(errorMsg);
        }
    }
  }

  /*EnumUtils.isValidEnum(BackendType.class, deployType);*/
  //  private <T extends Enum<T>> T stringToEnum(Class<T> enumClass, String valueTypeString) {
  //    log.traceEntry();
  //    log.debug("Resolving enum: {} {} ", enumClass, valueTypeString);
  //    T someEnum = null;
  //    try {
  //      someEnum = T.valueOf(enumClass, valueTypeString.toUpperCase());
  //    } catch (IllegalArgumentException e) {
  //      log.error(e.getMessage(), e);
  //      String header = Lang.getString("e.003.header");
  //      String msg = Lang.getString("e.003.msg");
  //      ExceptionAlert.create().showError(e, header, String.format(msg, valueTypeString));
  //    }
  //    return someEnum;
  //  }
}
