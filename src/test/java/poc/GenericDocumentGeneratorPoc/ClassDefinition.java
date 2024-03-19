package poc.GenericDocumentGeneratorPoc;

import java.util.ArrayList;
import java.util.List;

public class ClassDefinition {
  private final String codeClass;
  private final String nameClass;

  private final List<AttributeDefinition> attributeDefinition;

  public ClassDefinition(String codeClass, String nameClass) {
    this.codeClass = codeClass;
    this.nameClass = nameClass;
    attributeDefinition = new ArrayList<>();
  }

  public static ClassDefinition create(String klasa1, String klasaEcho) {
    return new ClassDefinition(klasa1, klasaEcho);
  }

  public void addAttributeDefinition(AttributeDefinition definition) {
    if (alreadyExistsSame(definition)) {
      String msg =
          String.format(
              "Duplicate attibute definition for class, class name: %s, code: %s are not allowed in the set (per one class of document)",
              definition.getAtrubuteClass(), definition.getAttributeCode());
      throw new NonUniqueException(msg);
    }
    attributeDefinition.add(definition);
  }

  private boolean alreadyExistsSame(AttributeDefinition definition) {
    return attributeDefinition.stream()
        .anyMatch(
            e ->
                e.getAtrubuteClass().equals(definition.getAtrubuteClass())
                    && e.getAttributeCode().equals(definition.getAttributeCode()));
  }

  public List<AttributeDefinition> fields() {
    return attributeDefinition;
  }

  @Override
  public String toString() {
    return "ClassDefinition{"
        + "codeClass='"
        + codeClass
        + '\''
        + ", nameClass='"
        + nameClass
        + '\''
        + '}';
  }
}
