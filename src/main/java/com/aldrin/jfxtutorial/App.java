package com.aldrin.jfxtutorial;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import static javafx.application.Application.launch;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.StageStyle;

/**
 * JavaFX App
 */
public class App extends Application {

    @FXML
    private AnchorPane contentArea;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Image icon = new Image(getClass().getResourceAsStream("registration.png"));

        // Set the icon for the stage
        primaryStage.initStyle(StageStyle.UNDECORATED);

        primaryStage.getIcons().add(icon);
        Parent root = FXMLLoader.load(getClass().getResource("views/app.fxml"));
        root.setStyle("-fx-padding: 0; "
//                + "-fx-background-color: lightblue; "
                + "-fx-background-color: #4D4D4D,linear-gradient(to bottom,#3C3C3C,#A5A5A5);"
                + "-fx-background-radius: 10; "
                + // Set the corner radius
                "-fx-border-radius: 10; "
                + // Set the border radius
                "-fx-border-color: red; "
                + "-fx-border-width: 0;");
        Scene scene = new Scene(root);
        scene.setFill(null);

        primaryStage.setTitle("Pane Swap Example");
        primaryStage.setScene(scene);
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}