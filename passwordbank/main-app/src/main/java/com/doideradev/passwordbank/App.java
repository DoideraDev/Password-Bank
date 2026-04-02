package com.doideradev.passwordbank;

import com.doideradev.passwordbank.controllers.BaseController;
import com.doideradev.passwordbank.model.AppUser;
import com.doideradev.passwordbank.utilities.LoginList;
import com.doideradev.passwordbank.utilities.UpdaterManager;
import com.doideradev.passwordbank.utilities.AccountManager;
import com.doideradev.passwordbank.utilities.AppConfigs;
import com.doideradev.passwordbank.utilities.FXWindowControl;
import com.doideradev.doiderautils.Controller;
import com.doideradev.doiderautils.SceneManager;
import com.doideradev.doiderautils.UtilsClasses.Answer;
import com.doideradev.doiderautils.UtilsClasses.Question;

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
    public  static final String appVersion = "v0.7.0-alpha";
    public  static final double defH = 724;
    public  static final double defW = 1284;
    public  static final double minH = 600;
    public  static final double minW = 400;
    public  static UpdaterManager updaterManager;
    public  static BaseController baseCtrlInstance;
    public  static boolean haveUser;
    public  static boolean stayLoggedIn = false;
    public  static AppConfigs configs;
    public  static AppUser user;
    public  static LoginList logs;
    public  static SimpleBooleanProperty darkMode = new SimpleBooleanProperty(false);
    public  static SimpleBooleanProperty updateAvailable = new SimpleBooleanProperty(false);
    public  static FXWindowControl windowControl = null;
    private static boolean startUpdate = false;

    
    public static void main(String[] args) {
        launch();
    }
    
    
    @Override
    public void start(Stage stage) {
        primaryStage = stage;
        setDefaultAppProps();
        loadUserAccount();
        setObservables();
        Scene mainScene = decideFirstPage();

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
     * <p> Save all the data of the application and close it properly.
     */
    private void closeApplication() {
        if(user != null) {
            System.out.println("Saving files");
            AccountManager.saveAccountData();
        } 
        System.out.println("Closing Application...");
    }


    

    /**
     * <p> Verify if there is a user registered and load all the properties of the application
     * <p> such as user data, app configurations and saved logins.
     */
    private void loadUserAccount() {
        AccountManager.loadAccount();
        haveUser = AccountManager.haveUser;
        stayLoggedIn = AccountManager.stayLoggedIn;
        user = AccountManager.user();
        logs = AccountManager.logins();
        configs = AccountManager.configs();
    }

    

    /**
     * <p> Delete the user account and all related data such as saved logins.
     * <p> Returns true if the deletion was successful, false otherwise.
     */
    public static boolean deleteAccount() {
        var deleted = AccountManager.deleteAccount();
        if (deleted) {
            user = null;
            logs = null;
            stayLoggedIn = false;
            darkMode.set(false);
            haveUser = AccountManager.haveUser;
        }
        windowControl.stopWindowControl();
        windowControl = null;
        return deleted;
    }

    public static void createAccount(AppUser pUser, Question[] questions, Answer[] answers) {
        user = AccountManager.createAccount(pUser, questions, answers);
        haveUser = AccountManager.haveUser;
    }

    /**
     * <p> Update the user of the application.
     * 
     * <b> The attributes of the user should all be null except for the ones intending to change </b>
     * @param updateUser - the user containing only the attributes to change.
     */
    public static void UpdateAccount(AppUser updateUser) {
        AccountManager.updateAccountData(updateUser);
    }


    /**
     * <p> Decide which page to load as the first one depending on the value of {@code stayLoggedIn}.
     * @return The Scene object of the page to load as the first one.
     */
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



    public void setObservables() {
        darkMode.addListener((obs, oldV, newV) -> {
            var loadedControllers = SceneManager.getLoadedControllers();
            for (Controller ctrl : loadedControllers.values()) ctrl.setElementsStyle(newV);
        });
        updateAvailable.addListener((obs, oldV, newV) -> {if (newV) startUpdate = UpdaterManager.notifyUpdateAvailability();});
    }


    public static Stage getStage() {final var stage = primaryStage; return stage;}
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
        primaryStage.centerOnScreen();
    }
    

    public static void changePage(String sceneName) {
        primaryStage.hide();
        primaryStage.setScene(null);
        primaryStage.setScene(SceneManager.loadPage(sceneName, App.class));
        SceneManager.getController(sceneName);
        primaryStage.centerOnScreen();
        primaryStage.show();
    }
}