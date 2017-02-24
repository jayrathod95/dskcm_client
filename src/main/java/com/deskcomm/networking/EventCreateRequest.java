package com.deskcomm.networking;

import com.deskcomm.core.CurrentUser;
import com.deskcomm.core.Event;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jay_rathod on 21-02-2017.
 */
public class EventCreateRequest extends Request {
    private static String path = "/event/create";
    private static int methodType = MethodType.POST;
    private Event event;


    public EventCreateRequest(Event event) {
        super(path, methodType);
        this.event = event;
    }

    @Override
    public Map<String, String> getParams() {
        HashMap<String, String> map = new HashMap<>();
        map.put("data", event.toString());
        map.put("identity", CurrentUser.getInstance().getIdentity().toString());
        return map;
    }

}
