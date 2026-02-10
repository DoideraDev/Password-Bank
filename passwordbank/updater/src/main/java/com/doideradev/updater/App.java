package com.doideradev.updater;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

import com.doideradev.doiderautils.SceneManager;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Stage primaryStage;
    private static Scene scene;

    
    public static void main(String[] args) {
        launch();
    }
    
    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.setTitle("Updater Application");
        scene = SceneManager.loadPage("MainView", this.getClass());
        primaryStage.setScene(scene);

        primaryStage.show();
    }
    
    public static Stage getStage() {
        return primaryStage;
    }

}