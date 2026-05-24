package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.io.IOException;


public class AccountPage {
    @FXML private Label balanceLabel;

    @FXML public void back(ActionEvent event) throws IOException {
        SceneManager.changeScene(event, "/fxml/framework.fxml");
    }

    @FXML public void add(){

    }
}
