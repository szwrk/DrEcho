package net.wilamowski.drecho.connectors.model.standalone.domain.visit;

public class PatientNotFoundException extends RuntimeException {
  public PatientNotFoundException(String message) {
    super(message);
  }
}
