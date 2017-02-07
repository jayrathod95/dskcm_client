package com.deskcomm.ui.controllers;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

/**
 * Created by Jay Rathod on 23-01-2017.
 */
public class RowUsersController {
    @FXML
    public void onMouseEnteredVBox(MouseEvent mouseEvent) {
        ((VBox) mouseEvent.getSource()).styleProperty().set("-fx-background-color: lightgray");
    }

    @FXML
    public void onMouseExitedVBox(MouseEvent mouseEvent) {
        ((VBox) mouseEvent.getSource()).styleProperty().set("-fx-background-color: #f9f9f9");
    }
}
