module com.quest {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;
    requires com.fasterxml.jackson.databind;
    requires com.zaxxer.hikari;
    requires mysql.connector.j;

    opens com.quest to javafx.graphics, javafx.fxml;
    opens com.quest.scene to javafx.fxml;
    opens com.quest.model to com.fasterxml.jackson.databind;

    exports com.quest;
    exports com.quest.scene;
    exports com.quest.model;
    exports com.quest.service;
    exports com.quest.dao;
    exports com.quest.util;
    exports com.quest.graphics;
}
