package com.deskcomm.ui2.controllers;

import com.deskcomm.core.CurrentUser;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXRippler;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
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
    }
}
