package com.doideradev.passwordbank.controllers;

import java.io.IOException;

import com.doideradev.passwordbank.App;

import animatefx.animation.SlideInLeft;
import animatefx.animation.SlideInRight;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

public class RecoverAccountController {

    @FXML private Button buttonBack;
    @FXML private Button buttonConfirm;
    @FXML private StackPane stackPaneMain;
    @FXML private Text textPresentation;


    /**
     * <p> A Pane representing either the {@code recAccFirst} or {@code recAccAlt1} root pane view, 
     * depending on {@link com.doideradev.passwordbank.model.AppUser#hasRecoverInfo() hasRecoverInfo} returning value.
     */
    Pane panePage1;
    
    /**
     * <p> A Pane representing either the {@code recAccQuest} or {@code recAccAlt2} root pane view, 
     * depending on {@link com.doideradev.passwordbank.model.AppUser#hasRecoverInfo() hasRecoverInfo} returning value.
     */
    Pane panePage2;
    
    StartScreenController startController;
    RecoverAltOptController altOptCtrl;
    RecoverMainOptController mainOptCtrl;


    public void initialize() {
        setActions();
        if (App.user.hasRecoverInfo()) 
            {loadMainPages();}
        else loadAltPages();
    }
    
    
    
    private void setActions() {
        buttonBack.getStyleClass().setAll("button-LeftArrow");
        buttonConfirm.getStyleClass().setAll("button-RightArrow");
        buttonConfirm.setText("Next");
        buttonBack.setOnMouseClicked(event -> startController.changePage(false, startController.pLogin));
        buttonConfirm.setOnMouseClicked(event -> changePage(panePage1, true));
        buttonConfirm.setOnKeyPressed(event -> {
        if (event.getCode().equals(KeyCode.ENTER)) {
                changePage(panePage1, true);
            }
        });
        textPresentation.setText("We're sorry to know you can no longer access your account...\n\n" +
                                 "But don't worry, we can solve this problem,\n" + 
                                 "you just have to provide the necessery info.");
    }
    

    protected void changePage(Pane pageToGo, boolean isForward) {
        transitionAnimation(pageToGo, isForward);
    }


    private void transitionAnimation(Pane pageToGo, boolean isForward) {
        if (isForward) {
            SlideInRight slide = new SlideInRight(pageToGo);
            slide.setSpeed(1.5);
            stackPaneMain.getChildren().clear();
            stackPaneMain.getChildren().add(pageToGo);
            slide.play();
        } else {
            SlideInLeft slide = new SlideInLeft(pageToGo);
            slide.setSpeed(1.5);
            stackPaneMain.getChildren().clear();
            stackPaneMain.getChildren().add(pageToGo);
            slide.play();
        }
    }

    
    private void loadMainPages() {
        try {
            FXMLLoader loader1 = new FXMLLoader(App.class.getResource("views/recAccFirst.fxml"));
            Parent root1 = loader1.load();
            mainOptCtrl = loader1.getController();
            mainOptCtrl.controller = this;
            panePage1 = (Pane) root1;
            
            FXMLLoader loader2 = new FXMLLoader(App.class.getResource("views/recAccQuest.fxml"));
            loader2.setController(mainOptCtrl);
            Parent root2 = loader2.load();
            panePage2 = (Pane) root2;
            
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.out.println(e.getCause());
            e.printStackTrace();
        }
    }
    
    private void loadAltPages() {
        try {
            FXMLLoader loader1 = new FXMLLoader(App.class.getResource("views/recAccAlt1.fxml"));
            Parent root1 = loader1.load();
            altOptCtrl = loader1.getController();
            altOptCtrl.controller = this;
            panePage1 = (Pane) root1;
            
            FXMLLoader loader2 = new FXMLLoader(App.class.getResource("views/recAccAlt2.fxml"));
            loader2.setController(altOptCtrl);
            Parent root2 = loader2.load();
            panePage2 = (Pane) root2;
            
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.out.println(e.getCause());
            e.printStackTrace();
        }
    }
}