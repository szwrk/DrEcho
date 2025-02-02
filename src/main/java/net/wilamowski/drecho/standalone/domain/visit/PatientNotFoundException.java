package net.wilamowski.drecho.standalone.domain.visit;

public class PatientNotFoundException extends RuntimeException {
  public PatientNotFoundException(String message) {
    super(message);
  }
}
