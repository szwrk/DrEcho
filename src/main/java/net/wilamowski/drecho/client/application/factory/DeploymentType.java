package net.wilamowski.drecho.client.application.factory;

import lombok.ToString;
import net.wilamowski.drecho.client.presentation.customs.modals.ExceptionAlert;
import net.wilamowski.drecho.shared.bundle.Lang;
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
public enum DeploymentType {
  STANDALONE_INMEMORY("Demo", "Demo application with in-memory database"),
  STANDALONE_LOCALDB("Standalone", "Standalone application with local file-based database"),
  STANDALONE_REMOTEDB("Network (RDBMS)", "Standalone application with remote database"),
  CLIENTSERVER("Network (REST Server)", "Standalone application for REST server");
  private static final Logger logger = LogManager.getLogger(DeploymentType.class);
  private final String label;
  private final String description;

  DeploymentType(String label, String description) {
    this.label = label;
    this.description = description;
  }

  public static DeploymentType of(String enumName) {
    DeploymentType deploymentType = null;
    try {
      deploymentType = valueOf(enumName);
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
    return deploymentType;
  }

  public String getLabel() {
    return label;
  }

  public String getDescription() {
    return description;
  }
}
