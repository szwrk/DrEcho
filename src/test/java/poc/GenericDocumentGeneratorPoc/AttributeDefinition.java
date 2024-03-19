package poc.GenericDocumentGeneratorPoc;

public class AttributeDefinition {

  private String atrubuteClass;
  private String atrubuteCode;
  private final String type;

  public AttributeDefinition(String attributeClass, String attributeCode, String type) {
    this.atrubuteClass = attributeClass;
    this.atrubuteCode = attributeCode;
    this.type = type;
  }

  public static AttributeDefinition create(
      String attributeClass, String attributeCode, String type) {
    return new AttributeDefinition(attributeClass, attributeCode, type);
  }

  public String getAtrubuteClass() {
    return atrubuteClass;
  }

  public void setAttributeClass(String atrubuteClass) {
    this.atrubuteClass = atrubuteClass;
  }

  public String getAttributeCode() {
    return atrubuteCode;
  }

  public void setAtrubuteCode(String atrubuteCode) {
    this.atrubuteCode = atrubuteCode;
  }

  public String getType() {
    return type;
  }

  @Override
  public String toString() {
    return "AttributeDefinition{"
        + "atrubuteClass='"
        + atrubuteClass
        + '\''
        + ", atrubuteCode='"
        + atrubuteCode
        + '\''
        + ", type='"
        + type
        + '\''
        + '}';
  }
}
