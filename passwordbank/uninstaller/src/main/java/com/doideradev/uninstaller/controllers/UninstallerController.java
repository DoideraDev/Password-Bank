package com.doideradev.uninstaller.controllers;

import java.io.IOException;

import com.doideradev.uninstaller.utilities.UninstallerManager;
import com.doideradev.uninstaller.utilities.AppConfigs;
// import com.doideradev.doiderautils.AppConfigs;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class UninstallerController {


    @FXML private Button buttonCancel;
    @FXML private Button buttonClose;
    @FXML private Button buttonConfirm;
    @FXML private HBox hBoxButtons;
    @FXML private Pane paneUninstall;
    @FXML private StackPane sPaneUninstall;
    @FXML private Text textUninstall;

    
    private static ProgressBar bar = new ProgressBar();
    public volatile static boolean uninstallComplete = false;
    public volatile static double uninstallProgress = 0;
    private AppConfigs configs;

    double xPos, yPos;

    

    public void initialize() {
        sPaneUninstall.setBackground(Background.fill(Color.TRANSPARENT));
        setPageStyle();
        firstPageActions();
        configs = UninstallerManager.getConfigs();
    }



    private void firstPageActions() {
        buttonCancel.setOnMouseClicked(event -> cancelUninstall());
        buttonCancel.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) cancelUninstall();
        });

        buttonClose.setVisible(false);
        buttonConfirm.setOnMouseClicked(event -> configureSecondPage());
        buttonCancel.setText("Cancel");
        buttonConfirm.setText("Yes");
        textUninstall.setText("Sorry to see you go... \nAre you sure you want to uninstall Password Bank application?");
    }


    private void configureSecondPage() {
        sPaneUninstall.getScene().getWindow().centerOnScreen();
        xPos = hBoxButtons.getLayoutX();
        yPos = hBoxButtons.getLayoutY();
        paneUninstall.getChildren().remove(hBoxButtons);
        textUninstall.setText("Removing all data related to Password Bank application");
        secondPageActions();
    }


    private void secondPageActions() {
        paneUninstall.getChildren().add(bar);
        bar.setStyle("-fx-accent: #8A59EE;");
        bar.setPrefWidth(450);
        bar.setPrefHeight(40);
        bar.setLayoutX(xPos);
        bar.setLayoutY(yPos);
        bar.setProgress(0);
        buttonClose.setOnMouseClicked(event -> Platform.exit());
        new Thread(() -> {
            UninstallerManager.uninstallApp();

            while (!uninstallComplete) {
                Platform.runLater(() -> {
                    UninstallerController.setUninstallProgress(UninstallerController.uninstallProgress);
                    UninstallerController.uninstallProgress += 0.01;
                });
                    
                try {
                    Thread.sleep(150);
                } catch (InterruptedException e) {
                    System.out.println(e.getMessage());
                    e.printStackTrace();
                }
            }
        }).start();
    }




    private void cancelUninstall() {
        reopenApplications();
        Platform.exit();
    }




    private void reopenApplications() {
        ProcessBuilder builder = new ProcessBuilder(configs.getInstallDir() + "\\Password Bank");

        try {
            builder.start();
        } catch (IOException e) {
            textUninstall.setText(e.getMessage() +"\n"+ e.getCause());
            try {
                Thread.sleep(5000);
                System.exit(32);
            } catch (Exception e2) {
                textUninstall.setText(e.getMessage() +"\n"+ e.getCause());
            }
            System.out.println(e.getMessage()); System.out.println(e.getCause());
            e.printStackTrace();
        }
    }

    public static void setUninstallProgress(double value) {
        var actualprog = bar.getProgress();
        if (value > actualprog)
            bar.setProgress(value);
    }


    private void setPageStyle() {
        paneUninstall.setStyle("-fx-background-color: WHITE; "+
                                "-fx-background-radius: 5px; " + 
                                "-fx-border-color: #8A59EE; "  + 
                                "-fx-border-radius: 5px;"      +
                                "-fx-border-width: 3px;");
        textUninstall.setFill(Color.BLACK);
    }
}
