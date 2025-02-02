package net.wilamowski.drecho.gateway.configuration;

import lombok.ToString;
import net.wilamowski.drecho.client.presentation.customs.modals.ExceptionAlert;
import net.wilamowski.drecho.app.bundle.Lang;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Arkadiusz Wilamowski
 *     <p></><a href="https://github.com/szwrk">GitHub</a>
 *     <p>For questions or inquiries, at contact arek@wilamowski.net High-level parameters that
 *     determine the choice of model implementations and repository configurations. In client-server
 *     mode, the repository is a server dependency.
 */
@ToString
public enum BackendType {
  STANDALONE_INMEMORY("Demo", "Demo application with in-memory database"),
  STANDALONE_LOCALDB("Standalone", "Standalone application with local file-based database"),
  STANDALONE_REMOTEDB("Network (RDBMS)", "Standalone application with remote database"),
  CLIENTSERVER("Network (REST Server)", "Standalone application for REST server");
  private static final Logger logger = LogManager.getLogger( BackendType.class);
  private final String label;
  private final String description;

  BackendType(String label, String description) {
    this.label = label;
    this.description = description;
  }

  public static BackendType of(String enumName) {
    BackendType backendType = null;
    try {
      backendType = valueOf(enumName);
    } catch (IllegalArgumentException e) {
      logger.error("An error occurred:", e.getMessage(), e);
      String header = Lang.getString("e.008.header");
      String msg = Lang.getString("e.008.msg");
      ExceptionAlert.create().showError(e, header, String.format(msg, enumName));
    } catch (NullPointerException e) {
      logger.error("An error occurred:", e.getMessage(), e);
      String header = Lang.getString("e.009.header");
      String msg = Lang.getString("e.009.msg");
      ExceptionAlert.create().showError(e, header, String.format(msg, enumName));
    }
    return backendType;
  }

  public String getLabel() {
    return label;
  }

  public String getDescription() {
    return description;
  }
}
