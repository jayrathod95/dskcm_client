package com.deskcomm.networking.websocket;

import com.deskcomm.core.CurrentUser;
import com.deskcomm.core.Identity;
import com.deskcomm.support.Keys;
import org.json.JSONObject;

/**
 * Created by Jay Rathod on 11-02-2017.
 */
public class OutboundWebsocketMessage {
    private String path;
    private Identity identity;
    private JSONObject data;


    public OutboundWebsocketMessage(String path, JSONObject data, boolean attachIdentity) {
        this.path = path;
        this.data = data;
        if (attachIdentity) {
            identity = CurrentUser.getInstance().getIdentity();
        }

    }

    public OutboundWebsocketMessage() {
    }


    public JSONObject toJsonObject() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(Keys.WS_PATH, path);
        jsonObject.put(Keys.WS_DATA, data);
        if (identity != null) {
            jsonObject.put(Keys.WS_IDENTITY, identity.toJSON());
        }
        return jsonObject;
    }

    public void send() {

        WebSocketEndPoint.sendMessage(this.toString());
    }

    public void attachIdentity() {
        identity = CurrentUser.getInstance().getIdentity();
    }

    @Override
    public String toString() {
        return toJsonObject().toString();
    }


    public void setPath(String path) {
        this.path = path;
    }

    public void setIdentity(Identity identity) {
        this.identity = identity;
    }

    public void setData(JSONObject data) {
        this.data = data;
    }
}
