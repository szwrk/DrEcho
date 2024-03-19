package net.wilamowski.drecho.connectors.model.standalone.service.authenticator;

import java.util.*;
import net.wilamowski.drecho.connectors.model.AuthenticatorService;
import net.wilamowski.drecho.connectors.model.standalone.domain.user.UserService;
import net.wilamowski.drecho.connectors.model.standalone.domain.user.account.User;
import net.wilamowski.drecho.shared.auth.AuthenticationResults;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AuthenticatorServiceImpl implements AuthenticatorService {

  private static final Logger log = LogManager.getLogger(AuthenticatorServiceImpl.class);

  private final UserService userService;

  public AuthenticatorServiceImpl(UserService userService) {
    this.userService = userService;
  }

  @Override
  public AuthenticationResults performAuthentication(Credentials credential) {
    log.trace("[SERVICE] Perform authentication...");
    log.debug("[SERVICE] Perform authentication for user: {}", credential.getLogin());
    Objects.requireNonNull(credential);
    String inputLogin = credential.getLogin();
    Optional<User> optionalFoundUser = userService.getUserByLogin(inputLogin);

    AuthenticationResults authResults;

    if (optionalFoundUser.isPresent()) {
      log.debug("[SERVICE] User found. Process credentials...");
      User foundUser = optionalFoundUser.get();
      authResults = checkUserPassword(foundUser, credential);
    } else {
      String errorMessage = String.format("Wrong credentials! User %s does not exist!", inputLogin);
      log.info("[SERVICE] Wrong credentials!");
      log.debug(errorMessage);
      authResults = AuthenticationResults.createFailedAuthResults(inputLogin, errorMessage, "");
    }

    log.trace("[SERVICE] Authentication performed.");
    return authResults;
  }

  private static AuthenticationResults checkUserPassword(User foundUser, Credentials credentials) {
    log.trace("[SERVICE] Password checking...");
    AuthenticationResults authResults;
    if (foundUser.getPassword().equals(credentials.getPassword())) {
      log.debug("[SERVICE] Password is correct");
      authResults = AuthenticationResults.createConfirmAuthResults(foundUser.getLogin());
      log.info("[SERVICE] Session created for user: {}", foundUser.getLogin());
    } else {
      log.trace("[SERVICE] Password isn't correct");
      authResults =
          AuthenticationResults.createFailedAuthResults(
              credentials.getLogin(), null, "Wrong password!");
    }
    log.traceExit();
    return authResults;
  }
}
