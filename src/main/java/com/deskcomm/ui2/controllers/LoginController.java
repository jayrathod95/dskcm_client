package com.deskcomm.ui2.controllers;

import com.deskcomm.core.CurrentUser;
import com.deskcomm.resources.images.Images;
import com.deskcomm.support.L;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXSpinner;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;

/**
 * Created by Jay Rathod on 16-01-2017.
 */
@SuppressWarnings("Duplicates")
public class LoginController extends Controller {
    private static final double PREF_WIDTH = 600;
    private static final double PREF_HEIGHT = 650;
    private static LoginController loginController;
    final private String FXML_FILE = "login.fxml";
    @FXML
    Label labelError;
    @FXML
    JFXSpinner spinner;
    private String windowTitle = "DeskComm";
    private Pane root;
    private Stage primaryStage = null;
    @FXML
    private Button btnLogin, btnSignup;
    @FXML
    private TextField textFieldEmail, textFieldPassword;
    @FXML
    private JFXHamburger hamburger;

    private LoginController() throws IOException {
        init();


    }

    public static LoginController getInstance() throws IOException {
        if (loginController == null) loginController = new LoginController();
        return loginController;
    }

    @Override
    public void startControlling(Stage primaryStage) {

        this.primaryStage = primaryStage;
        Scene scene;
        if (root == null) init();
        if (root.getScene() != null) {
            scene = root.getScene();
        } else {
            scene = new Scene(root, PREF_WIDTH, PREF_HEIGHT);
        }
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.setTitle(windowTitle);
        primaryStage.getIcons().add(Images.get(Images.APP_ICON));
        if (!primaryStage.isShowing()) primaryStage.show();


    }


    private void init() {


        FXMLLoader loader = new FXMLLoader(getClass().getResource("fxmls/login.fxml"));
        loader.setController(this);
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        URL url = getClass().getResource("stylesheets/login_controller.css");
        String s = url.toExternalForm();
        root.getStylesheets().add(s);
        btnSignup.setOnAction(event -> {
            try {
                RegistrationController controller = RegistrationController.getInstance();
                controller.startControlling(primaryStage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        btnLogin.setOnAction(event -> {
            labelError.setText("");
            if (textFieldEmail.getText().trim().length() > 0 && textFieldPassword.getText().trim().length() > 0) {
                btnLogin.setDisable(true);
                spinner.setVisible(true);

                Task<com.deskcomm.support.Response<JSONObject>> task = new Task<com.deskcomm.support.Response<JSONObject>>() {
                    @Override
                    protected com.deskcomm.support.Response<JSONObject> call() throws Exception {
                        CurrentUser currentUser = CurrentUser.getInstance();
                        if (currentUser.isLoggedIn()) currentUser.logout();
                        return currentUser.login(textFieldEmail.getText(), textFieldPassword.getText());
                    }
                };
                new Thread(task).start();

                task.setOnSucceeded(event1 -> {
                    btnLogin.setDisable(false);
                    spinner.setVisible(false);
                    com.deskcomm.support.Response<JSONObject> response = task.getValue();
                    L.println(response.toString());
                    try {
                        if (response.isResult() && CurrentUser.getInstance().save(response.getData())) {
                            HomeController controller = HomeController.getInstance();
                            try {
                                controller.startControlling(primaryStage);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            Platform.runLater(() -> root = null);
                        } else {
                            labelError.setText(response.getMessage());
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                        labelError.setText("There was an error. Please try again");
                    }
                });
                task.setOnFailed(event1 -> {
                    btnLogin.setDisable(false);
                    spinner.setVisible(false);
                    if (task.getException().getMessage().contains("ConnectException")) {
                        labelError.setText("Unable to reach server.\nServer might be offline or you may not be in the network.");
                    } else {
                        task.getException().printStackTrace();
                        labelError.setText(task.getException().getMessage());
                    }
                });
            } else {
                labelError.setText("Username/Password is empty");
            }
        });
    }
}
