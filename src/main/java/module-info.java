module com.aldrin.jfxtutorial {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.jfoenix;
    requires de.jensd.fx.glyphs.fontawesome;
    requires de.jensd.fx.glyphs.commons;
    requires lombok;
    requires java.logging;
    requires java.sql;

    opens com.aldrin.jfxtutorial to javafx.fxml;
    exports com.aldrin.jfxtutorial;
    exports com.aldrin.jfxtutorial.dao;
    exports com.aldrin.jfxtutorial.db;
    exports com.aldrin.jfxtutorial.model;
}
