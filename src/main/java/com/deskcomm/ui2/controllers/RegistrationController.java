package com.deskcomm.ui2.controllers;

import com.deskcomm.networking.RegistrationRequest;
import com.deskcomm.support.Response;
import com.jfoenix.controls.*;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Jay Rathod on 15-01-2017.
 */
public class RegistrationController extends Controller implements EventHandler<ActionEvent>, Initializable {
    private static final double PREF_WIDTH = 600;
    private static final double PREF_HEIGHT = 650;
    private static RegistrationController registrationController;
    final private String FXML_FILE = "fxmls/registration.fxml";
    private String windowTitle = "Sign Up";
    private AnchorPane root;


    @FXML
    private MaterialDesignIconView buttonBack;

    @FXML
    private JFXTextField fname;

    @FXML
    private JFXTextField lname;

    @FXML
    private JFXTextField email;

    @FXML
    private JFXTextField mobile;

    @FXML
    private JFXPasswordField password;

    @FXML
    private JFXPasswordField password1;

    @FXML
    private JFXButton btnSignup;
    @FXML
    private Label errors;
    @FXML
    private JFXSpinner spinner;

    @FXML
    private ToggleGroup gender;
    private JFXSnackbar snackbar;

    private RegistrationController() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(FXML_FILE));
        loader.setController(this);
        root = loader.load();
        root.getStylesheets().add(getClass().getResource("stylesheets/regi_controller.css").toExternalForm());
        root.setOnKeyPressed(this::keyEventHandler);

        this.btnSignup.setOnAction(this::signUpButtonClicked);
        buttonBack.setOnMouseClicked(this::backButtonClicked);
        snackbar = new JFXSnackbar(root);
        snackbar.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
    }

    public static RegistrationController getInstance() throws IOException {
        if (registrationController == null) registrationController = new RegistrationController();
        return registrationController;
    }

    private void backButtonClicked(MouseEvent mouseEvent) {
        backButtonClicked();
    }

    private void backButtonClicked() {
        try {
            LoginController controller = LoginController.getInstance();
            controller.startControlling(primaryStage);
            registrationController = null;
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
            btnSignup.setDisable(true);
            spinner.setVisible(true);
            Task<javax.ws.rs.core.Response> task = new Task<javax.ws.rs.core.Response>() {
                @Override
                protected javax.ws.rs.core.Response call() throws Exception {
                    RegistrationRequest request = new RegistrationRequest(fname.getText(), lname.getText(), email.getText(),
                            mobile.getText(), password.getText(), gender.getSelectedToggle().getUserData().toString());
                    return request.perform();
                }
            };
            new Thread(task).start();
            task.setOnSucceeded(event -> {
                btnSignup.setDisable(false);
                spinner.setVisible(false);
                javax.ws.rs.core.Response value = task.getValue();
                if (value.getStatus() == 200) {
                    Response<JSONObject> response = new Response<>(value.readEntity(String.class));
                    snackbar.show(response.getMessage(), 4000);
                } else {
                    snackbar.show(value.getStatusInfo().getStatusCode() + ": " + value.getStatusInfo().getReasonPhrase(), 4000);
                }
            });
            task.setOnFailed(event -> {
                btnSignup.setDisable(false);
                spinner.setVisible(false);
                if (task.getException().getMessage().contains("ConnectException")) {
                    snackbar.show("Unable to reach server.\nServer might be offline or you may not be in the network.", 4000);
                } else snackbar.show(task.getException().getMessage(), 4000);
            });

        }

    }

    //returns true if validation is successful else returns false
    private boolean validateFields() {
        String initText = "Missing fields: ";
        StringBuilder stringBuilder = new StringBuilder(initText);
        if (fname.getText().trim().length() == 0) stringBuilder.append(" first name,");
        if (lname.getText().trim().length() == 0) stringBuilder.append(" last name,");
        if (email.getText().trim().length() == 0) stringBuilder.append(" email,");
        if (password.getText().trim().length() == 0) stringBuilder.append(" password,");
        if (gender.getSelectedToggle() == null) stringBuilder.append("gender");
        else {
            if (!password.getText().equals(password1.getText()))
                stringBuilder.append("\nPasswords didn't match");
        }
        if (stringBuilder.length() != initText.length()) {
            errors.setText(stringBuilder.toString());
            return false;
        } else {
            errors.setText("");
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
        Scene scene;
        if (root.getScene() != null) {
            scene = root.getScene();
        } else {
            scene = new Scene(root, PREF_WIDTH, PREF_HEIGHT);
        }
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
