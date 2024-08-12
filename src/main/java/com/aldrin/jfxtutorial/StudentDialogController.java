/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.aldrin.jfxtutorial;


import com.aldrin.jfxtutorial.dao.CourseDAO;
import com.aldrin.jfxtutorial.dao.StudentDAO;
import com.aldrin.jfxtutorial.model.Course;
import com.aldrin.jfxtutorial.model.Student;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author Java Programming with Aldrin
 */
public class StudentDialogController implements Initializable {

    @FXML
    public Button btnSave = new Button();
    @FXML
    public TextField txtFirstname = new TextField();
    @FXML
    public TextField txtSurname = new TextField();
    @FXML
    public TextField txtEmail = new TextField();
    @FXML
    public TextField txtAddress = new TextField();
    @FXML
    ComboBox<Course> comboBoxCourse = new ComboBox<>();

    private CourseDAO courseDAO = new CourseDAO();
    private StudentDAO studentDAO = new StudentDAO();

    private boolean update;

    Long studentId;

    private StudentController studentController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

//        comboBoxCourse.setPromptText("Select Student");
        List<Course> students = courseDAO.getAllCourses();
        comboBoxCourse.getItems().addAll(students);

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
    protected void addStudent(ActionEvent event) throws IOException {
        Stage currentStage = getRoot(event);
        Stage newStage = new Stage();
        newStage.initOwner(currentStage);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.WINDOW_MODAL); // Set modality to block input to the currentStage window
        alert.initOwner(currentStage); // Set the currentStage window for the alert

        alert.setTitle("Adding student confirmation");
        alert.setHeaderText("Please confirm your action");
        alert.setContentText("Are you sure you want to save student?");
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
            System.out.println("User confirmed the action.");

            Student student = new Student();
            student.setFirstname(txtFirstname.getText());
            student.setSurname(txtSurname.getText());
            student.setEmail(txtEmail.getText());
            student.setAddress(txtAddress.getText());
            Course selectedCourse = comboBoxCourse.getSelectionModel().getSelectedItem();
            student.setCourse_id(selectedCourse.getId());
            studentDAO.addStudent(student);
            currentStage.close();
        } else {
            System.out.println("User canceled the action.");
        }

    }

    void setUpdate(boolean b) {
        this.update = b;

    }

    void setTextField(Long id, String firstname, String surname, String email, String address, Long cId) {
        List<Course> course = courseDAO.getAllCourses();
        try {
            int employeeId = Integer.parseInt(cId.toString());
            Course selectedEmployee = course.stream()
                    .filter(emp -> emp.getId() == employeeId)
                    .findFirst()
                    .orElse(null);

            if (selectedEmployee != null) {
                comboBoxCourse.getSelectionModel().select(selectedEmployee);
            }
        } catch (NumberFormatException e) {
//            selectedEmployeeLabel.setText("Invalid Employee ID.");
        }
        studentId = id;
        txtFirstname.setText(firstname);
        txtSurname.setText(surname);
        txtEmail.setText(email);
        txtAddress.setText(address);

    }

    @FXML
    private void save(MouseEvent event) {

        Stage currentStage = getRoot(event);
        Stage newStage = new Stage();
        newStage.initOwner(currentStage);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.WINDOW_MODAL); // Set modality to block input to the currentStage window
        alert.initOwner(currentStage); // Set the currentStage window for the alert

        alert.setTitle("Adding student confirmation");
        alert.setHeaderText("Please confirm your action");
        alert.setContentText("Are you sure you want to save student?");
        // Get the buttons from the alert and add style classes
        alert.getDialogPane().getStylesheets().add(getClass().getResource("css/bootstrap2.css").toExternalForm());

        ButtonType confirmButton = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(confirmButton, cancelButton);

        // Apply custom styles to buttons
        alert.getDialogPane().lookupButton(confirmButton).getStyleClass().add("primary");
        alert.getDialogPane().lookupButton(cancelButton).getStyleClass().add("danger");

        Optional<ButtonType> result = alert.showAndWait();

        if (txtFirstname.getText().isEmpty() || txtSurname.getText().isEmpty() || txtEmail.getText().isEmpty() || txtAddress.getText().isEmpty()) {
            Alert alertError = new Alert(Alert.AlertType.ERROR);
            alertError.setHeaderText(null);
            alertError.setContentText("Please Fill All DATA");
            alertError.showAndWait();

        } else {
            if (result.isPresent() && result.get() == confirmButton) {
                Student student = new Student();
                student.setFirstname(txtFirstname.getText());
                student.setSurname(txtSurname.getText());
                student.setEmail(txtEmail.getText());
                student.setAddress(txtAddress.getText());
                Course selectedCourse = comboBoxCourse.getSelectionModel().getSelectedItem();
                student.setCourse_id(selectedCourse.getId());
                student.setId(studentId);
                saveStudent(student);

                currentStage.close();

            }
            retrieveData();
        }
    }

    String query = null;

    private void saveStudent(Student student) {
        StudentDAO studentDAO = new StudentDAO();
        if (update == false) {
            studentDAO.addStudent(student);
        } else {
            studentDAO.updateStudent(student);
        }
        retrieveData();
    }

    public void setMainController(StudentController studentController) {
        this.studentController = studentController;
    }

    public void retrieveData() {
        if (studentController != null) {
            studentController.selectStudents();
        }

    }

}
