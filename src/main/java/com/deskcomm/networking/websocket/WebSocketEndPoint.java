package com.deskcomm.networking.websocket;

import com.deskcomm.core.CurrentUser;
import com.deskcomm.core.User;
import com.deskcomm.core.bookkeeping.BookKeeper;
import com.deskcomm.ui.controllers.HomeController;
import javafx.application.Platform;
import javafx.scene.image.Image;

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
        Logger.getLogger(session.getId()).log(Level.SEVERE, session.getId());
    }

    @OnOpen
    public void onOpen(Session session) {
        WebSocketEndPoint.session = session;
        System.out.println("onOpen: " + session.getId());
        Platform.runLater(() -> {
            HomeController.getInstance().getLabelFooterStatus().setText("Online");
        });

        try {
            session.getBasicRemote().sendText("New User Connected: " + CurrentUser.getInstance().getFullName());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


    }

    @OnMessage
    public void onMessage(InputStream input) {
        System.out.println("WebSocket InputStream Received!");
        Image image = new Image(input);
        // imageView.setImage(image);
    }

    @OnMessage
    public void onMessage(String rawMessage, Session session) {
        //Logger.getLogger("").log(Level.SEVERE, "OnMessage-" + rawMessage + "-" + session.getId());
        WebSocketMessage webSocketMessage = new WebSocketMessage(rawMessage);

        Map<Integer, String> pathComponents = webSocketMessage.getPathDecomposed();


        switch (webSocketMessage.getPath()) {
            case "message/user/":
                break;
            case "message/group/":
                break;
            case "event":
                break;
            case "bookkeeping/users/add":
                BookKeeper.getUsersUpdater().getAdder().add(new User(webSocketMessage.getData()));
                break;

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
}
