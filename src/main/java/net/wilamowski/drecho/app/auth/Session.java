package net.wilamowski.drecho.app.auth;

import java.time.LocalDateTime;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Arkadiusz Wilamowski
 *     <p></><a href="https://github.com/szwrk">GitHub</a>
 *     <p>For questions or inquiries, at contact arek@wilamowski.net
 */
public class Session {
  private static final Logger logger = LogManager.getLogger(Session.class);
  private static Session instance = null;
  private final String login;
  private final LocalDateTime sessionStartAt;

  private Session(String login, LocalDateTime sessionStartAt) {
    this.login = login;
    this.sessionStartAt = sessionStartAt;
  }

  public static void initialize(String login) {
    logger.traceEntry();
    if (instance == null) {
      LocalDateTime now = LocalDateTime.now();
      instance = new Session(login, now);
      logger.info("Session created for login: {}; start at: {}", login, now);
    } else {
      logger.warn("Session already created");
    }
    logger.traceExit();
  }

  public static Session instance() {
    return instance;
  }

  public String getUserLogin() {
    return login;
  }

  public LocalDateTime sessionStartAt() {
    return sessionStartAt;
  }
}
