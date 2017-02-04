package com.deskcomm.networking;

import com.deskcomm.support.Keys;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jay Rathod on 23-01-2017.
 */
public class UserDetailsRequest extends Request {
    private final static String path = "/user/get_details";
    final static private int methodType = MethodType.POST;
    Map<String, String> map;

    public UserDetailsRequest(String uuid, String sessionId, SuccessListener successListener, ErrorListener errorListener) {
        super(path, methodType);
        map = new HashMap<>();
        map.put(Keys.USER_UUID, uuid);
        map.put(Keys.SESSION_ID, sessionId);
    }

    @Override
    public Map<String, String> getParams() {
        return map;
    }
}
