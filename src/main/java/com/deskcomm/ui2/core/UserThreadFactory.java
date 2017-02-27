package com.deskcomm.ui2.core;

import com.deskcomm.core.User;
import com.deskcomm.ui2.controllers.UserThreadController;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jay_rathod on 2/25/2017.
 */
public class UserThreadFactory {
    private static Map<String, UserThreadController> map = new HashMap<>();

    public static UserThreadController getUserThreadController(User user) {
        if (map.containsKey(user.getUuid())) {
            return map.get(user.getUuid());
        } else {
            UserThreadController controller = new UserThreadController(user);
            map.put(user.getUuid(), controller);
            return controller;
        }
    }

}
