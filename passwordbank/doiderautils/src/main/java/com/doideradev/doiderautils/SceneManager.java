package com.doideradev.doiderautils;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;

/**
 * This class is used to manage the scenes of the application.
 * It is used to load the fxml files and return the scene.
 */
public abstract class SceneManager {
    

    /**
     * Loads the fxml file and returns the scene.
     * 
     * @param name - name of the fxml file to load (without the .fxml extension)
     * @param anchor - the class to use as a resource anchor (typically App.class or controller class)
     * @return Scene - the loaded scene or null if there was an error loading the file
     */
    public static Scene loadPage(String name, Class<?> anchor) {
        
        try {
            
            FXMLLoader sceneLoader = new FXMLLoader(anchor.getResource("views/" + name + ".fxml"));
            Parent rootScene = sceneLoader.load();
            Scene scene = new Scene(rootScene);
            scene.setFill(Color.rgb(0, 0, 0, 0.01));
            return scene;

        } catch (IOException e) {
            System.out.println("ERROR: There was an error trying to load the desired file... ");
            System.out.println(e.getMessage() + "\n\n");
            e.printStackTrace();
        }
        return null;
    }
}
