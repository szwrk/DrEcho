package net.wilamowski.drecho.client.presentation.debugger;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import net.wilamowski.drecho.shared.bundle.Lang;
import net.wilamowski.drecho.client.properties.ClientPropertyReader;
import net.wilamowski.drecho.client.presentation.customs.modals.DebugAlert;
import net.wilamowski.drecho.client.presentation.customs.modals.ExceptionAlert;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class KeyDebugHandlerGui implements DebugHandler {
    public static final String NEW_LINE = "@@@@@";
    private static final Logger logger = LogManager.getLogger( KeyDebugHandlerGui.class);
    final String DEFAULT_KEY_COMBINATION = "F12";
    private String customKeyCombination;
    private Parent root;

    private Object[] observableObjects;

    public KeyDebugHandlerGui() {
    }

    @Override
    public void initNode(Parent root) {
        if(root!=null) {
            this.root = root;
            setKeyCombination( );
        } else {
            NullPointerException npe = new NullPointerException( "Passed Parent is null" );
            logger.error( npe.getMessage(), npe );
            throw new NullPointerException( "Passed Parent is null" );
        }
    }

    private void setKeyCombination() {
        String keyCombinationPropertyValue = ClientPropertyReader.getString( "user.ui.debug.key-invoke-modal" );
        customKeyCombination = keyCombinationPropertyValue.isEmpty() ? DEFAULT_KEY_COMBINATION : keyCombinationPropertyValue;
        logger.debug("Set custom key for debug: {}", customKeyCombination );
    }
    @Override
    public void watch(Object... objects) throws NullPointerException {
        logger.traceEntry();
        if (objects != null) {
            this.observableObjects = objects;
            root.addEventFilter(KeyEvent.KEY_PRESSED, createKeyEventHandler());
        } else {
            throw new NullPointerException("Passed objects are null");
        }
        logger.traceExit();
    }
    private EventHandler<KeyEvent> createKeyEventHandler() {
        return keyEvent -> {
            KeyCombination keyComb = KeyCombination.keyCombination( customKeyCombination );

            if (keyComb.match(keyEvent)) {
                handleDebugKey();
            }
        };
    }

    private void handleDebugKey() {
        logger.debug("Key event. Opening debug alert...");
        logger.debug("Debug handler, current observed objects: {}", Arrays.toString(observableObjects));

        try {
            String toStrings = Arrays.stream(observableObjects)
                    .filter( Objects::nonNull)
                    .map(Object::toString)
                    .collect(Collectors.joining("\n\n\n" + NEW_LINE));

            DebugAlert.createWithContents(toStrings).show();
        } catch (Exception e) {
            handleError(e, "e.999.header", "e.999.msg");
        }
    }

    private void handleError(Exception e , String headerKey , String contentKey) {
        logger.error("An error occurred: ", e.getMessage( ) , e );
        String header = Lang.getString( headerKey );
        String msg    = Lang.getString( contentKey );
        ExceptionAlert.create( ).showError( e
                , header
                , msg );
    }
}
