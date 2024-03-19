package net.wilamowski.drecho.shared.auth;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDateTime;
/**
 * @author Arkadiusz Wilamowski
 * <p></><a href="https://github.com/szwrk">GitHub</a></p>
 * <p> For questions or inquiries, at contact arek@wilamowski.net </p>
 */
public class Session {
  private static final Logger logger = LogManager.getLogger(Session.class);
  private static Session instance = null;
  private  String login;
  private  LocalDateTime sessionStartAt;

  private Session(String login , LocalDateTime sessionStartAt) {
    this.login = login;
    this.sessionStartAt = sessionStartAt;
  }

  public static void initialize(String login) {
    logger.traceEntry();
    if (instance == null) {
      LocalDateTime now = LocalDateTime.now( );
      instance = new Session(login, now);
      logger.info("Session created for login: {}; start at: {}", login, now );
    } else {
      logger.warn( "Session already created" );
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
