package com.deskcomm.core;

import com.deskcomm.db.DbConnection;
import com.deskcomm.db.tables.UserPrefsTable;
import com.deskcomm.networking.LoginRequest;
import com.deskcomm.support.Keys;
import com.deskcomm.support.Response;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.deskcomm.support.Keys.*;

/**
 * Created by Jay Rathod on 17-01-2017.
 */
public class CurrentUser extends User {

    private static CurrentUser mCurrentUser;
    private boolean isLoggedIn;
    private String sessionId;
    private String created;
    private String loggedInOn;
    private Object identity;

    private CurrentUser() throws SQLException {
        UserPrefsTable.createTableIfNotExits();
    }

    private CurrentUser(JSONObject object) {
        firstName = object.getString(JSON_FNAME);
        lastName = object.getString(JSON_LNAME);
        gender = object.getString(GENDER);
        imageUrl = object.getString(USER_IMG_URL);
        created = object.getString(USER_CREATED);
        mobile = object.getString(JSON_MOBILE);
        sessionId = object.getString(SESSION_ID);
        uuid = object.getString(USER_UUID);
        email = object.getString(USER_EMAIL);
    }

    public CurrentUser(String uuid, String firstName, String lastName, String email, String mobile, String imageUrl, String gender) {
        super(uuid, firstName, lastName, email, mobile, imageUrl, gender);
    }


    public static CurrentUser getInstance() {
        if (mCurrentUser == null || !mCurrentUser.isLoggedIn) {
            try {
                mCurrentUser = new CurrentUser();
                Connection connection = DbConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement("SELECT session_id,_uuid,fname,lname,email,mobile,gender,img_url,created FROM user_preferences LIMIT 1");
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    mCurrentUser.isLoggedIn = true;
                    mCurrentUser.sessionId = resultSet.getString(SESSION_ID);
                    mCurrentUser.uuid = resultSet.getString(USER_UUID);
                    mCurrentUser.firstName = resultSet.getString(USER_FIRSTNAME);
                    mCurrentUser.lastName = resultSet.getString(USER_LASTNAME);
                    mCurrentUser.email = resultSet.getString(USER_EMAIL);
                    mCurrentUser.mobile = resultSet.getString(USER_MOBILE);
                    mCurrentUser.imageUrl = resultSet.getString(USER_IMG_URL);
                    mCurrentUser.loggedInOn = resultSet.getString(USER_CREATED);
                    mCurrentUser.gender = resultSet.getString(GENDER);
                } else {
                    mCurrentUser.isLoggedIn = false;
                }
                resultSet.close();
                statement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return mCurrentUser;
    }

    public boolean logout() {
        try {
            UserPrefsTable table = new UserPrefsTable();
            mCurrentUser = null;
            return table.clearRecord();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    public Response<JSONObject> login(String email, String password) {
        //     LoginRequest request=new LoginRequest();
        Map<String, String> map = new HashMap<>();
        map.put(PARAM_USERNAME, email);
        map.put(PARAM_PASSWORD, password);
        LoginRequest request = new LoginRequest(email, password);
        return new Response<JSONObject>(request.perform().readEntity(String.class));
    }


    //Saves data to Local Database
    public boolean save(String response) throws SQLException, ClassNotFoundException {
        JSONObject object = new JSONObject(response);
        UserPrefsTable table = new UserPrefsTable(object);
        return table.update();
    }

    //Saves data to Local Database
    public boolean save(JSONObject object) throws SQLException, ClassNotFoundException {

        CurrentUser currentUser = new CurrentUser(object);

        String sql = "INSERT INTO user_preferences(session_id,session_id1,login_status,_uuid,fname,lname,email,mobile,img_url,created,gender)" +
                "VALUES(?,?,?,?,?,?,?,?,?,CURRENT_TIMESTAMP,?) ";

        Connection connection = DbConnection.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, currentUser.getSessionId());
        statement.setString(2, UUID.randomUUID().toString());
        statement.setString(3, Keys.LOGGED_IN);
        statement.setString(4, currentUser.getUuid());
        statement.setString(5, currentUser.getFirstName());
        statement.setString(6, currentUser.getLastName());
        statement.setString(7, currentUser.getEmail());
        statement.setString(8, currentUser.getMobile());
        statement.setString(9, currentUser.getImageUrl());
        statement.setString(10, currentUser.getGender());
        int i = statement.executeUpdate();
        statement.close();
        connection.close();
        return i > 0;
    }


    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public String getSessionId() {
        return sessionId;
    }

    public String getLoggedInOn() {
        return loggedInOn;
    }

    public Identity getIdentity() {
        return new Identity(uuid, sessionId);
    }
}
