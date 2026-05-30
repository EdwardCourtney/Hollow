package controller.layout;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import controller.navigation.SceneManager;

import java.io.IOException;
import java.util.Objects;

// Note: Main shell that swaps dashboard tabs inside one content area.
public class Framework {
    @FXML private StackPane contentArea;

    @FXML public void dashboard() throws IOException {
        loadTab("/fxml/dashboardTab.fxml");
    }
    @FXML public void browse() throws IOException {
        SceneManager.changeContent("/fxml/browseAuctionTab.fxml");
    }
    @FXML public void mySale(){

    }
    @FXML public void watchlist() throws IOException {
        loadTab("/fxml/watchlistTab.fxml");
    }
    @FXML public void account(){

    }

    public void initialize() throws IOException {
<<<<<<< HEAD:src/main/java/controller/layout/Framework.java
        // Note: Register the shared content area before loading the first tab.
        SceneManager.setContentArea(contentArea);
        SceneManager.changeContent("/fxml/dashboardTab.fxml");
=======
        loadTab("/fxml/dashboardTab.fxml");
    }

    private void loadTab(String fxml) throws IOException {
        Parent view = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(fxml)));
        contentArea.getChildren().setAll(view);
>>>>>>> parent of aff6f03 (feat: publish auction):src/main/java/controller/Framework.java
    }
}
