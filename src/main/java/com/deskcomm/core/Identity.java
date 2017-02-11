package com.deskcomm.core;

import com.deskcomm.support.Keys;
import org.json.JSONObject;

/**
 * Created by Jay Rathod on 10-02-2017.
 */
public class Identity {
    private String uuid;
    private String sessionId;


    public Identity(String uuid, String sessionId) {
        this.uuid = uuid;
        this.sessionId = sessionId;
    }


    public String getSessionId() {
        return sessionId;
    }


    public String getUuid() {
        return uuid;
    }


    public JSONObject toJSON() {
        return new JSONObject().put(Keys.USER_UUID, uuid).put(Keys.SESSION_ID, sessionId);
    }

    @Override
    public String toString() {
        return toJSON().toString();
    }
}
