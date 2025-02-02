package net.wilamowski.drecho.standalone.service.authenticator;

import java.util.List;
import net.wilamowski.drecho.app.configuration.SettingValue;
import net.wilamowski.drecho.gateway.SettingsService;
import net.wilamowski.drecho.standalone.persistance.ConfigurationRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SettingsServiceService implements SettingsService {
  private static final Logger logger = LogManager.getLogger( SettingsServiceService.class);
  private final ConfigurationRepository repository;

  public SettingsServiceService(ConfigurationRepository repository) {
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
