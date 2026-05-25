package controller.auth;

import controller.app.SceneManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.io.IOException;

public class LandingPage {
    @FXML public void login(ActionEvent event) throws IOException {
        SceneManager.changeScene(event, "/fxml/loginPage.fxml");
    }
    @FXML public void register(ActionEvent event) throws IOException {
        SceneManager.changeScene(event, "/fxml/registerPage.fxml");
    }
}
