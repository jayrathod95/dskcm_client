package experiments;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * Created by jay_rathod on 13-02-2017.
 */
public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Image image = new Image(getClass().getResourceAsStream("grey_bubble.9-1.png"));


        Label label = new Label("jay here");
        BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.ROUND, null, null, null);
        label.setBackground(new Background(backgroundImage));
        label.setPrefWidth(300);
        label.setPrefHeight(300);
        Pane pane = new Pane();
        pane.getChildren().add(label);
        Scene scene = new Scene(pane, 500, 500);
        primaryStage.setScene(scene);
        primaryStage.show();

    }
}
