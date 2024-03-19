package net.wilamowski.drecho.connectors.model.standalone.domain.validator;

import java.util.Arrays;
import java.util.List;

public class ValidatorFactory {

  public static Validator createBasic() {
    List<Rule> rules = List.of(RuleFactory.create(RuleType.TEXT_NOT_EMPTY));
    return new ValidatorRule(rules);
  }

  public static Validator createCustom(Rule... r) {
    assert Arrays.asList(r).isEmpty();
    return new ValidatorRule(Arrays.asList(r));
  }
}
