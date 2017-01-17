package com.deskcomm.ui.controllers;

import com.deskcomm.Main;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Jay Rathod on 15-01-2017.
 */
public class RegistrationController extends Controller implements EventHandler<ActionEvent>,Initializable {
    private Parent root;
    final private String FXML_FILE = "../fxmls/registration.fxml";
    @FXML
    private Button buttonSignUp;

    public RegistrationController() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(FXML_FILE));
        loader.setController(this);
        root = loader.load();

        this.buttonSignUp.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.print("Hey");
            }
        });
    }



    @Override
    public void handle(ActionEvent event) {
        Control control = (Control) event.getSource();
        System.out.print(control.getId());
    }

    @Override
    public void startControlling() {
        Scene scene = new Scene(root, 280, 420);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
