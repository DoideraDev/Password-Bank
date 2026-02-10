module com.doideradev.doiderautils {

    requires transitive javafx.fxml;
    requires transitive javafx.graphics;
    requires javafx.controls;
    requires java.desktop;

    exports com.doideradev.doiderautils;
    exports com.doideradev.doiderautils.popup;

    opens com.doideradev.doiderautils.popup to javafx.fxml;
}
