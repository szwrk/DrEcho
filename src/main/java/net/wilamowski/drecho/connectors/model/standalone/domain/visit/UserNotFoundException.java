package net.wilamowski.drecho.connectors.model.standalone.domain.visit;

public class UserNotFoundException extends RuntimeException {

  public UserNotFoundException(String message) {
    super(message);
  }
}
