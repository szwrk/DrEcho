package net.wilamowski.drecho.connectors.model;

import net.wilamowski.drecho.shared.auth.AuthenticationResults;
import net.wilamowski.drecho.connectors.model.standalone.service.authenticator.Credentials;

public interface AuthenticatorService {
  AuthenticationResults performAuthentication(Credentials credentials);
}
