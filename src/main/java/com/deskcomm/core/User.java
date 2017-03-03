package com.deskcomm.core;

import com.deskcomm.core.messages.LocalPersonalMessage;
import com.deskcomm.db.DbConnection;
import com.deskcomm.db.Table;
import com.deskcomm.support.Keys;
import org.json.JSONObject;

import javax.websocket.Session;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jay Rathod on 15-01-2017.
 */
public class User implements Persistent {

    protected String uuidTrimmed;
    protected String fullName;
    String uuid;
    String firstName;
    String lastName;
    String email;
    String mobile;
    String imageUrl;
    String gender;
    private boolean isOnline = false;


    public User(String uuid, String firstName, String lastName, String email, String mobile, String imageUrl, String gender) {

        this.gender = gender;
        this.uuid = uuid;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.mobile = mobile;
        this.imageUrl = imageUrl;
    }


    public User(String uuid) {
        this.uuid = uuid;
    }


    public User(JSONObject user) {
        setUuid(user.getString(Keys.USER_UUID));
        setFirstName(user.getString(Keys.USER_FIRSTNAME));
        setLastName(user.getString(Keys.USER_LASTNAME));
        setEmail(user.getString(Keys.USER_EMAIL));
        setMobile(user.getString(Keys.JSON_MOBILE));
        setImageUrl(user.getString(Keys.USER_IMG_URL));
        setGender(user.getString(Keys.GENDER));
    }


    public User() {
    }

    public static ArrayList<User> getAllUsers() {
        try {
            ArrayList<User> arrayList = new ArrayList<>();
            Connection connection = DbConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT uuid,fname,lname,email,mobile,img_url,gender FROM users ");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                User user = new User();
                user.setUuid(resultSet.getString(1));
                user.setFirstName(resultSet.getString(2));
                user.setLastName(resultSet.getString(3));
                user.setEmail(resultSet.getString(4));
                user.setMobile(resultSet.getString(5));
                user.setImageUrl(resultSet.getString(6));
                user.setGender(resultSet.getString(7));
                arrayList.add(user);
            }
            return arrayList;
        } catch (SQLException e) {
            if (e.getMessage().equals(Keys.NO_SUCH_TABLE)) {
                Table.createUsersTable();
                return getAllUsers();
            } else e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean insertToTable() throws SQLException {
        try {
            Connection connection = DbConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement("INSERT INTO users(uuid,fname,lname,email,mobile,img_url,gender) VALUES(?,?,?,?,?,?,?)");
            statement.setString(1, uuid);
            statement.setString(2, firstName);
            statement.setString(3, lastName);
            statement.setString(4, email);
            statement.setString(5, mobile);
            statement.setString(6, imageUrl);
            statement.setString(7, gender);
            statement.executeUpdate();
            int updateCount = statement.getUpdateCount();
            statement.close();
            connection.close();
            return updateCount > 0;
        } catch (SQLException e) {
            if (e.getMessage().contains(Keys.NO_SUCH_TABLE)) {
                Table.createUsersTable();
                return insertToTable();
            } else {
                e.printStackTrace();
                throw e;
            }
        }
    }

    public JSONObject toJSON() {
        return new JSONObject().accumulate(Keys.USER_UUID, uuid)
                .accumulate(Keys.JSON_FNAME, firstName)
                .accumulate(Keys.JSON_LNAME, lastName)
                .accumulate(Keys.JSON_EMAIL, email)
                .accumulate(Keys.JSON_MOBILE, email)
                .accumulate(Keys.USER_IMG_URL, imageUrl)
                .accumulate(Keys.GENDER, gender);
    }

    @Override
    public String toString() {
        return toJSON().toString();
    }

    public String getUuid() {
        return uuid;
    }

    private void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getFirstName() {
        return firstName;
    }

    private void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    private void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    private void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    private void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    private void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getGender() {
        return gender;
    }

    private void setGender(String gender) {
        this.gender = gender;
    }

    public String getFullName() {
        if (firstName == null) this.fetchFromDb();
        return firstName + " " + lastName;
    }

    @Override
    public boolean fetchFromDb() {
        Connection connection = null;
        try {
            connection = DbConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT fname,lname,email,img_url,gender FROM users WHERE uuid=?");
            statement.setString(1, uuid);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                firstName = resultSet.getString(1);
                lastName = resultSet.getString(2);
                email = resultSet.getString(3);
                imageUrl = resultSet.getString(4);
                gender = resultSet.getString(5);
                statement.close();
                connection.close();
                return true;
            }
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
        return false;
    }

    public String getUuidTrimmed() {
        return uuid.substring(0, 8);
    }

    public boolean save() {
        try {
            Connection connection = DbConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement("INSERT INTO users(uuid,fname,lname,email,mobile,img_url,gender) VALUES (?,?,?,?,?,?,?)");
            statement.setString(1, uuid);
            statement.setString(2, firstName);
            statement.setString(3, lastName);
            statement.setString(4, email);
            statement.setString(5, mobile);
            statement.setString(6, imageUrl);
            statement.setString(7, gender);
            int i = statement.executeUpdate();
            statement.close();
            connection.close();
            return i > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }


    @Override
    public Updater getUpdater() {
        return new Updater();
    }


    public List<LocalPersonalMessage> getConversation(int limit) {
        return getConversation(0, limit);
    }

    public List<LocalPersonalMessage> getConversation(int skip, int limit) {

        List<LocalPersonalMessage> list = new ArrayList<>();
        Connection connection = null;
        try {
            connection = DbConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT _uuid,data,_to,_from,READ,server_timestamp FROM messages_personal WHERE (_from=? AND _to=?) OR (_from=? AND _to=?)  ORDER BY server_timestamp DESC LIMIT ?,?");
            statement.setString(1, uuid);
            statement.setString(2, CurrentUser.getInstance().getUuid());
            statement.setString(3, CurrentUser.getInstance().getUuid());
            statement.setString(4, uuid);
            statement.setInt(5, skip);
            statement.setInt(6, limit);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                LocalPersonalMessage message = new LocalPersonalMessage(resultSet.getString(1));
                message.setBody(resultSet.getString(2));
                message.setToUserUuid(resultSet.getString(3));
                message.setFromUserUuid(resultSet.getString(4));
                message.setToUserUuid(this.uuid);
                message.setUnread(resultSet.getInt(5) == 0);
                message.setTimeStamp(resultSet.getString(6));
                list.add(message);
            }
            statement.close();
            connection.close();
            return list;

        } catch (SQLException e) {
            e.printStackTrace();
            if (connection != null) try {
                connection.close();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            if (e.getMessage().contains(Keys.NO_SUCH_TABLE)) {
                Table.createUsersTable();
                Table.createMessagesPersonalTable();
            }

        }

        return list;
    }

    public boolean setAllMessagesAsRead() {
        try {
            Connection connection = DbConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement("UPDATE messages_personal SET read=1 WHERE _from=?");
            statement.setString(1, uuid);
            int i = statement.executeUpdate();
            statement.close();
            connection.close();
            return i > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean b) {
        isOnline = b;
    }

    public class Updater {

        public boolean updateFirstName(String value) throws SQLException, ClassNotFoundException {
            Connection connection = DbConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement("UPDATE users SET fname=? WHERE uuid=?");
            statement.setString(1, value);
            statement.setString(2, uuid);
            int i = statement.executeUpdate();
            statement.close();
            connection.close();
            return i > 0;
        }

        public boolean updateLastName(String value) throws SQLException, ClassNotFoundException {
            Connection connection = DbConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement("UPDATE users SET lname=? WHERE uuid=?");
            statement.setString(1, value);
            statement.setString(2, uuid);
            int i = statement.executeUpdate();
            statement.close();
            connection.close();
            return i > 0;
        }

        public boolean updateEmail(String value) throws SQLException, ClassNotFoundException {
            Connection connection = DbConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement("UPDATE users SET email=? WHERE uuid=?");
            statement.setString(1, value);
            statement.setString(2, uuid);
            int i = statement.executeUpdate();
            statement.close();
            connection.close();
            return i > 0;
        }

        public boolean updateMobile(String value) throws SQLException, ClassNotFoundException {
            Connection connection = DbConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement("UPDATE users SET mobile=? WHERE uuid=?");
            statement.setString(1, value);
            statement.setString(2, uuid);
            int i = statement.executeUpdate();
            statement.close();
            connection.close();
            return i > 0;
        }

        public boolean updateImageUrl(String value) throws SQLException, ClassNotFoundException {
            Connection connection = DbConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement("UPDATE users SET img_url=? WHERE uuid=?");
            statement.setString(1, value);
            statement.setString(2, uuid);
            int i = statement.executeUpdate();
            statement.close();
            connection.close();
            return i > 0;
        }


        public void updateWsSession(Session session) {
        }
    }
}
