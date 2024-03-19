package net.wilamowski.drecho.client.presentation.dictionaries.general;

import java.util.Optional;
import java.util.stream.Collectors;
import javafx.beans.property.ListProperty;
import net.wilamowski.drecho.connectors.model.standalone.domain.dictionary.Dictionary;
import net.wilamowski.drecho.connectors.model.SimpleDictionariesService;
import net.wilamowski.drecho.client.application.mapper.DictionaryVmMapper;

public class ListLoader {
    private final SimpleDictionariesService service;

    private ListLoader(SimpleDictionariesService service) {
        this.service = service;
    }
    
    public static ListLoader source(SimpleDictionariesService service){
        return new ListLoader( service );
    } 

    public void loadValuesToFxList(
            String dictionaryCode, ListProperty<PositionFx> positions) {
        Optional<Dictionary> optionalDictionary = service.getDictionary(dictionaryCode);
        DictionaryVmMapper   mapper             = new DictionaryVmMapper();
        optionalDictionary.ifPresent(
                dict -> {
                    DictionaryFx dictionaryFx = mapper.toJavaFx(dict);
                    positions.setAll(
                            dictionaryFx.getPositions().stream()
                                    .filter( PositionFx::getActive )
                                    .collect( Collectors.toList()));
                });
    }
    
}
