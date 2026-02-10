module com.doideradev.passwordbank {

    requires transitive javafx.controls;
    requires transitive javafx.fxml;
    requires javafx.graphics;
    requires java.desktop;
    requires java.xml;
    requires java.compiler;
    
    requires animatefx;
    requires org.apache.commons.io;
    requires transitive com.doideradev.doiderautils;
    requires jdk.httpserver;
    requires org.eclipse.parsson;

    opens com.doideradev.passwordbank to javafx.fxml;
    opens com.doideradev.passwordbank.controllers to javafx.fxml;
    opens com.doideradev.passwordbank.utilities to javafx.fxml;
    opens com.doideradev.passwordbank.model to javafx.fxml;
    opens com.doideradev.passwordbank.views to com.doideradev.doiderautils;

    exports com.doideradev.passwordbank;
    exports com.doideradev.passwordbank.controllers;
    exports com.doideradev.passwordbank.utilities;
    exports com.doideradev.passwordbank.model;
}