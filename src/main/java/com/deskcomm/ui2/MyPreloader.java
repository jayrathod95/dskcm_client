package com.deskcomm.ui2;

import javafx.application.Preloader;
import javafx.stage.Stage;

/**
 * Created by jay_rathod on 2/25/2017.
 */
public class MyPreloader extends Preloader {
    @Override
    public void start(Stage primaryStage) throws Exception {
        System.out.println("Preloader called");
    }
}
