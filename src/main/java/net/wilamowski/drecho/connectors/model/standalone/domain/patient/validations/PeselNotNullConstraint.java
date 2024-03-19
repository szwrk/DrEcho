package net.wilamowski.drecho.connectors.model.standalone.domain.patient.validations;

 class PeselNotNullConstraint implements Constraint {

  private final String pesel;

  public PeselNotNullConstraint(String pesel) {
    this.pesel = pesel;
  }

  @Override
  public boolean validate() {
    if (!pesel.isEmpty()) {
      return true;
    }
    return false;
  }

  @Override
  public String errorMessage() {
    return "Pesel code is required!";
  }
}

