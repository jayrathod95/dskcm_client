package com.deskcomm.core.messages;

import com.deskcomm.core.CurrentUser;
import com.deskcomm.networking.websocket.OutboundWebsocketMessage;
import com.deskcomm.support.Keys;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.UUID;

/**
 * Created by Jay Rathod on 10-02-2017.
 */
public class OutboundGroupMessage extends Message implements Sendable {


    public OutboundGroupMessage(String recipientGroupUuid, String body) {
        super(UUID.randomUUID().toString(), body, CurrentUser.getInstance().getUuid(), recipientGroupUuid);
    }

    @Override
    JSONObject toJSON() {
        return new JSONObject().put(Keys.MESSAGE_ID, messageId).put(Keys.MESSAGE_TO, toUuid)
                .put(Keys.MESSAGE_FROM, CurrentUser.getInstance().getUuid())
                .put(Keys.MESSAGE_BODY, body);
    }

    @Override
    public String toString() {
        return toJSON().toString();
    }

    @Override
    public boolean insertToTable() throws SQLException, ClassNotFoundException {
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
        OutboundWebsocketMessage websocketMessage = new OutboundWebsocketMessage("message/group", this.toJSON(), true);
        websocketMessage.send();
    }
}
