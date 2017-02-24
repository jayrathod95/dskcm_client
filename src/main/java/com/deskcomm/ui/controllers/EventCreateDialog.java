package com.deskcomm.ui.controllers;

import com.deskcomm.core.Event;
import com.deskcomm.networking.EventCreateRequest;
import com.deskcomm.resources.images.Images;
import com.deskcomm.support.Response;
import com.deskcomm.ui.support.Toast;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.json.JSONObject;

import javax.ws.rs.ProcessingException;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by jay_rathod on 20-02-2017.
 */
public class EventCreateDialog {

    FXMLLoader loader;
    AnchorPane rootPane;
    private Stage owner;
    private TextField eventTitle;
    private Button submitButton;
    private TextArea description;
    private TextField venue;
    private DatePicker endsDate;
    private TextField endsTime;
    private TextField startTime;
    private DatePicker startsDate;
    private Label errors;


    public EventCreateDialog(Stage owner) {
        this.owner = owner;
    }

    public void show() {
        try {
            loader = new FXMLLoader(getClass().getResource("../fxmls/create_event.fxml"));
            AnchorPane rootPane = loader.load();
            eventTitle = (TextField) loader.getNamespace().get("eventTitle");
            startTime = (TextField) loader.getNamespace().get("startsTime");
            startsDate = (DatePicker) loader.getNamespace().get("startsDate");
            endsTime = (TextField) loader.getNamespace().get("endsTime");
            endsDate = (DatePicker) loader.getNamespace().get("endsDate");
            venue = (TextField) loader.getNamespace().get("venue");
            description = (TextArea) loader.getNamespace().get("desc");
            Button imageButton = (Button) loader.getNamespace().get("imagebtn");
            submitButton = (Button) loader.getNamespace().get("submitbtn");
            Button cancelButton = (Button) loader.getNamespace().get("cancelbtn");
            errors = ((Label) loader.getNamespace().get("errors"));
            Stage stage = new Stage();
            Scene scene = new Scene(rootPane, AnchorPane.USE_COMPUTED_SIZE, AnchorPane.USE_COMPUTED_SIZE);
            stage.setScene(scene);
            stage.initOwner(owner);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.show();

            cancelButton.setOnAction(event -> stage.close());
            scene.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ESCAPE) {
                    stage.close();
                } else if (event.getCode() == KeyCode.ENTER) {
                    submitData();
                }
            });
            submitButton.setOnAction(event -> {
                submitData();

            });

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void submitData() {
        String eventTitleText = eventTitle.getText();
        if (eventTitleText.trim().length() > 0) {
            if (startTime.getText().trim().length() == 5) {
                if (startsDate.getValue().toString().length() > 0) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    String starts = startsDate.getValue().toString() + " " + startTime.getText();
                    System.out.println(starts);
                    Timestamp startsTimeStamp = null;
                    try {
                        startsTimeStamp = new Timestamp(dateFormat.parse(starts).getTime());
                        System.out.println(new Date(startsTimeStamp.getTime()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                        errors.setText("Invalid start date/time provided");
                        return;
                    }
                    if (endsTime.getText().trim().length() == 5) {
                        if (endsDate.getValue().toString().length() > 0) {
                            String ends = endsDate.getValue().toString() + " " + endsTime.getText();
                            Timestamp endsTimeStamp;
                            try {
                                endsTimeStamp = new Timestamp(dateFormat.parse(ends).getTime());
                                System.out.println(new Date(endsTimeStamp.getTime()));
                            } catch (ParseException e) {
                                e.printStackTrace();
                                errors.setText("Invalid ends date/time provided");
                                return;
                            }
                            if (venue.getText().trim().length() > 0) {
                                if (description.getText().trim().length() > 0) {
                                    Event.Builder builder = new Event.Builder();
                                    builder.setTitle(eventTitleText);
                                    builder.setStarts(startsTimeStamp);
                                    builder.setEnds(endsTimeStamp);
                                    builder.setVenue(venue.getText());
                                    builder.setDescription(description.getText());
                                    builder.setImageUrl("");
                                    Event event = builder.build();
                                    submitButton.setDisable(true);
                                    //Toast.make("Event Create Dialog Says:",event.toString(),Duration.seconds(4));
                                    Task<javax.ws.rs.core.Response> task = new Task<javax.ws.rs.core.Response>() {
                                        @Override
                                        protected javax.ws.rs.core.Response call() throws Exception {
                                            EventCreateRequest request = new EventCreateRequest(event);
                                            try {
                                                return request.perform();
                                            } catch (ProcessingException e) {
                                                Platform.runLater(() -> {
                                                    submitButton.setDisable(false);
                                                    Toast.make("Error", e.getMessage(), Images.get(Images.ERROR));
                                                });
                                            }
                                            return null;
                                        }
                                    };
                                    new Thread(task).start();
                                    task.setOnSucceeded(event1 -> {
                                        submitButton.setDisable(false);
                                        javax.ws.rs.core.Response response1 = task.getValue();
                                        if (response1.getStatus() == 200) {
                                            Response<JSONObject> response = new Response<>(response1.readEntity(String.class));
                                            if (response.isResult()) {
                                                Toast.make("Event Created Successfully", "", Images.get(Images.SUCCESS));
                                            } else {
                                                Toast.make("Could not create event. Please try again");
                                            }
                                        } else {
                                            if (response1.getStatus() == 404)
                                                Toast.make("Error", "Unable to connect to server", Images.get(Images.ERROR));
                                            else
                                                Toast.make("Error", "Something went wrong. Please check if you're connected to network", Images.get(Images.ERROR));
                                        }
                                    });
                                    task.setOnFailed(event1 -> {
                                        submitButton.setDisable(false);
                                        Toast.make("Error", task.getException().getMessage(), Images.get(Images.ERROR));
                                    });

                                } else errors.setText("Description not provided");
                            } else errors.setText("Venue is required");
                        } else errors.setText("Invalid ends date provided");
                    } else errors.setText("Invalid ends time provided");
                } else errors.setText("Invalid start date provided");
            } else errors.setText("Invalid start time provided");
        } else errors.setText("Enter event title");


    }
}
