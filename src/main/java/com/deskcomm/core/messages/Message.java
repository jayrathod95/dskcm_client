package com.deskcomm.core.messages;

import org.json.JSONObject;

/**
 * Created by Jay Rathod on 06-02-2017.
 */
abstract public class Message {
    protected String id;
    protected String body;


    public Message(String id, String body) {
        this.id = id;
        this.body = body;
    }

    public Message() {
    }

    public Message(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    abstract JSONObject toJSON();
}
