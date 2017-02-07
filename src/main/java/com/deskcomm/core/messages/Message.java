package com.deskcomm.core.messages;

import com.deskcomm.core.Persistent;
import org.json.JSONObject;

import java.sql.SQLException;

/**
 * Created by Jay Rathod on 06-02-2017.
 */
public class Message implements Persistent {
    String id;
    String from;
    String body;

    public Message(String jsonString) {
        JSONObject jsonObject = new JSONObject(jsonString);
        id = jsonObject.getString("id");

    }

    @Override
    public boolean insertToTable() throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public Object getUpdater() {
        return null;
    }
}
