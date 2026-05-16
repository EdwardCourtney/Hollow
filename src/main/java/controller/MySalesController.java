package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class MySalesController {
    @FXML
    private TextField titleField;

    @FXML
    private TextField startPriceField;

    @FXML
    private TextField durationField;

    @FXML
    private TextArea descriptionArea;

    @FXML
    private Label statusLabel;

    @FXML
    public void postAuction() {
        String title = titleField.getText().trim();
        String startPrice = startPriceField.getText().trim();
        String duration = durationField.getText().trim();

        if (title.isEmpty() || startPrice.isEmpty() || duration.isEmpty()) {
            statusLabel.setText("Please fill in title, start price, and duration.");
            return;
        }

        statusLabel.setText("Mock post created for \"" + title + "\".");
        titleField.clear();
        startPriceField.clear();
        durationField.clear();
        descriptionArea.clear();
    }
}
