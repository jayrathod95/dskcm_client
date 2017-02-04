package com.deskcomm;

import com.deskcomm.support.L;
import javafx.application.Preloader;
import javafx.stage.Stage;

/**
 * Created by Jay Rathod on 28-01-2017.
 */
public class MainPreloader extends Preloader {
    @Override
    public void start(Stage primaryStage) throws Exception {
        L.println("Hi from Preloader Class ");
    }

    @Override
    public void handleProgressNotification(ProgressNotification info) {
        super.handleProgressNotification(info);
    }

    @Override
    public void handleStateChangeNotification(StateChangeNotification info) {
        super.handleStateChangeNotification(info);
        L.println("State Changed : " + info.toString());
    }

    @Override
    public void handleApplicationNotification(PreloaderNotification info) {
        super.handleApplicationNotification(info);
    }

    @Override
    public boolean handleErrorNotification(ErrorNotification info) {
        return super.handleErrorNotification(info);
    }
}
