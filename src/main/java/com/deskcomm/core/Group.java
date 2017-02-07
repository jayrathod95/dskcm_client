package com.deskcomm.core;

import com.deskcomm.core.messages.Message;
import com.deskcomm.support.Keys;
import org.json.JSONObject;

/**
 * Created by Jay Rathod on 05-02-2017.
 */
public class Group {
    String uuid;
    String groupName;
    String iconUrl;
    User[] members;

    public Group(String uuid) {
        this.uuid = uuid;
    }

    public Group(JSONObject jsonObject) {
        setUuid(jsonObject.getString(Keys.GROUP_ID));
        setUuid(jsonObject.getString(Keys.GROUP_NAME));
        setUuid(jsonObject.getString(Keys.GROUP_ICON_URL));
        setUuid(jsonObject.getString(Keys.GROUP_MEMBER_IDS));

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
}
