/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.aldrin.jfxtutorial.dao;

import com.aldrin.jfxtutorial.db.DBConnection;
import com.aldrin.jfxtutorial.model.Course;
import com.aldrin.jfxtutorial.model.Student;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Java Programming with Aldrin
 */
public class StudentDAO {

    DBConnection dbCon = new DBConnection();

    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        dbCon.getDBConn();

        try (Connection conn = dbCon.getCon(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("SELECT\n"
                + "    `student`.`id`\n"
                + "    , `student`.`firstname`\n"
                + "    , `student`.`surname`\n"
                + "    , `student`.`email`\n"
                + "    , `student`.`address`\n"
                + "    , `student`.`course_id`\n"
                + "    , `course`.`course`\n"
                + "FROM\n"
                + "    `student`\n"
                + "    INNER JOIN `course` \n"
                + "        ON (`student`.`course_id` = `course`.`id`) ORDER BY `student`.`surname` ASC ;")) {
            while (rs.next()) {
                Student student = new Student();
                Course course = new Course();
                course.setCourse(rs.getString("course"));
                student.setCourse_id(rs.getLong("course_id"));
                student.setCourse(course);
                student.setId(rs.getLong("id"));
                student.setFirstname(rs.getString("firstname"));
                student.setSurname(rs.getString("surname"));
                student.setEmail(rs.getString("email"));
                student.setAddress(rs.getString("address"));
                students.add(student);

            }
            rs.close();
            conn.close();
            dbCon.getCon().close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }

    public void addStudent(Student student) {
        String sql = "INSERT INTO `student` (\n"
                + "  `firstname`,\n"
                + "  `surname`,\n"
                + "  `email`,\n"
                + "  `address`,\n"
                + "  `course_id`\n"
                + ") VALUES(?,?,?,?,?) ;";
        dbCon.getDBConn();
        try (Connection conn = dbCon.getCon(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, student.getFirstname());
            pstmt.setString(2, student.getSurname());
            pstmt.setString(3, student.getEmail());
            pstmt.setString(4, student.getAddress());
            pstmt.setLong(5, student.getCourse_id());
            pstmt.executeUpdate();
            pstmt.close();
            conn.close();
            dbCon.getCon().close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateStudent(Student student) {
        String sql = "UPDATE `student` \n"
                + "SET \n"
                + "  `firstname` = ?,\n"
                + "  `surname` = ?,\n"
                + "  `email` = ?,\n"
                + "  `address` = ?,\n"
                + "  `course_id` = ?\n"
                + "WHERE `id` = ? ";
        dbCon.getDBConn();
        try (Connection conn = dbCon.getCon(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, student.getFirstname());
            pstmt.setString(2, student.getSurname());
            pstmt.setString(3, student.getEmail());
            pstmt.setString(4, student.getAddress());
            pstmt.setLong(5, student.getCourse_id());
            pstmt.setLong(6, student.getId());
            pstmt.executeUpdate();
            pstmt.close();
            conn.close();
            dbCon.getCon().close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
