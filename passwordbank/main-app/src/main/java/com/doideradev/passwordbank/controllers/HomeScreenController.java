package com.doideradev.passwordbank.controllers;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import com.doideradev.doiderautils.Controller;
import com.doideradev.passwordbank.App;
import com.doideradev.passwordbank.model.Login;
import com.doideradev.passwordbank.utilities.LoginList;
import com.doideradev.passwordbank.utilities.PasswordFXElement;
import com.doideradev.passwordbank.utilities.AppEnums.FillType;

import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class HomeScreenController implements Controller {
    

    @FXML private GridPane gPaneHome;
    @FXML private GridPane gPaneLastUsed;
    @FXML private GridPane gPaneOldstReg;
    @FXML private Text textLastUsed;
    @FXML private Text textOldestRegistered;  


    @FXML
    public void initialize() {
        setTexts();
        setElementsStyle(App.darkMode.get());
        setUpGridPanes();
        loadHomePanels();
    }
    
    
    private void setUpGridPanes() {
        gPaneLastUsed.setHgap(50);
        gPaneOldstReg.setHgap(50);
    }


    private void setTexts() {
        textLastUsed.setText("Most used passwords");
        textOldestRegistered.setText("Oldest registered passwords");
    }
    

    @Override
    public void setActions() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setActions'");
    }

    
    /**
     * Find the three most used passwords and show them in the grid pane
     * If two or more passwords have the same use count, the one with the oldest last edit date will be prioritized
     */
    public void findMostUsedPass() {
        gPaneLastUsed.getChildren().clear();
        LoginList logins = App.logs;
        LoginList updatedList = LoginList.getMostUsed(logins);
        int colCount = 0;

        gPaneLastUsed.getChildren().clear();
        for (Login login : updatedList) {
            PasswordFXElement pass = new PasswordFXElement(login, false);
            gPaneLastUsed.add(pass.getRoot(), colCount++, 0);
        }
    }
    
    /**
     * Find the three oldest edited passwords and show them in the grid pane
     * If two or more passwords have the same last edit date, the one with the highest use count will be prioritized
     */
    public void findOldestPass() {
        gPaneOldstReg.getChildren().clear();
        LoginList logins = App.logs;
        LoginList updatedList = LoginList.getOldestEdited(logins);
        int colCount = 0;

        gPaneOldstReg.getChildren().clear();
        for (Login login : updatedList) {
            PasswordFXElement pass = createPassFXElement(login);
            gPaneOldstReg.add(pass.getRoot(), colCount++, 0);
        }
    }


    /**
     * Create a PasswordFXElement to be shown in the screen.
     * The color of the element will change according to the time passed since the last edit date
     * @param login - Login object to be converted in a PasswordFXElement
     * @return PasswordFXElement created
     */
    private PasswordFXElement createPassFXElement(Login login) {
        long timePassed = ChronoUnit.DAYS.between(login.getLastEditDate(), LocalDate.now());
        if (timePassed >= 90) {
            return new PasswordFXElement(login, false, FillType.RED, this);
        }
        if (timePassed >= 60) {
            return new PasswordFXElement(login, false, FillType.PUR, this);
        }
        return new PasswordFXElement(login, false);
    }


    /**
     * Load the Password panels in the home screen (most used and oldest registered)
     */
    protected void loadHomePanels() {
        if ((App.logs != null) && (App.logs.size() > 2)) {
            findMostUsedPass();
            findOldestPass();
            PasswordFXElement.setStokeByTheme();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setElementsStyle(boolean darkMode) {
        setTheme(darkMode, gPaneHome);
        setTextsStyle(darkMode);
    }

    @Override
    public void setTextsStyle(boolean darkMode) {
        Text[] texts = {textLastUsed, textOldestRegistered};

        if (darkMode) for (Text text : texts)     {text.setFill(Color.WHITE);}
        else for (Text text : texts)     {text.setFill(Color.BLACK);}
    }

}