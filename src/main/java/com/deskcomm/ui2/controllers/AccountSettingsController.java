package com.deskcomm.ui2.controllers;

import com.deskcomm.core.CurrentUser;
import com.deskcomm.networking.MethodType;
import com.deskcomm.networking.Request;
import com.deskcomm.support.Keys;
import com.jfoenix.controls.*;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import org.json.JSONObject;

import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jay_rathod on 2/27/2017.
 */
public class AccountSettingsController extends Controller {
    private static AccountSettingsController instance;
    StackPane rootPane;
    Scene scene;
    @FXML
    private JFXHamburger hamburger;
    @FXML
    private AnchorPane anchorpanemain;
    @FXML
    private ImageView avatar;
    @FXML
    private Label name;
    @FXML
    private Label email;
    @FXML
    private HBox changepassword;
    @FXML
    private HBox changeemail;
    @FXML
    private HBox blockedusers;
    @FXML
    private HBox logout;
    @FXML
    private VBox vboxmenucontainer;
    private CurrentUser currentUser;
    private Stage primaryStage;


    private AccountSettingsController() {
    }

    public static AccountSettingsController getInstance() {
        if (instance == null) instance = new AccountSettingsController();

        return instance;
    }

    @Override
    public void startControlling(Stage primaryStage) throws IOException {
        this.primaryStage = primaryStage;
        init();
    }

    private void init() throws IOException {
        currentUser = CurrentUser.getInstance();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxmls/account.fxml"));
        loader.setController(this);
        rootPane = loader.load();
        name.setText(currentUser.getFullName());
        email.setText(currentUser.getEmail());
        vboxmenucontainer.getChildren().add(new JFXRippler(changepassword));
        vboxmenucontainer.getChildren().add(new JFXRippler(changeemail));
        vboxmenucontainer.getChildren().add(new JFXRippler(blockedusers));
        vboxmenucontainer.getChildren().add(new JFXRippler(logout));
        if (rootPane.getScene() != null) scene = rootPane.getScene();
        else {
            scene = new Scene(rootPane, HomeController.WIDTH, HomeController.HEIGHT);
            scene.getStylesheets().add(getClass().getResource("../stylesheets/account.css").toExternalForm());
        }
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();

        HamburgerBackArrowBasicTransition transition = new HamburgerBackArrowBasicTransition(hamburger);
        transition.setRate(1);
        transition.play();

        hamburger.setOnMouseClicked(event -> {
            try {
                HomeController.getInstance().startControlling(primaryStage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        changepassword.setOnMouseClicked((MouseEvent event) -> {
            JFXDialogLayout layout = new JFXDialogLayout();
            layout.setHeading(new Label("Change password"));
            JFXPasswordField oldPassword = new JFXPasswordField();
            oldPassword.setPromptText("Old Password");
            JFXPasswordField newPassword = new JFXPasswordField();
            newPassword.setPromptText("New Password");
            JFXPasswordField newPassword1 = new JFXPasswordField();
            newPassword1.setPromptText("Confirm Password");
            Label error = new Label("Error message!");
            error.setTextFill(Paint.valueOf("#CE070F"));
            error.setAlignment(Pos.CENTER);
            error.setPadding(new Insets(10, 10, 10, 10));
            VBox vBox = new VBox(oldPassword, newPassword, newPassword1, error);
            layout.setBody(vBox);
            JFXButton submit = new JFXButton("SUBMIT");
            JFXButton cancel = new JFXButton("CANCEL");
            layout.setActions(cancel, submit);
            JFXDialog dialog = new JFXDialog();
            dialog.setContent(layout);
            dialog.setDialogContainer(rootPane);
            dialog.show();
            dialog.setOverlayClose(false);

            submit.setOnAction(event1 -> {
                        if (newPassword.getText().equals(newPassword1.getText()) && oldPassword.getText().trim().length() > 0) {
                            Map map = new HashMap<String, String>();
                            map.put("old_password", oldPassword.getText());
                            map.put("new_password", newPassword.getText());

                            String path = "/user/" + CurrentUser.getInstance().getUuid() + "/change_password";
                            Request request = new Request(path, MethodType.POST, map);
                            Response response = request.perform();
                            if (response.getStatus() == 200) {
                                String s = response.readEntity(String.class);
                                JSONObject jsonObject = new JSONObject(s);
                                if (jsonObject.getBoolean(Keys.JSON_RESULT)) {
                                    System.out.print("Password Changed Successfully");
                                }
                            }

                        }
                    }
            );

            cancel.setOnAction(event1 -> {
                dialog.close();
            });
        });
        changeemail.setOnMouseClicked((MouseEvent event3) -> {
            JFXDialogLayout layout1 = new JFXDialogLayout();
            layout1.setHeading(new Label("Change Email"));
            JFXTextField oldEmail = new JFXTextField();
            oldEmail.setPromptText("Old Email");
            JFXTextField newEmail = new JFXTextField();
            newEmail.setPromptText("New Email");
            JFXTextField newEmail1 = new JFXTextField();
            newEmail1.setPromptText("Confirm Email");
            Label error1 = new Label("Error message!");
            error1.setTextFill(Paint.valueOf("#CE070F"));
            error1.setAlignment(Pos.CENTER);
            error1.setPadding(new Insets(10, 10, 10, 10));
            VBox vBox1 = new VBox(oldEmail, newEmail, newEmail1, error1);
            layout1.setBody(vBox1);
            JFXButton submit1 = new JFXButton("SUBMIT");
            JFXButton cancel1 = new JFXButton("CANCEL");
            layout1.setActions(cancel1, submit1);
            JFXDialog dialog1 = new JFXDialog();
            dialog1.setContent(layout1);
            dialog1.setDialogContainer(rootPane);
            dialog1.show();
            dialog1.setOverlayClose(false);
            submit1.setOnAction(event -> {
                        if (newEmail.getText().equals(newEmail1.getText()) && oldEmail.getText().trim().length() > 0) {
                            Map map = new HashMap<String, String>();
                            map.put("old_email", oldEmail.getText());
                            map.put("new_email", newEmail.getText());

                            String path = "/user/" + CurrentUser.getInstance().getUuid() + "/change_email";
                            Request request = new Request(path, MethodType.POST, map);
                            Response response = request.perform();
                            if (response.getStatus() == 200) {
                                String s = response.readEntity(String.class);
                                JSONObject jsonObject = new JSONObject(s);
                                if (jsonObject.getBoolean(Keys.JSON_RESULT)) {
                                    System.out.print("Email Changed Successfully");
                                }
                            }

                        }
                    }
            );

            cancel1.setOnAction(event4 -> {
                dialog1.close();
            });
        });

    }
}
