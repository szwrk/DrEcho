package net.wilamowski.drecho.gateway.ports;

import net.wilamowski.drecho.standalone.service.authenticator.Credentials;
import net.wilamowski.drecho.app.auth.AuthenticationResults;

public interface AuthenticatorService {
  AuthenticationResults performAuthentication(Credentials credentials);
}
