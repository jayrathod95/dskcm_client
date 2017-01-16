package com.deskcomm;

import com.deskcomm.db.DbConnection;
import com.deskcomm.ui.controllers.RegistrationController;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.sql.Connection;

public class Main extends Application {

    @FXML
    private Button btnRegister;

    @Override
    public void start(Stage primaryStage) throws Exception {




        RegistrationController controller = new RegistrationController();
        Parent root = controller.getDefaultScene();
        controller.getDefaultStage();

    }


    public static void main(String[] args) {
        launch(args);
    }
}
