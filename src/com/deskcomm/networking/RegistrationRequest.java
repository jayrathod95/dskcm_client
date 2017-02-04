package com.deskcomm.networking;

import com.deskcomm.support.Keys;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jay Rathod on 29-01-2017.
 */
public class RegistrationRequest extends Request {
    private static String path = "/user/register";
    private static int methodType = MethodType.PUT;
    private String firstName;
    private String lastName;
    private String email;
    private String mobile;
    private String eid;
    private String password;

    public RegistrationRequest(@NotNull String firstName, @NotNull String lastName, @NotNull String Email, @Nullable String mobile, @NotNull String eid, @NotNull String password, @NotNull SuccessListener successListener, @NotNull ErrorListener errorListener) {
        super(path, methodType, MediaType.APPLICATION_JSON, MediaType.TEXT_HTML, MediaType.TEXT_PLAIN);
        this.firstName = firstName;
        this.lastName = lastName;
        email = Email;
        this.mobile = mobile;
        this.eid = eid;
        this.password = password;
    }

    @Override
    public Map<String, String> getParams() {
        Map<String, String> map = new HashMap<String, String>();
        map.put(Keys.PARAM.FIRSTNAME, firstName);
        map.put(Keys.PARAM.LASTNAME, lastName);
        map.put(Keys.PARAM.EMAIL, email);
        map.put(Keys.PARAM.MOBILE, mobile);
        map.put(Keys.PARAM.EID, eid);
        map.put(Keys.PARAM.PASSWORD, password);
        return map;
    }
}
