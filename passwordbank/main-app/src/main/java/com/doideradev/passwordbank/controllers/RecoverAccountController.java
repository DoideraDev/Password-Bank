package com.doideradev.passwordbank.controllers;

import com.doideradev.doiderautils.Controller;
import com.doideradev.doiderautils.SceneManager;
import com.doideradev.passwordbank.App;

import animatefx.animation.SlideInLeft;
import animatefx.animation.SlideInRight;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

public class RecoverAccountController implements Controller {

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
        setElementsStyle(false);
        if (App.user.hasRecoverInfo()) 
            {loadMainPages();}
        else loadAltPages();
    }
    
    
    public void setActions() {
        buttonBack.setOnMouseClicked(event -> startController.changePage(false, startController.pLogin));
        buttonConfirm.setOnMouseClicked(event -> changePage(panePage1, true));
        buttonConfirm.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                    changePage(panePage1, true);
                }
            });
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
        Parent root1 = SceneManager.loadPage(App.class, "recAccFirst");
        mainOptCtrl = (RecoverMainOptController) SceneManager.getController("recAccFirst");
        mainOptCtrl.RecAccController = this;
        panePage1 = (Pane) root1;
        
        SceneManager.setController("recAccFirst", mainOptCtrl);
        Parent root2 = SceneManager.loadPage(App.class, "recAccQuest");
        panePage2 = (Pane) root2;
    }
    

    private void loadAltPages() {
        Parent root1 = SceneManager.loadPage(App.class, "recAccAlt1");
        altOptCtrl = (RecoverAltOptController) SceneManager.getController("recAccAlt1");
        altOptCtrl.RecAccController = this;
        panePage1 = (Pane) root1;
        
        SceneManager.setController("recAccAlt2", altOptCtrl);
        Parent root2 = SceneManager.loadPage(App.class, "recAccAlt2");
        panePage2 = (Pane) root2;
    }


    @Override
    public void setElementsStyle(boolean darkMode) {
        buttonBack.getStyleClass().setAll("button-LeftArrow");
        buttonConfirm.getStyleClass().setAll("button-RightArrow");
        buttonConfirm.setText("Next");
        textPresentation.setText("We're sorry to know you can no longer access your account...\n\n" +
                                 "But don't worry, we can solve this problem,\n" + 
                                 "you just have to provide the necessery info.");
    }
}