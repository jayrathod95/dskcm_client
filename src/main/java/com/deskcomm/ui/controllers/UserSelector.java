package com.deskcomm.ui.controllers;

import com.deskcomm.core.User;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Jay Rathod on 13-02-2017.
 */
public class UserSelector {
    final static public ObservableSet<String> selectedUserIds = FXCollections.observableSet();
    private static UserSelector userSelector;
    final private String FXML_FILE = "../fxmls/user_selector.fxml";
    final private String FXML_FILE1 = "../fxmls/row_user_selector.fxml";

    private UserSelector() throws IOException {

        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource(FXML_FILE));
        loader.setController(this);
        VBox rootElement = loader.load();
        Scene scene = new Scene(rootElement, 600, 461);
        stage.setScene(scene);
        stage.show();

        ArrayList<User> allUsers = User.getAllUsers();
        ListView<AnchorPane> listView = new ListView<>();
        listView.setPrefWidth(stage.getWidth());
        listView.setPrefHeight(stage.getHeight());
        if (allUsers != null)
            for (User user : allUsers) {
                FXMLLoader loader1 = new FXMLLoader(getClass().getResource(FXML_FILE1));
                AnchorPane anchorPane = loader1.load();
                Label userName = (Label) loader1.getNamespace().get("userName");
                userName.setText(user.getFullName());
                listView.getItems().add(anchorPane);
                CheckBox checkBox = (CheckBox) loader1.getNamespace().get("checkBox");
                checkBox.setUserData(user.getUuid());
                checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
                    if (!oldValue && newValue) selectedUserIds.add((String) checkBox.getUserData());
                    if (oldValue && !newValue) selectedUserIds.remove(checkBox.getUserData());
                });

            }

        Pane pane = (Pane) loader.getNamespace().get("pane");
        pane.getChildren().add(listView);

        Label label = (Label) loader.getNamespace().get("labelUsersSelected");

        selectedUserIds.addListener((SetChangeListener<String>) change -> {
            if (selectedUserIds.size() > 1)
                label.setText(selectedUserIds.size() + " users selected");
            else label.setText(selectedUserIds.size() + " user selected");

        });


        Button buttonDone = (Button) loader.getNamespace().get("buttonDone");
        Button buttonCancel = (Button) loader.getNamespace().get("buttonCancel");
        buttonDone.setOnAction(event -> {
            stage.close();
        });

        buttonCancel.setOnAction(event -> {
            selectedUserIds.clear();
            Platform.exit();
        });


    }


    public static void select() throws IOException {
        if (userSelector == null) userSelector = new UserSelector();
    }


}
