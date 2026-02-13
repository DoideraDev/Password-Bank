package com.doideradev.doiderautils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;

/**
 * This class is used to manage the scenes of the application.
 * It is used to load the fxml files and return the loaded resources.
 */
public abstract class SceneManager {
    
    private static Map<String, Parent> loadedPages = new HashMap<>();
    private static Map<String, Object> loadedControllers = new HashMap<>();

    /**
     * Loads the fxml file and returns the scene.
     * 
     * @param name - name of the fxml file to load (without the .fxml extension)
     * @param anchor - the class to use as a resource anchor (typically App.class or controller class)
     * @return Scene - the loaded scene or null if there was an error loading the file
     */
    public static Scene loadPage(String name, Class<?> anchor) {

        if (!loadedPages.containsKey(name)) loader(name, anchor);

        Parent rootScene = loadedPages.get(name);
        Scene scene = new Scene(rootScene);
        scene.setFill(Color.rgb(0, 0, 0, 0.01));

        return scene;
    }


    /**
     * Loads the fxml file and returns the root node.
     * 
     * @param name - name of the fxml file to load (without the .fxml extension)
     * @param anchor - the class to use as a resource anchor (typically App.class or controller class)
     * @return Parent - the root node of the loaded fxml file or null if there was an error loading the file
     */
    public static Parent loadPage(Class<?> anchor, String name) {
        
        if (!loadedPages.containsKey(name)) loader(name, anchor);

        return loadedPages.get(name);
    }

    /**
     * Returns the controller of the loaded fxml file.
     * @param name - name of the fxml file whose controller is to be returned (without the .fxml extension)
     * @return Object - the controller of the loaded fxml file or null if the file was not loaded or there was an error loading the file
     */
    public static Object getController(String name) {
        return loadedControllers.get(name);
    }

    /**
     * Sets the controller for a specific fxml file. This is useful when the controller needs to be created before loading the fxml file, or when the controller needs to be shared between multiple fxml files.
     * @param name - name of the fxml file whose controller is to be set (without the .fxml extension)
     * @param controller - the controller to set for the specified fxml file
     */
    public static void setController(String name, Object controller) {
        loadedControllers.put(name, controller);
    }


    /**
     * Loads the fxml file and stores the root node and controller in the respective maps.
     * @param name - name of the fxml file to load (without the .fxml extension)
     * @param anchor - the class to use as a resource anchor (typically App.class or controller class)
     */
    static private void loader(String name, Class<?> anchor) {
        try {
            
            FXMLLoader sceneLoader = new FXMLLoader(anchor.getResource("views/" + name + ".fxml"));
            sceneLoader.setController(getController(name));
            Parent root = sceneLoader.load();
            var controller = sceneLoader.getController();
            loadedPages.put(name, root);
            loadedControllers.put(name, controller);

        } catch (IOException e) {
            System.out.println("ERROR: There was an error trying to load the desired file... ");
            System.out.println(e.getMessage() + "\n\n");
            e.printStackTrace();
        }
    }

}
