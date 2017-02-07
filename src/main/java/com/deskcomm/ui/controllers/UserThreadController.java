package com.deskcomm.ui.controllers;

import com.deskcomm.core.User;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by Jay Rathod on 23-01-2017.
 */
public class UserThreadController extends Controller {
    private static UserThreadController userThreadController;
    private final TextField textFieldMessage;
    private User user;
    private FXMLLoader loader;
    private BorderPane root;
    private Button button;
    private ListView listView;
    private Tab tab;

    private UserThreadController(User user, Tab tab) throws IOException {
        this.user = user;
        this.tab = tab;
        loader = new FXMLLoader(getClass().getResource("../fxmls/user_thread.fxml"));
        root = loader.load();

        Label label = (Label) loader.getNamespace().get("labelUserName");
        label.setText(user.getFullName());

        textFieldMessage = (TextField) loader.getNamespace().get("textFieldMessage");
        button = (Button) loader.getNamespace().get("buttonSend");
        listView = (ListView) loader.getNamespace().get("listView");


        button.setOnAction(event -> {
            if (textFieldMessage.getText().length() > 0 && textFieldMessage.getText().trim().length() > 0) {
                listView.getItems().add(new Text(textFieldMessage.getText()));
                listView.scrollTo(listView.getItems().size());
                textFieldMessage.setText("");
            }

        });
        root.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                if (textFieldMessage.getText().length() > 0 && textFieldMessage.getText().trim().length() > 0) {
                    listView.getItems().add(new Text(textFieldMessage.getText()));
                    listView.scrollTo(listView.getItems().size());
                    textFieldMessage.setText("");
                }
            }
        });
        tab.setContent(root);
    }

    public static UserThreadController getInstance(User user, Tab tab) throws IOException {
        if (userThreadController == null) userThreadController = new UserThreadController(user, tab);
        return userThreadController;
    }

    @Override
    public void startControlling(Stage primaryStage) {
    }
}
