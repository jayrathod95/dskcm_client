package com.deskcomm.core;

import com.deskcomm.db.DbConnection;
import com.deskcomm.support.Keys;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Logger;

/**
 * Created by Jay Rathod on 05-02-2017.
 */
public class Event implements Persistent {
    private final Logger logger = Logger.getLogger(this.getClass().getName());
    private String clientTimeStamp;
    private String uuid;
    private String eventName;
    private String eventTimeStamp;
    private String description;
    private String eventIconUrl;
    private User[] organisers;
    private String venue;
    private String serverTimeStamp;

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

    public Event(String uuid) {
        this.uuid = uuid;
    }

    public Event() {
    }

    public Event(JSONObject jsonObject) {
        this.eventName = jsonObject.getString("eventName");
        this.eventTimeStamp = jsonObject.getString("eventTimeStamp");
        this.description = jsonObject.getString("eventDescription");
        this.eventIconUrl = jsonObject.getString("eventIconUrl");
        this.venue = jsonObject.getString("eventVenue");
        this.serverTimeStamp = jsonObject.getString("serverTimeStamp");

        JSONArray organisers1 = jsonObject.getJSONArray("organisers");
        organisers = new User[organisers1.length()];
        for (int i = 0; i < organisers1.length(); i++) {
            String userId = organisers1.getString(i);
            this.organisers[i] = new User(userId);
        }
    }

    @Override
    public boolean insertToTable() throws ClassNotFoundException, SQLException {
        try {
            Connection connection = DbConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement("INSERT INTO events(uuid,name,description,icon_url,venue,event_time_stamp,server_timestamp)\n" +
                    "VALUES(?,?,?,?,?,?,?) ");
            statement.setString(1, uuid);
            statement.setString(2, eventName);
            statement.setString(3, description);
            statement.setString(4, eventIconUrl);
            statement.setString(5, venue);
            statement.setString(6, eventTimeStamp);
            statement.setString(7, serverTimeStamp);
            statement.executeUpdate();
            int updateCount = statement.getUpdateCount();
            statement.close();
            connection.close();
            return updateCount > 0;
        } catch (SQLException e) {
            if (e.getMessage().contains(Keys.NO_SUCH_TABLE)) {
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
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
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

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventDate() {
        return eventTimeStamp;
    }

    public void setEventDate(String eventTimeStamp) {
        this.eventTimeStamp = eventTimeStamp;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User[] getOrganisers() {
        return organisers;
    }

    public void setOrganisers(User... organisers) {
        this.organisers = organisers;
    }

    public String getEventIconUrl() {
        return eventIconUrl;
    }

    public void setEventIconUrl(String eventIconUrl) {
        this.eventIconUrl = eventIconUrl;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
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

        public boolean updateOrganisers(String... userUuids) throws SQLException, ClassNotFoundException {


            return false;
        }

    }

}
