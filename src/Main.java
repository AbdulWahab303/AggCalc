import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("MainScene.fxml"));
        primaryStage.setTitle("University Application");
        Image image=new Image("images/calculator.png");
        primaryStage.getIcons().add(image);
        primaryStage.setResizable(false);
        primaryStage.setFullScreen(true);
        Scene scene=new Scene(root);
        scene.setOnKeyPressed(event->{
            if(event.getCode()== KeyCode.ESCAPE){
                primaryStage.close();
            }
        });
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
