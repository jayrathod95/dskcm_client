package com.deskcomm.core.messages;

import com.deskcomm.core.Persistent;
import org.json.JSONObject;

/**
 * Created by Jay Rathod on 06-02-2017.
 */
abstract public class Message implements Persistent {
    protected String messageId;
    protected String body;
    protected String from;
    protected String toUuid;


    public Message(String messageId, String body, String from, String toUuid) {
        this.messageId = messageId;
        this.body = body;
        this.from = from;
        this.toUuid = toUuid;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getToUuid() {
        return toUuid;
    }

    public void setToUuid(String toUuid) {
        this.toUuid = toUuid;
    }

    abstract JSONObject toJSON();
}
