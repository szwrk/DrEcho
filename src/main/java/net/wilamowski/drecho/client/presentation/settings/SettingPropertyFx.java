package net.wilamowski.drecho.client.presentation.settings;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
/**
 * @author Arkadiusz Wilamowski
 * <p></><a href="https://github.com/szwrk">GitHub</a></p>
 * <p> For questions or inquiries, at contact arek@wilamowski.net </p>
 */
@ToString
@Getter
@Setter
public class SettingPropertyFx {
  private StringProperty key = new SimpleStringProperty();
  private StringProperty value = new SimpleStringProperty();

  public SettingPropertyFx(String key, String value) {
    this.key = new SimpleStringProperty(key);
    this.value = new SimpleStringProperty(value);
  }

  public StringProperty keyProperty() {
    return key;
  }

  public StringProperty valueProperty() {
    return value;
  }
}
