package com.deskcomm;

import com.deskcomm.core.CurrentUser;
import com.deskcomm.support.L;
import com.deskcomm.ui.controllers.HomeController;
import com.deskcomm.ui.controllers.LoginController;
import com.deskcomm.ui.controllers.TempController;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.Logger;

public class Main extends Application {

    private static final int TESTING_MODE = 2;
    static private Logger LOGGER = Logger.getLogger(Main.class.getName());
    @FXML
    private Button btnRegister;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(final Stage primaryStage) {
        //   Logger.print(User.logout());


        /*
        try {
            WebSocketEndPoint.connectToWebSocket();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "WS connection failed");

            e.printStackTrace();
        } catch (DeploymentException e) {
            LOGGER.log(Level.SEVERE, "WS connection failed");

            e.printStackTrace();
        }

        */


        if (TESTING_MODE == 1) {
            TempController controller = TempController.getInstance();
            controller.startControlling(primaryStage);
            Button button = new Button();
            ListView<Button> listView = new ListView<>();
            GridPane gridPane;



        } else {
            try {
                if (CurrentUser.getInstance().isLoggedIn()) {
                    L.println("User Is Logged In");

                    HomeController controller = HomeController.getInstance();
                    controller.startControlling(primaryStage);
                } else {
                    L.println("User NOT Logged In");
                    LoginController controller = LoginController.getInstance();
                    // ProfilePicUploadController controller = new ProfilePicUploadController();
                    controller.startControlling(primaryStage);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }
}
