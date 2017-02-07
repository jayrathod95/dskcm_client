package com.deskcomm.db.tables;

import com.deskcomm.db.DbConnection;
import com.deskcomm.support.Keys;
import com.deskcomm.support.L;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

/**
 * Created by Jay Rathod on 16-01-2017.
 */
public class UserPrefsTable {
    private final static String TABLE_NAME = "user_preferences";
    private final static String COL_SESSION_ID = "session_id";
    private final static String COL_SESSION_ID1 = "session_id1";
    private final static String COL_LOGIN_STATUS = "login_status";
    private final static String COL_UUID = "_uuid";
    private final static String COL_FNAME = "fname";
    private final static String COL_LNAME = "lname";
    private final static String COL_EMAIL = "email";
    private final static String COL_MOBILE = "mobile";
    private final static String COL_IMG_URL = "img_url";
    private final static String COL_CREATED = "created";
    private static final String COL_GENDER = "gender";
    private String sessionId = null;
    private String sessionId1 = null;
    private String loginStatus = null;
    private String _uuid = null;
    private String firstName = null;
    private String lastName = null;
    private String email = null;
    private String mobile = null;
    private String img_url = null;

    private String created = null;
    private String gender = null;

    public UserPrefsTable(String sessionId, String sessionId1, boolean loginStatus, String _uuid, String firstName,
                          String lastName, String gender, String email, @Nullable String mobile, @Nullable String img_url) throws SQLException, ClassNotFoundException {
        createTableIfNotExits();
        this.sessionId = sessionId;
        this.sessionId1 = sessionId1;
        this.loginStatus = loginStatus ? "LOGGED_IN" : "NOT_LOGGED_IN";
        this._uuid = _uuid;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.mobile = mobile;
        this.img_url = img_url;
        this.gender = gender;
    }

    public UserPrefsTable(JSONObject jsonObject) throws SQLException, ClassNotFoundException {
        createTableIfNotExits();
        sessionId = jsonObject.getString(Keys.JSON.SESSION_ID);
        sessionId1 = UUID.randomUUID().toString();
        loginStatus = "LOGGED_IN";
        _uuid = jsonObject.getString(Keys.USER_UUID);
        firstName = jsonObject.getString(Keys.USER_FIRSTNAME);
        lastName = jsonObject.getString(Keys.USER_LASTNAME);
        email = jsonObject.getString(Keys.USER_EMAIL);
        mobile = jsonObject.getString(Keys.USER_MOBILE);
        img_url = jsonObject.getString(Keys.USER_IMG_URL);
        gender = jsonObject.getString(Keys.GENDER);


    }

    public UserPrefsTable() throws SQLException, ClassNotFoundException {
        createTableIfNotExits();
    }

    public static void createTableIfNotExits() throws SQLException, ClassNotFoundException {
        Connection connection = DbConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS user_preferences(\n" +
                "  session_id TEXT NOT NULL,\n" +
                "  session_id1 TEXT NOT NULL ,\n" +
                "  login_status TEXT NOT NULL DEFAULT 'NOT_LOGGED_IN',\n" +
                "  _uuid TEXT NOT NULL ,\n" +
                "  fname TEXT NOT NULL ,\n" +
                "  lname TEXT NOT NULL ,\n" +
                "  email TEXT NOT NULL ,\n" +
                "  mobile TEXT,\n" +
                "  gender TEXT,\n" +
                "  img_url TEXT ,\n" +
                "  created TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL \n" +
                ")");
        preparedStatement.executeUpdate();
        preparedStatement.close();
        connection.close();

    }

    public boolean update() throws SQLException, ClassNotFoundException {
        return insert();
    }

    private boolean insert() throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO user_preferences(session_id,session_id1,login_status,_uuid,fname,lname,email,mobile,img_url,created,gender)" +
                "VALUES(?,?,?,?,?,?,?,?,?,CURRENT_TIMESTAMP,?) ";
        Connection connection = DbConnection.getConnection();
        connection.setAutoCommit(false);
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, sessionId);
        L.println(UUID.randomUUID().toString());
        statement.setString(2, sessionId1);
        statement.setString(3, loginStatus);
        statement.setString(4, _uuid);
        statement.setString(5, firstName);
        statement.setString(6, lastName);
        statement.setString(7, email);
        statement.setString(8, mobile);
        statement.setString(9, img_url);
        statement.setString(10, gender);


        int i = statement.executeUpdate();
        connection.commit();
        statement.close();
        connection.close();
        return i > 0;
    }

    public boolean fetchData() throws SQLException, ClassNotFoundException {
        Connection connection = DbConnection.getConnection();
        PreparedStatement statement = connection.prepareStatement("SELECT session_id,session_id1,login_status,_uuid,fname,lname,email,mobile,img_url,created FROM user_preferences LIMIT 1");
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            sessionId = resultSet.getString(COL_SESSION_ID);
            sessionId1 = resultSet.getString(COL_SESSION_ID1);
            loginStatus = resultSet.getString(COL_LOGIN_STATUS);
            _uuid = resultSet.getString(COL_UUID);
            firstName = resultSet.getString(COL_FNAME);
            lastName = resultSet.getString(COL_LNAME);
            email = resultSet.getString(COL_EMAIL);
            mobile = resultSet.getString(COL_MOBILE);
            img_url = resultSet.getString(COL_IMG_URL);
            created = resultSet.getString(COL_CREATED);
            gender = resultSet.getString(COL_GENDER);
            resultSet.close();
            statement.close();
            connection.close();
            return true;
        }
        resultSet.close();
        statement.close();
        connection.close();

        return false;
    }

/*
    private boolean updateData() throws SQLException, ClassNotFoundException {
        PreparedStatement statement=DbConnection.getConnection().prepareStatement("UPDATE " + TABLE_NAME +
                " SET "+COL_SESSION_ID+" = "+sessionId+
                " SET "+COL_SESSION_ID1+" = "+sessionId1+
                " SET "+COL_LOGIN_STATUS+" = "+loginStatus+
                " SET "+COL_UUID+" = "+_uuid+
                " SET "+COL_FNAME+" = "+firstName+
                " SET "+COL_LNAME+" = "+lastName+
                " SET "+COL_EMAIL+" = "+email+
                " SET "+COL_MOBILE+" = "+mobile+
                " SET "+COL_IMG_URL+" = "+img_url+
                " SET "+COL_IMG_URL+" = "+img_url+

        );
    }
        */


    public String getSessionId() {

        return sessionId;
    }

    public boolean updateSessionId(String sessionId) throws SQLException, ClassNotFoundException {
        // this.sessionId = sessionId;
        return updateProperty(COL_SESSION_ID, sessionId);

    }

    public String getSessionId1() {
        return sessionId1;
    }

    public boolean updateSessionId1(String sessionId1) throws SQLException, ClassNotFoundException {
        //this.sessionId1 = sessionId1;
        return updateProperty(COL_SESSION_ID1, sessionId1);

    }

    public String getLoginStatus() {
        return loginStatus;
    }

    public boolean updateLoginStatus(String loginStatus) throws SQLException, ClassNotFoundException {
        //this.loginStatus = loginStatus;
        return updateProperty(COL_LOGIN_STATUS, loginStatus);

    }

    public String getUUID() {
        return _uuid;
    }

    public boolean updateUUID(String _uuid) throws SQLException, ClassNotFoundException {
        //this._uuid = _uuid;
        return updateProperty(COL_UUID, _uuid);


    }


    public boolean updateUserName(String userName) throws SQLException, ClassNotFoundException {
        //  this.userName = userName;
        return updateProperty(COL_FNAME, userName);

    }

    public String getFirstName() {
        return firstName;
    }

    public boolean updateFirstName(String firstName) throws SQLException, ClassNotFoundException {
        //this.firstName = firstName;
        return updateProperty(COL_FNAME, firstName);


    }

    public String getLastName() {
        return lastName;
    }

    public boolean updateLastName(String lastName) throws SQLException, ClassNotFoundException {
        //this.lastName = lastName;
        return updateProperty(COL_LNAME, lastName);


    }

    public String getEmail() {
        return email;
    }

    public boolean updateEmail(String email) throws SQLException, ClassNotFoundException {
        //this.email = email;
        return updateProperty(COL_EMAIL, email);

    }

    public String getMobile() {
        return mobile;
    }

    public boolean updateMobile(String mobile) throws SQLException, ClassNotFoundException {

        //this.mobile = mobile;
        return updateProperty(COL_MOBILE, mobile);

    }

    public String getCreated() {
        return created;
    }

    public boolean updateCreated(String created) throws SQLException, ClassNotFoundException {

        //this.created = created;
        return updateProperty(COL_CREATED, created);
    }

    public String getImg_url() {
        return img_url;
    }

    public boolean updateImg_url(String img_url) throws SQLException, ClassNotFoundException {
        //   this.img_url = img_url;
        return updateProperty(COL_IMG_URL, img_url);
    }

    public String getGender() {
        return gender;
    }

    private boolean updateProperty(String columnName, String newData) throws SQLException, ClassNotFoundException {
        Connection connection = DbConnection.getConnection();
        PreparedStatement statement = connection.prepareStatement("UPDATE " + TABLE_NAME + " SET " + columnName + "=?");
        statement.setString(1, newData);
        int result = statement.executeUpdate();
        L.println(result + "");
        statement.close();
        // connection.close();

        return result > 0;
    }

    public boolean clearRecord() throws SQLException, ClassNotFoundException {
        Connection connection = DbConnection.getConnection();
        PreparedStatement statement = connection.prepareStatement("DELETE FROM " + TABLE_NAME);
        boolean execute = statement.execute();
        statement.close();
        connection.close();
        return execute;
    }
}
