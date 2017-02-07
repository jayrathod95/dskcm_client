package com.deskcomm.ui.controllers;

import com.deskcomm.core.CurrentUser;
import com.deskcomm.core.User;
import com.deskcomm.networking.GetAllUsersRequest;
import com.deskcomm.networking.websocket.WebSocketEndPoint;
import com.deskcomm.support.L;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONException;

import javax.inject.Singleton;
import javax.websocket.DeploymentException;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by Jay Rathod on 20-01-2017.
 */
public class HomeController extends Controller implements EventHandler<MouseEvent> {
    private static final double PREF_WIDTH = 954;
    private static final double PREF_HEIGHT = 643;
    private static final String FXML_FILE_2 = "../fxmls/row_users.fxml";
    private static HomeController homeController;
    private final FXMLLoader loader;
    final private String FXML_FILE = "../fxmls/home1.fxml";

    @FXML
    private Label labelUserName;
    @FXML
    private Button buttonLogout, buttonLoadUsers;
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

    private String windowTitle = "Log In";
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
                controller = UserThreadController.getInstance(user, tab);
            } catch (IOException e) {
                e.printStackTrace();
            }
            controller.startControlling(primaryStage);
        }
    };

    @Singleton
    private HomeController() {
        L.println("Inside Home");
        loader = new FXMLLoader(getClass().getResource(FXML_FILE));
        loader.setController(this);
        try {
            rootvBox = loader.load();
            tabPane = (TabPane) loader.getNamespace().get("centerTabPane");
            tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.SELECTED_TAB);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static HomeController getInstance() {
        if (homeController == null) homeController = new HomeController();
        return homeController;
    }

    public void startControlling(final Stage primaryStage) throws SQLException, ClassNotFoundException {
        this.primaryStage = primaryStage;
        //this.primaryStage.setResizable(false);
        System.out.print("startControlling");
        scene = new Scene(rootvBox, PREF_WIDTH, PREF_HEIGHT);
        primaryStage.setScene(scene);
        primaryStage.show();
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
        buttonLoadUsers.setOnAction(event -> {

            Task<com.deskcomm.support.Response<JSONArray>> fetchUsersListTask = new Task<com.deskcomm.support.Response<JSONArray>>() {
                @Override
                protected com.deskcomm.support.Response<JSONArray> call() throws Exception {
                    GetAllUsersRequest request = new GetAllUsersRequest(CurrentUser.getInstance().getUuid(), CurrentUser.getInstance().getSessionId());
                    Response response = request.perform();
                    String s = response.readEntity(String.class);
                    L.println(s);
                    if (response.getStatus() >= 200 && response.getStatus() < 300)
                        return new com.deskcomm.support.Response<>(s);
                    else
                        throw new Exception(Response.Status.fromStatusCode(response.getStatus()).getReasonPhrase() + " - " + response.getStatus());
                }
            };
            new Thread(fetchUsersListTask).start();
            fetchUsersListTask.setOnSucceeded(event1 -> {
                try {
                    inflateUsersList(fetchUsersListTask.getValue());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            fetchUsersListTask.setOnFailed(event2 -> {
                L.println(fetchUsersListTask.getException().getMessage());

            });
        });

    }

    private void startControllingChatsTab(Tab tab) {
        tab.setClosable(false);

    }

    private void startControllingEventsTab(Tab tab) {
        tab.setClosable(false);
    }

    private void startControllingAccountTab(Tab tab) {
        tab.setClosable(false);


        buttonLogout.setOnAction(event -> {
            try {
                CurrentUser.getInstance().logout();
            } catch (SQLException e) {
                e.printStackTrace();

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            try {
                LoginController.getInstance().startControlling(primaryStage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });


    }

    private void startControllingMoreTab(Tab tab) {
        tab.setClosable(false);
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
}
