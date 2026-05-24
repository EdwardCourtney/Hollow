package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.io.IOException;


public class AccountPage {
    @FXML private Label balanceLabel;

    @FXML public void back() throws IOException {
        SceneManager.changeContent("/fxml/dashboardTab.fxml");
    }

    @FXML public void add(){

    }
}
