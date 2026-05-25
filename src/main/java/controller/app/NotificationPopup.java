package controller.app;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class NotificationPopup {
    @FXML private HBox container;
    @FXML private Label messageLabel;

    public void setMessage(String message) {
        messageLabel.setText(message);
    }

    public void setType(String type) {
        container.getStyleClass().removeAll("popup-info", "popup-error", "popup-warning");
        container.getStyleClass().add("popup-" + type);
    }
}
