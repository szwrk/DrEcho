package poc.GenericDocumentGeneratorPoc;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDateTime;
import java.util.*;

public class Document {
    private static Logger log = LogManager.getLogger(Document.class);
    private List<AttributeValue> values;
    private String title;
    private String docCode;
    private String createdBy;
    private LocalDateTime now;

    public Document(DocumentDefinition definition, String createdBy) {
        this.docCode = definition.getCode();
        this.title = definition.getTitle();
        this.createdBy = createdBy;
        this.now = LocalDateTime.now();

        values = new ArrayList<>(  );
    }

    public void addAttributes(AttributeValue...attributes) {
        log.traceEntry();
        for (AttributeValue attribute : attributes) {
            try {
                addAtribute( attribute );
            } catch (NonUniqueException e) {
                log.error("Exception message: {}\nDuplicated attribute: {}\nAll attributes:  {}" ,e.getMessage(), attribute, attributes);
                throw new RuntimeException( e );
            }
        }
        log.traceExit();
    }

    public void addAtribute(AttributeValue attribute) throws NonUniqueException {
            log.traceEntry();
            if ( isNonUnique( attribute ))
            {
                log.debug("Not added attribute: {}", attribute );
                throw new NonUniqueException("Duplicate attribute code are not allowed in the set (per one class of document).");
            }
            values.add( attribute );
            log.traceExit();
    }

    private boolean isNonUnique(AttributeValue attribute) {
        log.debug("Checking unique of {}", attribute );
        boolean isUnique = values.stream( ).noneMatch( atr -> atr.getCode( ).equals( attribute.getCode( ) )
                           && values.stream( ).noneMatch( atr2 -> atr2.getClassType( ).equals( attribute.getClassType( ))) );
        log.debug( "Attribute is unique per instance of document and that document class: {}", isUnique );
        return isUnique;
    }
    public void save() {
        log.info( "Saving...",this );
        log.info( "Saved {}",this );
    }
    public Integer preSave() {
        log.error( "Fake get doc id - Not impl yet" );
        Random random = new Random( );
        return random.nextInt();
    }

    @Override
    public String toString() {
        return "Document{" +
                "values=" + values +
                ", title='" + title + '\'' +
                ", user='" + createdBy + '\'' +
                ", now=" + now +
                '}';
    }
}
