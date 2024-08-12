/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.aldrin.jfxtutorial;

import com.aldrin.jfxtutorial.dao.StudentDAO;
import com.aldrin.jfxtutorial.db.DBConnection;
import com.aldrin.jfxtutorial.model.Student;
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
public class StudentController implements Initializable {

    @FXML
    private TableView<Student> tableStudent = new TableView<>();
    @FXML
    public TableColumn<Student, Long> colId;
    @FXML
    public TableColumn<Student, String> colFirstname;
    @FXML
    public TableColumn<Student, String> colSurname;
    @FXML
    public TableColumn<Student, String> colEmail;
    @FXML
    public TableColumn<Student, String> colAddress;
    @FXML
    public TableColumn<Student, String> colCourse;
    @FXML
    public TableColumn<Student, String> colCourseId;
    @FXML
    public TableColumn<Student, String> colActions;

    @FXML
    public TextField txtSearch;

    @FXML
    public Button btnAddStudent = new Button();

    @FXML
    ObservableList<Student> StudentList = FXCollections.observableArrayList();

    @FXML
    public TitledPane titledPane = new TitledPane();

    String query = null;
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    Student student = null;
    DBConnection dbCon = new DBConnection();
    private StudentDAO studentDAO;

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
        btnAddStudent.setEffect(dropShadow);

        showStudents();
        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            searchStudent(newValue);
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
    protected void addStudentWindow(ActionEvent event) throws IOException {
        Stage currentStage = getRoot(event);

        FXMLLoader fl = new FXMLLoader();
        fl.setLocation(getClass().getResource("views/addStudent.fxml"));
        fl.load();
        StudentDialogController studentDialogController = fl.getController();
        studentDialogController.setMainController(this);
        Parent root = fl.getRoot();
        Stage newStage = new Stage();
        newStage.initOwner(currentStage);
        newStage.initModality(Modality.APPLICATION_MODAL);
        newStage.setResizable(false);
        Scene scene = new Scene(root);
        newStage.setScene(scene);
        newStage.setTitle("Add Student");
        newStage.show();

    }

    public void selectStudents() {
        StudentDAO studentDAO = new StudentDAO();
        List<Student> students = studentDAO.getAllStudents();
        ObservableList<Student> observableList = FXCollections.observableArrayList(students);
        tableStudent.setItems(observableList);
    }

    public void showStudents() {

        StudentDAO studentDAO = new StudentDAO();
        List<Student> students = studentDAO.getAllStudents();
        ObservableList<Student> observableList = FXCollections.observableArrayList(students);

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colFirstname.setCellValueFactory(new PropertyValueFactory<>("firstname"));
        colSurname.setCellValueFactory(new PropertyValueFactory<>("surname"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colCourse.setCellValueFactory(new PropertyValueFactory<>("course"));
        colCourseId.setCellValueFactory(new PropertyValueFactory<>("course_id"));
        //add cell of button edit 
        Callback<TableColumn<Student, String>, TableCell<Student, String>> cellFoctory = (TableColumn<Student, String> param) -> {
            // make cell containing buttons
            final TableCell<Student, String> cell = new TableCell<Student, String>() {
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
                                    student = tableStudent.getSelectionModel().getSelectedItem();
                                    query = "DELETE FROM `student` WHERE id  =" + student.getId();
                                    connection = dbCon.getCon();
                                    preparedStatement = connection.prepareStatement(query);
                                    preparedStatement.execute();
                                    selectStudents();

                                } catch (SQLException ex) {
                                    Logger.getLogger(StudentController.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }

                        });
//                        UPDATE MOUSEEVENT
                        editIcon.setOnMouseClicked((MouseEvent event) -> {

                            student = tableStudent.getSelectionModel().getSelectedItem();
                            FXMLLoader loader = new FXMLLoader();
                            loader.setLocation(getClass().getResource("views/addStudent.fxml"));
                            try {
                                Parent root = loader.load();
                            } catch (IOException ex) {
                                Logger.getLogger(StudentController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            StudentDialogController addStudentController = loader.getController();
                            addStudentController.setUpdate(true);
                            addStudentController.setTextField(student.getId(), student.getFirstname(), student.getSurname(), student.getEmail(), student.getAddress(), student.getCourse_id());
                            updateData(addStudentController);
                            Parent parent = loader.getRoot();
                            Stage stage = new Stage();
                            Stage currentStage = getRoot(event);
                            stage.initOwner(currentStage);
                            stage.initModality(Modality.APPLICATION_MODAL);
                            stage.setTitle("Update student");
                            stage.setScene(new Scene(parent));
                            stage.initStyle(StageStyle.UTILITY);
                            stage.show();
                            selectStudents();

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
        tableStudent.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        colActions.setCellFactory(cellFoctory);
        tableStudent.setItems(observableList);
        selectStudents();
    }

    private void searchStudent(String searchTerm) {
        StudentDAO studentDAO = new StudentDAO();
        ObservableList<Student> filteredData = FXCollections.observableArrayList();
        List<Student> list = studentDAO.getAllStudents();
        for (Student student : list) {
            if (student.getFirstname().toLowerCase().contains(searchTerm.toLowerCase())
                    || student.getSurname().toLowerCase().contains(searchTerm.toLowerCase())
                    || student.getEmail().toLowerCase().contains(searchTerm.toLowerCase())
                    || student.getAddress().toLowerCase().contains(searchTerm.toLowerCase())
                    || student.getCourse().getCourse().toLowerCase().contains(searchTerm.toLowerCase())) {
                filteredData.add(student);
            }
        }
        tableStudent.setItems(filteredData);
    }

    private void updateData(StudentDialogController addStudentController) {
        addStudentController.setMainController(this);
    }

}
