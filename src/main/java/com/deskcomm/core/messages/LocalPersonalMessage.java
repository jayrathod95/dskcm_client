package com.deskcomm.core.messages;

import com.deskcomm.core.CurrentUser;
import com.deskcomm.db.DbConnection;
import com.deskcomm.db.Table;
import com.deskcomm.support.Keys;
import org.json.JSONObject;

import java.sql.*;

import static com.deskcomm.support.Keys.*;

/**
 * Created by jay_rathod on 16-02-2017.
 */
public class LocalPersonalMessage extends Message {
    private String toUserUuid;
    private String fromUserUuid;
    private boolean isUnread;
    private String timeStamp;

    public LocalPersonalMessage(String id, String body, String toUserUuid, String fromUserUuid, Boolean isUnread, String timeStamp) {
        super(id, body);
        this.toUserUuid = toUserUuid;
        this.fromUserUuid = fromUserUuid;
        this.isUnread = isUnread;

        this.timeStamp = timeStamp;
    }

    public LocalPersonalMessage(String id) {
        super(id);
    }

    public static LocalPersonalMessage from(OutboundPersonalMessage outboundMessage) {
        return new LocalPersonalMessage(outboundMessage.getId(), outboundMessage.getBody(), outboundMessage.getToUserUuid(), CurrentUser.getInstance().getUuid(), false, new Timestamp(new java.util.Date().getTime()).toString());
    }

    public static LocalPersonalMessage from(InboundPersonalMessage inboundPersonalMessage) {
        return new LocalPersonalMessage(inboundPersonalMessage.getId(), inboundPersonalMessage.getBody(), CurrentUser.getInstance().getUuid(), inboundPersonalMessage.getFromUserUuid(), inboundPersonalMessage.getIsUnread(), inboundPersonalMessage.getTimestamp());
    }

    public String getToUserUuid() {
        return toUserUuid;
    }

    public void setToUserUuid(String toUserUuid) {
        this.toUserUuid = toUserUuid;
    }

    public String getFromUserUuid() {
        return fromUserUuid;
    }

    public void setFromUserUuid(String fromUserUuid) {
        this.fromUserUuid = fromUserUuid;
    }

    public boolean fetch() {
        boolean b = false;
        try {
            Connection connection = DbConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT data,_from,_to,READ,server_timestamp FROM messages_personal WHERE _uuid=?");
            statement.setString(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                body = resultSet.getString(1);
                toUserUuid = resultSet.getString(3);
                fromUserUuid = resultSet.getString(2);
                isUnread = resultSet.getInt(4) == 0;
                timeStamp = resultSet.getString(5);
                b = true;
            }
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            if (e.getMessage().equals(Keys.NO_SUCH_TABLE)) {
                Table.createMessagesPersonalTable();
            }
        }
        return b;
    }

    @Override
    public String toString() {
        return toJSON().toString();
    }

    @Override
    JSONObject toJSON() {
        return new JSONObject().put(MESSAGE_ID, id).put(MESSAGE_BODY, body).put(MESSAGE_TO, toUserUuid).put(MESSAGE_FROM, fromUserUuid).put(SERVER_TIMESTAMP, timeStamp);
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public boolean isUnread() {
        return isUnread;
    }

    public void setUnread(boolean unread) {
        isUnread = unread;
    }
}
