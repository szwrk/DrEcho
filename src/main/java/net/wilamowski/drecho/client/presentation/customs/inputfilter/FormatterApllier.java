package net.wilamowski.drecho.client.presentation.customs.inputfilter;

import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;

public class FormatterApllier {
  public void setFormatter(TextField field, TextFormatter formatter) {
    field.setTextFormatter(formatter);
  }

  public void setFormatter(TextArea field, TextFormatter formatter) {
    field.setTextFormatter(formatter);
  }
}
