package net.wilamowski.drecho.shared.auth;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
/**
 * @author Arkadiusz Wilamowski
 * <p></><a href="https://github.com/szwrk">GitHub</a></p>
 * <p> For questions or inquiries, at contact arek@wilamowski.net </p>
 */
public class AuthenticationResults {
  private static final Logger logger = LogManager.getLogger(AuthenticationResults.class);
  private boolean isAuthenticated;
  private String login;
  private String deniedReasonLogin;
  private String deniedReasonPassword;
  private Session session;

  private AuthenticationResults() {}

  private AuthenticationResults(
      boolean isAuthenticated,
      String login,
      String deniedReasonLogin,
      String deniedReasonPassword) {
    this.isAuthenticated = isAuthenticated;
    this.login = login;
    this.deniedReasonLogin = deniedReasonLogin;
    this.deniedReasonPassword = deniedReasonPassword;
    this.session = Session.instance( );
  }

  public static AuthenticationResults createConfirmAuthResults(String login) {
    logger.debug("Creating confirmation authentication results for login: {}", login);
    AuthenticationResults authenticationResults = new AuthenticationResults( true , login , null , null );
    Session.initialize( login );
    logger.traceExit();
    return authenticationResults;
  }

  public static AuthenticationResults createFailedAuthResults(
      String login, String reasonLoginMessage, String reasonPasswordMessage) {
    logger.trace("Creating failed authentication results for login: {}", login);
    return new AuthenticationResults(false, login, reasonLoginMessage, reasonPasswordMessage);
  }

  public final boolean isAuthenticated() {
    return isAuthenticated;
  }

  public final String loginMessage() {
    return deniedReasonLogin;
  }

  public final String passwordMessage() {
    return deniedReasonPassword;
  }

  public final Session getSession() {
    return session;
  }
}
