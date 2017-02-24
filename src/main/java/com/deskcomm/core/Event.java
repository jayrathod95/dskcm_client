package com.deskcomm.core;

import com.deskcomm.db.DbConnection;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.UUID;

import static com.deskcomm.support.Keys.*;

/**
 * Created by Jay Rathod on 05-02-2017.
 */
public class Event implements Persistent {
    private String uuid;
    private String title;
    private String starts;
    private String ends;
    private String description;
    private String imageUrl;
    private String venue;
    private String serverTimeStamp;
    private String clientTimeStamp;

    /*
    public Event(@NotNull String uuid, @NotNull String eventName, @NotNull String eventTimeStamp, @NotNull String description, @NotNull String venue, @NotNull String serverTimeStamp, @Nullable String eventIconUrl, User... organisers) {
        this.uuid = uuid;
        this.eventName = eventName;
        this.eventTimeStamp = eventTimeStamp;
        this.description = description;
        this.eventIconUrl = eventIconUrl;
        this.organisers = organisers;
        this.venue = venue;
        this.serverTimeStamp = serverTimeStamp;
    }
*/
    public Event(String uuid) {
        this.uuid = uuid;
    }

    public Event() {
    }

    public Event(JSONObject jsonObject) throws ParseException {
        this.uuid = jsonObject.getString(EVENT_ID);
        this.title = jsonObject.getString(EVENT_TITLE);
        this.starts = jsonObject.getString(EVENT_STARTS);
        this.ends = jsonObject.getString(EVENT_ENDS);
        this.description = jsonObject.getString(EVENT_DESC);
        if (jsonObject.has(EVENT_IMAGE_URL))
            this.imageUrl = jsonObject.getString(EVENT_IMAGE_URL);
        this.venue = jsonObject.getString(EVENT_VENUE);
        if (jsonObject.has(SERVER_TIMESTAMP))
            this.serverTimeStamp = jsonObject.getString(SERVER_TIMESTAMP);
    }

    public Event(String uuid, String title, String starts, String ends, String venue, String description, String imageUrl) {

        this.uuid = uuid;
        this.title = title;
        this.starts = starts;
        this.ends = ends;
        this.venue = venue;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    @Override
    public boolean insertToTable() throws ClassNotFoundException, SQLException {
        try {
            Connection connection = DbConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement("INSERT INTO events(_uuid,title,starts,ends,venue,description,image_url,server_timestamp)\n" +
                    "VALUES(?,?,?,?,?,?,?,?) ");
            statement.setString(1, uuid);
            statement.setString(2, title);
            statement.setString(3, starts);
            statement.setString(4, ends);
            statement.setString(5, venue);
            statement.setString(6, description);
            statement.setString(7, imageUrl);
            statement.setString(8, serverTimeStamp);
            statement.executeUpdate();
            int updateCount = statement.getUpdateCount();
            statement.close();
            connection.close();
            return updateCount > 0;
        } catch (SQLException e) {
            if (e.getMessage().contains(NO_SUCH_TABLE)) {
                createTable();
                return insertToTable();
            } else throw e;
        }
    }

    public void createTable() {
        Connection connection = null;
        PreparedStatement statement;

        try {
            connection = DbConnection.getConnection();
            statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS events(\n" +
                    "  uuid TEXT CONSTRAINT events_pk PRIMARY KEY ,name TEXT NOT NULL,description TEXT NOT NULL,icon_url TEXT,venue TEXT NOT NULL ,event_time_stamp TIMESTAMP NOT NULL ,server_timestamp TIMESTAMP NOT NULL ,created DEFAULT CURRENT_TIMESTAMP\n" +
                    ")");
            boolean i = statement.execute();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            if (connection != null) try {
                connection.close();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
    }

    @Override
    public Event.Updater getUpdater() {
        return new Event.Updater();
    }

    @Override
    public boolean fetchFromDb() {
        return false;
    }

    public void setServerTimeStamp(String serverTimeStamp) {
        this.serverTimeStamp = serverTimeStamp;
    }

    public String getClientTimeStamp() {
        return clientTimeStamp;
    }

    public void setClientTimeStamp(String clientTimeStamp) {
        this.clientTimeStamp = clientTimeStamp;
    }

    public String getUuid() {
        return uuid;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public JSONObject toJSON() {
        return new JSONObject()
                .put(EVENT_ID, uuid)
                .put(EVENT_TITLE, title)
                .put(EVENT_STARTS, starts)
                .put(EVENT_ENDS, ends)
                .put(EVENT_VENUE, venue)
                .put(EVENT_DESC, description)
                .put(EVENT_IMAGE_URL, imageUrl)
                .put(SERVER_TIMESTAMP, serverTimeStamp)
                ;
    }

    @Override
    public String toString() {
        return toJSON().toString();
    }

    public static class Builder {
        private String title;
        private Timestamp starts;
        private Timestamp ends;
        private String venue;
        private String description;
        private String imageUrl;

        public void setTitle(String title) {
            this.title = title;
        }

        public void setStarts(Timestamp starts) {
            this.starts = starts;
        }

        public void setEnds(Timestamp ends) {
            this.ends = ends;
        }

        public void setVenue(String venue) {
            this.venue = venue;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public Event build() {
            return new Event(UUID.randomUUID().toString(), title, starts.toString(), ends.toString(), venue, description, "");
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }
    }

    public class Updater implements com.deskcomm.core.Updater {
        private Updater() {
        }

        public boolean updateVenue(String value) throws SQLException, ClassNotFoundException {
            Connection connection = DbConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement("UPDATE events SET venue=? WHERE uuid=?");
            statement.setString(1, value);
            statement.setString(2, uuid);
            statement.executeUpdate();
            int i = statement.getUpdateCount();
            statement.close();
            connection.close();
            return i > 0;
        }

        public boolean updateEventName(String value) throws SQLException, ClassNotFoundException {
            Connection connection = DbConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement("UPDATE events SET name=? WHERE uuid=?");
            statement.setString(1, value);
            statement.setString(2, uuid);
            statement.executeUpdate();
            int i = statement.getUpdateCount();
            statement.close();
            connection.close();
            return i > 0;
        }

        public boolean updateEventTimeStamp(String value) throws SQLException, ClassNotFoundException {
            Connection connection = DbConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement("UPDATE events SET event_time_stamp=? WHERE uuid=?");
            statement.setString(1, value);
            statement.setString(2, uuid);
            statement.executeUpdate();
            int i = statement.getUpdateCount();
            statement.close();
            connection.close();
            return i > 0;
        }

        public boolean updateDescription(String value) throws SQLException, ClassNotFoundException {
            Connection connection = DbConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement("UPDATE events SET description=? WHERE uuid=?");
            statement.setString(1, value);
            statement.setString(2, uuid);
            statement.executeUpdate();
            int i = statement.getUpdateCount();
            statement.close();
            connection.close();
            return i > 0;
        }

        public boolean updateOrganisers(String... userUuids) {
            return false;
        }

    }
}
