package com.deskcomm.ui2.controllers;

import com.deskcomm.core.CurrentUser;
import com.deskcomm.resources.images.Images;
import com.jfoenix.controls.*;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("fxmls/account.fxml"));
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
            scene.getStylesheets().add(getClass().getResource("stylesheets/account.css").toExternalForm());
        }
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
        if (currentUser.getGender().equals("M"))
            avatar.setImage(Images.get(Images.AVATAR_MALE));
        else if (currentUser.getGender().equals("F")) avatar.setImage(Images.get(Images.AVATAR_FEMALE));

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
        changepassword.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                JFXDialogLayout layout = new JFXDialogLayout();
                layout.setHeading(new Label("Change Password"));

                JFXPasswordField oldPassword = new JFXPasswordField();
                oldPassword.setPromptText("Old Password");
                JFXPasswordField newPassword = new JFXPasswordField();
                newPassword.setPromptText("New Password");
                JFXPasswordField newPassword1 = new JFXPasswordField();
                newPassword1.setPromptText("Confirm Password");

                VBox vBox = new VBox(oldPassword, newPassword, newPassword1);
                layout.setBody(vBox);
                JFXButton submit = new JFXButton("SUBMIT");
                layout.setActions(submit);

                JFXDialog dialog = new JFXDialog();
                dialog.setContent(layout);
                dialog.setDialogContainer(rootPane);
                dialog.show();
            }

        });
    }
}
