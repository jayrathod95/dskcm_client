package com.deskcomm.ui.controllers;

import com.deskcomm.core.CurrentUser;
import com.deskcomm.core.Group;
import com.deskcomm.core.User;
import com.deskcomm.networking.websocket.OutboundWebsocketMessage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;

/**
 * Created by jay_rathod on 14-02-2017.
 */
public class GroupCreatorController extends Controller {


    final static public ObservableSet<String> selectedUserIds = FXCollections.observableSet();
    private static final String FXML_FILE = "../fxmls/create_new_group.fxml";
    private static final String FXML_FILE_2 = "../fxmls/row_user_selector.fxml";
    private static GroupCreatorController controller;
    @FXML
    Button buttonCancel, buttonCreateGroup, buttonIcon;
    @FXML
    AnchorPane anchorpaneUsersList;
    @FXML
    Label labelUserSelected;
    @FXML
    TextField textFieldGroupName;
    private FXMLLoader loader;
    private VBox vBox;


    public GroupCreatorController() {

        try {
            loader = new FXMLLoader(getClass().getResource(FXML_FILE));
            loader.setController(this);
            vBox = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }


        selectedUserIds.addListener((SetChangeListener<String>) change -> {
            if (selectedUserIds.size() > 1)
                labelUserSelected.setText(selectedUserIds.size() + " users selected");
            else labelUserSelected.setText(selectedUserIds.size() + " user selected");
        });

        buttonCancel.setOnAction(event -> {
            ((VBox) primaryStage.getScene().getRoot()).getChildren().clear();
            primaryStage.close();
            controller = null;
        });

        buttonCreateGroup.setOnAction(event -> {
            if (textFieldGroupName.getText().trim().length() > 0) {
                if (selectedUserIds.size() > 0) {
                    Iterator<String> iterator = selectedUserIds.iterator();
                    ArrayList<User> list = new ArrayList<User>();
                    while (iterator.hasNext()) {
                        list.add(new User(iterator.next()));
                    }
                    Group group = new Group(UUID.randomUUID().toString(),
                            textFieldGroupName.getText(),
                            "",
                            CurrentUser.getInstance(), null, ((User[]) list.toArray()));
                    OutboundWebsocketMessage websocketMessage = new OutboundWebsocketMessage("group/create", group.toJSON(), true);

                }
            }
        });

        buttonIcon.setOnAction(event -> {
            FileChooser chooser = new FileChooser();
            chooser.showOpenDialog(primaryStage);
        });

    }

    public static GroupCreatorController getInstance() {
        if (controller == null) controller = new GroupCreatorController();
        return controller;
    }

    @Override
    public void startControlling(Stage ownerStage) {
        this.primaryStage = new Stage();
        primaryStage.setTitle("Create Group");
        primaryStage.initOwner(ownerStage);
        primaryStage.initModality(Modality.APPLICATION_MODAL);


        try {
            Scene scene = new Scene(vBox, 600, 630);
            primaryStage.setScene(scene);
            primaryStage.show();
            ArrayList<User> allUsers = User.getAllUsers();
            ListView<AnchorPane> listView = new ListView<>();
            for (User user : allUsers) {
                FXMLLoader loader1 = new FXMLLoader(getClass().getResource(FXML_FILE_2));
                AnchorPane anchorPane = loader1.load();
                ImageView imageView = (ImageView) loader1.getNamespace().get("imageView");
                Label userName = (Label) loader1.getNamespace().get("userName");
                userName.setText(user.getFullName());
                CheckBox checkBox = (CheckBox) loader1.getNamespace().get("checkBox");
                checkBox.setUserData(user.getUuid());
                checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
                    if (!oldValue && newValue) selectedUserIds.add((String) checkBox.getUserData());
                    if (oldValue && !newValue) selectedUserIds.remove(checkBox.getUserData());
                });
                listView.setPrefWidth(540);
                listView.setPrefHeight(anchorpaneUsersList.getHeight());
                listView.getItems().add(anchorPane);
            }
            anchorpaneUsersList.getChildren().add(listView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
