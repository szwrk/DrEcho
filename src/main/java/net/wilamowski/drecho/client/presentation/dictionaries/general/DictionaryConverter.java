package net.wilamowski.drecho.client.presentation.dictionaries.general;

import javafx.util.StringConverter;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DictionaryConverter extends StringConverter<PositionFx> {
  private static final Logger logger = LogManager.getLogger(DictionaryConverter.class);

  @Override
  public String toString(PositionFx positionFx) {
    if (positionFx != null) {
      return positionFx.getName();
    }
    return "";
  }

  @Override
  public PositionFx fromString(String s) {
    String msg = "Dictionary from String is not implemented";
    logger.error(msg);
    throw new NotImplementedException(msg);
  }
}
