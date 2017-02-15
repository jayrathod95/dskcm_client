package com.deskcomm.core.messages;

import com.deskcomm.core.Group;
import com.deskcomm.core.Persistent;
import com.deskcomm.core.User;
import com.deskcomm.db.DbConnection;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by Swati Shende on 07-Feb-17.
 */
public class InboundGroupMessage implements Persistent {
    private String id;
    private Group groupId;
    private User sender;
    private String body;
    private String timestamp;

    public InboundGroupMessage() {
    }


    public InboundGroupMessage(String jsonString) {
        JSONObject jsonObject = new JSONObject(jsonString);
        this.id = jsonObject.getString("id");
        this.groupId = new Group(jsonObject.getString("groupId"));
        this.sender = new User(jsonObject.getString("sender"));
        this.body = jsonObject.getString("body");
        this.timestamp = jsonObject.getString("timestamp");

    }

    @Override
    public boolean insertToTable() throws SQLException, ClassNotFoundException {
        try {
            Connection connection = DbConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement("INSERT INTO messages (id, _from, body, saved_to_server_on, created) VALUES (?, ?, ?, ?, CURRENT_TIMESTAMP);");
            statement.setString(1, id);
            statement.setString(2, groupId.getUuid());
            statement.setString(3, sender.getUuid());
            statement.setString(4, body);
            int i = statement.executeUpdate();
            statement.close();
            connection.close();
            return i > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Object getUpdater() {
        return null;
    }

    @Override
    public boolean fetchFromDb() {
        return false;
    }
}
