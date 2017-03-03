package com.deskcomm;

import com.deskcomm.core.CurrentUser;
import com.deskcomm.support.L;
import com.deskcomm.ui2.MyPreloader;
import com.deskcomm.ui2.controllers.HomeController;
import com.deskcomm.ui2.controllers.LoginController;
import com.sun.javafx.application.LauncherImpl;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.Logger;

@SuppressWarnings("ALL")
public class Main extends Application {

    private static final int TESTING_MODE = 2;
    static private Logger LOGGER = Logger.getLogger(Main.class.getName());
    private static Stage primaryStage;
    @FXML
    private Button btnRegister;

    public static void main(String[] args) {
        //launch(args);
        LauncherImpl.launchApplication(Main.class, MyPreloader.class, args);
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    @Override
    public void start(final Stage primaryStage) {
        this.primaryStage = primaryStage;

        try {
            if (CurrentUser.getInstance().isLoggedIn()) {
                L.println("User Is Logged In");
                HomeController controller = HomeController.getInstance();
                controller.startControlling(this.primaryStage);
            } else {
                L.println("User NOT Logged In");
                LoginController controller = LoginController.getInstance();
                controller.startControlling(this.primaryStage);
            }
        } catch (IOException e) {
            e.printStackTrace();

        }
    }


}
