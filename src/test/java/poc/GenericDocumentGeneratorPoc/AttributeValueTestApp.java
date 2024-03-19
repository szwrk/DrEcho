package poc.GenericDocumentGeneratorPoc;


import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import org.junit.Before;
import org.junit.Test;

public class AttributeValueTestApp {

    private AttributeValue textAttributeValue;
    private AttributeValue textAttributeValueCopy;
    private  AttributeDefinition attributeDefinition;

    @Before
    public void setup() {
        AttributeDefinition attributeDefinition = new AttributeDefinition( "nameOfFormClass1" , "CODE1" , "TEXT" );
        textAttributeValue = AttributeValue.create( 1 , attributeDefinition ,"someValue1"  );
        textAttributeValueCopy = AttributeValue.create( 1 , attributeDefinition ,"someValue1"  );
    }

    @Test
    public void isTextType(){
        assertEquals( "TEXT" , textAttributeValue.getDataType( ) );
    }

    @Test
    public void givenTextFormValue_whenGettersCalled_thenCorrectValuesReturned(){
       assertEquals( 1 , (int) textAttributeValue.getRootId( ) );
       assertEquals( "nameOfFormClass1" , textAttributeValue.getClassType( ) );
       assertEquals( "TEXT" , textAttributeValue.getDataType( ) );
       assertEquals( "someValue1" , textAttributeValue.getTextValue( ) );
       assertEquals( "CODE1" , textAttributeValue.getCode( ) );
    }

    @Test
    public void givenTwoSameInstanceOfTextFormValues_WhenCompare_thenEquals(){
        assertEquals( textAttributeValue , textAttributeValueCopy );
    }

    @Test
    public void giveNewInstance_whenAnyNullParams_thenRunException(){
        assertThrows(IllegalArgumentException.class, () ->  AttributeValue.create( null , attributeDefinition,"someValue1"  ));
        assertThrows(IllegalArgumentException.class, () ->  AttributeValue.create( 1 ,null,"someValue1"  ));
        assertThrows(IllegalArgumentException.class, () ->  AttributeValue.create( 1 , attributeDefinition,"someValue1"  ));
    }

    @Test
    public void giveNewInstanceWithNullsParams_whenThrow_thenCheckNameOfNullParameters(){
        //given
        Integer nullInt = null;
        String emptyString = null;
        //when
        IllegalArgumentException illegalArgumentException = assertThrows( IllegalArgumentException.class ,
                () -> AttributeValue.create( nullInt , attributeDefinition, emptyString ) );
        // then
        assertTrue(illegalArgumentException.getMessage().contains("\n- instanceDocumentId\n- attributeClass\n- attributeCode"));
        //- containedDataType is static fabric method constant so in this case is not null
    }

    @Test
    public void givenAttributeWithNumber_WhenSimpleGetValue_thenEqualsPassed(){
        //given
        BigDecimal expected = BigDecimal.valueOf( 123.4 );
        //when
        AttributeValue attribute = AttributeValue.create( 1 , attributeDefinition , expected);
        BigDecimal     retrieved = attribute.getNumberValue( );
        //then
        assertEquals( BigDecimal.valueOf(123.4), retrieved );
    }

    @Test
    public void givenAttributeWithInsignificantZero_whenSimpleGetValue_thenEquals(){
        //given
        BigDecimal     expected  = BigDecimal.valueOf( 123.0 );
        AttributeValue attribute = AttributeValue.create( 1 , attributeDefinition , expected);
        BigDecimal     retrieved = attribute.getNumberValue( );
        assertEquals( BigDecimal.valueOf(123), retrieved );
    }
    @Test
    public void givenAttributeWithInsignificantZero_whenGettingNumberValue_thenReturnEquals(){
        //given
        BigDecimal expected = BigDecimal.valueOf( 123.0 );
        //when
        AttributeValue attribute = AttributeValue.create( 1 , attributeDefinition , expected);
        BigDecimal     retrieved = attribute.getNumberValue( );
        //then
        assertEquals( BigDecimal.valueOf(123), retrieved );
    }

    @Test
    public void givenFloatNumber_whenConstructorTrailingZeroAndWhenGettingNumberValue_thenRetrieveExactValue(){
        //given
        BigDecimal expected = BigDecimal.valueOf( 123.1 );
        //when

        AttributeValue attribute = AttributeValue.create( 1 , attributeDefinition, expected);
        BigDecimal     retrieved = attribute.getNumberValue( );
        //then
        assertEquals( BigDecimal.valueOf(123.1), retrieved );
    }

    @Test
    public void hash(){
        assertEquals( textAttributeValue.hashCode( ), textAttributeValueCopy.hashCode( ));
    }
}