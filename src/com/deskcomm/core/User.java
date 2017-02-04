package com.deskcomm.core;

import com.deskcomm.support.Keys;
import org.json.JSONObject;

/**
 * Created by Jay Rathod on 15-01-2017.
 */
public class User {
    protected String gender;
    protected String uuidTrimmed;
    protected String fullName;
    String uuid;
    String firstname;
    String lastname;
    String email;
    String mobile;
    String image_url;
    String uid;

    public User(String uuid) {
        this.uuid = uuid;
    }

    public User(JSONObject user) {
        setUuid(user.getString(Keys.USER_UUID));
        setFirstname(user.getString(Keys.USER_FIRSTNAME));
        setLastname(user.getString(Keys.USER_LASTNAME));
        setEmail(user.getString(Keys.USER_EMAIL));
        setMobile(user.getString(Keys.JSON_MOBILE));
        setImage_url(user.getString(Keys.USER_IMG_URL));
        setGender(user.getString(Keys.GENDER));
    }


    public User() {
    }

    public JSONObject toJSON() {
        return new JSONObject().accumulate(Keys.USER_UID, uuid)
                .accumulate(Keys.JSON_FNAME, firstname)
                .accumulate(Keys.JSON_LNAME, lastname)
                .accumulate(Keys.JSON_EMAIL, email)
                .accumulate(Keys.JSON_MOBILE, email)
                .accumulate(Keys.USER_IMG_URL, image_url)
                ;
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

    public String getFirstname() {
        return firstname;
    }

    private void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    private void setLastname(String lastname) {
        this.lastname = lastname;
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


    public String getImage_url() {
        return image_url;
    }

    private void setImage_url(String image_url) {
        this.image_url = image_url;
    }


    public String getGender() {
        return gender;
    }

    private void setGender(String gender) {
        this.gender = gender;
    }

    public String getFullName() {
        return firstname + " " + lastname;
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
}
