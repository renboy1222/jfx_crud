/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.aldrin.jfxtutorial;


import com.aldrin.jfxtutorial.dao.CourseDAO;
import com.aldrin.jfxtutorial.db.DBConnection;
import com.aldrin.jfxtutorial.model.Course;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;

/**
 *
 * @author Java Programming with Aldrin
 */
public class CourseController implements Initializable {

    @FXML
    private TableView<Course> tableCourse = new TableView<>();
    @FXML
    public TableColumn<Course, Long> colId;
    @FXML
    public TableColumn<Course, String> colCourse;
    @FXML
    public TableColumn<Course, String> colActions;

    @FXML
    public TextField txtSearch;

    @FXML
    public Button btnAddCourse = new Button();
    
    @FXML
    public TitledPane titledPane = new TitledPane();

    String query = null;
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    Course course = null;
    DBConnection dbCon = new DBConnection();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Set the TitledPane to always be expanded
        titledPane.setExpanded(true);

        // Prevent the TitledPane from collapsing
        titledPane.expandedProperty().addListener((obs, wasExpanded, isNowExpanded) -> {
            if (!isNowExpanded) {
                titledPane.setExpanded(true);
            }
        });
        
        // Create a DropShadow effect
        DropShadow dropShadow = new DropShadow();
        dropShadow.setOffsetX(5);
        dropShadow.setOffsetY(5);
        dropShadow.setColor(Color.GRAY);

        // Set inline styles
        // Apply the DropShadow effect to the button
        btnAddCourse.setEffect(dropShadow);

        showCourses();
        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            searchCourse(newValue);
        });

    }

    private static Stage getRoot(ActionEvent event) {
        Node root = (Node) event.getSource();
        return (Stage) root.getScene().getWindow();
    }

    private static Stage getRoot(MouseEvent event) {
        Node root = (Node) event.getSource();
        return (Stage) root.getScene().getWindow();
    }

    // modality is good
    @FXML
    protected void addCourseWindow(ActionEvent event) throws IOException {
        Stage currentStage = getRoot(event);

        FXMLLoader fl = new FXMLLoader();
        fl.setLocation(CourseController.class.getResource("views/addCourse.fxml"));
        fl.load();
        CourseDialogController addDataController = fl.getController();
        addDataController.setMainController(this);
        Parent root = fl.getRoot();
        Stage newStage = new Stage();
        newStage.initOwner(currentStage);
        newStage.initModality(Modality.APPLICATION_MODAL);
        newStage.setResizable(false);
        Scene scene = new Scene(root);
        newStage.setScene(scene);
        newStage.setTitle("Add Course");
        newStage.show();

    }

    @FXML
    public void selectCourses() {
        CourseDAO courseDAO = new CourseDAO();
        List<Course> students = courseDAO.getAllCourses();
        ObservableList<Course> observableList = FXCollections.observableArrayList(students);
        tableCourse.setItems(observableList);
    }

    public void showCourses() {

        CourseDAO courseDAO = new CourseDAO();
        List<Course> courses = courseDAO.getAllCourses();
        ObservableList<Course> observableList = FXCollections.observableArrayList(courses);

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colCourse.setCellValueFactory(new PropertyValueFactory<>("course"));
        //add cell of button edit 
        Callback<TableColumn<Course, String>, TableCell<Course, String>> cellFoctory = (TableColumn<Course, String> param) -> {
            // make cell containing buttons
            final TableCell<Course, String> cell = new TableCell<Course, String>() {
                @Override
                public void updateItem(String item, boolean empty) {

                    super.updateItem(item, empty);
                    //that cell created only on non-empty rows
                    if (empty) {
                        setGraphic(null);
                        setText(null);

                    } else {
                        FontAwesomeIconView deleteIcon = new FontAwesomeIconView(FontAwesomeIcon.TRASH);
                        FontAwesomeIconView editIcon = new FontAwesomeIconView(FontAwesomeIcon.PENCIL_SQUARE);
                        editIcon.setGlyphSize(24); // Set the size of the icon
                        editIcon.setFill(Color.rgb(255, 140, 0));
                        deleteIcon.setGlyphSize(24); // Set the size of the icon
                        deleteIcon.setFill(Color.rgb(220, 20, 60));
                        editIcon.setCursor(Cursor.HAND);
                        deleteIcon.setCursor(Cursor.HAND);

                        DropShadow dropShadow = new DropShadow();
                        dropShadow.setRadius(5);
                        dropShadow.setOffsetX(3);
                        dropShadow.setOffsetY(3);
                        dropShadow.setColor(javafx.scene.paint.Color.rgb(0, 0, 0, 0.5));

                        // Apply the DropShadow effect to the button
                        editIcon.setEffect(dropShadow);
                        deleteIcon.setEffect(dropShadow);
//                        DELETE MOUSEEVENT
                        deleteIcon.setOnMouseClicked((MouseEvent event) -> {
                            Stage currentStage = getRoot(event);
                            Stage newStage = new Stage();
                            newStage.initOwner(currentStage);
                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                            alert.initModality(Modality.WINDOW_MODAL); // Set modality to block input to the currentStage window
                            alert.initOwner(currentStage); // Set the currentStage window for the alert

                            alert.setTitle("Deleting student confirmation");
                            alert.setHeaderText("Please confirm your action");
                            alert.setContentText("Are you sure you want to delete student?");
                            // Get the buttons from the alert and add style classes
                            alert.getDialogPane().getStylesheets().add(getClass().getResource("css/bootstrap2.css").toExternalForm());

                            ButtonType confirmButton = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
                            ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

                            alert.getButtonTypes().setAll(confirmButton, cancelButton);

                            // Apply custom styles to buttons
                            alert.getDialogPane().lookupButton(confirmButton).getStyleClass().add("primary");
                            alert.getDialogPane().lookupButton(cancelButton).getStyleClass().add("danger");

                            Optional<ButtonType> result = alert.showAndWait();
                            if (result.isPresent() && result.get() == confirmButton) {
                                dbCon.getDBConn();
                                try {
                                    course = tableCourse.getSelectionModel().getSelectedItem();
                                    query = "DELETE FROM `course` WHERE id  =" + course.getId();
                                    connection = dbCon.getCon();
                                    preparedStatement = connection.prepareStatement(query);
                                    preparedStatement.execute();
                                    selectCourses();

                                } catch (SQLException ex) {
                                    Logger.getLogger(StudentController.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }

                        });
//                        UPDATE MOUSEEVENT
                        editIcon.setOnMouseClicked((MouseEvent event) -> {
                            course = tableCourse.getSelectionModel().getSelectedItem();
                            FXMLLoader loader = new FXMLLoader();
                            loader.setLocation(CourseController.class.getResource("views/addCourse.fxml"));
                            try {
                                Parent root = loader.load();
                            } catch (IOException ex) {
                                Logger.getLogger(StudentController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            CourseDialogController addCourseController = loader.getController();
                            addCourseController.setUpdate(true);
                            addCourseController.setTextField(course.getId(), course.getCourse());
                            updateData(addCourseController);
                            Parent parent = loader.getRoot();
                            Stage stage = new Stage();
                            Stage currentStage = getRoot(event);
                            stage.initOwner(currentStage);
                            stage.initModality(Modality.APPLICATION_MODAL);
                            stage.setTitle("Update student");
                            stage.setScene(new Scene(parent));
                            stage.initStyle(StageStyle.UTILITY);
                            stage.show();
                            selectCourses();

                        });

                        HBox managebtn = new HBox(editIcon, deleteIcon);
                        managebtn.setStyle("-fx-alignment:center");
                        HBox.setMargin(deleteIcon, new Insets(0, 2, 0, 3));
                        HBox.setMargin(editIcon, new Insets(0, 10, 0, 2));

                        setGraphic(managebtn);

                        setText(null);

                    }
                }

            };

            return cell;
        };
        tableCourse.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        colActions.setCellFactory(cellFoctory);
        tableCourse.setItems(observableList);
        selectCourses();
    }

    private void searchCourse(String searchTerm) {
        CourseDAO courseDAO = new CourseDAO();
        ObservableList<Course> filteredData = FXCollections.observableArrayList();
        List<Course> list = courseDAO.getAllCourses();
        for (Course student : list) {
            if (student.getCourse().toLowerCase().contains(searchTerm.toLowerCase())) {
                filteredData.add(student);
            }
        }
        tableCourse.setItems(filteredData);
    }


    
    private void updateData(CourseDialogController addCourseController){
        addCourseController.setMainController(this);
    }
}
