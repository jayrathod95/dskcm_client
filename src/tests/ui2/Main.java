package ui2;

import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXRippler;
import com.jfoenix.controls.JFXTabPane;
import com.jfoenix.transitions.hamburger.HamburgerSlideCloseTransition;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Created by jay_rathod on 23-02-2017.
 */
public class Main extends Application {

    @FXML
    private JFXTabPane tabpane;
    @FXML
    private Tab tab1;
    @FXML
    private Tab tab2, tab3;
    @FXML
    private JFXHamburger hamburger;
    @FXML
    private JFXDrawer drawer;
    @FXML
    private HBox manageaccount;
    @FXML
    private HBox newevent;
    @FXML
    private HBox settings;
    @FXML
    private AnchorPane anchorpanetab1;

    public static void main(String[] args) {
        launch(args);
    }

    @Override

    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("home.fxml"));
        loader.setController(this);
        AnchorPane rootPane = loader.load();
        FXMLLoader drawerContentLoader = new FXMLLoader(getClass().getResource("drawer_content.fxml"));
        drawerContentLoader.setController(this);

        VBox vBox = drawerContentLoader.load();

        JFXRippler rippler = new JFXRippler(manageaccount);
        rippler.setRipplerFill(Color.valueOf("#2196f3"));
        vBox.getChildren().add(rippler);
        JFXRippler rippler1 = new JFXRippler(newevent);
        rippler1.setRipplerFill(Color.valueOf("#2196f3"));
        vBox.getChildren().add(rippler1);
        JFXRippler rippler2 = new JFXRippler(settings);
        rippler2.setRipplerFill(Color.valueOf("#2196f3"));
        vBox.getChildren().add(rippler2);


        drawer.setSidePane(vBox);
        // JFXRippler rippler = new JFXRippler();
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

        hamburger.setOnMouseClicked(event -> {
            //slideCloseTransition.setRate(0.1);
            slideCloseTransition.setRate(slideCloseTransition.getRate() * -1);
            slideCloseTransition.play();
            if (drawer.isShown()) drawer.close();
            else drawer.open();
        });


        Scene scene = new Scene(rootPane, 600, 690);
        scene.getStylesheets().add(getClass().getResource("home.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();

    }
}
