package com.deskcomm.ui.controllers;

import com.deskcomm.core.CurrentUser;
import com.deskcomm.core.User;
import com.deskcomm.db.DbConnection;
import com.deskcomm.networking.websocket.WebSocketEndPoint;
import com.deskcomm.support.L;
import com.deskcomm.ui.rows.UserListRow;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONException;

import javax.inject.Singleton;
import javax.websocket.DeploymentException;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Jay Rathod on 20-01-2017.
 */
@Singleton
public class HomeController extends Controller implements EventHandler<MouseEvent> {
    private static final double PREF_WIDTH = 954;
    private static final double PREF_HEIGHT = 643;
    private static final String FXML_FILE_2 = "../fxmls/row_users.fxml";
    private static HomeController homeController;
    final private String FXML_FILE = "../fxmls/home1.fxml";
    final private String FXML_FILE_3 = "../fxmls/create_new_group.fxml";
    private FXMLLoader loader;
    @FXML
    private Label labelUserName;
    @FXML
    private Button buttonLogout, buttonLoadUsers, buttonCreateGroup, buttonNewEvent;
    @FXML
    private Tab tabUsers;
    @FXML
    private VBox vboxUsersTab, rootvBox;
    @FXML
    private ListView<VBox> listViewUsers;
    @FXML
    private Label labelUserNameList, labelFooterUserName, labelFooterStatus;
    @FXML
    private ScrollPane scrollPaneUsersList;
    @FXML
    private AnchorPane threadsListViewHolder, usersListHolder;
    private ObservableList<AnchorPane> userThreadsObservableList = FXCollections.observableArrayList();
    private ObservableList<HBox> usersObservableList = FXCollections.observableArrayList();


    private String windowTitle = "DeskComm";
    private TabPane tabPane;
    private Stage primaryStage;
    private JSONArray jsonArrayOfUsers;
    private Scene scene;
    private EventHandler<? super MouseEvent> rowInUsersListClickedListener = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            String uuid = ((VBox) event.getSource()).getId();
            int i = (int) ((VBox) event.getSource()).getUserData();
            L.println(i);


            User user = new User(jsonArrayOfUsers.getJSONObject(i));
            Tab tab = new Tab(user.getFullName());
            tab.setId(user.getUuidTrimmed());
            tab.setClosable(true);
            tabPane.getTabs().add(tab);
            tabPane.getSelectionModel().select(tab);
            UserThreadController controller = null;
            try {
                controller = UserThreadController.getInstance(user);
            } catch (IOException e) {
                e.printStackTrace();
            }
            controller.startControlling(primaryStage);
        }
    };

    private HomeController() {
        L.println("Inside Home");
        init();
    }

    public static HomeController getInstance() {
        if (homeController == null) homeController = new HomeController();
        return homeController;
    }

    public void startControlling(Stage primaryStage) {
        this.primaryStage = primaryStage;
        //this.primaryStage.setResizable(false);
        System.out.print("DatabaseFile: " + DbConnection.JDBC_SQLITE_URL);
        scene = new Scene(rootvBox, PREF_WIDTH, PREF_HEIGHT);
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setTitle(windowTitle);
        L.println(CurrentUser.getInstance().getFirstName() + " " + CurrentUser.getInstance().getLastName());
        labelUserName.setText(CurrentUser.getInstance().getFirstName() + " " + CurrentUser.getInstance().getLastName());
        labelFooterUserName.setText(CurrentUser.getInstance().getFullName());

        startControllingChatsTab(tabPane.getTabs().get(0));
        startControllingUsersTab(tabPane.getTabs().get(1));
        startControllingEventsTab(tabPane.getTabs().get(2));
        startControllingAccountTab(tabPane.getTabs().get(3));
        startControllingMoreTab(tabPane.getTabs().get(4));

        try {
            WebSocketEndPoint.connectToWebSocket();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DeploymentException e) {
            e.printStackTrace();
        }

    }

    private void startControllingUsersTab(Tab tab) {
        tab.setClosable(false);
        ListView<HBox> usersListView = new ListView<HBox>(usersObservableList);
        usersListView.setPrefWidth(usersListHolder.getWidth());
        usersListView.setPrefHeight(usersListHolder.getHeight());
        usersListHolder.getChildren().add(usersListView);
        ArrayList<User> users = User.getAllUsers();

        if (users != null)
            users.forEach(user -> {
                UserListRow row = new UserListRow(user);
                HBox pane = row.create();
                if (pane != null) usersObservableList.add(pane);
            });
    }

    private void startControllingChatsTab(Tab tab) {
        tab.setClosable(false);
        userThreadsObservableList.addListener(new ListChangeListener<AnchorPane>() {
            @Override
            public void onChanged(Change<? extends AnchorPane> c) {

                System.out.println(c.getAddedSize());
            }
        });
        if (userThreadsObservableList.size() == 0) {
            Button button = new Button();
            button.setOnAction(event -> {
                getTabPane().getSelectionModel().select(tabUsers);
            });
            threadsListViewHolder.getChildren().add(button);
        }
        ListView<AnchorPane> threadsListView = new ListView<>(userThreadsObservableList);
        threadsListView.setPrefWidth(primaryStage.getWidth());
        threadsListView.setPrefHeight(threadsListViewHolder.getHeight());
        threadsListViewHolder.getChildren().add(threadsListView);
    }

    private void startControllingEventsTab(Tab tab) {
        tab.setClosable(false);
    }

    private void startControllingAccountTab(Tab tab) {
        tab.setClosable(false);


        buttonLogout.setOnAction(event -> {
            CurrentUser.getInstance().logout();
            try {
                LoginController.getInstance().startControlling(primaryStage);
                homeController = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        });


    }

    public TabPane getTabPane() {
        return tabPane;
    }

    private void startControllingMoreTab(Tab tab) {
        tab.setClosable(false);
        buttonNewEvent.setOnAction(event -> {
            EventCreateDialog dialog = new EventCreateDialog(primaryStage);
            dialog.show();
        });
    }

    private void inflateUsersList(com.deskcomm.support.Response<JSONArray> response) throws JSONException, IOException {
        jsonArrayOfUsers = response.getData();
        VBox vBoxUsers = (VBox) loader.getNamespace().get("vBoxUsers");
        vBoxUsers.getChildren().removeAll();
        for (int i = 0; i < jsonArrayOfUsers.length(); i++) {
            User user = new User(jsonArrayOfUsers.getJSONObject(i));
            FXMLLoader loader1 = new FXMLLoader(getClass().getResource(FXML_FILE_2));
            VBox vBox = loader1.load();
            ImageView dp = (ImageView) loader1.getNamespace().get("imageView");
            if (user.getGender().equals("M"))
                dp.setImage(new Image(getClass().getResourceAsStream("../../resources/images/avatar_m.png")));
            else
                dp.setImage(new Image(getClass().getResourceAsStream("../../resources/images/avatar_f.png")));

            Label userName = (Label) loader1.getNamespace().get("labelUserName");
            userName.setText(user.getFullName());
            vBox.setId(user.getUuidTrimmed());
            vBox.setUserData(i);
            vBox.setOnMouseClicked(rowInUsersListClickedListener);
            vBoxUsers.getChildren().add(vBox);
        }

    }

    @Override
    public void handle(MouseEvent event) {
        L.println("gfhghghfg");
    }

    synchronized public Label getLabelFooterStatus() {
        return this.labelFooterStatus;
    }

    public ObservableList<AnchorPane> getUserThreadsObservableList() {
        return userThreadsObservableList;
    }

    private void init() {


        loader = new FXMLLoader(getClass().getResource(FXML_FILE));
        loader.setController(this);
        try {
            rootvBox = loader.load();
            tabPane = (TabPane) loader.getNamespace().get("centerTabPane");
            tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.SELECTED_TAB);
        } catch (IOException e) {
            e.printStackTrace();
        }

        buttonCreateGroup.setOnAction(event -> {
            GroupCreatorController creatorController = GroupCreatorController.getInstance();
            creatorController.startControlling(new Stage());
        });

    }

    public Stage getStage() {
        return primaryStage;
    }
}
