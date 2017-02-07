package com.deskcomm.core.messages;

import com.deskcomm.core.User;
import org.json.JSONObject;

/**
 * Created by Jay Rathod on 05-02-2017.
 */
public class TextMessage<S, R> {
    private String id;
    //private R recipient;
    private S sender;
    private String timestamp;
    private String content;
    private String converstation_type;


    public TextMessage(S sender, R to, String content, String timestamp) {
        this.sender = sender;
        //  this.recipient = to;
        this.content = content;
        this.timestamp = timestamp;
    }

    public TextMessage(String jsonString) {
        JSONObject jsonObject = new JSONObject(jsonString);
        this.id = jsonObject.getString("message_id");
        this.converstation_type = jsonObject.getString("conversation_type");
        if (this.converstation_type.equals("UU")) {
            String sender_id = jsonObject.getString("from");
            sender = (S) new User(sender_id);
            content = jsonObject.getString("body");
            timestamp = jsonObject.getString("timestamp");
        }
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
