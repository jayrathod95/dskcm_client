package com.deskcomm.networking;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jay Rathod on 27-01-2017.
 */
public class ChangePasswordRequest extends Request {

    final static private String path = "/user/change_password";
    static private String mOldPassword;
    static private String mNewPassword;
    static private String mUuid;


    public ChangePasswordRequest(String oldPassword, String newPassword, String uuid, SuccessListener successListener, ErrorListener errorListener) {
        super(path, MethodType.POST);
        mOldPassword = oldPassword;
        mNewPassword = newPassword;
        mUuid = uuid;
    }

    @Override
    public Map<String, String> getParams() {
        Map map = new HashMap();
        map.put("old_password", mOldPassword);
        map.put("new_password", mNewPassword);
        map.put("uuid", mUuid);
        return map;
    }
}
