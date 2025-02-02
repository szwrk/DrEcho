package net.wilamowski.drecho.client.presentation.dictionaries.general;

import java.util.Optional;
import java.util.stream.Collectors;
import javafx.beans.property.ListProperty;
import net.wilamowski.drecho.client.application.mapper.DictionaryVmMapper;
import net.wilamowski.drecho.configuration.backend_ports.DictionariesService;
import net.wilamowski.drecho.standalone.domain.dictionary.Dictionary;

public class ListLoader {
  private final DictionariesService service;

  private ListLoader(DictionariesService service) {
    this.service = service;
  }

  public static ListLoader source(DictionariesService service) {
    return new ListLoader(service);
  }

  public void loadValuesToFxList(String dictionaryCode, ListProperty<PositionFx> positions) {
    Optional<Dictionary> optionalDictionary = service.getDictionary(dictionaryCode);
    DictionaryVmMapper mapper = new DictionaryVmMapper();
    optionalDictionary.ifPresent(
        dict -> {
          DictionaryFx dictionaryFx = mapper.toJavaFx(dict);
          positions.setAll(
              dictionaryFx.getPositions().stream()
                  .filter(PositionFx::getActive)
                  .collect(Collectors.toList()));
        });
  }
}
