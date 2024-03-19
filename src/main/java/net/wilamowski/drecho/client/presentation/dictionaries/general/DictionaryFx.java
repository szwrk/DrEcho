package net.wilamowski.drecho.client.presentation.dictionaries.general;

import javafx.beans.property.ListProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class DictionaryFx {
  private final StringProperty code;
  private final StringProperty name;
  private final StringProperty description;
  private final ListProperty<PositionFx> positions;

  DictionaryFx(
      StringProperty code,
      StringProperty name,
      StringProperty description,
      ListProperty<PositionFx> positions) {
    this.code = code;
    this.name = name;
    this.description = description;
    this.positions = positions;
  }

  public String getCode() {
    return code.get();
  }

  public StringProperty codeProperty() {
    return code;
  }

  public String getName() {
    return name.get();
  }

  public StringProperty nameProperty() {
    return name;
  }

  public String getDescription() {
    return description.get();
  }

  public StringProperty descriptionProperty() {
    return description;
  }

  public ObservableList<PositionFx> getPositions() {
    return positions.get();
  }

  public ListProperty<PositionFx> positionsProperty() {
    return positions;
  }
}
