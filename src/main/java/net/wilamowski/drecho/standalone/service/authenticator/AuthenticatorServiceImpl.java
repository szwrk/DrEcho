package net.wilamowski.drecho.standalone.service.authenticator;

import java.util.*;
import net.wilamowski.drecho.app.auth.AuthenticationResults;
import net.wilamowski.drecho.app.dto.UserDto;
import net.wilamowski.drecho.gateway.AuthenticatorService;
import net.wilamowski.drecho.standalone.domain.user.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AuthenticatorServiceImpl implements AuthenticatorService {

  private static final Logger log = LogManager.getLogger( AuthenticatorServiceImpl.class);

  private final UserService userService;

  public AuthenticatorServiceImpl(UserService userService) {
    this.userService = userService;
  }

  @Override
  public AuthenticationResults performAuthentication(Credentials credential) {
    log.trace("[SERVICE] Perform authentication...");
    log.debug("[SERVICE] Perform authentication for user: {}", credential.getLogin());
    Objects.requireNonNull(credential);
    String            inputLogin        = credential.getLogin();
    Optional<UserDto> optionalFoundUser = userService.findUserByLogin(inputLogin);

    AuthenticationResults authResults;

    if (optionalFoundUser.isPresent()) {
      log.debug("[SERVICE] User found. Process credentials...");
      UserDto foundUser = optionalFoundUser.get();
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

  private static AuthenticationResults checkUserPassword(UserDto foundUser, Credentials credentials) {
    log.trace("[SERVICE] Password checking...");
    AuthenticationResults authResults;
    if (foundUser.password().equals(credentials.getPassword())) {
      log.debug("[SERVICE] Password is correct");
      authResults = AuthenticationResults.createConfirmAuthResults(foundUser.login());
      log.info("[SERVICE] Session created for user: {}", foundUser.login());
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
