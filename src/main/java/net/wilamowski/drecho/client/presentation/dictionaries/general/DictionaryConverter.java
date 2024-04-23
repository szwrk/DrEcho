package net.wilamowski.drecho.client.presentation.dictionaries.general;

import java.util.List;
import java.util.Optional;
import javafx.util.StringConverter;
import net.wilamowski.drecho.client.application.mapper.PositionVmMapper;
import net.wilamowski.drecho.connectors.model.ConnectorSimpleDictionaries;
import net.wilamowski.drecho.connectors.model.standalone.domain.dictionary.Dictionary;
import net.wilamowski.drecho.connectors.model.standalone.domain.dictionary.Position;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DictionaryConverter extends StringConverter<PositionFx> {
  private static final Logger logger = LogManager.getLogger(DictionaryConverter.class);
  private final ConnectorSimpleDictionaries service;
  private final String dictName;

  public DictionaryConverter(ConnectorSimpleDictionaries service, String dictName) {
    this.service = service;
    this.dictName = dictName;
  }

  @Override
  public String toString(PositionFx positionFx) {
    if (positionFx != null) {
      return positionFx.getName();
    } else {
      logger.warn( "DictionaryConverter can not convert toString! Return empty value." );
      return "";
    }
  }

  @Override
  public PositionFx fromString(String s) {
    String msg = "DictionaryConverters issue. Dictionary from String is not implemented. Passed val: " + s;
    Optional<Dictionary> dictionary = service.getDictionary( dictName );
    List<Position> positions        = null;
    if (!dictionary.isEmpty()) {
      positions = dictionary.get().getPositions();

      Optional<Position> first =
          positions.stream()
              .filter(x -> x.getCode().equals(s))
              .findFirst();
      Position position = first.orElseThrow();
      PositionVmMapper positionVmMapper = new PositionVmMapper();
      return positionVmMapper.toJavaFx(position);
    } else {
      logger.error(msg);
      throw new IllegalArgumentException(msg);
    }

  }
}
