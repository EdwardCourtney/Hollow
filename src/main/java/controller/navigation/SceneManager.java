package controller.navigation;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

// Note: Central helper for full scene changes and inner tab swaps.
public class SceneManager  {
    public static void changeScene(ActionEvent event, String fxml) throws IOException {
        // Note: Used by landing/login/register where the whole window changes.
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource(fxml));
        Scene scene = new Scene(loader.load());

        stage.setScene(scene);
        stage.show();
    }
<<<<<<< HEAD:src/main/java/controller/navigation/SceneManager.java

    public static void setContentArea(StackPane area) {
        contentArea = area;
    }

    public static void changeContent(String fxml) throws IOException {
        // Note: Used after login to replace only the main content panel.
        if (contentArea == null) {
            throw new IllegalStateException("Content area has not been initialized");
        }

        Parent view = FXMLLoader.load(Objects.requireNonNull(SceneManager.class.getResource(fxml)));
        contentArea.getChildren().setAll(view);
    }
=======
>>>>>>> parent of aff6f03 (feat: publish auction):src/main/java/controller/SceneManager.java
}
