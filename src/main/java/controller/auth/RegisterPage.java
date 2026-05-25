package controller.auth;

import controller.app.AppPopup;
import controller.app.SceneManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import service.auth.RegisterCallBack;
import service.auth.AuthService;

import java.io.IOException;

public class RegisterPage {
    @FXML private TextField username;
    @FXML private TextField displayName;
    @FXML private PasswordField password;

    private final AuthService authService = new AuthService();

    @FXML public void back(ActionEvent event) throws IOException {
        SceneManager.changeScene(event, "/fxml/landingPage.fxml");
    }
    @FXML public void submit(){
        authService.register(username.getText(), displayName.getText(), password.getText(), new RegisterCallBack() {
            @Override
            public void onSuccess(String message) {
                AppPopup.info(message);
            }

            @Override
            public void onError(String message) {
                AppPopup.error(message);
            }
        });
    }
}
