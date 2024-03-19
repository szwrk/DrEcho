package net.wilamowski.drecho.connectors.properties;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/** Properties */
public class BackendPropertyReader {
    private static final String PROPERTIES_PATH = "/backend.properties";
    private static final Logger log = LogManager.getLogger( net.wilamowski.drecho.client.properties.ClientPropertyReader.class);

    public static String getString(String key) {
      try (InputStream input = net.wilamowski.drecho.client.properties.ClientPropertyReader.class.getResourceAsStream(PROPERTIES_PATH)) {
        if (input == null) {
          String errorMsg = "Properties file '" + PROPERTIES_PATH + "' not found.";
          log.error(errorMsg);
          throw new IllegalArgumentException(errorMsg);
        }

        Properties appProps = new Properties();
        appProps.load(input);
        return appProps.getProperty(key);
      } catch (IOException e) {
        String errorMsg = "Error reading properties file: " + e.getMessage();
        log.error(errorMsg);
        throw new IllegalArgumentException(errorMsg, e);
      }
    }

    public static Integer getInt(String key) {
      String value = getString(key);
      try {
        return Integer.parseInt(value);
      } catch (NumberFormatException e) {
        String errorMsg = "Error parsing integer value for key '" + key + "': " + e.getMessage();
        log.error(errorMsg);
        throw new IllegalArgumentException(errorMsg, e);
      }
    }
    }



