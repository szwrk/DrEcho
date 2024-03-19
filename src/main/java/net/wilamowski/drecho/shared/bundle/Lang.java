package net.wilamowski.drecho.shared.bundle;

import java.util.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Arkadiusz Wilamowski
 * <p></><a href="https://github.com/szwrk">GitHub</a></p>
 * <p> For questions or inquiries, at contact arek@wilamowski.net </p>
 */
public class Lang {
    private static final Logger logger = LogManager.getLogger( Lang.class);
    private static final String DEFAULT_VALUE = "N/A";
    private static ResourceBundle bundle;

    private Lang() {
    }

    public static void initializeSingleton(ResourceBundle resourceBundle) {
        Objects.requireNonNull(resourceBundle, "ResourceBundle must not be null");
        bundle = resourceBundle;
    }

    public static ResourceBundle getBundle() {
        return bundle;
    }

    public static List<String> getKeysByValue(String targetValue) {
        List<String> matchingKeys = new ArrayList<>();

        if (targetValue != null && !targetValue.isBlank()) {
            Enumeration<String> keys = Lang.getKeys();

            while (keys.hasMoreElements()) {
                String key = keys.nextElement();
                String value = Lang.getString(key);

                if (!value.isBlank() && value.equals(targetValue)) {
                    matchingKeys.add(key);
                }
            }
        }
        return matchingKeys;
    }

    public static String getString(String key) {
        if (bundle == null) {
            logger.error("ResourceBundle is null.");
            return DEFAULT_VALUE;
        }

        try {
            return bundle.getString(key);
        } catch (MissingResourceException e) {
            logger.error("No value for bundle key: {}", key);
            return DEFAULT_VALUE;
        } catch (Exception e) {
            logger.error("Error while getting value for bundle key: {}", key, e);
            return DEFAULT_VALUE;
        }
    }

    public static Enumeration<String> getKeys() {
        return bundle.getKeys();
    }
}
