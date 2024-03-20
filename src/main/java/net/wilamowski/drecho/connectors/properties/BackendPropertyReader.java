package net.wilamowski.drecho.connectors.properties;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/** Properties */
public class BackendPropertyReader {
  private static final String PROPERTIES_PATH = "/backend.properties";
  private static final Logger logger = LogManager.getLogger(BackendPropertyReader.class);
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
    InputStream file = null;
    try {
      file = BackendPropertyReader.class.getResourceAsStream(PROPERTIES_PATH);
      if (file == null) {
        String errorMsg = "Properties file '" + PROPERTIES_PATH + "' not found.";
        logger.error(errorMsg);
        throw new IllegalArgumentException(errorMsg);
      }

      Properties properties = new Properties();
      properties.load(file);

      if (!properties.containsKey(key)) {
        throw new IllegalStateException("Missing value for key '" + key + "' in properties: " + PROPERTIES_PATH);
      }

      return properties.getProperty(key);
    } catch (IOException e) {
      String errorMsg = "Error reading properties file: " + e.getMessage();
      logger.error(errorMsg, e);
      throw new IllegalArgumentException(errorMsg, e);
    } finally {
      closeStream(file);
    }
  }


  private static void closeStream(InputStream stream) {
    if (stream != null) {
      try {
        stream.close();
      } catch (IOException e) {
        logger.error("Error closing input stream: {}", e.getMessage());
      }
    }
    }

  public static Boolean getBoolean(String key) {
    String value = getString(key);
    try {
      return BooleanUtils.toBooleanObject(value);
    } catch (IllegalArgumentException e) {
      String errorMsg = "Error parsing boolean value for key '" + key + "': " + e.getMessage();
      logger.error(errorMsg);
      throw new IllegalArgumentException(errorMsg, e);
    }
  }
}
