package net.wilamowski.drecho.client.application.exceptions;
/**
 * @author Arkadiusz Wilamowski
 * <p></><a href="https://github.com/szwrk">GitHub</a></p>
 * <p> For questions or inquiries, at contact arek@wilamowski.net </p>
 */
public class InvalidCredentialsException extends RuntimeException {
  public InvalidCredentialsException(String message) {
    super(message);
  }
}
