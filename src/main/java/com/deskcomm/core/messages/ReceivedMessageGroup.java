package com.deskcomm.core.messages;

import com.deskcomm.core.Group;
import com.deskcomm.core.User;
import com.deskcomm.db.DbConnection;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by Swati Shende on 07-Feb-17.
 */
public class ReceivedMessageGroup {
    private String id;
    private Group groupId;
    private User sender;
    private String body;
    private String timestamp;

    public ReceivedMessageGroup() {
    }

    public ReceivedMessageGroup(String jsonString) {
        JSONObject jsonObject = new JSONObject(jsonString);
        this.id = jsonObject.getString("id");
        this.groupId = new Group(jsonObject.getString("groupId"));
        this.sender = new User(jsonObject.getString("sender"));
        this.body = jsonObject.getString("body");
        this.timestamp = jsonObject.getString("timestamp");

    }

    public boolean save() {
        try {
            Connection connection = DbConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement("INSERT INTO messages (id, _from, body, saved_to_server_on, created) VALUES (?, ?, ?, ?, CURRENT_TIMESTAMP);");
            statement.setString(1, id);
            statement.setString(2, groupId.getUuid());
            statement.setString(3, sender.getUuid());
            statement.setString(4, body);
            statement.setString(5, timestamp);
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
