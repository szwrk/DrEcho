package net.wilamowski.drecho.client.presentation.examinations.pacemaker;

import javafx.beans.property.StringProperty;
import lombok.ToString;
import net.wilamowski.drecho.client.presentation.examinations.chooser.ExaminationDefinitionFx;
import org.apache.commons.lang3.NotImplementedException;

@ToString
public class PacemakerExaminationDefinitionFx extends ExaminationDefinitionFx {

  public static final String VIEW_PATH = "";

  public PacemakerExaminationDefinitionFx(StringProperty code, StringProperty name) {
    super(code, name, VIEW_PATH);
  }

  @Override
  public String getExamPath() throws NotImplementedException {
    throw new NotImplementedException(
        "The requested functionality is not implemented yet. This feature is planned for a future release.");
  }
}
