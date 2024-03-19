package net.wilamowski.drecho.connectors.model.standalone.domain.validator;

public class ValidationResult {
  private final Boolean aBoolean;
  private final String msg;

  private ValidationResult(Boolean aBoolean, String msg) {
    this.aBoolean = aBoolean;
    this.msg = msg;
  }

  /** Creating result if validation passed */
  public static ValidationResult valid() {
    return new ValidationResult(Boolean.TRUE, "");
  }

  /** Creating result if validation not passed */
  public static ValidationResult invalid(String msg) {
    return new ValidationResult(Boolean.FALSE, msg);
  }

  public String getMsg() {
    return msg;
  }

  public Boolean isValid() {
    return aBoolean;
  }
}
