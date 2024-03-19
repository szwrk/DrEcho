package net.wilamowski.drecho.client.presentation.examinations.chooser;

import javafx.beans.property.StringProperty;
import lombok.ToString;
import org.apache.commons.lang3.NotImplementedException;

@ToString
public abstract class ExaminationDefinitionFx {
  private final StringProperty code;
  private final StringProperty name;
  private final String examPath;

  public ExaminationDefinitionFx(StringProperty code, StringProperty name, String examPath) {
    this.code = code;
    this.name = name;
    this.examPath = examPath;
  }

  public String getCode() {
    return code.get();
  }

  public void setCode(String code) {
    this.code.set(code);
  }

  public StringProperty codeProperty() {
    return code;
  }

  public String getName() {
    return name.get();
  }

  public void setName(String name) {
    this.name.set(name);
  }

  public StringProperty nameProperty() {
    return name;
  }

  public String getExamPath() throws NotImplementedException {
    return examPath;
  }

  //
  //    abstract String showForm();
  //   abstract public ExamFx newInstanceByDefinition() throws ExaminationNotImplemented;
}
