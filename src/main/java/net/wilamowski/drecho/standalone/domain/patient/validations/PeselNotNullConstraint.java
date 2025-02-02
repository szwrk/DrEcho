package net.wilamowski.drecho.standalone.domain.patient.validations;

class PeselNotNullConstraint implements Constraint {

  private final String pesel;

  public PeselNotNullConstraint(String pesel) {
    this.pesel = pesel;
  }

  @Override
  public boolean validate() {
    return !pesel.isEmpty();
  }

  @Override
  public String errorMessage() {
    return "Pesel code is required!";
  }
}
