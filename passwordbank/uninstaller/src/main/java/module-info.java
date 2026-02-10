module com.doideradev.uninstaller {

    requires transitive com.doideradev.doiderautils;
    requires transitive javafx.controls;
    requires javafx.fxml;
    requires java.base;
    requires java.compiler;
    requires java.desktop;
    requires org.apache.commons.io;

    opens com.doideradev.uninstaller to javafx.fxml;
    opens com.doideradev.uninstaller.controllers to javafx.fxml;
    opens com.doideradev.uninstaller.utilities to javafx.fxml;
    opens com.doideradev.uninstaller.views to com.doideradev.doiderautils;
    
    exports com.doideradev.uninstaller to javafx.graphics;
    exports com.doideradev.uninstaller.controllers to javafx.graphics;
    exports com.doideradev.uninstaller.utilities to javafx.graphics;
}
