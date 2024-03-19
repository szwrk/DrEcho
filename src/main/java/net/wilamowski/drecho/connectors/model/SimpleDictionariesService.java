package net.wilamowski.drecho.connectors.model;

import java.util.Optional;
import java.util.Set;
import net.wilamowski.drecho.connectors.model.standalone.domain.dictionary.Dictionary;
import net.wilamowski.drecho.connectors.model.standalone.domain.dictionary.Position;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public interface SimpleDictionariesService {
   Logger logger = LogManager.getLogger(SimpleDictionariesService.class);
  void addDictionary(Dictionary dictionary);

  Optional<Dictionary> getDictionary(String dictName);

  Optional<Set<Dictionary>> getDictionaries();

  void enable(Dictionary dictionary, Position positionRowValue);

  void disable(Dictionary dictionary, Position positionRowValue);

  void updateDictionary(Dictionary dictionary);


}
