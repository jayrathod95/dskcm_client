package com.deskcomm.ui.controllers;

import com.deskcomm.core.CurrentUser;
import com.deskcomm.core.User;
import com.deskcomm.networking.GetAllUsersRequest;
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

import javax.ws.rs.core.Response;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by Jay Rathod on 20-01-2017.
 */
public class HomeController extends Controller implements EventHandler<MouseEvent> {
    private static final double PREF_WIDTH = 600;
    private static final double PREF_HEIGHT = 438;
    private static final String FXML_FILE_2 = "../fxmls/row_users.fxml";
    private final FXMLLoader loader;
    final private String FXML_FILE = "../fxmls/home1.fxml";
    @FXML
    Label labelUserName;
    @FXML
    Button buttonLogout, buttonLoadUsers;
    @FXML
    Tab tabUsers;
    @FXML
    VBox vboxUsersTab;
    @FXML
    ListView<VBox> listViewUsers;
    @FXML
    Label labelUserNameList;
    @FXML
    ScrollPane scrollPaneUsersList;
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
                controller = new UserThreadController(user, tab);
            } catch (IOException e) {
                e.printStackTrace();
            }
            controller.startControlling(primaryStage);

            /*
            User user = new User(uuid);
            UserDetailsRequest request = new UserDetailsRequest("33681ced-92cd-4f3b-83d9-803bceb4015a", "349fa78c-0cf9-4ba0-bf1d-95d912f3218d", new SuccessListener() {
                @Override
                public void onSuccess(Response response) {


                }
            }, new ErrorListener() {
                @Override
                public void onError(ResponseException e) {

                }
            });
            request.perform();
            */


        }
    };

    public HomeController() {
        L.println("Inside Home");
        loader = new FXMLLoader(getClass().getResource(FXML_FILE));
        loader.setController(this);

        try {
            tabPane = loader.load();
            tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.SELECTED_TAB);
        } catch (IOException e) {
            e.printStackTrace();
        }


        startControllingChatsTab(tabPane.getTabs().get(0));
        startControllingUsersTab(tabPane.getTabs().get(1));
        startControllingEventsTab(tabPane.getTabs().get(2));
        startControllingAccountTab(tabPane.getTabs().get(3));
        startControllingMoreTab(tabPane.getTabs().get(4));

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
                new LoginController().startControlling(primaryStage);
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
    public void startControlling(final Stage primaryStage) throws SQLException, ClassNotFoundException {
        this.primaryStage = primaryStage;
        System.out.print("startControlling");
        scene = new Scene(tabPane, PREF_WIDTH, PREF_HEIGHT);
        primaryStage.setScene(scene);
        primaryStage.show();
        L.println(CurrentUser.getInstance().getFirstname() + " " + CurrentUser.getInstance().getLastname());
        labelUserName.setText(CurrentUser.getInstance().getFirstname() + " " + CurrentUser.getInstance().getLastname());


    }

    @Override
    public void handle(MouseEvent event) {
        L.println("gfhghghfg");
    }
}
