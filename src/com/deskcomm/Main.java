package com.deskcomm;

import com.deskcomm.core.CurrentUser;
import com.deskcomm.support.Logger;
import com.deskcomm.tests.Tester;
import com.deskcomm.ui.controllers.LoginController;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

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
        }
    }

    private <U> String func1() {
        return "Hey there";
    }


    public static void main(String[] args) {
        launch(args);
    }
}
