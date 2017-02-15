package experiments;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
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
        VBox vBox = new VBox();
        ObservableList<String> strings = FXCollections.observableArrayList();
        ListView<String> listView = new ListView<>(strings);

        TextField index = new TextField();
        TextField data = new TextField();
        Button button = new Button("Add");
        vBox.getChildren().addAll(listView, index, data, button);

        Scene scene = new Scene(vBox, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();


        strings.addAll("Nagpur", "Pune", "Mumbai", "Nashik");

        button.setOnAction(event -> {
            strings.add(Integer.parseInt(index.getText()), data.getText());
        });

    }
}
