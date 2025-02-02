package net.wilamowski.drecho.standalone.domain.validator;

import java.util.List;

public interface Validator {
  void addRule(Rule rule);

  List<ValidationResult> checkRules(String testedValue);

  boolean isAllPassed(String newVal);
}
