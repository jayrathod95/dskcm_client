package com.deskcomm.ui2.controllers;

import com.deskcomm.core.CurrentUser;
import com.deskcomm.core.Event;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;

/**
 * Created by jay_rathod on 3/3/2017.
 */
public class EventRow extends HBox implements EventHandler<MouseEvent> {

    @FXML
    private VBox row_event;

    @FXML
    private Label title;

    @FXML
    private Label description;

    @FXML
    private MaterialDesignIconView heart;

    @FXML
    private Label int_users;

    @FXML
    private Label starts;

    @FXML
    private Label ends;

    @FXML
    private Label createdBy;
    private Event event;

    public EventRow(Event event) {
        this.event = event;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("fxmls/row_event.fxml"));
            loader.setController(this);
            HBox root = loader.load();
            this.getChildren().add(root);
            this.setOnMouseClicked(this);
            title.setText(event.getTitle());
            description.setText(event.getDescription());
            int_users.setText(event.getInterestedUsersCount() + "");
            starts.setText(event.getStarts());
            ends.setText(event.getEnds());
            if (event.getCreatedBy().getUuid().equals(CurrentUser.getInstance().getUuid()))
                createdBy.setText(CurrentUser.getInstance().getFullName() + " (You)");
            else
                createdBy.setText(event.getCreatedBy().getFullName());
            if (event.interested()) heart.setGlyphName("HEART");
            else heart.setGlyphName("HEART_OUTLINE");

            heart.setOnMouseClicked(event1 -> {
                if (heart.getGlyphName().equals("HEART")) {
                    event.setInterested(false);
                    heart.setGlyphName("HEART_OUTLINE");
                } else {
                    event.setInterested(true);
                    heart.setGlyphName("HEART");
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Label getInt_users() {
        return int_users;
    }

    public MaterialDesignIconView getHeart() {
        return heart;
    }

    public Event getEvent() {
        return event;
    }

    @Override
    public void handle(MouseEvent event) {
        System.out.println(this.event.getUuid() + " Event Row Clicked");
    }
}
