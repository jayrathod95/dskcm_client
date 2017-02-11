package com.deskcomm.networking.websocket;

import com.deskcomm.support.Keys;
import org.json.JSONObject;

/**
 * Created by Jay Rathod on 11-02-2017.
 */
public class InboundWebsocketMessage {

    private String path;
    private JSONObject data;


    public InboundWebsocketMessage(String rawMessageString) {
        JSONObject jsonObject = new JSONObject(rawMessageString);
        path = jsonObject.getString(Keys.WS_PATH);
        data = jsonObject.getJSONObject(Keys.WS_DATA);
    }


    public String getPath() {
        return path;
    }

    public JSONObject getData() {
        return data;
    }
}
