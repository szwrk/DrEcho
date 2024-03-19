package net.wilamowski.drecho.client.presentation.examinations.echo;

import javafx.beans.property.StringProperty;
import lombok.ToString;
import net.wilamowski.drecho.client.presentation.examinations.chooser.ExaminationDefinitionFx;

@ToString
public class EchoExaminationDefinition extends ExaminationDefinitionFx {

  public static final String SPECIFIC_EXAMINATION_VIEW = "/views/examination/EchoTteView.fxml";

  public EchoExaminationDefinition(StringProperty code, StringProperty name) {
    super(code, name, SPECIFIC_EXAMINATION_VIEW);
  }
}
