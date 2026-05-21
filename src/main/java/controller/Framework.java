package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.util.Objects;

public class Framework {
    @FXML private StackPane contentArea;

    @FXML public void dashboard() throws IOException {
        loadTab("/fxml/dashboardTab.fxml");
    }
    @FXML public void browse(){

    }
    @FXML public void mySale(){

    }
    @FXML public void watchlist() throws IOException {
        loadTab("/fxml/watchlistTab.fxml");
    }
    @FXML public void account(){

    }

    public void initialize() throws IOException {
        loadTab("/fxml/dashboardTab.fxml");
    }

    private void loadTab(String fxml) throws IOException {
        Parent view = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(fxml)));
        contentArea.getChildren().setAll(view);
    }
}
