package com.deskcomm.ui2.controllers;

import com.deskcomm.core.CurrentUser;
import com.deskcomm.core.User;
import com.deskcomm.core.messages.LocalPersonalMessage;
import com.deskcomm.core.messages.Message;
import com.deskcomm.core.messages.OutboundPersonalMessage;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * Created by jay_rathod on 2/25/2017.
 */
public class UserThreadController extends Controller {
    VBox root;
    @FXML
    private JFXHamburger hamburger;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox vBoxMessagesContainer;
    @FXML
    private JFXTextField messagefield;
    @FXML
    private JFXButton sendbtn;
    @FXML
    private Label title;
    private Stage primaryStage;
    private User user;

    public UserThreadController(User user) {
        this.user = user;
        user.fetchFromDb();
    }


    @Override
    public void startControlling(Stage primaryStage) throws IOException {
        this.primaryStage = primaryStage;
        init();

    }

    private void init() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxmls/user_thread.fxml"));
        loader.setController(this);
        root = loader.load();
        Scene scene;
        if (root.getScene() != null) {
            scene = root.getScene();
        } else {
            scene = new Scene(root, HomeController.WIDTH, HomeController.HEIGHT);
        }
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
        title.setText(user.getFullName());

        HamburgerBackArrowBasicTransition transition = new HamburgerBackArrowBasicTransition(hamburger);
        transition.setRate(1);
        transition.play();

        //fetch messages from local database and  update ui
        refresh();
        scrollPane.vvalueProperty().bind(vBoxMessagesContainer.heightProperty());


        sendbtn.setOnAction(event -> {
            if (messagefield.getText().trim().length() > 0) {
                OutboundPersonalMessage personalMessage = new OutboundPersonalMessage(user.getUuid(), messagefield.getText());
                personalMessage.insertToTable();
                personalMessage.send();
                MessageBoxOut messageBoxOut = new MessageBoxOut(personalMessage);
                messageBoxOut.setTime(new Date());
                vBoxMessagesContainer.getChildren().add(messageBoxOut);
            }
        });
        hamburger.setOnMouseClicked(event -> {
            System.out.println("hamburger clicked");
            try {
                HomeController.getInstance().startControlling(primaryStage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }


    public void refresh() {
        List<LocalPersonalMessage> conversation = user.getConversation(100);
        conversation.forEach(message -> {
            if (message.getFromUserUuid().equals(CurrentUser.getInstance().getUuid())) {
                MessageBoxOut messageBoxOut = new MessageBoxOut(message);
                //messageBoxOut.setTime();
                vBoxMessagesContainer.getChildren().add(messageBoxOut);
            } else {
                MessageBoxIn messageBoxIn = new MessageBoxIn(message);
                //messageBoxIn.setTime();
                vBoxMessagesContainer.getChildren().add(messageBoxIn);
            }
        });
    }


    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public User getUser() {
        return user;
    }


    private class MessageBoxIn extends VBox implements EventHandler<MouseEvent> {
        private Date time;
        private FXMLLoader loader;
        private Text messageBody;
        private Label timelbl;
        private Message message;


        MessageBoxIn(Message message) {
            this.setOnMouseClicked(this);
            this.message = message;
            try {
                loader = new FXMLLoader(getClass().getResource("../fxmls/row_message_in.fxml"));
                loader.setController(this);
                VBox root = loader.load();
                this.getChildren().add(root);
                messageBody = (Text) loader.getNamespace().get("messagebody");
                timelbl = (Label) loader.getNamespace().get("time");
                messageBody.setText(message.getBody());

            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        public void setTime(Date date) {
            this.time = date;
            timelbl.setText("to do");
        }

        @Override
        public void handle(MouseEvent event) {

        }
    }

    private class MessageBoxOut extends AnchorPane implements EventHandler<MouseEvent> {
        private FXMLLoader loader;
        private Text messageBody;
        private Label timelbl;
        private String messageUuid;
        private Message message;
        private Date date;


        MessageBoxOut(Message message) {
            this.setOnMouseClicked(this);
            this.message = message;
            try {
                loader = new FXMLLoader(getClass().getResource("../fxmls/row_message_out.fxml"));
                loader.setController(this);
                VBox root = loader.load();
                this.getChildren().add(root);
                setRightAnchor(root, 0.0);
                messageBody = (Text) loader.getNamespace().get("messagebody");
                timelbl = (Label) loader.getNamespace().get("time");
                messageBody.setText(message.getBody());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public Message getMessage() {
            return message;
        }

        public void setTime(Date date) {
            this.date = date;
        }

        public String getMessageUuid() {
            return messageUuid;
        }

        @Override
        public void handle(MouseEvent event) {

        }
    }
}
