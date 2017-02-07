package com.deskcomm.networking;

import com.deskcomm.support.Keys;
import com.deskcomm.support.L;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jay Rathod on 22-01-2017.
 */
public class GetAllUsersRequest extends Request {


    final static private String path = "/users/get_all";
    private static int methodType = MethodType.POST;
    private String uuid;
    private String sessionId;

    public GetAllUsersRequest(String uuid, String sessionId) {
        super(path, MethodType.POST);
        this.uuid = uuid;
        this.sessionId = sessionId;
    }


    @Override
    public Map<String, String> getParams() {
        Map<String, String> params = new HashMap<>();
        params.put(Keys.PARAM.UUID_USER, uuid);
        params.put(Keys.PARAM.SESSION_ID, sessionId);
        L.println(params.toString());
        return params;
    }
}
