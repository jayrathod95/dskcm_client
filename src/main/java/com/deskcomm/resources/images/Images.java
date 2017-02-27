package com.deskcomm.resources.images;

import javafx.scene.image.Image;

/**
 * Created by jay_rathod on 21-02-2017.
 */
public class Images {
    public static final String AVATAR_MALE = "avatar_m.png";
    public static final String AVATAR_FEMALE = "avatar_f.png";
    public static String ERROR = "error.png";
    public static String SUCCESS = "success.png";
    public static String APP_ICON = "logo_min.png";

    private static Images imagesServer = null;

    public static Image get(String imageName) {
        if (imagesServer == null) imagesServer = new Images();
        return imagesServer.func1(imageName);
    }

    private Image func1(String s) {
        return new Image(getClass().getResourceAsStream(s));
    }
}
