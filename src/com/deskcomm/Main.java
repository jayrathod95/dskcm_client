package com.deskcomm;

import com.deskcomm.core.CurrentUser;
import com.deskcomm.support.L;
import com.deskcomm.ui.controllers.HomeController;
import com.deskcomm.ui.controllers.LoginController;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class Main extends Application {

    private static final int TESTING_MODE = 0;
    @FXML
    private Button btnRegister;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(final Stage primaryStage) throws Exception {
        //   Logger.print(User.logout());

        if (TESTING_MODE == 1) {
            //new Tester(primaryStage).test();
        } else {
            if (CurrentUser.getInstance().isLoggedIn()) {
                L.println("User Is Logged In");
                HomeController controller = new HomeController();
                controller.startControlling(primaryStage);
            } else {
                L.println("User NOT Logged In");
                LoginController controller = new LoginController();
                // ProfilePicUploadController controller = new ProfilePicUploadController();
                controller.startControlling(primaryStage);
            }
        }


    }
}
