package com.deskcomm.ui.support;

import com.deskcomm.ui.imported.jtoast.notification.JToast;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.util.Duration;

/**
 * Created by jay_rathod on 21-02-2017.
 */
public class Toast {
    public static void make(String title, String body, Duration seconds) {
        JToast jToast = new JToast(title, body, null, Color.AZURE);
        jToast.showAndDismiss(seconds);
    }

    public static void make(String title, String body, Image image) {
        JToast jToast = new JToast(title, body, image, Color.AZURE);
        jToast.showAndDismiss(Duration.seconds(5));
    }


    public static void make(String title, String body) {
        JToast jToast = new JToast(title, body, null, Color.AZURE);
        jToast.showAndDismiss(Duration.seconds(5));
    }

    public static void make(String title) {
        JToast jToast = new JToast(title, "", null, Color.AZURE);
        jToast.showAndDismiss(Duration.seconds(5));
    }
}
