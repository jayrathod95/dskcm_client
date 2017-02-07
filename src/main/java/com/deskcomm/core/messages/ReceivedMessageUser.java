package com.deskcomm.core.messages;

import com.deskcomm.core.User;
import com.deskcomm.db.DbConnection;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by Jay Rathod on 07-02-2017.
 * This class represents a received message from a user
 */

public class ReceivedMessageUser {

    private String id;
    private User sender;
    private String body;
    private String timestamp;

    public ReceivedMessageUser() {
    }

    public ReceivedMessageUser(String jsonString) {
        JSONObject jsonObject = new JSONObject(jsonString);
        this.id = jsonObject.getString("id");
        this.sender = new User(jsonObject.getString("sender"));
        this.body = jsonObject.getString("body");
        this.timestamp = jsonObject.getString("timestamp");

    }

    public ReceivedMessageUser(JSONObject jsonObject) {
        this.id = jsonObject.getString("id");
        this.sender = new User(jsonObject.getString("sender"));
        this.body = jsonObject.getString("body");
        this.timestamp = jsonObject.getString("timestamp");
    }


    public boolean save() {
        try {
            Connection connection = DbConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement("INSERT INTO messages (id, _from, body, saved_to_server_on, created) VALUES (?, ?, ?, ?, CURRENT_TIMESTAMP);");
            statement.setString(1, id);
            statement.setString(2, sender.getUuid());
            statement.setString(3, body);
            statement.setString(4, timestamp);
            int i = statement.executeUpdate();
            statement.close();
            connection.close();
            return i > 0;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
