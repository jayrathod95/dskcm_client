package com.deskcomm.networking.websocket;

import com.deskcomm.support.Keys;
import com.deskcomm.support.PathDecoder;
import org.json.JSONObject;

import javax.websocket.Session;
import java.util.Map;

/**
 * Created by Jay Rathod on 07-02-2017.
 */
public class WebSocketMessage {

    private JSONObject jsonObject;
    private String path;
    private JSONObject data;
    private Map<Integer, String> pathDecomposed;

    public WebSocketMessage(String wholeMessageJsonString) {
        jsonObject = new JSONObject(wholeMessageJsonString);
        path = jsonObject.getString(Keys.PATH);
        data = jsonObject.getJSONObject(Keys.DATA);
    }

    public WebSocketMessage() {
    }


    public JSONObject toJsonObject() {
        return jsonObject;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public JSONObject getData() {
        return data;
    }

    public void setData(JSONObject data) {
        this.data = data;
    }

    public Map<Integer, String> getPathDecomposed() {
        return PathDecoder.getPathParams(path);
    }

    @Override
    public String toString() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(Keys.PATH, path);
        jsonObject.put(Keys.DATA, data);
        return jsonObject.toString();
    }

    public void send(Session session) {
        session.getAsyncRemote().sendText(toString());
    }
}
