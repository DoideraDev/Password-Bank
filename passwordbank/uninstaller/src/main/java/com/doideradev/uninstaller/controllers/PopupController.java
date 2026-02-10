package com.doideradev.uninstaller.controllers;

import java.io.File;
import java.io.IOException;
import java.awt.Desktop;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class PopupController {


    @FXML private Button buttonClose;
    @FXML private Button buttonSeeMore;
    @FXML private Pane paneError;
    @FXML private Text textError;

    public File logFile;

    public void initialize() {
        setStyle();
        setActions();
    }


    private void setStyle() {
        paneError.setStyle(paneError.getStyle() + "-fx-background-color: WHITE;");
        textError.setFill(Color.BLACK);
    }


    private void setActions() {
        buttonClose.setOnMouseClicked(event -> closePopup());
        buttonSeeMore.setOnMouseClicked(event -> {
            try {
                Desktop desktop = Desktop.getDesktop();
                if (Desktop.isDesktopSupported()) {
                    desktop.open(logFile);
                    closePopup();
                }
            } catch (IOException e) {
                textError.setText("Sorry, the log file could not be opened...");
            }
        });
    }


    private void closePopup() {
        Platform.exit();
    }


    public void setTextError(String text) {
        this.textError.setText(text);
    }

    public void hideButton() {
        buttonSeeMore.setVisible(false);
    }
}
