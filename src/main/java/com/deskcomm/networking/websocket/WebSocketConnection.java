package com.deskcomm.networking.websocket;

import javax.inject.Singleton;

/**
 * Created by Jay Rathod on 05-02-2017.
 */
@Singleton
public class WebSocketConnection {
    private static WebSocketConnection webSocketConnection = null;
    String path = "ws://localhost:8080/dskcm_rest/wsconn/image";

    private WebSocketConnection() {

    }

    public static WebSocketConnection getConnection() {
        if (webSocketConnection == null) webSocketConnection = new WebSocketConnection();
        return webSocketConnection;
    }

    public boolean isOpen() {
        return webSocketConnection.isOpen();
    }


}
