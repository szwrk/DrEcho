package net.wilamowski.drecho.standalone.domain.patient.validations;

class PeselCodeLengthConstraint implements Constraint {

  private final String pesel;

  public PeselCodeLengthConstraint(String pesel) {
    this.pesel = pesel;
  }

  @Override
  public boolean validate() {
    return pesel != null && pesel.length() == 11;
  }

  @Override
  public String errorMessage() {
    return "Pesel code should contain 11 digit";
  }
}
