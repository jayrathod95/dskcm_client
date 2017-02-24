package com.deskcomm.ui.rows;

import com.deskcomm.core.User;
import com.deskcomm.ui.controllers.HomeController;
import com.deskcomm.ui.controllers.UserThreadTab;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

import java.io.IOException;

/**
 * Created by jay_rathod on 16-02-2017.
 */
public class UserListRow {
    private static final String FXML_FILE = "fxmls/row_users.fxml";
    @FXML
    private ImageView imageView;
    @FXML
    private Label labelUserName;
    @FXML
    private AnchorPane rootPane;
    private User user;
    private EventHandler<? super MouseEvent> clickListener = (EventHandler<MouseEvent>) event -> {
        func1();
    };

    public UserListRow(User user) {
        this.user = user;
        this.user.fetchFromDb();
    }

    public HBox create() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(FXML_FILE));
            loader.setController(this);
            HBox hBox = loader.load();
            labelUserName.setText(user.getFullName());
            hBox.setUserData(user.toJSON());
            hBox.setOnMouseClicked(clickListener);
            return hBox;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void func1() {
        UserThreadTab instance = UserThreadTab.getInstance(user.getUuid());
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
