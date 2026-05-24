package controller;

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
        switch (type) {
            case "error" -> container.setStyle(baseStyle("#b42318"));
            case "warning" -> container.setStyle(baseStyle("#a15c00"));
            default -> container.setStyle(baseStyle("#1f6f43"));
        }
    }

    private String baseStyle(String color) {
        return "-fx-background-color: " + color + ";"
                + "-fx-background-radius: 6;"
                + "-fx-padding: 10 14;"
                + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.26), 12, 0.2, 0, 3);";
    }
}
