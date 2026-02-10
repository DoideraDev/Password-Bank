package com.doideradev.passwordbank.controllers;

import java.util.Collections;

import com.doideradev.passwordbank.App;
import com.doideradev.passwordbank.model.Login;
import com.doideradev.passwordbank.utilities.LoginList;
import com.doideradev.passwordbank.utilities.ModalManager;
import com.doideradev.passwordbank.utilities.ModalState;
import com.doideradev.passwordbank.utilities.PasswordFXElement;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

public class PasswordScreenController {

    @FXML private Button buttonAddPass;
    @FXML private GridPane gPanePassword;
    @FXML private GridPane gPanePassReg;
    @FXML private TextField tFSearchPass;
    @FXML private Text textRegPass;

    private LoginList logins = App.logs;
    int colsMin = 3, colsMax = 4, limCount = 3;
    int minW = 350, minH = 300;
    int hgap = 50;

    ObservableList<Login> passwordsList;

    public void initialize() {
        setActions();
        setTexts();
        setTextTheme();
        setButtonsStyle();
        setUpGridPane();

        App.getStage().maximizedProperty().addListener((a, b, c) -> {
            if (c) setGridItems(colsMax, 100);
            else setGridItems(colsMin, 50);
        });
        App.getStage().widthProperty().addListener((a, b, c) -> {
            if (c.doubleValue() >= (1820)) 
                 setGridItems(colsMax, 100);
            else {
                setGridItems(colsMin, hgap);
                hgap = hgap + ((c.intValue() - b.intValue())/2);
                if (hgap < 50 || hgap > 300) hgap = 50;
            }
        });
        if (App.logs != null) {
            passwordsList = App.logs.getObservableList();
            changeListener();
        }
    }

    
    private void setGridItems(int colCount, int hGap) {
        limCount = colCount;
        gPanePassReg.setHgap(hGap);
        if (App.logs != null) printLogins();
    }
    
    private void setUpGridPane() {
        gPanePassReg.setHgap(50);
        gPanePassReg.setVgap(80);
    }


    /**
     * Print all the passwords stored in the application
     * <p> if the application has no logins registered, nothing will be printed
     */
    protected void printLogins() {
        if (App.logs != null) {    
            int rowCount = 0, colCount = 0;
            gPanePassReg.getChildren().clear();
            for (Login login : logins) {
                PasswordFXElement pass = new PasswordFXElement(login, true);
                gPanePassReg.add(pass.getRoot(), colCount++, rowCount);
                if (colCount == limCount) {
                    colCount = 0;
                    rowCount++;
                    gPanePassReg.addRow(rowCount);
                }
            }
            PasswordFXElement.setStokeByTheme();
        }
    }


    /**
     * Set the actions for the buttons in the password screen
     */
    private void setActions() {
        buttonAddPass.setOnMouseClicked(event -> createNewPass());
        tFSearchPass.setOnKeyTyped(event -> {
            if (tFSearchPass.getText().length() >= 3) {searchPass();}
            logins = App.logs;
            if (tFSearchPass.getText().isEmpty()) {printLogins();}
        });
    }
    

    /**
     * Create a new password entry
     */
    private void createNewPass() {
        ModalManager modal = new ModalManager(null, buttonAddPass, ModalState.CREATE);
        modal.showModal();

        Login newLogin = new Login();
        newLogin = modal.getLoginUpdated();
        
        if (newLogin != null) {
            if (App.logs == null) {
                logins = new LoginList(App.user);
                App.logs = logins;
            }
            logins.add(newLogin);
            Collections.sort(logins);
            printLogins();
        }
        modal = null;
        newLogin = null;
    }


    /**
     * Search for a password in the registered passwords
     */
    private void searchPass() {
        String searchedPass = tFSearchPass.getText();
        searchedPass = searchedPass.toLowerCase();
        java.util.Iterator<Login> it = logins.iterator();
        LoginList foundList = new LoginList(App.user);
        Login foundLog = new Login();

        while (it.hasNext()) {
            foundLog = it.next();
            if (foundLog.getIdentifier().toLowerCase().contains(searchedPass)) 
                foundList.add(foundLog);
        }
        logins = foundList;
        foundList = null;
        printLogins();
    }

    
    /**
     * Listen for changes in the passwords list and update the observable list accordingly
     */
    private void changeListener() {
        passwordsList.addListener(new ListChangeListener<Login>() {
            @Override
            public void onChanged(Change<? extends Login> c) {
                while (c.next()) if (c.wasRemoved()) printLogins();
            }
        });
    }


    private void setButtonsStyle() {buttonAddPass.getStyleClass().setAll("button-Add");}
    
    private void setTexts() {textRegPass.setText("Passwords registered");}

    protected void setTextTheme() {BaseController.setTextTheme(new Text[] {textRegPass});}
}