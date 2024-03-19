package poc.GenericDocumentGeneratorPoc;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.util.Objects;

public class AttributeValue {

    private static Logger log = LogManager.getLogger( AttributeValue.class);
    protected Integer rootId;
    protected String classType;
    protected AttributeDefinition definition;
    protected String dataType;
    protected String textValue;
    protected BigDecimal numberValue;
    protected String classCod;

    private AttributeValue(Integer rootId , String classType , String code, String containedDataType , String textValue, BigDecimal numericValue) {
        this.rootId = rootId;
        this.classType = classType;
        this.dataType = containedDataType;
        this.textValue = textValue;
        this.numberValue = numericValue;
        this.classCod = code;
    }

    /**
     * Creates an {@link AttributeValue} based on the provided parameters.
     *
     * @param instanceDocumentId The ID of the instance document.
     * @param definition         The {@link AttributeDefinition} specifying the attribute.
     * @param textValue              The value to be associated with the attribute.
     * @return An {@link AttributeValue} representing the attribute with the provided value.
     * @throws NullPointerException If {@code definition} is {@code null}.
     * @throws IllegalArgumentException If the attribute type specified in {@code definition} is not "TEXT".
     */
    public static AttributeValue create(Integer instanceDocumentId, AttributeDefinition definition, String textValue) {
        Objects.requireNonNull( definition,"Definicja atrybutu nie moze być pusta" );
        if (definition.getType().equals( "TEXT" )){
            log.debug( "If start with TEXT" );
            String  attributeClass = definition.getAtrubuteClass();
            String  attributeCode = definition.getAttributeCode();
            //checks null
            String type = definition.getType( );
            parametersCheckNull( instanceDocumentId , attributeClass , attributeCode ,  type );
            return new AttributeValue( instanceDocumentId,  attributeClass, attributeCode, type ,  textValue, null);
        } else {
            log.error("Creating attibuteValue failed. Parameter doesn't exists: {}", definition.getType());
            throw new IllegalArgumentException( "Creating attibuteValue failed. Parameter doesn't exists: " + definition.getType());
        }
    }

    /**
     * Creates an {@link AttributeValue} based on the provided parameters.
     *
     * @param instanceDocumentId The ID of the instance document.
     * @param definition         The {@link AttributeDefinition} specifying the attribute.
     * @param numericValue              The numericValue to be associated with the attribute.
     * @return An {@link AttributeValue} representing the attribute with the provided numericValue.
     * @throws NullPointerException If {@code definition} is {@code null}.
     * @throws IllegalArgumentException If the attribute type specified in {@code definition} is not "NUM".
     */

    public static AttributeValue create(Integer instanceDocumentId, AttributeDefinition definition, BigDecimal numericValue) {
        Objects.requireNonNull( definition,"Definicja atrybutu nie moze być pusta" );
          if ( definition.getType().equals( "NUM" ) ) {
            log.debug( "If start with NUM" );
            Objects.requireNonNull( definition,"Definicja atrybutu nie moze być pusta" );
            String  attributeClass = definition.getAtrubuteClass();
            String    attributeCode = definition.getAttributeCode();
            String       type              = definition.getType( );
            parametersCheckNull( instanceDocumentId , attributeClass , attributeCode , type );
            return new AttributeValue( instanceDocumentId,  attributeClass, attributeCode, type,  null, numericValue.stripTrailingZeros( ));
        } else {
            log.error("Creating attibuteValue failed. Parameter dosnt exists: {}", definition.getType());
            throw new IllegalArgumentException( "Creating attibuteValue failed. Parameter dosnt exists: "+ definition.getType());
        }
    }

    private static void parametersCheckNull(Integer instanceDocumentId , String attributeClass , String attributeCode , String containedDataType) {
        StringBuilder sb        = new StringBuilder("Null values are not allowed. Please provide non-null values for the following parameters:");
        boolean       isAnyNull = false;
        if ( instanceDocumentId == null){
            sb.append("\n- instanceDocumentId");
            isAnyNull = true;
        }
        if ( attributeClass == null) {
            sb.append("\n- attributeClass");
            isAnyNull = true;
        }
        if ( attributeCode == null) {
            sb.append("\n- attributeCode");
            isAnyNull = true;
        }
        if ( containedDataType == null ) {
            sb.append("\n- containedDataType");
            isAnyNull = true;
        }
        if (isAnyNull){
            throw new IllegalArgumentException( sb.toString() );
        }

    }

    String getDataType() {
        return dataType;
    }

    String getTextValue(){
        return textValue;
    };

    String getClassType() {
        return classType;
    }

    Integer getRootId() {
        return rootId;
    }

    String getCode() {
        return classCod;
    }

    BigDecimal getNumberValue() {
        return numberValue;
    }

    @Override
    public boolean equals(Object o) {
        if ( this == o ) return true;
        if ( o == null || getClass( ) != o.getClass( ) ) return false;
        AttributeValue attribute = (AttributeValue) o;
        return Objects.equals( rootId , attribute.rootId ) && Objects.equals( classType , attribute.classType ) && Objects.equals( dataType , attribute.dataType ) && Objects.equals( textValue , attribute.textValue ) && Objects.equals( numberValue , attribute.numberValue ) && Objects.equals( classCod , attribute.classCod );
    }

    @Override
    public int hashCode() {
        return Objects.hash( rootId );
    }

    @Override
    public String toString() {
        return "FormAttribute{" +
                "rootId=" + rootId +
                ", classType='" + classType + '\'' +
                ", dataType='" + dataType + '\'' +
                ", textValue='" + textValue + '\'' +
                ", numberValue=" + numberValue +
                ", classCod='" + classCod + '\'' +
                '}';
    }
}
