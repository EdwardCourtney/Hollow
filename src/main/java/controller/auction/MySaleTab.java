package controller.auction;

import controller.app.SceneManager;
import javafx.fxml.FXML;

import java.io.IOException;

public class MySaleTab {
    @FXML public void create() throws IOException {
        SceneManager.changeContent("/fxml/createPage.fxml");
    }
}
