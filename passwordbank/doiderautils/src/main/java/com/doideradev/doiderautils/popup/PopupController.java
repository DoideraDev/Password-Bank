package com.doideradev.doiderautils.popup;

import java.io.File;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Window;

public class PopupController {


    @FXML private Button buttonClose;
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
        buttonClose.setOnMouseEntered(e -> {buttonClose.setCursor(Cursor.HAND);});
        buttonClose.setOnMouseClicked(event -> closePopup());
    }


    private void closePopup() {
        Pane pane = (Pane) buttonClose.getParent();
        Window wind = pane.getParent().getScene().getWindow();
        wind.hide();
        PopupError.popupVisible = false;
    }


    public void setTextError(String text) {
        this.textError.setText(text);
    }
}
