package com.deskcomm.tests;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by Jay Rathod on 16-01-2017.
 */

public class Tester {
    @FXML
    private Button button;
    @FXML
    private Text text;
    private Stage primaryStage;

    public Tester(Stage primaryStage) throws IOException {
        this.primaryStage = primaryStage;


    }

    public void test() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../ui/controllers/temp.fxml"));
        loader.setController(this);
        Parent root = loader.load();
        Scene scene = new Scene(root, 400, 400);
        primaryStage.setScene(scene);
        primaryStage.show();


    }
}
