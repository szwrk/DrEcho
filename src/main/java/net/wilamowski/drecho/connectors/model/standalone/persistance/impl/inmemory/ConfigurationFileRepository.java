package net.wilamowski.drecho.connectors.model.standalone.persistance.impl.inmemory;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import net.wilamowski.drecho.connectors.configuration.SettingValue;
import net.wilamowski.drecho.connectors.model.standalone.persistance.ConfigurationRepository;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ConfigurationFileRepository implements ConfigurationRepository {
    public static final String DEFAULT_PROPERTIES_FILE = "/client.properties";
    public static final String LOGIN_PROPERTIES_FILE = "/login.properties";
    private final Logger logger = LogManager.getLogger(ConfigurationFileRepository.class);
    private final PropertiesConfiguration conf;
    private ConfigurationFileRepository(String pathToPropertiesFile) {
        this.conf = loadConfiguration(pathToPropertiesFile);
    }

    private PropertiesConfiguration loadConfiguration(String pathToPropertiesFile) {
        PropertiesConfiguration propertiesConfiguration = new PropertiesConfiguration();
        try {
            URL url = getClass().getResource(DEFAULT_PROPERTIES_FILE);
            Objects.requireNonNull(url, "[REPOSITORY] Configuration file URL is null. Please check if 'client.properties' is in the correct location.");

            propertiesConfiguration.setFile(new File(url.toURI()));
            logger.debug("[REPOSITORY] File repository loading configuration from: {}", url);
            propertiesConfiguration.load();
        } catch (ConfigurationException | URISyntaxException e) {
            logger.error("[REPOSITORY] Error loading configuration: {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }

        return propertiesConfiguration;
    }

    public static ConfigurationRepository defaultConfigurationFileRepository() {
        return new ConfigurationFileRepository(DEFAULT_PROPERTIES_FILE);
    }

    public static ConfigurationRepository loginConfigurationFileRepository() {
        return new ConfigurationFileRepository(LOGIN_PROPERTIES_FILE);
    }

    @Override
    public void saveProperty(SettingValue setting) {
        try {
            logger.debug("[REPOSITORY] Configuration file path: {}", conf.getFile().getAbsolutePath());
            logger.info("[REPOSITORY] Before setting property - Key: {}, Value: {}", setting.getKey(), conf.getString(setting.getKey()));
            conf.setProperty(setting.getKey(), setting.getValue());
            logger.info("[REPOSITORY] After setting property - Key: {}, Value: {}", setting.getKey(), conf.getString(setting.getKey()));
            conf.save();
        } catch (Exception e) {
            logger.error("[REPOSITORY] Error saving configuration: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
    @Override
    public String readProperty(String key) {
            return conf.getString(key);
    }
    @Override
    public List<SettingValue> readAllProperty() {
        logger.traceEntry();
            List<SettingValue> settingList = new ArrayList<>();
            Iterator<String>   keys        = conf.getKeys();
            while (keys.hasNext()) {
                String key = keys.next();
                String value = conf.getString(key);
                settingList.add(new SettingValue(key, value));
            }
            logger.traceExit();
            return settingList;
    }

    @Override
    public List<SettingValue> readByContextName(String contextName) {
        logger.traceEntry();
        List<SettingValue> settingList = new ArrayList<>();
        Iterator<String>   keys        = conf.getKeys();
        while (keys.hasNext()) {
            String key = keys.next();
            String value = conf.getString(key);
            if (key.startsWith( contextName )){
                settingList.add(new SettingValue(key, value));
            }
        }
        logger.traceExit();
        return settingList;
    }
}
