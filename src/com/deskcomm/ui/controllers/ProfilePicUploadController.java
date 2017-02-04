package com.deskcomm.ui.controllers;

import com.deskcomm.exceptions.ResponseException;
import com.deskcomm.networking.ProfilePicUpload;
import com.deskcomm.support.L;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.json.JSONException;
import org.json.JSONObject;

import javax.ws.rs.core.Response;
import java.io.*;
import java.sql.SQLException;

/**
 * Created by Jay Rathod on 03-02-2017.
 */
public class ProfilePicUploadController extends Controller {

    private static final double PREF_WIDTH = 721;
    private static final double PREF_HEIGHT = 510;
    final private String FXML_FILE = "../fxmls/upload_profile_pic.fxml";
    AnchorPane rootPane;
    Stage primaryStage;
    FXMLLoader loader;
    @FXML
    Button buttonUpload, buttonChooseImage;
    @FXML
    TextField textField;
    @FXML
    ImageView imageView;
    @FXML
    ProgressIndicator progressIndicator;
    @FXML
    Label labelInfo;
    File imageFile;


    public ProfilePicUploadController() throws IOException {
        loader = new FXMLLoader(getClass().getResource(FXML_FILE));
        loader.setController(this);
        rootPane = loader.load();
        buttonChooseImage.setOnAction(this::chooseImageClicked);
        buttonUpload.setOnAction(this::uploadButtonClicked);
        imageView.setOnMouseClicked(this::chooseImageClicked);
    }

    private void chooseImageClicked(MouseEvent mouseEvent) {
        chooseImageClicked(new ActionEvent());
    }

    private void chooseImageClicked(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose An Image File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("JPEG", "*.jpeg"),
                new FileChooser.ExtensionFilter("PNG", "*.png"),
                new FileChooser.ExtensionFilter("BMP", "*.bmp")
        );
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home") + System.getProperty("file.separator") + "Pictures"));
        imageFile = fileChooser.showOpenDialog(primaryStage);
        if (imageFile != null) {
            textField.setText(imageFile.getAbsolutePath());
            try {
                InputStream inputStream = new FileInputStream(imageFile.getAbsolutePath());
                imageView.setImage(new Image(inputStream));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

    }

    private void uploadButtonClicked(ActionEvent actionEvent) {
        if (imageFile != null) {
            labelInfo.setVisible(true);
            labelInfo.setTextFill(Color.YELLOWGREEN);
            labelInfo.setText("Hang on. Image is being uploaded.");
            buttonUpload.setDisable(true);
            progressIndicator.setVisible(true);

            Task<JSONObject> task = new Task<JSONObject>() {
                @Override
                protected JSONObject call() throws Exception {
                    ProfilePicUpload profilePicUpload = new ProfilePicUpload("c10fddfe-c640-4117-b499-376ff7bd5d8b", "asdfasdfadsfasfasdfasdfasdfsdf", imageFile);
                    try {
                        Response response = profilePicUpload.perform();
                        return new JSONObject(response.readEntity(String.class));
                    } catch (ResponseException | JSONException e) {
                        e.printStackTrace();
                        setException(e);
                    }
                    setException(new Exception());
                    return null;
                }
            };

            new Thread(task).start();


            task.setOnSucceeded(event -> {
                buttonUpload.setDisable(false);
                progressIndicator.setVisible(false);
                JSONObject response = task.getValue();
                com.deskcomm.support.Response<JSONObject> response1 = new com.deskcomm.support.Response<JSONObject>(response);
                if (response1.isResult()) {
                    labelInfo.setText(response1.getMessage());
                    labelInfo.setTextFill(Color.GREEN);
                } else {
                    labelInfo.setText(response1.getMessage());
                    labelInfo.setTextFill(Color.RED);
                }
            });

            task.setOnFailed(event -> {
                L.println(event.getSource().getException().getMessage());
                buttonUpload.setDisable(false);
                progressIndicator.setVisible(false);
                labelInfo.setText(event.getSource().getException().getMessage());
            });


        }
    }


    @Override
    public void startControlling(Stage primaryStage) throws SQLException, ClassNotFoundException {
        this.primaryStage = primaryStage;
        Scene scene = new Scene(rootPane, PREF_WIDTH, PREF_HEIGHT);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
