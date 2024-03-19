package net.wilamowski.drecho.connectors.model.standalone.domain.validator;

class TextCanNotBeEmptyRule implements Rule<String> {

  @Override
  public ValidationResult check(String value) {
    if (value != null)
      if (value.isEmpty()) {
        return ValidationResult.invalid("Value can not be empty");
      } else {
        return ValidationResult.valid();
      }
    return ValidationResult.invalid("Value is null");
  }
}
