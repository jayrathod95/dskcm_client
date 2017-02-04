package com.deskcomm.networking.websocket;

import javax.websocket.ClientEndpoint;
import javax.websocket.OnOpen;
import javax.websocket.Session;

/**
 * Created by Jay Rathod on 05-02-2017.
 */
@ClientEndpoint
public class WebSocketEndPoint {
    @OnOpen
    public void onOpen(Session session) {
        
    }
}
