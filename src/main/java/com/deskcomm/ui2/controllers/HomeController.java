package com.deskcomm.ui2.controllers;

import com.deskcomm.core.CurrentUser;
import com.deskcomm.core.User;
import com.deskcomm.core.messages.LocalPersonalMessage;
import com.deskcomm.db.DbConnection;
import com.deskcomm.db.Table;
import com.deskcomm.networking.websocket.WebSocketEndPoint;
import com.deskcomm.resources.images.Images;
import com.deskcomm.support.Keys;
import com.deskcomm.ui.rows.UserListRow;
import com.jfoenix.controls.*;
import com.jfoenix.transitions.hamburger.HamburgerSlideCloseTransition;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableIntegerValue;
import javafx.collections.*;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.websocket.DeploymentException;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by jay_rathod on 2/25/2017.
 */
@SuppressWarnings("Duplicates")
public class HomeController extends Controller {


    public static final double WIDTH = 600;
    public static final double HEIGHT = 690;
    private static HomeController homeController;
    private static URL FXML_FILE_MAIN;
    private Stage primaryStage;
    private StackPane root;
    private FXMLLoader drawerContentLoader;
    private VBox drawerContent;

    @SuppressWarnings("unused")
    @FXML
    private JFXHamburger hamburger;

    @FXML
    private MaterialDesignIconView menu;

    @SuppressWarnings("unused")
    @FXML
    private JFXTabPane tabpane;

    @SuppressWarnings("unused")
    @FXML
    private Tab users;

    @SuppressWarnings("unused")
    @FXML
    private AnchorPane anchorpanetab1;

    @SuppressWarnings("unused")
    @FXML
    private Tab chats;

    @SuppressWarnings("unused")
    @FXML
    private Tab events;


    @SuppressWarnings("unused")
    @FXML
    private JFXDrawer drawer;
    @SuppressWarnings("unused")

    @FXML
    private HBox manageaccount;
    @SuppressWarnings("unused")

    @FXML
    private VBox vBoxEventsContainer;

    @FXML
    private HBox newevent;

    @SuppressWarnings("unused")
    @FXML
    private HBox settings;
    @SuppressWarnings("unused")
    @FXML
    private ImageView avatar, wall;
    @SuppressWarnings("unused")
    @FXML
    private JFXListView<UserListRow> userListView;
    @SuppressWarnings("unused")
    @FXML
    private JFXListView<ThreadListRow> threadListView;
    private ObservableList<UserListRow> userObservableList;
    private ObservableList<ThreadListRow> threadObservableList;
    private ObservableList<String> onlineUsers;
    private ObservableIntegerValue observableIntegerValue;
    @FXML
    private Label drawerUsername;
    private Scene scene;
    private Object eventsList;
    private ObservableMap<String, EventRow> eventsMap;


    private HomeController() {

        FXML_FILE_MAIN = getClass().getResource("fxmls/home.fxml");
        userObservableList = FXCollections.observableArrayList();
        threadObservableList = FXCollections.observableArrayList();
        observableIntegerValue = new SimpleIntegerProperty();
        onlineUsers = FXCollections.observableArrayList();
        eventsMap = FXCollections.observableHashMap();
    }

    public static HomeController getInstance() {
        if (homeController == null) homeController = new HomeController();
        return homeController;
    }

    public ObservableList<String> getOnlineUsers() {
        return onlineUsers;
    }

    public ObservableMap<String, EventRow> getEventsMap() {
        return eventsMap;
    }

    @Override
    public void startControlling(Stage primaryStage) throws IOException {
        this.primaryStage = primaryStage;

        if (root != null && root.getScene() != null) {
            //returning back
            this.primaryStage.setScene(root.getScene());
            this.primaryStage.show();
        } else {
            FXMLLoader loader = new FXMLLoader(FXML_FILE_MAIN);
            loader.setController(this);
            root = loader.load();
            if (root.getScene() != null) scene = root.getScene();
            else {
                scene = new Scene(root, HomeController.WIDTH, HomeController.HEIGHT);
                scene.getStylesheets().add(getClass().getResource("stylesheets/home.css").toExternalForm());
            }
            this.primaryStage.setScene(scene);
            this.primaryStage.setResizable(false);
            this.primaryStage.show();
            this.init();
            updateThreadListAsync();
        }
        this.primaryStage.setTitle(CurrentUser.getInstance().getFullName());
    }

    private void init() throws IOException {


        drawerContentLoader = new FXMLLoader(getClass().getResource("fxmls/drawer_content.fxml"));
        drawerContentLoader.setController(this);
        drawerContent = drawerContentLoader.load();
        drawerContent.getChildren().add(new JFXRippler(manageaccount));
        drawerContent.getChildren().add(new JFXRippler(newevent));
        drawerContent.getChildren().add(new JFXRippler(settings));
        drawer.setSidePane(drawerContent);
        wall.setImage(Images.get(Images.WALL1));
        HamburgerSlideCloseTransition slideCloseTransition = new HamburgerSlideCloseTransition(hamburger);
        slideCloseTransition.setRate(-1);
        slideCloseTransition.setDelay(Duration.millis(100));
        drawer.setOnDrawerClosing(event -> {
            slideCloseTransition.setRate(-1);
            slideCloseTransition.play();
        });
        drawer.setOnDrawerOpening(event -> {
            slideCloseTransition.setRate(1);
            slideCloseTransition.play();
        });
        //Label drawerUsername = (Label) drawerContentLoader.getNamespace().get("drawerUsername");
        drawerUsername.setText(CurrentUser.getInstance().getFullName());
        hamburger.setOnMouseClicked(event -> {
            slideCloseTransition.setRate(slideCloseTransition.getRate() * -1);
            slideCloseTransition.play();
            if (drawer.isShown()) drawer.close();
            else drawer.open();
        });
        manageaccount.setOnMouseClicked(event -> {
            try {
                AccountSettingsController controller = AccountSettingsController.getInstance();
                controller.startControlling(primaryStage);
                drawer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        Circle clip = new Circle(33, 33, 33);
        avatar.setClip(clip);

        establishWebsocketConnection();
        userListView.setItems(userObservableList);
        userListView.setSelectionModel(null);
        threadListView.setItems(threadObservableList);
        threadListView.setSelectionModel(null);
        updateThreadListAsync(event -> {
            updateUsersListAsync();
        });

        menu.setOnMouseClicked(event -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("fxmls/menu_home.fxml"));
                VBox vBox = loader.load();
                JFXPopup popup = new JFXPopup(root, vBox);
                popup.setSource(menu);
                popup.show(JFXPopup.PopupVPosition.TOP, JFXPopup.PopupHPosition.RIGHT, event.getX() - 3, event.getY() + 20);
                Label label = (Label) loader.getNamespace().get("refresh");
                label.setOnMouseClicked(event1 -> {
                    popup.close();
                    updateThreadListAsync(event2 -> {
                        updateUsersListAsync();
                    });
                });

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        threadObservableList.addListener((ListChangeListener<ThreadListRow>) c -> {
            int unreadThreadRows = getUnreadThreadRowsCount();
            if (unreadThreadRows > 0)
                chats.setText("Chats (" + unreadThreadRows + ")");
            else chats.setText("Chats");
        });
        threadObservableList.removeListener((ListChangeListener<? super ThreadListRow>) c -> {
            int unreadThreadRows = getUnreadThreadRowsCount();
            if (unreadThreadRows > 0)
                chats.setText("Chats (" + unreadThreadRows + ")");
            else chats.setText("Chats");
        });
        onlineUsers.addListener((ListChangeListener<String>) c -> {
            updateUsersListAsync();
        });

        eventsMap.addListener((MapChangeListener<String, EventRow>) change -> {
            Platform.runLater(() -> vBoxEventsContainer.getChildren().add(change.getValueAdded()));
        });

        newevent.setOnMouseClicked(event -> {
            EventCreateDialog dialog = new EventCreateDialog(primaryStage);
            dialog.show();
            drawer.close();
        });


    }

    public void updateThreadListAsync(EventHandler<WorkerStateEvent> onTaskCompleted) {
        Task<Boolean> task = new Task<Boolean>() {
            @SuppressWarnings("Duplicates")
            @Override
            protected Boolean call() throws Exception {
                List<User> distinctConversations = getDistinctConversations();
                Platform.runLater(() -> {
                    threadObservableList.clear();
                    distinctConversations.forEach(user -> {
                        try {
                            threadObservableList.add(new ThreadListRow(user, primaryStage));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                });
                return null;
            }
        };
        task.stateProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == Worker.State.FAILED || newValue == Worker.State.SUCCEEDED) {
                onTaskCompleted.handle(null);
            }
        });
        new Thread(task).start();
    }

    public void updateThreadListAsync() {
        Task<Boolean> task = new Task<Boolean>() {
            @SuppressWarnings("Duplicates")
            @Override
            protected Boolean call() throws Exception {
                List<User> distinctConversations = getDistinctConversations();
                Platform.runLater(() -> {
                    threadObservableList.clear();
                    distinctConversations.forEach(user -> {
                        try {
                            threadObservableList.add(new ThreadListRow(user, primaryStage));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                });
                return null;
            }
        };
        new Thread(task).start();
    }


    private void establishWebsocketConnection() {
        try {
            WebSocketEndPoint.connectToWebSocket();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DeploymentException e) {
            e.printStackTrace();
        }
    }

    public ObservableList<UserListRow> getUsersListObservable() {
        return userObservableList;
    }

    public void updateUsersListAsync(EventHandler<WorkerStateEvent> onTaskCompleted) {
        Task<Boolean> task = new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {
                ArrayList<User> allUsers = User.getAllUsers();
                if (allUsers != null) {
                    Platform.runLater(() -> {
                        userObservableList.clear();
                        allUsers.forEach(user -> {
                            user.setOnline(onlineUsers.contains(user.getUuid()));
                            userObservableList.add(new UserListRow(user, primaryStage));
                        });
                    });
                }
                return true;
            }
        };
        task.stateProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == Worker.State.FAILED || newValue == Worker.State.SUCCEEDED) {
                onTaskCompleted.handle(null);
            }
        });
        new Thread(task).start();
    }

    public void updateUsersListAsync() {
        Task<Boolean> task = new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {
                ArrayList<User> allUsers = User.getAllUsers();
                if (allUsers != null) {
                    Platform.runLater(() -> {
                        userObservableList.clear();
                        allUsers.forEach(user -> {
                            System.out.println(onlineUsers.contains(user.getUuid()));
                            user.setOnline(onlineUsers.contains(user.getUuid()));
                            userObservableList.add(new UserListRow(user, primaryStage));
                        });
                    });
                }
                return true;
            }
        };
        new Thread(task).start();
    }

    private List<User> getDistinctConversations() {
        List<User> distinctConversations = new ArrayList<>();
        try {
            Connection connection = DbConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT DISTINCT _from FROM messages_personal " +
                    "UNION " +
                    "SELECT DISTINCT _to FROM messages_personal");
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                if (resultSet.getString(1).equals(CurrentUser.getInstance().getUuid())) continue;
                distinctConversations.add(new User(resultSet.getString(1)));
            }
        } catch (SQLException e) {
            if (e.getMessage().contains(Keys.NO_SUCH_TABLE)) {
                Table.createMessagesPersonalTable();
                return getDistinctConversations();
            } else e.printStackTrace();
        }
        return distinctConversations;
    }

    public void updateThreadList(LocalPersonalMessage personalMessage) {
        try {
            User user = new User(personalMessage.getFromUserUuid());
            user.fetchFromDb();
            Iterator<ThreadListRow> iterator = threadObservableList.iterator();
            int i = 0;
            while (iterator.hasNext()) {
                ThreadListRow next = iterator.next();
                if (next.getUser().getUuid().equals(user.getUuid())) {
                    iterator.remove();
                    break;
                }
                i++;
            }
            threadObservableList.add(0, new ThreadListRow(user, primaryStage));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getUnreadThreadRowsCount() {
        Iterator<ThreadListRow> iterator = threadObservableList.iterator();
        int i = 0;
        while (iterator.hasNext()) i = iterator.next().isUnread() ? i + 1 : i;
        return i;
    }

}
