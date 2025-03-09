package net.wilamowski.drecho;

import java.util.function.Supplier;
import javafx.scene.Parent;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.layout.BorderPane;

public class ModalWrapper<T> extends Dialog<T> {

    public ModalWrapper(Parent parent, Supplier<T> onConfirm) {
        setTitle("Modal");

        ButtonType okButton = ButtonType.OK;
        ButtonType cancelButton = ButtonType.CANCEL;
        getDialogPane().getButtonTypes().addAll(okButton, cancelButton);

        BorderPane root = new BorderPane();
        root.setCenter(parent);
        getDialogPane().setContent(root);

        setResultConverter(button -> {
            if (button == okButton && onConfirm != null) {
                return onConfirm.get();
            }
            return null; //cancel
        });
    }
}
