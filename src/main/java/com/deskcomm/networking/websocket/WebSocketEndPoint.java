package com.deskcomm.networking.websocket;

import com.deskcomm.core.CurrentUser;
import com.deskcomm.core.messages.ReceivedMessageUser;
import com.deskcomm.support.Keys;
import com.deskcomm.ui.controllers.HomeController;
import javafx.application.Platform;
import javafx.scene.image.Image;
import org.json.JSONObject;

import javax.websocket.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.sql.SQLException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Jay Rathod on 06-02-2017.
 */
@ClientEndpoint
public class WebSocketEndPoint {

    private static final Logger LOGGER = Logger.getLogger(WebSocketEndPoint.class.getName());
    public static Session session;
    HomeController controller;

    public static void connectToWebSocket() throws IOException, DeploymentException {
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        URI uri = URI.create("ws://localhost:8080/dskcm_rest/ws");
        Session session = container.connectToServer(WebSocketEndPoint.class, uri);
//        Logger.getLogger(session.getId()).log(Level.SEVERE, session.getId());
    }

    @OnOpen
    public void onOpen(Session session) {
        WebSocketEndPoint.session = session;
        System.out.println("onOpen: " + session.getId());
        Platform.runLater(() -> {
            HomeController.getInstance().getLabelFooterStatus().setText("Online");
        });
        sendHandShakeMessage();
        // session.getBasicRemote().sendText("New User Connected: " + CurrentUser.getInstance().getFullName());


    }


    @OnMessage
    public void onMessage(InputStream input) {
        System.out.println("WebSocket InputStream Received!");
        Image image = new Image(input);
        // imageView.setImage(image);
    }

    @OnMessage
    public void onMessage(String rawMessage, Session session) {

        WebSocketMessage webSocketMessage = new WebSocketMessage(rawMessage);
        Map<Integer, String> pathComponents = webSocketMessage.getPathDecomposed();

        switch (webSocketMessage.getPath()) {
            case "message/user/":
                ReceivedMessageUser messageUser = new ReceivedMessageUser(webSocketMessage.getData());
                break;
            case "message/group/":
                break;
            case "event":
                break;
            case "bookkeeping/users/all":
                //  BookKeeper.updateUsers(webSocketMessage.getData());
                break;
            case "response/" + Keys.HANDSHAKE_REQ:
                handleHandShakeResponse(webSocketMessage.getData());
                break;
        }
    }

    private void handleHandShakeResponse(JSONObject data) {
        System.out.println(data);
        try {
            if (data.getBoolean(Keys.JSON_RESULT)) {
                WebSocketMessage webSocketMessage = new WebSocketMessage();
                webSocketMessage.setPath("request/get_users");
                JSONObject jsonObject = new JSONObject();
                jsonObject.put(Keys.USER_UUID, CurrentUser.getInstance().getUuid());
                jsonObject.put(Keys.SESSION_ID, CurrentUser.getInstance().getSessionId());
                webSocketMessage.setData(jsonObject);
                webSocketMessage.send(session);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @OnClose
    public void onClose() {
        Logger.getLogger("").log(Level.INFO, "onclose called");
        //    connectToWebSocket();
        Platform.runLater(() -> {
            HomeController.getInstance().getLabelFooterStatus().setText("Offline");
        });
    }

    @OnError
    public void onError(Throwable throwable) {
        throwable.printStackTrace();

    }

    private void sendHandShakeMessage() {
        try {
            WebSocketMessage webSocketMessage = new WebSocketMessage();
            webSocketMessage.setPath("request/" + Keys.HANDSHAKE_REQ);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(Keys.USER_UUID, CurrentUser.getInstance().getUuid());
            jsonObject.put(Keys.SESSION_ID, CurrentUser.getInstance().getSessionId());
            webSocketMessage.setData(jsonObject);
            session.getBasicRemote().sendText(webSocketMessage.toString());
        } catch (IOException | ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
}
