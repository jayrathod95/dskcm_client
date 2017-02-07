package com.deskcomm.core.messages;

import com.deskcomm.core.User;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Jay Rathod on 06-02-2017.
 */
public class ReceivedMessage<S> implements Serializable {
    private String id;
    private S sender;
    private String timestamp;
    private String content;


    public ReceivedMessage(String jsonString) {
        JSONObject jsonObject = new JSONObject(jsonString);
        this.id = jsonObject.getString("message_id");
        String sender_id = jsonObject.getString("from");
        if (sender instanceof User) {
            sender = (S) new User(sender_id);
        }
        content = jsonObject.getString("body");
        timestamp = jsonObject.getString("timestamp");
    }

    public ReceivedMessage(JSONObject jsonObject) {
        this.id = jsonObject.getString("message_id");
        String sender_id = jsonObject.getString("from");
        sender = (S) new User(sender_id);
        content = jsonObject.getString("body");
        timestamp = jsonObject.getString("timestamp");
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public S getSender() {
        return sender;
    }

    public void setSender(S sender) {
        this.sender = sender;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean save() {

        return false;
    }
}
