package net.wilamowski.drecho.connectors.model.standalone.domain.dictionary;

import java.util.List;
import lombok.ToString;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@ToString
public class Dictionary {
    private static final Logger logger = LogManager.getLogger(Dictionary.class);
    private final String code;
    private final String name;
    private final String description;
    private final List<Position> positions;

    public Dictionary(String code , String name , String description ,  List<Position> positions) {
        this.code = code;
        this.name = name;
        this.description = description;
        this.positions = positions;
    }


    public void enablePosition(String positionName) {
        positions.stream()
                .filter( pos -> pos.getCode().equals( positionName ) )
                .forEach ( Position::enable );
    }

    public void disablePosition(String positionName) {
        positions.stream()
                .filter( pos -> pos.getCode().equals( positionName ) )
                .forEach ( Position::disable );
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<Position> getPositions() {
        return positions;
    }
}
