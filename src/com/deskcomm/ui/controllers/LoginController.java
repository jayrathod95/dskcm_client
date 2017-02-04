package com.deskcomm.ui.controllers;

import com.deskcomm.core.CurrentUser;
import com.deskcomm.exceptions.ResponseException;
import com.deskcomm.networking.ErrorListener;
import com.deskcomm.networking.SuccessListener;
import com.deskcomm.support.L;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.json.JSONObject;

import javax.ws.rs.core.Response;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by Jay Rathod on 16-01-2017.
 */
public class LoginController extends Controller {
    private static final double PREF_WIDTH = 358;
    private static final double PREF_HEIGHT = 616;
    final private String FXML_FILE = "../fxmls/login.fxml";
    @FXML
    Label labelError;
    private String windowTitle = "Log In";
    private Parent root;
    private Stage primaryStage = null;
    @FXML
    private Button btnLogin, btnSignup;
    @FXML
    private TextField textFieldEmail, textFieldPassword;
    private SuccessListener successListener;
    private ErrorListener errorListener;

    public LoginController() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(FXML_FILE));
        loader.setController(this);
        root = loader.load();
        init();

    }

    @Override
    public void startControlling(Stage primaryStage) {
        this.primaryStage = primaryStage;
        Scene scene = new Scene(root, PREF_WIDTH, PREF_HEIGHT);
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.setTitle(windowTitle);
        primaryStage.getIcons().add(new Image(this.getClass().getResource("../../resources/images/dskcm_logo_temp.png").toString()));
        if (!primaryStage.isShowing()) primaryStage.show();
    }


    private void init() {
        successListener = new SuccessListener() {
            @Override
            public void onSuccess(Response mResponse) {
                String response = mResponse.readEntity(String.class);
                L.println(response);
                try {
                    if (CurrentUser.getInstance().save(response)) {
                        Controller controller = new HomeController();
                        controller.startControlling(primaryStage);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();

                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        };
        errorListener = new ErrorListener() {
            @Override
            public void onError(ResponseException e) {
                e.printStackTrace();
                L.println(e.getMessage());
            }
        };

        btnSignup.setOnAction(event -> {
            try {
                RegistrationController controller = new RegistrationController();
                controller.startControlling(primaryStage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        btnLogin.setOnAction(event -> {

            if ((textFieldEmail.getText() + "" + textFieldPassword.getText()).trim().length() > 0) {
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
                    com.deskcomm.support.Response<JSONObject> response = task.getValue();
                    L.println(response.toString());
                    try {
                        if (response.isResult() && CurrentUser.getInstance().save(response.getData())) {
                            HomeController controller = new HomeController();
                            controller.startControlling(primaryStage);
                        } else {
                            labelError.setText(response.getMessage());
                        }
                    } catch (SQLException | ClassNotFoundException e) {
                        e.printStackTrace();
                        labelError.setText("There was an error. Please try again");
                    }
                });
                task.setOnFailed(event1 -> {
                    labelError.setText(task.getException().getMessage());
                });
            } else {
                labelError.setText("Username/Password is empty");
            }
        });
    }
}
