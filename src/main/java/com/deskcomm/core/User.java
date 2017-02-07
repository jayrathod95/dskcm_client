package com.deskcomm.core;

import com.deskcomm.db.DbConnection;
import com.deskcomm.support.Keys;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by Jay Rathod on 15-01-2017.
 */
public class User implements Persistent {

    protected String gender;
    protected String uuidTrimmed;
    protected String fullName;
    String uuid;
    String firstName;
    String lastName;
    String email;
    String mobile;
    String imageUrl;
    String uid;

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

    @Override
    public boolean insertToTable() {
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
            e.printStackTrace();
            createTable();
            return insertToTable();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }

    }

    public JSONObject toJSON() {
        return new JSONObject().accumulate(Keys.USER_UID, uuid)
                .accumulate(Keys.JSON_FNAME, firstName)
                .accumulate(Keys.JSON_LNAME, lastName)
                .accumulate(Keys.JSON_EMAIL, email)
                .accumulate(Keys.JSON_MOBILE, email)
                .accumulate(Keys.USER_IMG_URL, imageUrl);
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
        return firstName + " " + lastName;
    }

    public String getUuidTrimmed() {
        return uuid.substring(0, 8);
    }


    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public void createTable() {
        Connection connection = null;
        PreparedStatement statement;
        try {
            connection = DbConnection.getConnection();
            statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS users(uuid TEXT CONSTRAINT users_pk PRIMARY KEY ,fname TEXT NOT NULL ,lname TEXT NOT NULL ,email TEXT NOT NULL ,mobile TEXT,img_url TEXT,gender TEXT NOT NULL, created TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");
            statement.execute();
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
    public Updater getUpdater() {
        return new Updater();
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


    }
}