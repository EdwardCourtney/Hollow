package controller;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.stage.Window;
import javafx.util.Duration;

import java.io.IOException;

public class AppPopup {
    public static void info(String message) {
        show(message, "info");
    }

    public static void error(String message) {
        show(message, "error");
    }

    public static void warning(String message) {
        show(message, "warning");
    }

    private static void show(String message, String type) {
        if (Platform.isFxApplicationThread()) {
            showOnFxThread(message, type);
        } else {
            Platform.runLater(() -> showOnFxThread(message, type));
        }
    }

    private static void showOnFxThread(String message, String type) {
        Scene scene = getCurrentScene();
        if (scene == null) {
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(AppPopup.class.getResource("/fxml/notificationPopup.fxml"));
            Region popup = loader.load();

            NotificationPopup controller = loader.getController();
            controller.setMessage(message);
            controller.setType(type);

            popup.setMouseTransparent(true);
            popup.setMinSize(220, Region.USE_PREF_SIZE);
            popup.setPrefWidth(300);
            popup.setMaxSize(320, 80);

            StackPane overlayRoot = ensureOverlayRoot(scene);
            StackPane.setAlignment(popup, Pos.BOTTOM_CENTER);
            StackPane.setMargin(popup, new Insets(0, 0, 12, 0));
            overlayRoot.getChildren().add(popup);

            FadeTransition fadeIn = new FadeTransition(Duration.millis(120), popup);
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1);
            fadeIn.play();

            PauseTransition delay = new PauseTransition(Duration.seconds(1.8));
            delay.setOnFinished(event -> {
                FadeTransition fadeOut = new FadeTransition(Duration.millis(220), popup);
                fadeOut.setFromValue(1);
                fadeOut.setToValue(0);
                fadeOut.setOnFinished(done -> overlayRoot.getChildren().remove(popup));
                fadeOut.play();
            });
            delay.play();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static Scene getCurrentScene() {
        for (Window window : Window.getWindows()) {
            if (window.isShowing() && window.getScene() != null && window.isFocused()) {
                return window.getScene();
            }
        }

        for (Window window : Window.getWindows()) {
            if (window.isShowing() && window.getScene() != null) {
                return window.getScene();
            }
        }

        return null;
    }

    private static StackPane ensureOverlayRoot(Scene scene) {
        javafx.scene.Parent root = scene.getRoot();
        if (root instanceof StackPane stackPane && "appOverlayRoot".equals(stackPane.getId())) {
            return stackPane;
        }

        StackPane overlayRoot = new StackPane();
        overlayRoot.setId("appOverlayRoot");
        overlayRoot.setPickOnBounds(false);
        overlayRoot.getChildren().add(root);
        scene.setRoot(overlayRoot);
        return overlayRoot;
    }
}
