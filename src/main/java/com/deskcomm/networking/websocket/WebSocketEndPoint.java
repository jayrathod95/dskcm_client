package com.deskcomm.networking.websocket;

import com.deskcomm.Main;
import com.deskcomm.core.CurrentUser;
import com.deskcomm.core.Event;
import com.deskcomm.core.User;
import com.deskcomm.core.bookkeeping.UsersUpdater;
import com.deskcomm.core.messages.InboundPersonalMessage;
import com.deskcomm.core.messages.LocalPersonalMessage;
import com.deskcomm.networking.URLManager;
import com.deskcomm.support.Keys;
import com.deskcomm.ui2.controllers.EventRow;
import com.deskcomm.ui2.controllers.HomeController;
import com.deskcomm.ui2.controllers.UserThreadController;
import com.deskcomm.ui2.core.UserThreadFactory;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.scene.image.Image;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.websocket.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
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
        URI uri = URI.create(URLManager.TARGET_URL_WS);
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
            // HomeController.getInstance().getLabelFooterStatus().setText("Online");
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
    public void onMessage(String rawMessage, Session session) throws ParseException, SQLException, ClassNotFoundException {

        InboundWebsocketMessage webSocketMessage = new InboundWebsocketMessage(rawMessage);

        switch (webSocketMessage.getPath()) {
            case "message/personal":
                System.out.println(webSocketMessage.getData());
                InboundPersonalMessage personalMessage = new InboundPersonalMessage(webSocketMessage.getData());
                personalMessage.setUnread(true);
                // boolean b = personalMessage.insertToTable();
                updateUserInterface(personalMessage);
                // if (b) sendAcknowledgement(personalMessage.getId());
                break;
            case "message/group":

                break;
            case "event/get/all":
                processEventsArray(webSocketMessage.getData());
                break;
            case "event/new":
                Event event = new Event(webSocketMessage.getData());
                event.insertToTable();
                HomeController.getInstance().getEventsMap().put(event.getUuid(), new EventRow(event));
                break;
            case "response/" + Keys.HANDSHAKE_REQ:
                handleHandShakeResponse(webSocketMessage.getData());
                break;
            case "bookkeeping/users":
                Platform.runLater(() -> {
                    Task<Boolean> task = new Task<Boolean>() {
                        @Override
                        protected Boolean call() throws Exception {
                            JSONObject data = webSocketMessage.getData();
                            return UsersUpdater.getInstance().updateAllUsers(data.getJSONArray("users"));
                        }
                    };
                    new Thread(task).start();
                    task.setOnSucceeded((WorkerStateEvent event1) -> {
                        Platform.runLater(() -> {
                            com.deskcomm.ui2.controllers.HomeController.getInstance().updateUsersListAsync();
                        });
                    });
                });
                break;
            case "users/online":
                handleOnlineUsersArray(webSocketMessage.getData().getJSONArray("data"));
                break;
            case "int_users_count":
                Platform.runLater(() -> HomeController.getInstance().getEventsMap().get(webSocketMessage.getData().getString(Keys.EVENT_ID)).getInt_users().setText(webSocketMessage.getData().getInt(Keys.INTERESTED_USERS_COUNT) + ""));
                break;
        }
    }

    private void processEventsArray(JSONObject data) throws ParseException, SQLException, ClassNotFoundException {
        JSONArray array = data.getJSONArray("data");
        for (int i = 0; i < array.length(); i++) {
            Event event = new Event(array.getJSONObject(i));
            event.insertToTable();

            HomeController.getInstance().getEventsMap().put(event.getUuid(), new EventRow(event));
        }
    }

    private void processEvent(JSONObject jsonObject) {
        try {
            Event event = new Event(jsonObject);
            event.insertToTable();
            HomeController.getInstance().getEventsMap().put(event.getUuid(), new EventRow(event));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }


    }

    private void handleOnlineUsersArray(JSONArray data) {
        System.out.println(data.toString());
        HomeController.getInstance().getOnlineUsers().clear();
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < data.length(); i++) {
            list.add(data.getString(i));
        }
        HomeController.getInstance().getOnlineUsers().addAll(list);
    }

    private void sendAcknowledgement(String messageId) {
        OutboundWebsocketMessage websocketMessage = new OutboundWebsocketMessage("message/personal/received", new JSONObject().put("id", messageId), false);
        session.getAsyncRemote().sendText(websocketMessage.toString());
    }

    private void handleHandShakeResponse(JSONObject data) {
        System.out.println(data);
        if (data.getBoolean(Keys.JSON_RESULT)) {
            CurrentUser.getInstance().getUpdater().updateWsSession(session);
            OutboundWebsocketMessage message = new OutboundWebsocketMessage("request/users", null, true);
            message.send();
            OutboundWebsocketMessage websocketMessage = new OutboundWebsocketMessage("messages/get_undelivered_messages", null, true);
            websocketMessage.send();
            OutboundWebsocketMessage websocketMessage1 = new OutboundWebsocketMessage("event/get/all", null, true);
            websocketMessage1.send();
        }
    }

    @OnClose
    public void onClose() {
        Logger.getLogger("").log(Level.INFO, "onclose called");
        //    connectToWebSocket();
        Platform.runLater(() -> {
            //HomeController.getInstance().getLabelFooterStatus().setText("Offline");
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

        Platform.runLater(() -> {
            UserThreadController userThreadController = UserThreadFactory.getUserThreadController(new User(personalMessage.getFromUserUuid()));
            String idOfVisibleRoot = Main.getPrimaryStage().getScene().getRoot().getId();
            if (idOfVisibleRoot != null && idOfVisibleRoot.equals("user_thread")) {
                personalMessage.setUnread(false);
                userThreadController.addNewMessage(LocalPersonalMessage.from(personalMessage));
                Task<Boolean> task = new Task<Boolean>() {
                    @Override
                    protected Boolean call() throws Exception {
                        return personalMessage.insertToTableAsRead();
                    }
                };
                task.setOnSucceeded(event -> {
                    sendAcknowledgement(personalMessage.getId());
                });
                new Thread(task).start();
            } else {
                Task<Boolean> task = new Task<Boolean>() {
                    @Override
                    protected Boolean call() throws Exception {
                        return personalMessage.insertToTable();
                    }
                };
                task.setOnSucceeded(event -> {
                    Platform.runLater(() -> HomeController.getInstance().updateThreadListAsync());
                    sendAcknowledgement(personalMessage.getId());
                });
                new Thread(task).start();
            }
        });
    }


}
