/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.aldrin.jfxtutorial;

/**
 *
 * @author Java Programming with Aldrin
 */
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

public class AppController implements Initializable {

    @FXML
    private AnchorPane contentArea;
    @FXML
    private Pane titledPane = new Pane();

    private double xOffset = 0;
    private double yOffset = 0;

    // Variables to track window state
    private boolean isMaximized = false;
    private double previousWidth;
    private double previousHeight;
    private double previousX;
    private double previousY;
    Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
    double screenWidth = screenBounds.getWidth();
    double screenHeight = screenBounds.getHeight();

    @FXML
    Button minimizeButton = new Button("Minimize");
    @FXML
    Button maximizeButton = new Button("Maximize");
    @FXML
    Button closeButton = new Button("Close");

    @FXML
    Label lblTitle = new Label();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Parent fxml;
        try {
            fxml = FXMLLoader.load(getClass().getResource("views/student.fxml"));
            AnchorPane.setTopAnchor(fxml, 0.0); // 0 pixels from the top edge
            AnchorPane.setLeftAnchor(fxml, 0.0); // 0 pixels from the left edge
            AnchorPane.setBottomAnchor(fxml, 0.0); // 0 pixels from the bottom edge
            AnchorPane.setRightAnchor(fxml, 0.0); // 0 pixels from the right edge
            contentArea.getChildren().removeAll();
            contentArea.getChildren().setAll(fxml);
        } catch (IOException ex) {
            Logger.getLogger(AppController.class.getName()).log(Level.SEVERE, null, ex);
        }
        lblTitle.setStyle("-fx-font-size: 24px; -fx-text-fill: linear-gradient(to bottom, #f9a123, #f89406);");


    }

    @FXML
    private void course(javafx.event.ActionEvent actionEvent) throws IOException {
        Parent fxml = FXMLLoader.load(getClass().getResource("views/course.fxml"));
        AnchorPane.setTopAnchor(fxml, 0.0); // 0 pixels from the top edge
        AnchorPane.setLeftAnchor(fxml, 0.0); // 0 pixels from the left edge
        AnchorPane.setBottomAnchor(fxml, 0.0); // 0 pixels from the bottom edge
        AnchorPane.setRightAnchor(fxml, 0.0); // 0 pixels from the right edge
        contentArea.getChildren().removeAll();
        contentArea.getChildren().setAll(fxml);
    }

    @FXML
    private void student(javafx.event.ActionEvent actionEvent) throws IOException {
        Parent fxml = FXMLLoader.load(getClass().getResource("views/student.fxml"));
        AnchorPane.setTopAnchor(fxml, 0.0); // 0 pixels from the top edge
        AnchorPane.setLeftAnchor(fxml, 0.0); // 0 pixels from the left edge
        AnchorPane.setBottomAnchor(fxml, 0.0); // 0 pixels from the bottom edge
        AnchorPane.setRightAnchor(fxml, 0.0); // 0 pixels from the right edge
        contentArea.getChildren().removeAll();
        contentArea.getChildren().setAll(fxml);
    }

    @FXML
    private void addStudent(MouseEvent event) {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("views/addStudent.fxml"));
            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.setScene(scene);
//            stage.initStyle(StageStyle.UTILITY);
//            stage.show();

            // Initially set the scale of the root pane to zero
            parent.setScaleX(0);
            parent.setScaleY(0);

            // Create a ScaleTransition for scaling animation
            ScaleTransition scaleUp = new ScaleTransition(Duration.seconds(1), parent);
            scaleUp.setFromX(0); // Start scale for X
            scaleUp.setFromY(0); // Start scale for Y
            scaleUp.setToX(1); // End scale for X
            scaleUp.setToY(1); // End scale for Y

            // Show the window and play the animation
            stage.show();
            scaleUp.play();
            System.out.println("fxml:" + parent.toString());

        } catch (IOException ex) {
//            Logger.getLogger(TableViewController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private static Stage getRoot(ActionEvent event) {
        Node root = (Node) event.getSource();
        return (Stage) root.getScene().getWindow();
    }

    private static Stage getRoot(MouseEvent event) {
        Node root = (Node) event.getSource();
        return (Stage) root.getScene().getWindow();
    }

    @FXML
    private void closeWindow(ActionEvent event) {
        Stage primaryStage = getRoot(event);
        primaryStage.close();
    }

    @FXML
    private void minimizeWindow(ActionEvent event) {
        Stage primaryStage = getRoot(event);
        primaryStage.setIconified(true);
    }

    @FXML
    private void maximizeWindow(ActionEvent event) {
        Stage primaryStage = getRoot(event);
        if (isMaximized) {
            primaryStage.setX(previousX);
            primaryStage.setY(previousY);
            primaryStage.setWidth(previousWidth);
            primaryStage.setHeight(previousHeight);
        } else {
            previousX = primaryStage.getX();
            previousY = primaryStage.getY();
            previousWidth = primaryStage.getWidth();
            previousHeight = primaryStage.getHeight();
            primaryStage.setX(0);
            primaryStage.setY(0);
            primaryStage.setWidth(screenWidth);
            primaryStage.setHeight(screenHeight);
        }
        isMaximized = !isMaximized;
    }

    @FXML
    public void mousePressed(MouseEvent event) {
        Stage currentStage = getRoot(event);
        if (!isMaximized) {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        }
    }

    @FXML
    public void mouseDragged(MouseEvent event) {
        Stage currentStage = getRoot(event);
        if (!isMaximized) {
            currentStage.setX(event.getScreenX() - xOffset);
            currentStage.setY(event.getScreenY() - yOffset);
        }
    }

}
