package net.wilamowski.drecho.connectors.model.standalone.domain.dictionary;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DictionaryBuilder {
  private static final Logger logger = LogManager.getLogger(DictionaryBuilder.class);
  private String code;
  private String name;
  private String description;
  private List<Position> positions;

  public DictionaryBuilder() {}

  public Dictionary create(Dictionary existingDictionary) {
    return new Dictionary(
        existingDictionary.getCode(),
        existingDictionary.getName(),
        existingDictionary.getDescription(),
        existingDictionary.getPositions());
  }

  public DictionaryBuilder setCode(String code) {
    this.code = code;
    return this;
  }

  public DictionaryBuilder setName(String name) {
    this.name = name;
    return this;
  }

  public DictionaryBuilder setDescription(String description) {
    this.description = description;
    return this;
  }

  public DictionaryBuilder addPositions(List<Position> positions) {
    Objects.requireNonNull(positions, "Parameter is null");

    if (this.positions == null) {
      this.positions = new ArrayList<>();
    }

    for (Position position : positions) {
      if (this.positions.stream().anyMatch(e -> e.getCode().equals(position.getCode()))) {
        logger.warn("Position with code '{}' already exists!", position.getCode());
        throw new IllegalArgumentException(
            "Position with code '" + position.getCode() + "' already exists");
      }
      this.positions.add(position);
    }

    return this;
  }

  public DictionaryBuilder addPosition(Position position) {
    Objects.requireNonNull(position, "Parameter is null");
    if (positions == null) {
      this.positions = new ArrayList<>();
    }
    if (positions.stream().anyMatch(e -> e.getCode().equals(position.getCode()))) {
      logger.warn("Position with code '{}' already exists!", position.getCode());
      throw new IllegalArgumentException(
          "Position with code '" + position.getCode() + "' already exists");
    }
    positions.add(position);
    return this;
  }

  public Dictionary createDictionary() {
    return new Dictionary(code, name, description, positions);
  }
}
