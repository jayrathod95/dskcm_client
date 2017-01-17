package com.deskcomm;

import com.deskcomm.core.CurrentUser;
import com.deskcomm.core.User;
import com.deskcomm.db.DbConnection;
import com.deskcomm.support.Logger;
import com.deskcomm.tests.Tester;
import com.deskcomm.ui.controllers.LoginController;
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

    private static final int TESTING_MODE = 0;
    @FXML
    private Button btnRegister;

    @Override
    public void start(Stage primaryStage) throws Exception {
     //   Logger.print(User.logout());

        if (TESTING_MODE == 1) {
            Tester tester = new Tester();
            tester.test();
        } else {

            if (CurrentUser.isLoggedIn()) {
                Logger.print("User Is Logged In");


            } else {
                Logger.print("User NOT Logged In");
                LoginController controller = new LoginController();
                controller.startControlling();
            }


            //   RegistrationController controller = new RegistrationController();
            //  Parent root = controller.getDefaultScene();
            //  controller.getDefaultStage();

        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
