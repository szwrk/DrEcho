package net.wilamowski.drecho.standalone.domain.visit;

public class UserNotFoundException extends RuntimeException {

  public UserNotFoundException(String message) {
    super(message);
  }
}
