import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AuctionLauncher extends Application{
    public void start(Stage stage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/auth/landingPage.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root, 980, 620);

        stage.setScene(scene);
        stage.setTitle("Ponzi Auction");
        stage.setMinWidth(900);
        stage.setMinHeight(580);
        stage.show();
    }

}
