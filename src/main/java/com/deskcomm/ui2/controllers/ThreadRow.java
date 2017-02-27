package com.deskcomm.ui2.controllers;

import com.deskcomm.core.User;
import com.deskcomm.ui2.core.UserThreadFactory;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by jay_rathod on 2/27/2017.
 * This class represents a row in threads (chats) list on HomeController
 * The class takes User class object and created an HBOX row.
 * Only passing User class object is enough as class implicitly finds out the latest
 * message in the thread (from local database)
 */
public class ThreadRow extends HBox implements EventHandler<MouseEvent> {

    AnchorPane rootPane;
    @FXML
    private ImageView imageViewAvatar;
    @FXML
    private Label labelUserName;
    @FXML
    private Label labelMessageText;
    private User user;
    private Stage stage;

    public ThreadRow(User user, Stage stage) throws IOException {
        this.user = user;
        this.stage = stage;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxmls/row_thread.fxml"));
        loader.setController(this);
        rootPane = loader.load();
        this.getChildren().add(rootPane);
        labelUserName.setText(user.getFullName());
        labelMessageText.setText(user.getConversation(1).get(0).getBody());
        rootPane.setOnMouseClicked(this);
    }


    @Override
    public void handle(MouseEvent event) {
        try {
            UserThreadFactory.getUserThreadController(user).startControlling(stage);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
