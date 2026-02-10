module com.doideradev.updater {
    requires transitive javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;
    requires com.doideradev.doiderautils;
    requires org.apache.commons.io;

    opens com.doideradev.updater to javafx.fxml;
    opens com.doideradev.updater.controllers to javafx.fxml;
    opens com.doideradev.updater.views to com.doideradev.doiderautils;

    exports com.doideradev.updater;
    exports com.doideradev.updater.controllers;
}
