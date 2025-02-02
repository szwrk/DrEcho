package net.wilamowski.drecho.app.bundle;

import java.util.*;
import net.wilamowski.drecho.client.presentation.customs.modals.NativeAlert;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Arkadiusz Wilamowski
 *     <p></><a href="https://github.com/szwrk">GitHub</a>
 *     <p>For questions or inquiries, at contact arek@wilamowski.net Static factory method for
 *     building resource
 */
public class ResourceBundleFactory {

  private static final Logger log = LogManager.getLogger(ResourceBundleFactory.class);
  private static final String BUNDLE_PATH_STRING_FORMAT = "%1$s/%2$s_%3$s_%4$s";
  private static final String DIR_NAME_OF_BUNDLES = "bundles";

  private ResourceBundleFactory() {}

  public static ResourceBundle instanceMainApp(String language) {
    if (language.equals("PL")) {
      return createBundle("MedNoteApp", "pl", "PL");
    } else if (language.equals("GB")) {
      return createBundle("MedNoteApp", "en", "GB");
    } else {
      throw new IllegalArgumentException();
    }
  }

  private static ResourceBundle createBundle(
      String fileName, String localeLanguage, String localeCountry) {
    Locale locale = new Locale(localeLanguage, localeCountry);
    String bundlePath =
        String.format(
            BUNDLE_PATH_STRING_FORMAT,
            ResourceBundleFactory.DIR_NAME_OF_BUNDLES,
            fileName,
            localeLanguage,
            localeCountry);
    ResourceBundle bundle = null;
    log.debug("Resource path for {} is {} ", fileName, bundlePath);
    try {
      bundle = ResourceBundle.getBundle(bundlePath, locale);
    } catch (MissingResourceException e) {
      log.error(e.getMessage(), e);
      String errorMsg =
          String.format(
              "Resource bundle not found for path %1$s, with name: %2$s", bundlePath, fileName);
      log.error(errorMsg);
      NativeAlert.safeError(e);
    }
    return bundle;
  }

  static ResourceBundle getNewInstanceLoginApp(String language) {
    if (language.equals("PL")) {
      return createBundle("Login", "pl", "PL");
    } else if (language.equals("GB")) {
      return createBundle("Login", "en", "GB");
    } else {
      throw new IllegalArgumentException();
    }
  }
}
