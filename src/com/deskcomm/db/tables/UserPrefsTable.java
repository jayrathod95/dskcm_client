package com.deskcomm.db.tables;

import com.deskcomm.db.DbConnection;
import com.deskcomm.support.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Jay Rathod on 16-01-2017.
 */
public class UserPrefsTable {
    final static public String TABLE_NAME = "user_preferences";
    final static public String COL_SESSION_ID = "session_id";
    final static public String COL_SESSION_ID1 = "session_id1";
    final static public String COL_LOGIN_STATUS = "login_status";
    final static public String COL_UUID = "_uuid";
    final static public String COL_FNAME = "fname";
    final static public String COL_LNAME = "lname";
    final static public String COL_EMAIL = "email";
    final static public String COL_MOBILE = "mobile";
    final static public String COL_IMG_URL = "img_url";
    final static public String COL_CREATED = "created";
    private String sessionId = null;
    private String sessionId1 = null;
    private String loginStatus = null;
    private String _uuid = null;
    private String userName = null;
    private String firstName = null;
    private String lastName = null;
    private String email = null;
    private String mobile = null;
    private String img_url = null;

    private String created = null;


    public UserPrefsTable() throws SQLException, ClassNotFoundException {
        Connection connection = DbConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS user_preferences(\n" +
                "  session_id TEXT NOT NULL,\n" +
                "  session_id1 TEXT NOT NULL ,\n" +
                "  login_status TEXT NOT NULL ,\n" +
                "  _uuid TEXT NOT NULL ,\n" +
                "  fname TEXT NOT NULL ,\n" +
                "  lname TEXT NOT NULL ,\n" +
                "  email TEXT NOT NULL ,\n" +
                "  mobile TEXT NOT NULL,\n" +
                "  img_url TEXT NOT NULL ,\n" +
                "  created TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL \n" +
                ")");
        preparedStatement.executeUpdate();
    }

    private boolean fetchData() throws SQLException, ClassNotFoundException {
        PreparedStatement statement = DbConnection.getConnection().prepareStatement("SELECT session_id,session_id1,login_status,_uuid,fname,lname,email,mobile,img_url,created FROM user_preferences LIMIT 1");
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
            return true;
        }
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

    public String getUserName() {
        return userName;
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

    public boolean updateProperty(String columnName, String newData) throws SQLException, ClassNotFoundException {
        PreparedStatement statement = DbConnection.getConnection().prepareStatement("UPDATE " + TABLE_NAME + " SET " + columnName + "=" + newData);
        int result = statement.executeUpdate();
        Logger.print(result + "");
        return result > 0;
    }
}
