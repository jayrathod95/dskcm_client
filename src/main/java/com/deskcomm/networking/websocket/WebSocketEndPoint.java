package com.deskcomm.networking.websocket;

import com.deskcomm.core.CurrentUser;
import com.deskcomm.core.bookkeeping.UsersUpdater;
import com.deskcomm.core.messages.InboundPersonalMessage;
import com.deskcomm.support.Keys;
import com.deskcomm.ui.controllers.HomeController;
import com.deskcomm.ui.rows.PersonalThreadRow;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import org.json.JSONObject;

import javax.websocket.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
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

    public static void sendMessage(String string) {
        WebSocketEndPoint.session.getAsyncRemote().sendText(string);
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

        InboundWebsocketMessage webSocketMessage = new InboundWebsocketMessage(rawMessage);

        switch (webSocketMessage.getPath()) {
            case "message/personal":
                System.out.println(webSocketMessage.getData());
                InboundPersonalMessage personalMessage = new InboundPersonalMessage(webSocketMessage.getData());
                //personalMessage.insertToTable();
                updateUserInterface(personalMessage);
                break;
            case "message/group":

                break;
            case "event":
                break;
            case "response/" + Keys.HANDSHAKE_REQ:
                handleHandShakeResponse(webSocketMessage.getData());
                break;
            case "bookkeeping/users":
                JSONObject data = webSocketMessage.getData();
                UsersUpdater.getInstance().updateAllUsers(data.getJSONArray("users"));
                break;
        }
    }

    private void handleHandShakeResponse(JSONObject data) {
        System.out.println(data);
        if (data.getBoolean(Keys.JSON_RESULT)) {
            CurrentUser.getInstance().getUpdater().updateWsSession(session);
            OutboundWebsocketMessage message = new OutboundWebsocketMessage("request/users", null, true);
            message.send();
            OutboundWebsocketMessage websocketMessage = new OutboundWebsocketMessage("messages/get_undelivered_messages", null, true);
            websocketMessage.send();
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
            OutboundWebsocketMessage webSocketMessage = new OutboundWebsocketMessage("request/" + Keys.HANDSHAKE_REQ, null, true);
            session.getBasicRemote().sendText(webSocketMessage.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void updateUserInterface(InboundPersonalMessage personalMessage) {
        PersonalThreadRow row = new PersonalThreadRow(personalMessage.getId(), personalMessage.getFromUser(), personalMessage.getBody(), personalMessage.getTimestamp());
        AnchorPane anc = row.create();
        if (anc != null)
            Platform.runLater(() -> {
                ObservableList<AnchorPane> userThreadsObservableList = HomeController.getInstance().getUserThreadsObservableList();
                userThreadsObservableList.removeIf(next -> next.getUserData().equals(anc.getUserData()));
                HomeController.getInstance().getUserThreadsObservableList().add(0, anc);
            });
    }


}
