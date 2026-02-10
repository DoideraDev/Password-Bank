package com.doideradev.uninstaller.utilities;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.doideradev.uninstaller.App;
import com.doideradev.uninstaller.controllers.PopupController;

// import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.stage.Popup;



/**
 * <p> 
 * This class is intended to be used only to show error popups.
 * 
 * <p> 
 * Every new instance created show its related popup automatically, 
 * but only one popup can be showed at a time.
 * 
 * <p> 
 * To create a new popup error just create a new instance of the class
 * passing the required parameters, 
 * two types of popups can be created by this class.
 * 
 * <p> <b> Error Log popup</b> - When this type of popup is created it also creates a log 
 * file of the error, giving the user the option to see the log file. 
 * <p> <b> Simple error popup </b> - this type of popup only show to the user a brief description of the error occoured
 * 
 */
public class PopupError {



    private Pane rootPane;
    private PopupController controller;
    private String textError;
    private Node node;
    private File error;

    
    public static boolean popupVisible = false; 
    // static {
    //     final SimpleBooleanProperty boolProp  = new SimpleBooleanProperty(popupVisible == true);
    //     boolProp.addListener((a, b, c) -> {
    //         if (c) {
    //             showPopup();
    //         }
    //     });
    // }
    
    



    /**
     * <p> Sole constructor for Error Log popups.
     * <p> This constructor will load, create and show a new popup if no other popup is being showed. 
     * 
     * <p> 
     * 
     * @param textToShow - Text description to be showed on the popup.
     * @param logText - List with all the information about the error, is list will be the content of the log file created. 
     * The list must contain the error message, cause and stack of the error.
     * @param activeNode - The node in wich the action caused the popup to be shown, 
     * the error were not caused by a node action, 
     * any other active node can be passed for this parameter.
     * <b> The node passed will be disabled after the popup is showed, 
     * to prevent the same error to happen and to prevent multiple 
     * identical log files be created. </b>
     * @throws IllegalArgumentException If the list passed to the constructor happens to be {@code null}.
     */
    public PopupError(String textToShow, Node activeNode, List<String> logText) throws IllegalArgumentException {
        this.textError = textToShow;
        this.node = activeNode;
        loadPopup();
        controller.setTextError(textToShow);

        if (logText != null) {
            error = createLog(logText);
            controller.logFile = error;
        }else 
            throw new IllegalArgumentException("The list cannot be null. Please fill all the paramaters of the constructor");
        
        if (!popupVisible) {
            showPopup();
            if (activeNode != null) activeNode.setDisable(true);
        } else error.delete();
    }
    
    
    /**
     * <p> Sole constructor for simple error popups.
     * <p> This constructor will load, create and show a simple popup with a brief discription of the error that occoured.
     * 
     * @param textToShow - Text description to be showed on the popup.
     * @param logText - List with all the information about the error, is list will be the content of the log file created. 
     * The list must contain the error message, cause and stack of the error.
     * @param activeNode - The node in wich the action caused the popup to be shown, 
     * the error were not caused by a node action, 
     * any other active node can be passed for this parameter.
     * <b> The node passed will be disabled after the popup is showed
     * to prevent the same error to happen and to prevent multiple 
     * identical log files be created. </b>
     */
    public PopupError(String textToShow, Node activeNode) {
        this.textError = textToShow;
        this.node = activeNode;
        loadPopup();
        controller.setTextError(textToShow);
        controller.hideButton();
        if (!popupVisible) {
            showPopup();
            activeNode.setDisable(true);
        }
    }



    private File createLog(List<String> logText) {
        try {
            File errorLog = new File(System.getProperty("user.dir") + "/app/logs/" + "Password Bank Uninstallation log error.txt");
            if (errorLog.createNewFile()) {
                FileWriter writer = new FileWriter(errorLog);
                for (String string : logText) {
                    writer.write(string);
                }
                writer.close();
            }
            return errorLog;
        } catch (IOException e) {
            textError = "The log file could no be created. The uninstalltion process was canceled.";
            controller.setTextError(textError);
            controller.hideButton();
            return null;
        }
    }



    private void showPopup() {
        Popup popup = new Popup();

        popup.getContent().addAll(rootPane);
        popup.setHideOnEscape(false);
        popup.setAutoHide(false);
        
        // Stage appWindow = (Stage) node.getScene().getWindow();
        // double popupX, popupY;
        // popupX = ((appWindow.getX() + appWindow.getWidth()/2) - 300 /* <- half modal width */);
        // popupY = ((appWindow.getY() + appWindow.getHeight()/2) - 200 /* <- half modal height*/);
        // popup.setX(popupX);
        // popup.setY(popupY);
        popup.centerOnScreen();
        popup.show(node.getScene().getWindow());
        popupVisible = true;
    }



    private void loadPopup() {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("views/popup.fxml"));
            rootPane = (Pane) loader.load();
            controller = loader.getController();

        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
            System.out.println(e.getMessage());
            System.out.println(e.getCause());
        }
    }

    public static List<String> createErrorList(Throwable errorCause, String errorMessage, StackTraceElement[] errorStack) {
        List<String> error  = new ArrayList<>();
        if (errorCause != null) {
            error.add("Cause: \n\t"+errorCause.toString()+"\n");
        }
        error.add("Message: \n\t"+errorMessage + "\n");
        error.add("Error stack: \n");
        for (StackTraceElement element : errorStack) {
            error.add("\t"+element.toString() + "\n");
        }
        return error;
    }
}
