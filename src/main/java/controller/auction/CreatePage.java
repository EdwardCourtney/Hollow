package controller.auction;

import controller.app.AppPopup;
import controller.app.SceneManager;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import dto.auction.BaseItemResponse;
import service.auction.ItemCallback;
import service.auction.ItemService;

import java.io.IOException;

public class CreatePage {
    @FXML private TextField title;
    @FXML private TextArea description;
    @FXML private TextField endTime;
    @FXML private TextField startPrice;
    @FXML private TextField increment;
    @FXML private TextField buyItNow;

    private final ItemService itemService = new ItemService();

    @FXML public void back() throws IOException {
        SceneManager.changeContent("/fxml/mySaleTab.fxml");
    }
    @FXML public void create(){
        itemService.createItem(
                title.getText(),
                description.getText(),
                endTime.getText(),
                startPrice.getText(),
                increment.getText(),
                buyItNow.getText(),
                new ItemCallback() {
                    @Override
                    public void onSuccess(BaseItemResponse response) {
                        Platform.runLater(() -> {
                            try {
                                SceneManager.changeContent("/fxml/mySaleTab.fxml");
                                AppPopup.info("Created item: " + response.item.title);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        });
                    }

                    @Override
                    public void onError(String message) {
                        AppPopup.error(message);
                    }
                }
        );
    }
}
