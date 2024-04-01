package net.wilamowski.drecho.connectors.model.standalone.service.authenticator;

import java.util.List;
import net.wilamowski.drecho.connectors.configuration.SettingValue;
import net.wilamowski.drecho.connectors.model.ConnectorConfiguration;
import net.wilamowski.drecho.connectors.model.standalone.persistance.ConfigurationRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ConnectorConfigurationService implements ConnectorConfiguration {
  private static final Logger logger = LogManager.getLogger( ConnectorConfigurationService.class);
  private final ConfigurationRepository repository;

  public ConnectorConfigurationService(ConfigurationRepository repository) {
    this.repository = repository;
    logger.debug(
        "[SERVICE] Initializing ConfigurationService {} {} ...",
        getClass().getName(),
        repository.getClass().getSimpleName());
  }

  @Override
  public void save(SettingValue s) {
    logger.debug("[SERVICE] Saving data...");
    repository.saveProperty(s);
  }

  @Override
  public String read(String key) {
    logger.debug("[SERVICE] Reading data...");
    return repository.readProperty(key);
  }

  @Override
  public List<SettingValue> getAllSettings() {
    logger.debug("[SERVICE] Getting all properties...");
    return repository.readAllProperty();
  }

  @Override
  public List<SettingValue> getSettingsByContextName(String contextName) {
    logger.debug("[SERVICE] Getting {} properties...", contextName);
    return repository.readByContextName(contextName);
  }
}
