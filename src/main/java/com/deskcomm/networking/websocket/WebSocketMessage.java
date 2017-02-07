package com.deskcomm.networking.websocket;

import com.deskcomm.support.Keys;
import com.deskcomm.support.PathDecoder;
import org.json.JSONObject;

import java.util.Map;

/**
 * Created by Jay Rathod on 07-02-2017.
 */
public class WebSocketMessage {

    private final JSONObject jsonObject;
    private String path;
    private String data;
    private Map<Integer, String> pathDecomposed;

    public WebSocketMessage(String rawMessage) {
        jsonObject = new JSONObject(rawMessage);
        path = jsonObject.getString(Keys.PATH);
        data = jsonObject.getString(Keys.DATA);
    }

    public JSONObject toJsonObject() {
        return jsonObject;
    }

    public String getPath() {
        return path;
    }

    public String getData() {
        return data;
    }

    public Map<Integer, String> getPathDecomposed() {
        return PathDecoder.getPathParams(path);
    }
}
