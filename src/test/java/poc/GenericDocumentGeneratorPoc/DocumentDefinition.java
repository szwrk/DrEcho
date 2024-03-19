package poc.GenericDocumentGeneratorPoc;

public class DocumentDefinition {
  private final String code;
  private final String title;

  public DocumentDefinition(String code, String title) {
    this.code = code;
    this.title = title;
  }

  public static DocumentDefinition create(String code, String title) {
    return new DocumentDefinition(code, title);
  }

  public String getTitle() {
    return title;
  }

  public String getCode() {
    return code;
  }

  @Override
  public String toString() {
    return "DocumentDefinition{" + "code='" + code + '\'' + ", title='" + title + '\'' + '}';
  }
}
