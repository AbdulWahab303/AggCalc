import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class MainSceneController {

    @FXML
    private Button calcButton;

    @FXML
    private Button showMeritsButton;

    @FXML
    private Button exit;

    @FXML
    public void initialize() {
        calcButton.setOnAction(e -> switchScene("CalculateAggregateScene.fxml"));
        showMeritsButton.setOnAction(e -> switchScene("ShowMeritsScene.fxml"));
        exit.setOnAction(e -> Platform.exit());
    }

    private void switchScene(String fxmlFile) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlFile));
            Stage stage = (Stage) calcButton.getScene().getWindow();
            Scene scene=new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.setFullScreen(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
