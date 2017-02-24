package com.deskcomm.core.messages;

import com.deskcomm.core.CurrentUser;
import com.deskcomm.core.Persistent;
import com.deskcomm.db.DbConnection;
import com.deskcomm.db.Table;
import com.deskcomm.networking.websocket.OutboundWebsocketMessage;
import com.deskcomm.support.Keys;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.UUID;

/**
 * Created by Jay Rathod on 10-02-2017.
 */
public class OutboundPersonalMessage extends Message implements Sendable, Persistent {
    private String toUserUuid;

    public OutboundPersonalMessage(String toUserUuid, String body) {
        super(UUID.randomUUID().toString(), body);
        this.toUserUuid = toUserUuid;
    }


    @Override
    JSONObject toJSON() {
        return new JSONObject().put(Keys.MESSAGE_ID, id).put(Keys.MESSAGE_TO, toUserUuid)
                .put(Keys.MESSAGE_FROM, CurrentUser.getInstance().getUuid())
                .put(Keys.MESSAGE_BODY, body);
    }

    @Override
    public String toString() {
        return toJSON().toString();
    }

    @Override
    public boolean insertToTable() {
        try {
            Connection connection = DbConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement("INSERT INTO messages_personal (_uuid, data,_to,_from, server_timestamp, created) VALUES (?, ?,?, ?, ?, ?);");
            statement.setString(1, id);
            statement.setString(2, body);
            statement.setString(3, toUserUuid);
            statement.setString(4, CurrentUser.getInstance().getUuid());
            statement.setString(5, new Timestamp(new java.util.Date().getTime()).toString());
            statement.setString(6, new Timestamp(new java.util.Date().getTime()).toString());
            int i = statement.executeUpdate();
            statement.close();
            connection.close();
            return i > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            if (e.getMessage().contains(Keys.NO_SUCH_TABLE)) {
                Table.createMessagesPersonalTable();
                return insertToTable();
            }
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

    @Override
    public void send() {
        OutboundWebsocketMessage outboundWebsocketMessage = new OutboundWebsocketMessage("message/personal", this.toJSON(), true);
        outboundWebsocketMessage.send();
    }

    public String getToUserUuid() {
        return toUserUuid;
    }
}
