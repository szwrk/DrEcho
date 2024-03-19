package poc.GenericDocumentGeneratorPoc;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.Test;

public class DocumentTestApp {

    @Test
    public void givenDocumentAndAttributesWithDuplicatedCode_whenAddingAttributes_thenThrowDuplicateAttributeException1(){
        //given
        AttributeDefinition attributeDefinition = new AttributeDefinition( "nameOfFormClass1" , "CODE1" , "TEXT" );
        AttributeValue firstAttribute                                               = AttributeValue.create( 1 , attributeDefinition,"someValue1"  );
        AttributeValue copyOfFirstAttribute = AttributeValue.create( 1 , attributeDefinition,"someValue1"  );

        DocumentDefinition docdefcode                                               = new DocumentDefinition( "DOCDEFCODE" , "Title of document - definition" );
        Document       document                                                     = new Document(docdefcode,"JANKOW");
        try {
            document.addAtribute( firstAttribute );
        } catch (NonUniqueException e) {
            //nothing
        }
        //when-then
        assertThrows( NonUniqueException.class, () ->   document.addAtribute( copyOfFirstAttribute ));
    }

    @Test
    public void givenNewClassDefinition_whenAddDuplicateOfAttributeDefinition_thenThrowException() {
        //given
        ClassDefinition classDefinition = ClassDefinition.create("KLASA1", "Klasa echo");
        AttributeDefinition defAtrAttribute1 = AttributeDefinition.create( "ATR" , "Attribute1", "TEXT" );
        AttributeDefinition defAtrAttribute2 = AttributeDefinition.create( "ATR" , "Attribute1", "TEXT" );
        //when
        classDefinition.addAttributeDefinition(defAtrAttribute1);
        //then
        assertThrows( NonUniqueException.class, () ->  classDefinition.addAttributeDefinition(defAtrAttribute2));

    }

    @Test
    public void givenNewClassDefinition_whenAddUniqueAttributeDefinition_thenContainTwo() {
        //given
        ClassDefinition classDefinition = ClassDefinition.create("KLASA1", "Klasa echo");
        AttributeDefinition defAtrAttribute1 = AttributeDefinition.create( "ATR" , "Attribute1", "TEXT" );
        AttributeDefinition defAtrAttribute2 = AttributeDefinition.create( "ATR" , "Attribute2", "TEXT" );
        //when
        classDefinition.addAttributeDefinition(defAtrAttribute1);
        classDefinition.addAttributeDefinition(defAtrAttribute2);
        //then
        assertEquals(2, classDefinition.fields().size() );

    }

    @Test
    public void poc(){
        ClassDefinition classDefinition = new ClassDefinition("KLASA1", "Klasa echo");

        AttributeDefinition defAtrAttribute1 = AttributeDefinition.create( "ATR" , "Attribute1", "TEXT" );
        AttributeDefinition defAtrAttribute2 = AttributeDefinition.create( "ATR" , "Attribute2", "TEXT" );
        classDefinition.addAttributeDefinition(defAtrAttribute1);
        classDefinition.addAttributeDefinition(defAtrAttribute2);

        DocumentDefinition badanieXyz = DocumentDefinition.create( "TTE","Test document title" );
        String  createdBy =  "JANKOW";

        Document document = new Document(badanieXyz,createdBy);
        //open transaction
        Integer currentDocId = document.preSave();
        AttributeValue attribute1 = AttributeValue.create( currentDocId , defAtrAttribute1, "someValue1"  );
        AttributeValue attribute2 = AttributeValue.create( currentDocId , defAtrAttribute2, "someValue1"  );
        document.addAttributes(attribute1,attribute2);
        document.save();

    }

}

