package com.doideradev.updater.controllers;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import com.doideradev.updater.App;
import com.doideradev.updater.model.Updater;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

public class MainViewController {
    
    @FXML private GridPane gPaneMain;
    @FXML private Button minimizeButton;
    
    private static Text textUpdateInfo = new Text();
    private static ProgressBar progressBar = new ProgressBar();

    public static boolean updateComplete = false;



    public void initialize() {
        setStyles();
        setActions();
        doUpdate();
    }

    
    private void setActions() {
        minimizeButton.setOnMouseClicked(event -> {App.getStage().setIconified(true);});    
    }
    

    private void setStyles() {
        gPaneMain.setStyle("-fx-background-radius: 5px; -fx-border-color: #804DE5; -fx-border-width: 2px; -fx-border-radius: 5px;");
        minimizeButton.setText("");
        minimizeButton.setPrefWidth(20);
        minimizeButton.setMaxHeight(20);
        minimizeButton.setMinHeight(20);
        minimizeButton.getStylesheets().add(App.class.getResource("styles.css").toExternalForm());
        minimizeButton.getStyleClass().add("button-minimize");
        
        progressBar.setPrefWidth(300);
        progressBar.setPrefHeight(25);
        progressBar.getStylesheets().add(App.class.getResource("styles.css").toExternalForm());
        progressBar.getStyleClass().add("progress-bar");

        gPaneMain.setPadding(new Insets(5));
        GridPane.setHalignment(minimizeButton, HPos.RIGHT);
        GridPane.setValignment(minimizeButton, javafx.geometry.VPos.TOP);
        GridPane.setHalignment(textUpdateInfo, HPos.CENTER);
        gPaneMain.getRowConstraints().get(0).setPercentHeight(10);
        gPaneMain.getRowConstraints().get(1).setPercentHeight(70);
        gPaneMain.getRowConstraints().get(2).setPercentHeight(20);
        gPaneMain.add(textUpdateInfo, 0, 1);
        gPaneMain.add(progressBar, 0, 2);
        GridPane.setHalignment(progressBar, HPos.CENTER);
    }


    public void doUpdate() {
        new Thread(() -> {
            MainViewController.updateApplication();
            Platform.runLater(() -> MainViewController.setUpdateProgress(0.003));
            while (!MainViewController.updateComplete) {
                Platform.runLater(() -> increaseProgress());
                try {
                    Thread.sleep(400);
                } catch (InterruptedException e) {
                    System.out.println(e.getMessage());
                    System.out.println(e.getCause());
                    e.printStackTrace();
                }
            }
            
            var configs = Updater.getConfigs();
            try {
                MainViewController.changeTextInfo("Restarting application");
                var builder = new ProcessBuilder(configs.getInstallDir() + "\\Password Bank"); 
                builder.start();
                Thread.sleep(1500);
                Platform.exit();
            } catch (Exception e) {
                System.out.println(e.getMessage());
                System.out.println(e.getCause());
                e.printStackTrace();
            }


        }).start();
    }



    /* ################################################## 
                        STATIC METHODS 
       ################################################## 
    */

    public static void updateApplication() {
        new Thread(() -> {

            abstract class Reverse {
                static void revertUpdate(String text) {
                    Platform.runLater(() -> MainViewController.changeTextInfo(text));
                    Platform.runLater(() -> MainViewController.setUpdateProgress(1));
                    Platform.runLater(() -> MainViewController.cancelUpdate());
                }
            }

            if (Updater.downloadMSIFile()) Platform.runLater(() -> MainViewController.setUpdateProgress(0.5));
            else {Reverse.revertUpdate("Download failed. Please try again later.");   return;}

            if (Updater.deleteOldVersion()) Platform.runLater(() -> MainViewController.setUpdateProgress(0.8));
            else {Reverse.revertUpdate("Failed to delete old version. Please try again later.");  return;}

            if (Updater.installNewVersion()) Platform.runLater(() -> MainViewController.setUpdateProgress(1.0));
            else {Reverse.revertUpdate("Failed to delete old version. Please try again later.");  return;}
            
            MainViewController.updateComplete = true;
        }).start();
    }

    public static void changeTextInfo(String newText) {
        textUpdateInfo.setText(newText);
    }

    public static void increaseProgress() {
        var actualprog = progressBar.getProgress();
        progressBar.setProgress(actualprog + 0.004);
    }
    
    public static void setUpdateProgress(double value) {
        var actualprog = progressBar.getProgress();
        if (value > actualprog) progressBar.setProgress(value);
    }

    public static void cancelUpdate() {
        updateComplete = true;
        Updater.revertUpdate();
        MainViewController.changeTextInfo("Update canceled. Reverting changes...");
        MainViewController.autoDelete();
    }

    public static void autoDelete() {
        Platform.runLater(() -> {
            App.getStage().setOnCloseRequest(event -> {try {
                FileUtils.forceDeleteOnExit(new File(System.getProperty("user.dir")));
            } catch (IOException e) {
                System.out.println(e.getMessage());
                System.out.println(e.getCause());
                e.printStackTrace();
            }});
        });
        Platform.exit();
    }
    
}