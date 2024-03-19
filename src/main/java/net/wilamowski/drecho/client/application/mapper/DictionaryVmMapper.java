package net.wilamowski.drecho.client.application.mapper;

import java.util.List;
import java.util.stream.Collectors;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import net.wilamowski.drecho.client.presentation.dictionaries.general.DictionaryFx;
import net.wilamowski.drecho.client.presentation.dictionaries.general.DictionaryFxBuilder;
import net.wilamowski.drecho.client.presentation.dictionaries.general.PositionFx;
import net.wilamowski.drecho.connectors.model.standalone.domain.dictionary.Dictionary;
import net.wilamowski.drecho.connectors.model.standalone.domain.dictionary.DictionaryBuilder;

/**
 * @author Arkadiusz Wilamowski
 * <p></><a href="https://github.com/szwrk">GitHub</a></p>
 * <p> For questions or inquiries, at contact arek@wilamowski.net </p>
 */

public class DictionaryVmMapper {
    public DictionaryFx toJavaFx(Dictionary dictionary) {
        PositionVmMapper positionVmMapper = new PositionVmMapper( );
        List<PositionFx> positions = dictionary.getPositions( ).stream( )
                .map( positionVmMapper::toJavaFx )
                .collect( Collectors.toList( ) );

        ObservableList<PositionFx> positionFxObservableList = FXCollections.observableArrayList( positions );
        ListProperty<PositionFx>   positionsProperty        = new SimpleListProperty<>( positionFxObservableList );

        return new DictionaryFxBuilder( )
                .setCode( new SimpleStringProperty( dictionary.getCode( ) ) )
                .setName( new SimpleStringProperty( dictionary.getName( ) ) )
                .setDescription( new SimpleStringProperty( dictionary.getDescription( ) ) )
                .setPositions( positionsProperty )
                .createDictionaryFx( );
    }
    public Dictionary toDomain(DictionaryFx dictionaryFx) {
        PositionVmMapper positionVmMapper = new PositionVmMapper( );
        return new DictionaryBuilder( )
                .setCode( dictionaryFx.getCode( ) )
                .setName( dictionaryFx.getName( ) )
                .setDescription( dictionaryFx.getDescription( ) )
                .addPositions( dictionaryFx.getPositions( )
                        .stream( )
                        .map( positionFx -> positionVmMapper.toDomain( positionFx ) )
                        .collect( Collectors.toList( ) ) )
                .createDictionary( );
    }
}
