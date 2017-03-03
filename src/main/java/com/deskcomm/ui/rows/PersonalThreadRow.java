package com.deskcomm.ui.rows;

import com.deskcomm.core.User;
import com.deskcomm.ui.controllers.HomeController;
import com.deskcomm.ui.controllers.UserThreadTab;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

/**
 * Created by jay_rathod on 15-02-2017.
 */
public class PersonalThreadRow {

    private static final String FXML_FILE = "fxmls/row_thread.fxml";
    private final String id;
    private final User fromUser;
    private final String body;
    private final String timestamp;

    @FXML
    Label labelUserName, labelMessageText;
    @FXML
    ImageView imageViewAvatar;

    public PersonalThreadRow(String id, User fromUser, String body, String timestamp) {
        this.id = id;
        this.fromUser = fromUser;
        this.body = body;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public User getFromUser() {
        return fromUser;
    }

    public String getBody() {
        return body;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public AnchorPane create() {
        try {
            if (fromUser.fetchFromDb()) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(FXML_FILE));
                loader.setController(this);
                AnchorPane anchorPane = loader.load();
                labelUserName.setText(fromUser.getFullName());
                labelMessageText.setText(body.length() > 100 ? body.substring(0, 100) : body);
                anchorPane.setUserData(fromUser.getUuid());
                anchorPane.setOnMouseClicked(event -> {
                    func1();
                });
                anchorPane.setOnKeyPressed(event -> {
                    if (event.getCode().equals(KeyCode.ENTER)) {
                        func1();
                    }
                });
                return anchorPane;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @SuppressWarnings("Duplicates")
    private void func1() {
        UserThreadTab instance = UserThreadTab.getInstance(fromUser.getUuid());
        if (instance.isCreated()) {
            HomeController.getInstance().getTabPane().getSelectionModel().select(instance.getTab());
        } else {
            try {
                Tab tab = instance.create();
                HomeController.getInstance().getTabPane().getTabs().add(tab);
                HomeController.getInstance().getTabPane().getSelectionModel().select(tab);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
