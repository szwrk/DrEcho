package net.wilamowski.drecho.connectors.model;

import net.wilamowski.drecho.connectors.model.standalone.service.authenticator.Credentials;
import net.wilamowski.drecho.shared.auth.AuthenticationResults;

public interface ConnectorAuthenticator {
  AuthenticationResults performAuthentication(Credentials credentials);
}
