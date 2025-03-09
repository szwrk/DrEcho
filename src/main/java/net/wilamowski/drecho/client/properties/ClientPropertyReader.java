package net.wilamowski.drecho.client.properties;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/** Properties */
public class ClientPropertyReader {
  private static final String PROPERTIES_PATH = "/client.properties";
  private static final Logger logger = LogManager.getLogger(ClientPropertyReader.class);

  public static Integer getInt(String key) {
    String value = getString(key);
    try {
      return Integer.parseInt(value);
    } catch (NumberFormatException e) {
      String errorMsg = "Error parsing integer value for key '" + key + "': " + e.getMessage();
      logger.error(errorMsg);
      throw new IllegalArgumentException(errorMsg, e);
    }
  }

  public static String getString(String key) {
    try (InputStream input = ClientPropertyReader.class.getResourceAsStream(PROPERTIES_PATH)) {
      if (input == null) {
        String errorMsg = "Properties file '" + PROPERTIES_PATH + "' not found.";
        logger.error(errorMsg);
        throw new IllegalArgumentException(errorMsg);
      }

      Properties appProps = new Properties();
      appProps.load(input);
      return appProps.getProperty(key);
    } catch (IOException e) {
      String errorMsg = "Error reading properties file: " + e.getMessage();
      logger.error(errorMsg);
      throw new IllegalArgumentException(errorMsg, e);
    }
  }

  public static boolean getBoolean(String key) {
    String value = getString(key);
    return BooleanUtils.toBoolean(value);
  }
}
