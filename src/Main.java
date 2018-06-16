import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

/**
 * Created by Matin Afkhami on 10/16/17.
 * Java 10 Recommended.
 */
public class Main extends Application {

    private double x = 0;
    private double y = 0;
    static Stage primaryStage;

    public static void main(String[] args) {
        launch(args);
        System.exit(0);
    }
    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("view/mainForm.fxml"));
        root.setOnMousePressed(event -> {
            x = event.getSceneX();
            y = event.getSceneY();
        });
        root.setOnMouseDragged(event -> {
            primaryStage.setX(event.getScreenX() - x);
            primaryStage.setY(event.getScreenY() - y);
        });

        primaryStage.setScene(new Scene(root,520 ,350));
        primaryStage.setResizable(false);
        primaryStage.setTitle("Name Find & Replacer");
        primaryStage.initStyle(StageStyle.UNDECORATED);
        Main.primaryStage = primaryStage;
        primaryStage.show();
    }
}
