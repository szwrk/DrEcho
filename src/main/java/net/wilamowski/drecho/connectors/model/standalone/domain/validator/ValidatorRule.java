package net.wilamowski.drecho.connectors.model.standalone.domain.validator;

import java.util.List;
import java.util.stream.Collectors;

class ValidatorRule implements Validator {
  private final List<Rule> rules;

  ValidatorRule(List<Rule> rules) {
    this.rules = rules;
  }

  @Override
  public void addRule(Rule rule) {
    rules.add(rule);
  }

  public List<ValidationResult> checkRules(String testedValue) {
    assert rules.isEmpty();
    return rules.stream().map(r -> r.check(testedValue)).collect(Collectors.toList());
  }

  @Override
  public boolean isAllPassed(String newVal) {
    return this.checkRules(newVal).stream().allMatch(x -> x.isValid().equals(Boolean.TRUE));
  }
}
