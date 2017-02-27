package com.deskcomm.ui2.controllers;

import com.deskcomm.core.CurrentUser;
import com.deskcomm.core.User;
import com.deskcomm.db.DbConnection;
import com.deskcomm.networking.websocket.WebSocketEndPoint;
import com.deskcomm.ui.rows.UserListRow;
import com.jfoenix.controls.*;
import com.jfoenix.transitions.hamburger.HamburgerSlideCloseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import java.util.List;

/**
 * Created by jay_rathod on 2/25/2017.
 */
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

    @SuppressWarnings("unused")
    @FXML
    private JFXTabPane tabpane;

    @SuppressWarnings("unused")
    @FXML
    private Tab tab1;

    @SuppressWarnings("unused")
    @FXML
    private AnchorPane anchorpanetab1;

    @SuppressWarnings("unused")
    @FXML
    private Tab tab2;

    @SuppressWarnings("unused")
    @FXML
    private Tab tab3;


    @SuppressWarnings("unused")
    @FXML
    private JFXDrawer drawer;
    @SuppressWarnings("unused")

    @FXML
    private HBox manageaccount;
    @SuppressWarnings("unused")

    @FXML
    private HBox newevent;

    @SuppressWarnings("unused")
    @FXML
    private HBox settings;
    @SuppressWarnings("unused")
    @FXML
    private ImageView avatar;
    @SuppressWarnings("unused")
    @FXML
    private JFXListView<UserListRow> userListView;
    @SuppressWarnings("unused")
    @FXML
    private JFXListView<ThreadRow> threadListView;
    private ObservableList<UserListRow> userObservableList;
    private ObservableList<ThreadRow> threadObservableList;
    @FXML
    private Label drawerUsername;
    private Scene scene;

    private HomeController() {
        FXML_FILE_MAIN = getClass().getResource("../fxmls/home.fxml");
        userObservableList = FXCollections.observableArrayList();
        threadObservableList = FXCollections.observableArrayList();
    }

    public static HomeController getInstance() {
        if (homeController == null) homeController = new HomeController();
        return homeController;
    }

    @Override
    public void startControlling(Stage primaryStage) throws IOException {
        if (root != null && root.getScene() != null) {
            //returning back
            primaryStage.setScene(root.getScene());
            primaryStage.show();
        } else {
            init(primaryStage);
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.show();
        }
    }

    private void init(Stage primaryStage) throws IOException {
        this.primaryStage = primaryStage;
        FXMLLoader loader = new FXMLLoader(FXML_FILE_MAIN);
        loader.setController(this);
        root = loader.load();
        if (root.getScene() != null) scene = root.getScene();
        else {
            scene = new Scene(root, HomeController.WIDTH, HomeController.HEIGHT);
            scene.getStylesheets().add(getClass().getResource("../stylesheets/home.css").toExternalForm());
        }

        drawerContentLoader = new FXMLLoader(getClass().getResource("../fxmls/drawer_content.fxml"));
        drawerContentLoader.setController(this);
        drawerContent = drawerContentLoader.load();
        drawerContent.getChildren().add(new JFXRippler(manageaccount));
        drawerContent.getChildren().add(new JFXRippler(newevent));
        drawerContent.getChildren().add(new JFXRippler(settings));
        drawer.setSidePane(drawerContent);
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
        updateThreadList();
        updateUsersList();

    }

    private void updateThreadList() {
        List<User> distinctConversations = getDistinctConversations();
        distinctConversations.forEach(user -> {
            try {
                threadObservableList.add(new ThreadRow(user, primaryStage));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
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

    public void updateUsersList() {
        ArrayList<User> allUsers = User.getAllUsers();
        if (allUsers != null) {
            userObservableList.clear();
            allUsers.forEach(user -> userObservableList.add(new UserListRow(user, primaryStage)));
        }
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
            e.printStackTrace();
        }
        return distinctConversations;
    }
}
