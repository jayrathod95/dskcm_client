package experiments;/*
 * Copyright 2016 Andreas Fester
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.sql.Timestamp;
import java.util.Date;

/**
 * Example for a Region border definition. Just launch this class using a Java8
 * runtime environment. No other dependencies required.
 * See http://www.software-architect.net/blog/article/date/2015/11/14/9-patch-scaling-in-javafx.html
 */
public class BorderExample extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    public void run() {
        start(new Stage());
    }

    @Override
    public void start(Stage primaryStage) {
        /*
        Region hello = new Button("Hello World");
        hello.setId("hello");

        Region hello2 = new Button("Hello World");
        hello2.setId("hello2");

        Region hello3 = new Button("Hello World");
        hello3.setId("hello3");

        // JavaFX boilerplate - add the three buttons to a group and
        // setup and show the scene
        */
        /*
        try {



            FXMLLoader loader = new FXMLLoader(getClass().getResource("row_message_in.fxml"));
            AnchorPane pane = loader.load();


            Object body = loader.getNamespace().get("body");
            ((Text) body).setText("goodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgood");
            ((Label) loader.getNamespace().get("timestamp")).setText(new Timestamp(new Date().getTime()).toString());

            pane.setId("hello");

            VBox mainGroup = new VBox();
            mainGroup.setPadding(new Insets(10));
            mainGroup.setSpacing(10);
            mainGroup.getChildren().addAll(pane);
            Scene scene = new Scene(mainGroup);

            URL styleSheet = getClass().getResource("panelexample.css");
            scene.getStylesheets().add(styleSheet.toExternalForm());

            primaryStage.setScene(scene);
            primaryStage.show();


        } catch (IOException e) {
            e.printStackTrace();
        }*/


        Text text1 = new Text("goodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgoodgood");
        TextFlow textFlow = new TextFlow(text1);
        Text text2 = new Text(new Timestamp(new Date().getTime()).toString());
        text2.setTextAlignment(TextAlignment.RIGHT);
        text1.setFill(Color.WHITE);
        text1.setSmooth(true);

        VBox vBox = new VBox();
        vBox.setId("hello");
        vBox.setPadding(new Insets(10));
        vBox.setAlignment(Pos.CENTER_RIGHT);
        vBox.getChildren().addAll(textFlow, text2);

        textFlow.setPrefWidth(300);


        primaryStage.setScene(new Scene(vBox, VBox.USE_COMPUTED_SIZE, VBox.USE_COMPUTED_SIZE));
        primaryStage.show();
        primaryStage.getScene().getStylesheets().add(getClass().getResource("panelexample.css").toExternalForm());


    }

}