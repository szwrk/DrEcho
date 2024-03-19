package net.wilamowski.drecho.client.presentation.dictionaries.general;

import javafx.beans.property.ListProperty;
import javafx.beans.property.StringProperty;

public class DictionaryFxBuilder {
  private StringProperty code;
  private StringProperty name;
  private StringProperty description;
  private ListProperty<PositionFx> positions;

  public DictionaryFxBuilder setCode(StringProperty code) {
    this.code = code;
    return this;
  }

  public DictionaryFxBuilder setName(StringProperty name) {
    this.name = name;
    return this;
  }

  public DictionaryFxBuilder setDescription(StringProperty description) {
    this.description = description;
    return this;
  }

  public DictionaryFxBuilder setPositions(ListProperty<PositionFx> positions) {
    this.positions = positions;
    return this;
  }

  public DictionaryFx createDictionaryFx() {
    return new DictionaryFx(code, name, description, positions);
  }
}
