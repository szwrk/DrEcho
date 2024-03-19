package net.wilamowski.drecho.connectors.model.standalone.domain.validator;

class RuleFactory {
  public static Rule create(RuleType type) {
    switch (type) {
      case TEXT_NOT_EMPTY:
        return new TextCanNotBeEmptyRule();
      default:
        throw new IllegalArgumentException("Creating instance of Rule error. Unknown enum value");
    }
  }
}
