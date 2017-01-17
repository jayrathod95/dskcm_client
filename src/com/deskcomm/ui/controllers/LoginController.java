package com.deskcomm.ui.controllers;

import com.deskcomm.core.CurrentUser;
import com.deskcomm.support.Logger;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Created by Jay Rathod on 16-01-2017.
 */
public class LoginController extends Controller implements EventHandler<ActionEvent> {
    private Parent root;
    final private String FXML_FILE = "../fxmls/login.fxml";
    @FXML
    private Button btnLogin, btnSignup;
    @FXML
    private TextField textFieldEmail, textFieldPassword;

    public LoginController() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(FXML_FILE));
        loader.setController(this);
        root = loader.load();

        btnLogin.setOnAction(this);
        btnSignup.setOnAction(this);
    }

    @Override
    public void startControlling() {
        Scene scene = new Scene(root, 358, 616);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }


    @Override
    public void handle(ActionEvent event) {
        String id = ((Control) event.getSource()).getId();
        switch (id) {
            case "btnLogin":
                if (CurrentUser.isLoggedIn()) {
                    CurrentUser.logout();
                }
                CurrentUser.login(textFieldEmail.getText(), textFieldPassword.getText());
                break;
            case "btnSignup":
                try {
                    RegistrationController controller=new RegistrationController();
                    controller.startControlling();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                break;
        }

    }
}
