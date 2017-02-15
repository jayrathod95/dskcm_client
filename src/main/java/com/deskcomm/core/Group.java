package com.deskcomm.core;

import com.deskcomm.core.messages.Message;
import com.deskcomm.db.DbConnection;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static com.deskcomm.support.Keys.*;

/**
 * Created by Jay Rathod on 05-02-2017.
 */
public class Group implements Persistent {
    private String uuid;
    private String groupName;
    private String iconUrl;
    private User createdBy;
    private User[] members;
    private String serverTimeStamp;

    public Group(String uuid, String groupName, String iconUrl, User createdBy, String serverTimeStamp, User... members) {
        this.uuid = uuid;
        this.groupName = groupName;
        this.iconUrl = iconUrl;
        this.createdBy = createdBy;
        this.members = members;
        this.serverTimeStamp = serverTimeStamp;
    }


    public Group(String uuid) {
        this.uuid = uuid;
    }

    public Group(JSONObject jsonObject) {
        setUuid(jsonObject.getString(GROUP_ID));
        setUuid(jsonObject.getString(GROUP_NAME));
        setUuid(jsonObject.getString(GROUP_ICON_URL));
        setUuid(jsonObject.getString(GROUP_MEMBER_IDS));
        setServerTimeStamp(jsonObject.getString(SERVER_TIMESTAMP));
    }

    @Override
    public String toString() {
        return toJSON().toString();
    }

    public JSONObject toJSON() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(GROUP_NAME, groupName);
        jsonObject.put(GROUP_ID, uuid);
        jsonObject.put(GROUP_ICON_URL, iconUrl);
        jsonObject.put(GROUP_CREATED_BY, createdBy);
        JSONArray memberIds = new JSONArray();
        for (User user :
                members) {
            memberIds.put(user.getUuid());
        }
        jsonObject.put(GROUP_MEMBER_IDS, memberIds);
        if (serverTimeStamp != null) jsonObject.put(SERVER_TIMESTAMP, serverTimeStamp);
        return jsonObject;
    }

    public Message[] getMessages() {
        return null;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public User[] getMembers() {
        return members;
    }

    public void setMembers(User[] members) {
        this.members = members;
    }

    @Override
    public boolean insertToTable() throws SQLException, ClassNotFoundException {
        try {
            Connection connection = DbConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement("INSERT INTO groups(_uuid,name,icon_url,created_by,server_timestamp) VALUES (?,?,?,?,?)");
            statement.setString(1, uuid);
            statement.setString(2, groupName);
            statement.setString(3, iconUrl);
            statement.setString(4, createdBy.getUuid());
            statement.setString(5, serverTimeStamp);
            statement.executeUpdate();
            int updateCount = statement.getUpdateCount();
            statement.close();
            connection.close();
            return updateCount > 0;
        } catch (SQLException e) {
            if (e.getMessage().contains("no such table")) {
                createTable();
                return insertToTable();
            } else throw e;
        }
    }

    public void createTable() {
        Connection connection = null;
        PreparedStatement statement;

        String sql = "CREATE TABLE groups(\n" +
                "_uuid TEXT,\n" +
                "name TEXT NOT NULL ,\n" +
                "icon_url TEXT,\n" +
                "created_by TEXT NOT NULL,\n" +
                "server_timestamp TIMESTAMP NOT NULL ,\n" +
                "created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,\n" +
                "CONSTRAINT pk PRIMARY KEY(_uuid)\n" +
                ")";
        try {
            connection = DbConnection.getConnection();
            statement = connection.prepareStatement(sql);
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
    public Updater getUpdater() {
        return new Updater();
    }

    @Override
    public boolean fetchFromDb() {
        return false;
    }

    public void setServerTimeStamp(String serverTimeStamp) {
        this.serverTimeStamp = serverTimeStamp;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }


    public class Updater {

        public boolean updateGroupName(String newValue) throws SQLException, ClassNotFoundException {
            try {
                Connection connection = DbConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement("UPDATE groups SET name=? WHERE _uuid=?");
                statement.setString(1, newValue);
                statement.setString(2, uuid);
                statement.executeUpdate();
                int updateCount = statement.getUpdateCount();
                statement.close();
                connection.close();
                return updateCount > 0;
            } catch (SQLException e) {
                if (e.getMessage().contains("no such table")) {
                    createTable();
                    return updateGroupName(newValue);
                } else throw e;
            }


        }

        public boolean updateIconUrl(String newValue) throws SQLException, ClassNotFoundException {
            try {

                Connection connection = DbConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement("UPDATE groups SET icon_url=? WHERE _uuid=?");
                statement.setString(1, newValue);
                statement.setString(2, uuid);
                statement.executeUpdate();
                int updateCount = statement.getUpdateCount();
                statement.close();
                connection.close();
                return updateCount > 0;
            } catch (SQLException e) {
                if (e.getMessage().contains("no such table")) {
                    createTable();
                    return updateIconUrl(newValue);
                } else throw e;
            }

        }


        public boolean addMember(String newUserId) throws SQLException, ClassNotFoundException {
            try {
                Connection connection = DbConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement("INSERT INTO group_members(group_id,user_id) VALUES (?,?)");
                statement.setString(1, uuid);
                statement.setString(2, newUserId);
                statement.executeUpdate();
                int updateCount = statement.getUpdateCount();
                statement.close();
                connection.close();
                return updateCount > 0;
            } catch (SQLException e) {
                if (e.getMessage().contains("no such table")) {
                    createGroupMembersTable();
                    return addMember(newUserId);
                } else throw e;
            }
        }

        public boolean deleteMember(String userId) throws SQLException, ClassNotFoundException {
            try {
                Connection connection = DbConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement("DELETE FROM group_members WHERE user_id=? AND group_id=?");
                statement.setString(1, userId);
                statement.setString(2, uuid);
                statement.executeUpdate();
                int updateCount = statement.getUpdateCount();
                statement.close();
                connection.close();
                return updateCount > 0;
            } catch (SQLException e) {
                if (e.getMessage().contains("no such table")) {
                    createGroupMembersTable();
                    return deleteMember(userId);
                } else throw e;
            }

        }

        public void createGroupMembersTable() {
            Connection connection = null;
            PreparedStatement statement;

            String sql = "CREATE TABLE IF NOT EXISTS group_members(\n" +
                    "\tgroup_id TEXT,\n" +
                    "\tuser_id TEXT,\n" +
                    "\tCONSTRAINT pk PRIMARY KEY(group_id,user_id),\n" +
                    "\tFOREIGN KEY (group_id) REFERENCES groups(_uuid),\n" +
                    "\tFOREIGN KEY (user_id) REFERENCES users(_uuid)\n" +
                    ")";
            try {
                connection = DbConnection.getConnection();
                statement = connection.prepareStatement(sql);
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


    }


}
