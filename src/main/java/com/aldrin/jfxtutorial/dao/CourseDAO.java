/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.aldrin.jfxtutorial.dao;


import com.aldrin.jfxtutorial.db.DBConnection;
import com.aldrin.jfxtutorial.model.Course;
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
public class CourseDAO {

    DBConnection dbCon = new DBConnection();

    public List<Course> getAllCourses() {
        dbCon.getDBConn();
        List<Course> courses = new ArrayList<>();
        try (Connection conn = dbCon.getCon(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("SELECT * FROM course  order by course asc ")) {
            
            while (rs.next()) {
                Course course = new Course();
                course.setId(rs.getLong("id"));
                course.setCourse(rs.getString("course"));
                courses.add(course);
            }
            rs.close();
            conn.close();
            dbCon.getCon().close();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return courses;
    }

   public void addCourse(Course course) {
        String sql = "INSERT INTO `course` ( `course`) VALUES (?) ;";
        dbCon.getDBConn();
        try (Connection conn = dbCon.getCon(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, course.getCourse());
            pstmt.executeUpdate();
            pstmt.close();
            conn.close();
            dbCon.getCon().close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateCourse(Course course) {
        String sql = "UPDATE `course` SET`course` = ?  WHERE `id` = ? ;";
        dbCon.getDBConn();
        try (Connection conn = dbCon.getCon(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, course.getCourse());
            pstmt.setLong(2, course.getId());
            pstmt.executeUpdate();
            pstmt.close();
            conn.close();
            dbCon.getCon().close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
