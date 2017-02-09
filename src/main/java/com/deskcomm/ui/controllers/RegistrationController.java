package com.deskcomm.ui.controllers;

import com.deskcomm.networking.RegistrationRequest;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Jay Rathod on 15-01-2017.
 */
public class RegistrationController extends Controller implements EventHandler<ActionEvent>, Initializable {
    private static final double PREF_WIDTH = 358;
    private static final double PREF_HEIGHT = 616;
    private static RegistrationController registrationController;
    final private String FXML_FILE = "../fxmls/registration.fxml";
    private String windowTitle = "Sign Up";
    private Parent root;
    @FXML
    private Button buttonSignUp, buttonBack;
    @FXML
    private TextField textFieldFirstName, textFieldLastName, textFieldEmail, textFieldMobile, textFieldEid;
    @FXML
    private PasswordField passFieldPassword, passFieldPasswordRepeat;
    @FXML
    private Text textErrors;

    private RegistrationController() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(FXML_FILE));
        loader.setController(this);
        root = loader.load();
        root.setOnKeyPressed(this::keyEventHandler);

        this.buttonSignUp.setOnAction(this::signUpButtonClicked);
        buttonBack.setOnAction(this::backButtonClicked);
    }

    public static RegistrationController getInstance() throws IOException {
        if (registrationController == null) registrationController = new RegistrationController();
        return registrationController;
    }

    private void backButtonClicked(ActionEvent actionEvent) {
        try {
            LoginController controller = LoginController.getInstance();
            controller.startControlling(primaryStage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void signUpButtonClicked(ActionEvent actionEvent) {
        signUpButtonClicked();
    }

    private void keyEventHandler(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            signUpButtonClicked();
        }
    }

    private void signUpButtonClicked() {
        if (validateFields()) {
            RegistrationRequest request = new RegistrationRequest(textFieldFirstName.getText(), textFieldLastName.getText(), textFieldEmail.getText(),
                    textFieldMobile.getText(), textFieldEid.getText(), passFieldPassword.getText());
        }

    }

    //returns true if validation is successful else returns false
    private boolean validateFields() {
        String initText = "Missing fields: ";
        StringBuilder stringBuilder = new StringBuilder(initText);
        if (textFieldFirstName.getText().trim().length() == 0) stringBuilder.append(" first name,");
        if (textFieldLastName.getText().trim().length() == 0) stringBuilder.append(" last name,");
        if (textFieldEmail.getText().trim().length() == 0) stringBuilder.append(" email,");
        if (textFieldEid.getText().trim().length() == 0) stringBuilder.append(" employee id,");
        if (passFieldPassword.getText().trim().length() == 0) stringBuilder.append(" password,");
        else {
            if (!passFieldPassword.getText().equals(passFieldPasswordRepeat.getText()))
                stringBuilder.append("\nPasswords didn't match");
        }
        if (stringBuilder.length() != initText.length()) {
            textErrors.setText(stringBuilder.toString());
            return false;
        } else {
            textErrors.setText("");
            return true;
        }
    }

    @Override
    public void handle(ActionEvent event) {
        Control control = (Control) event.getSource();
        System.out.print(control.getId());
    }

    @Override
    public void startControlling(Stage primaryStage) {
        this.primaryStage = primaryStage;
        Scene scene = new Scene(root, PREF_WIDTH, PREF_HEIGHT);
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.setTitle(windowTitle);
        if (!primaryStage.isShowing()) primaryStage.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void setWindowTitle(String windowTitle) {
        this.windowTitle = windowTitle;
    }
}
