package com.doideradev.uninstaller;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import com.doideradev.doiderautils.*;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    private static Stage primaryStage;
    public  static String appVersion;
    
    public static void main(String[] args) {
        launch();
    }
    
    @Override
    public void start(Stage stage){
        primaryStage = stage;
        primaryStage.setResizable(false);
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.setTitle("Password Bank Uninstaller");

        scene = SceneManager.loadPage("uninstaller", getClass());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static Stage getStage() {
        return primaryStage;
    }
}