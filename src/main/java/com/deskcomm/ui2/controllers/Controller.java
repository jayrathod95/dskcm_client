package com.deskcomm.ui2.controllers;

import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by Jay Rathod on 16-01-2017.
 */
abstract public class Controller {

    protected String windowTitle = "DeskComm";
    Stage primaryStage;

    abstract public void startControlling(Stage primaryStage) throws IOException;
}
