package com.deskcomm.networking;

import com.deskcomm.support.Keys;

import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jay Rathod on 17-01-2017.
 */
public class LoginRequest extends Request {

    private static String path = "/user/login";
    private static int methodType = MethodType.POST;
    private static String mediaType1 = MediaType.APPLICATION_JSON;
    private static String mediaType2 = MediaType.TEXT_HTML;
    private static String mediaType3 = MediaType.TEXT_PLAIN;
    private String userName;
    private String password;

    public LoginRequest(String userName, String password) {
        super(path, methodType, mediaType1, mediaType2, mediaType3);
        this.userName = userName;
        this.password = password;
    }

    @Override
    public Map<String, String> getParams() {
        Map<String, String> map = new HashMap<>();
        map.put(Keys.PARAM_USERNAME, userName);
        map.put(Keys.PARAM_PASSWORD, password);
        return map;
    }

    /*

    public LoginRequest( int methodType, MediaType mediaType, Map<String, String> map, SuccessListener successListener, ErrorListener errorListener) {
        super(path, MethodType.POST, mediaType, map, successListener, errorListener);
    }
    */
}
