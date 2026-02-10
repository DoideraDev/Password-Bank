package com.doideradev.passwordbank;

import java.util.Collections;

import com.doideradev.passwordbank.controllers.BaseController;
import com.doideradev.passwordbank.model.AppUser;
import com.doideradev.passwordbank.model.Login;
import com.doideradev.passwordbank.utilities.FilesManager;
import com.doideradev.passwordbank.utilities.LoginList;
import com.doideradev.passwordbank.utilities.UpdaterManager;
import com.doideradev.passwordbank.utilities.AppConfigs;
import com.doideradev.doiderautils.SceneManager;

import javafx.application.Application;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


/**
 * Password-Bank App class
 */
public class App extends Application {


    private static Stage primaryStage;
    public  static final FilesManager filesManager = new FilesManager();
    public  static final String appVersion = "v0.7.0-alpha";
    public  static final double defH = 724;
    
    
    public  static final double defW = 1284;
    public  static final double minH = 600;
    public  static final double minW = 400;
    public  static UpdaterManager updaterManager;
    public  static BaseController baseCtrlInstance;
    public  static boolean haveUser;
    public  static boolean darkMode = true;
    public  static boolean stayLoggedIn = false;
    public  static AppConfigs configs;
    public  static AppUser user;
    public  static LoginList logs;
    public  static SimpleBooleanProperty updateAvailable = new SimpleBooleanProperty(false);
    private static boolean startUpdate = false;

    
    public static void main(String[] args) {
        launch();
    }
    
    
    @Override
    public void start(Stage stage) {
        primaryStage = stage;
        setDefaultAppProps();
        verifyUserProps();
        Scene mainScene = decideFirstPage();
        
        updateAvailable.addListener((obs, oldV, newV) -> {if (newV) startUpdate = UpdaterManager.notifyUpdateAvailability();});

        primaryStage.setScene(mainScene);
        primaryStage.show();
        UpdaterManager.lookForUpdates(configs);
    }
    @Override
    public void stop() {
        closeApplication();
        if (startUpdate) UpdaterManager.startUpdateInitialSteps();
    }


    

    /**
     * <p> Verify if there is a user registered and load all the properties of the application
     * <p> such as user data, app configurations and saved logins.
     */
    private void verifyUserProps() {
        if (FilesManager.haveUser) {
            haveUser = true;
            user = filesManager.openUserFile();
            user.getPassword().retrievePass();
            darkMode = user.isDarkMode();
            stayLoggedIn = user.isStayLoggedIn();
        }
        if (!FilesManager.isConfigured)
            configs = new AppConfigs(App.appVersion);
        else configs = filesManager.getConfigs();

        if (FilesManager.havePass) {
            var loadedLogs = filesManager.openPassFile();
            if (loadedLogs.checkPassOwnership(user)) {
                logs = loadedLogs;
                logs.newObservableList();
                Collections.sort(logs);
                for (Login login : logs) {
                    login.getPassword().retrievePass();
                }
            }
        }
    }


    /**
     * <p> Save all the data of the application and close it properly.
     */
    private void closeApplication() {
        if(user != null) {
            System.out.println("Saving files");
            user.getPassword().protectPassword();
            if (logs != null) {
                Collections.sort(logs);
                for (Login login : logs) {
                    login.getPassword().protectPassword();
                }
            }
            user.setDarkMode(darkMode);
            user.setStayLogged(stayLoggedIn);
            filesManager.closeFiles(user, logs);
        } 
        System.out.println("Closing Application...");
    }



    private Scene decideFirstPage() {
        Scene mainScene;
        if (stayLoggedIn) {
            mainScene = SceneManager.loadPage("base", this.getClass());
            setDefAppSize();
        } else {
            mainScene = SceneManager.loadPage("start", this.getClass());
            setMinAppSize();
        }
        return mainScene;
    }



    /**
     * <p> Delete the user account and all related data such as saved logins.
     * <p> Returns true if the deletion was successful, false otherwise.
     */
    public static boolean deleteAccount() {
        haveUser = false;
        user = null;
        return filesManager.deleteFiles();
    }

    public static Stage getStage() {return primaryStage;}
    public static Scene getScene() {return primaryStage.getScene();}


    /**
     * Set default properties for the application.
     * Such as title, style, resizability and positioning.
     */
    private void setDefaultAppProps() {
        primaryStage.setTitle("Password Bank");
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.setResizable(true);
        primaryStage.centerOnScreen();
    }


    public static void setDefAppSize() {
        primaryStage.setMinHeight(defH);
        primaryStage.setMinWidth(defW);
        primaryStage.setHeight(defH);
        primaryStage.setWidth(defW);
        primaryStage.centerOnScreen();
    }
    public static void setMinAppSize() {
        primaryStage.setMinHeight(minH);
        primaryStage.setMinWidth(minW);
        primaryStage.setHeight(minH);
        primaryStage.setWidth(minW);
    }
    

    public static void changePage(String sceneName) {
        primaryStage.hide();
        primaryStage.setScene(SceneManager.loadPage(sceneName, App.class));
        primaryStage.centerOnScreen();
        primaryStage.show();
    }
}