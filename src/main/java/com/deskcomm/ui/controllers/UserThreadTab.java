package com.deskcomm.ui.controllers;

import com.deskcomm.core.CurrentUser;
import com.deskcomm.core.User;
import com.deskcomm.core.messages.LocalPersonalMessage;
import com.deskcomm.core.messages.OutboundPersonalMessage;
import com.sun.javafx.scene.control.behavior.TabPaneBehavior;
import com.sun.javafx.scene.control.skin.TabPaneSkin;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;

import java.io.IOException;
import java.util.*;

/**
 * Created by jay_rathod on 16-02-2017.
 */
public class UserThreadTab extends Tab {

    private static Set<UserThreadTab> set = new HashSet<>();
    private static String currentUserUuid = CurrentUser.getInstance().getUuid();
    @FXML
    TextField textFieldMessage;
    ScrollPane scrollPane;
    boolean b = false;
    private User user;
    private FXMLLoader loader;
    private BorderPane root;
    private boolean isCreated = false;
    private VBox containerVbox;
    private ObservableList<LocalPersonalMessage> observableConversationList;
    private Tab tab;
    private EventHandler<ActionEvent> sendButtonClickListener = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            func1();
        }
    };

    public UserThreadTab(User user) {
        this.user = user;
    }

    public static UserThreadTab getInstance(String userId) {
        Iterator<UserThreadTab> iterator = set.iterator();
        while (iterator.hasNext()) {
            UserThreadTab next = iterator.next();
            if (next.user.getUuid().equals(userId)) return next;
        }
        User user = new User(userId);
        user.fetchFromDb();
        UserThreadTab userThreadTab = new UserThreadTab(user);
        set.add(userThreadTab);
        return userThreadTab;
    }

    public Tab create() throws IOException {
        tab = new Tab(user.getFullName());
        tab.setUserData(user);
        tab.setClosable(true);
        loader = new FXMLLoader(getClass().getResource("../fxmls/user_thread.fxml"));
        loader.setController(this);
        root = loader.load();
        containerVbox = (VBox) loader.getNamespace().get("vbox1");
        Label label = (Label) loader.getNamespace().get("labelUserName");
        label.setText(user.getFullName());
        scrollPane = (ScrollPane) loader.getNamespace().get("scrollpane");
        scrollPane.addEventFilter(ScrollEvent.SCROLL, event -> {
            if (event.getDeltaX() != 0) event.consume();
        });

        Button sendButton = (Button) loader.getNamespace().get("buttonSend");
        sendButton.setOnAction(sendButtonClickListener);
        tab.setContent(root);
        HomeController.getInstance().getStage().getScene().getStylesheets().add(getClass().getResource("../stylesheets/sheet1.css").toExternalForm());

        Platform.runLater(() -> {
            List<LocalPersonalMessage> conversation = user.getConversation(10);
            Collections.reverse(conversation);
            observableConversationList = FXCollections.observableArrayList(conversation);
            displayMessages(observableConversationList);
        });
        root.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) func1();
            else if (event.getCode() == KeyCode.C) {
                TabPaneBehavior behavior = ((TabPaneSkin) tab.getTabPane().getSkin()).getBehavior();
                behavior.closeTab(tab);

            }

        });
        tab.setOnClosed(event -> {
            set.remove(this);
        });

        isCreated = true;
        return tab;
    }

    public boolean isCreated() {
        return isCreated;
    }

    public void isCreated(boolean b) {
        this.isCreated = b;
    }

    private void func1() {
        if (textFieldMessage.getText().length() > 0 && textFieldMessage.getText().trim().length() > 0) {

            OutboundPersonalMessage outboundMessage = new OutboundPersonalMessage(user.getUuid(), textFieldMessage.getText());
            outboundMessage.insertToTable();
            outboundMessage.send();
            LocalPersonalMessage localMessage = LocalPersonalMessage.from(outboundMessage);

            Text body = new Text(localMessage.getBody());
            body.setFill(Color.WHITE);
            TextFlow textFlow = new TextFlow(body);
            textFlow.setPrefWidth(300);
            Text timeStamp = new Text(localMessage.getTimeStamp());
            timeStamp.setFill(Color.WHITE);
            timeStamp.setTextAlignment(TextAlignment.RIGHT);
            VBox vBox = new VBox(textFlow, timeStamp);
            vBox.setPadding(new Insets(5));
            vBox.setPrefWidth(320);
            //vBox.setMinWidth(100);
            vBox.setMaxWidth(320);
            VBox vBox1;
            vBox1 = new VBox(vBox);
            vBox1.setPadding(new Insets(5, 0, 0, 0));
            vBox1.setPrefWidth(containerVbox.getWidth());
            vBox1.setAlignment(Pos.CENTER_RIGHT);
            vBox.setId("out");
            containerVbox.getChildren().add(containerVbox.getChildren().size(), vBox1);
            textFieldMessage.setText("");
            scrollPane.setVvalue(2.0);
        }
    }

    public ObservableList<LocalPersonalMessage> getObservableConversationList() {
        return observableConversationList;
    }

    private void displayMessages(ObservableList<LocalPersonalMessage> observableConversationList) {
        Iterator<LocalPersonalMessage> iterator = observableConversationList.iterator();
        while (iterator.hasNext()) {
            LocalPersonalMessage localMessage = iterator.next();
            Text body = new Text(localMessage.getBody());
            body.setFill(Color.WHITE);
            TextFlow textFlow = new TextFlow(body);
            textFlow.setPrefWidth(300);
            Text timeStamp = new Text(localMessage.getTimeStamp());
            timeStamp.setFill(Color.WHITE);
            timeStamp.setTextAlignment(TextAlignment.RIGHT);
            VBox vBox = new VBox(textFlow, timeStamp);
            vBox.setPadding(new Insets(5));
            vBox.setPrefWidth(320);
            //vBox.setMinWidth(100);
            vBox.setMaxWidth(320);
            VBox vBox1;
            vBox1 = new VBox(vBox);
            vBox1.setPadding(new Insets(5, 0, 0, 0));
            vBox1.setPrefWidth(containerVbox.getWidth());
            if (localMessage.getFromUserUuid().equals(currentUserUuid)) {
                vBox1.setAlignment(Pos.BOTTOM_RIGHT);
                vBox.setId("out");
            } else {
                vBox1.setAlignment(Pos.BOTTOM_LEFT);
                vBox.setId("in");
            }

            containerVbox.getChildren().add(containerVbox.getChildren().size(), vBox1);
            scrollPane.setVvalue(1.0);
        }
    }

    public Tab getTab() {
        return tab;
    }
}


