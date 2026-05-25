package controller.auth;

import controller.AppPopup;
import controller.SceneManager;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import dto.response.AuthResponse;
import service.auth.LoginCallback;
import service.auth.AuthService;

import java.io.IOException;

public class LoginPage {
    @FXML private TextField username;
    @FXML private PasswordField password;

    private final AuthService authService = new AuthService();

    @FXML public void back(ActionEvent event) throws IOException {
        SceneManager.changeScene(event, "/fxml/auth/landingPage.fxml");
    }
    @FXML public void submit(ActionEvent event){
        authService.login(username.getText(), password.getText(), new LoginCallback() {
            @Override
            public void onSuccess(AuthResponse response) {
                Platform.runLater(() -> {
                            try {
                                SceneManager.changeScene(event, "/fxml/framework.fxml");
                                AppPopup.info(response.message);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                );
            }

            @Override
            public void onError(String message) {
                AppPopup.error(message);
            }
        });
    }
}
