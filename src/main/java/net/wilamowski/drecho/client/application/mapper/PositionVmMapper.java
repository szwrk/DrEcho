package net.wilamowski.drecho.client.application.mapper;

import java.util.List;
import java.util.stream.Collectors;
import net.wilamowski.drecho.client.presentation.dictionaries.general.PositionFx;
import net.wilamowski.drecho.connectors.model.standalone.domain.dictionary.Position;

/**
 * @author Arkadiusz Wilamowski
 * <p></><a href="https://github.com/szwrk">GitHub</a></p>
 * <p> For questions or inquiries, at contact arek@wilamowski.net </p>
 */

public class PositionVmMapper {
    public List<PositionFx> toJavaFx(List<Position> positions) {
        return positions.stream( ).map( this::toJavaFx ).collect( Collectors.toList( ) );
    }
    public PositionFx toJavaFx(Position position) {
        return new PositionFx( position );
    }
    public Position toDomain(PositionFx positionFx) {
        String code = positionFx.getCode( );
        String name = positionFx.getName( );
        int order = positionFx.getOrder( );
        boolean active = positionFx.getActive( );
        return new Position(code, name, order, active);
    }
}
