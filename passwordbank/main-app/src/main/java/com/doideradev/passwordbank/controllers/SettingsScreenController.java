package com.doideradev.passwordbank.controllers;

import com.doideradev.doiderautils.Controller;
import com.doideradev.passwordbank.App;
import com.doideradev.passwordbank.utilities.UninstallerInitializer;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

public class SettingsScreenController implements Controller {

    @FXML private Button buttonAbout;
    @FXML private Button buttonAppearence;
    @FXML private Button buttonUninstall;
    @FXML private Button buttonUserInfo;
    @FXML private GridPane gPaneSettings;
    @FXML private Label labelAbout;
    @FXML private Label labelAppearence;
    @FXML private Label labelUninstall;
    @FXML private Label labelUserInfo;
    @FXML private Text textAbout;
    @FXML private Text textAppearence;
    @FXML private Text textSettings;
    @FXML private Text textTitle;
    @FXML private Text textUninstall;
    @FXML private Text textUserInfo;


    public void initialize() {
        setActions();
        setTexts();
        setButtonsStyle();
        setElementsStyle(App.darkMode.get());
    }


    /**
     * Set the actions for the buttons in the settings page
     */
    public void setActions() {
        buttonAbout.setOnMouseClicked(event -> changeTo("about"));
        buttonAppearence.setOnMouseClicked(event -> App.darkMode.set(!App.darkMode.get()));
        buttonAppearence.setOnMouseMoved(event -> buttonAppearence.setCursor(Cursor.HAND));
        buttonUserInfo.setOnMouseClicked(event -> changeTo("userInfo"));
        buttonUninstall.setOnMouseClicked(event -> startUninstaller());
    }


    /**
     * Change the current page to the given page.
     * @param page the page to change to.
     */
    private void changeTo(String page) {App.baseCtrlInstance.nextPage(App.baseCtrlInstance.loadPane(page));}


    private void setButtonsStyle() {
        buttonAbout.getStyleClass().setAll("button-Next");
        buttonUserInfo.getStyleClass().setAll("button-Next");
        buttonUninstall.getStyleClass().setAll("button-Uninstall");
    }


    public void setTexts() {
        buttonUninstall.setText("Uninstall");
        labelAbout.setText("Informations about the program such as application guide, version, and related informations");
        labelAppearence.setText("Click the button to switch bettween Dark and Light Mode");
        labelUserInfo.setText("All the informations related to the user, as the application Username and password");
        labelUninstall.setText("Uninstall Password Bank application and all related data such as your account and passwords registered");
    }



    private void startUninstaller() {
        UninstallerInitializer.initializeUninstaller();
    }


    @Override
    public void setElementsStyle(boolean darkMode) {
        if (App.darkMode.get()) 
            buttonAppearence.getStyleClass().setAll("button-DarkMode");
        else buttonAppearence.getStyleClass().setAll("button-LightMode");

        setTheme(darkMode, gPaneSettings);
        setTextTheme(darkMode, new Text[] {textAbout, textAppearence, textSettings, textUninstall, textUserInfo},
                                    new Label[] {labelAbout, labelAppearence, labelUninstall, labelUserInfo});
    }
}