package com.deskcomm.core.messages;

import com.deskcomm.core.CurrentUser;
import com.deskcomm.core.Persistent;
import com.deskcomm.db.DbConnection;
import com.deskcomm.db.Table;
import com.deskcomm.support.Keys;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static com.deskcomm.support.Keys.*;

/**
 * Created by Jay Rathod on 07-02-2017.
 * This class represents received message from a user
 */

public class InboundPersonalMessage extends Message implements Persistent {

    private String fromUserUuid;
    private String body;
    private String timestamp;

    public InboundPersonalMessage() {
    }

    public InboundPersonalMessage(String jsonString) {
        JSONObject jsonObject = new JSONObject(jsonString);
        this.id = jsonObject.getString(MESSAGE_ID);
        this.fromUserUuid = jsonObject.getString(MESSAGE_FROM);
        this.body = jsonObject.getString(MESSAGE_BODY);
        this.timestamp = jsonObject.getString(SERVER_TIMESTAMP);
    }

    public InboundPersonalMessage(JSONObject jsonObject) {
        this.id = jsonObject.getString(MESSAGE_ID);
        this.fromUserUuid = jsonObject.getString(MESSAGE_FROM);
        this.body = jsonObject.getString(MESSAGE_BODY);
        this.timestamp = jsonObject.getString(SERVER_TIMESTAMP);
    }


    @Override
    public boolean insertToTable() {
        try {
            Connection connection = DbConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement("INSERT INTO messages_personal (_uuid, data,_to,_from, server_timestamp, created) VALUES (?, ?,?, ?, ?, CURRENT_TIMESTAMP);");
            statement.setString(1, id);
            statement.setString(2, body);
            statement.setString(3, CurrentUser.getInstance().getUuid());
            statement.setString(4, fromUserUuid);
            statement.setString(5, timestamp);
            int i = statement.executeUpdate();
            statement.close();
            connection.close();
            return i > 0;
        } catch (SQLException e) {
            // e.printStackTrace();
            if (e.getMessage().contains(Keys.NO_SUCH_TABLE)) {
                System.out.println("Creating messages_personal Table");
                Table.createMessagesPersonalTable();
                return insertToTable();
            } else e.printStackTrace();
        }
        return false;
    }

    @Override
    public Object getUpdater() {

        return null;
    }

    @Override
    public boolean fetchFromDb() {
        return false;
    }

    public String getId() {
        return id;
    }

    public String getFromUserUuid() {
        return fromUserUuid;
    }

    public String getBody() {
        return body;
    }

    @Override
    JSONObject toJSON() {
        return null;
    }

    public String getTimestamp() {
        return timestamp;
    }

}
