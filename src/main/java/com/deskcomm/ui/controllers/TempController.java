package com.deskcomm.ui.controllers;

import com.deskcomm.networking.websocket.WebSocketEndPoint;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javax.websocket.DeploymentException;
import java.io.IOException;

/**
 * Created by Jay Rathod on 06-02-2017.
 */
public class TempController {
    private static Stage stage;
    private static TempController tempController;
    @FXML
    private Button button;
    @FXML
    private Text text;

    private TempController() {
    }

    public static TempController getInstance() {
        if (tempController == null) tempController = new TempController();
        return tempController;
    }

    public void startControlling(Stage stage) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("temp.fxml"));
        loader.setController(this);
        try {
            Pane pane = loader.load();
            Scene scene = new Scene(pane, 500, 500);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

        button.setOnAction(event -> {
            try {
                WebSocketEndPoint.connectToWebSocket();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (DeploymentException e) {
                e.printStackTrace();
            }
        });
    }

    public Text getText() {
        return text;
    }

    /*
    public void startControlling(Stage primaryStage) throws SQLException, ClassNotFoundException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("temp.fxml"));


        loader.setController(this);
        try {
            Pane pane = loader.load();
            Scene scene = new Scene(pane, 500, 500);
            primaryStage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }

        button.setOnAction(event -> {
            try {
                WebSocketEndPoint.connectToWebSocket();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (DeploymentException e) {
                e.printStackTrace();
            }

        });

        */

}

