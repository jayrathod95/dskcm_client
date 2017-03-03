package com.deskcomm.ui.rows;

import com.deskcomm.core.User;
import com.deskcomm.resources.images.Images;
import com.deskcomm.ui2.controllers.HomeController;
import com.deskcomm.ui2.core.UserThreadFactory;
import com.jfoenix.controls.JFXRippler;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by jay_rathod on 16-02-2017.
 */
public class UserListRow extends HBox implements EventHandler<MouseEvent> {
    private static final String FXML_FILE = "fxmls/row_users.fxml";
    @FXML
    private Label labelUserName;
    @FXML
    private AnchorPane rootPane;
    @FXML
    private Circle greenCircle;
    @FXML
    private ImageView imageView;
    private JFXRippler rippler;
    private User user;

    public UserListRow(User user, Stage stage) {
        this.user = user;
        //     this.user.fetchFromDb();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("fxmls/row_users.fxml"));
            loader.setController(this);
            HBox hBox = loader.load();
            hBox.setPrefWidth(HomeController.WIDTH);
            rippler = new JFXRippler(hBox);
            this.getChildren().add(rippler);
            labelUserName.setText(user.getFullName());

            if (user.getGender().equals("M"))
                imageView.setImage(Images.get(Images.AVATAR_MALE));
            else imageView.setImage(Images.get(Images.AVATAR_FEMALE));

            if (user.isOnline()) greenCircle.setVisible(true);
            else greenCircle.setVisible(false);

            hBox.setOnMouseClicked(event -> {
                System.out.println("mouse");
                try {
                    UserThreadFactory.getUserThreadController(user).startControlling(stage);
                    new Thread(new Task<Boolean>() {
                        @Override
                        protected Boolean call() throws Exception {
                            user.setAllMessagesAsRead();
                            return null;
                        }
                    }).start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public HBox create() {
        return null;
    }


    @Override
    public void handle(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY)
            System.out.println("Mouse event - ");
    }

    public User getUser() {
        return user;
    }
}
