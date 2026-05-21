package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneManager  {
    public static void changeScene(ActionEvent event, String fxml) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource(fxml));
        Scene scene = new Scene(loader.load());

        stage.setScene(scene);
        stage.show();
    }
}
