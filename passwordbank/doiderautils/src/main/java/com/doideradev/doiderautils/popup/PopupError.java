package com.doideradev.doiderautils.popup;

import java.io.IOException;
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
 * <p> <b> Simple error popup </b> - this type of popup only show to the user a brief description of the error that occurred
 * 
 */
public class PopupError {

    private Pane rootPane;
    private Node node;
    private PopupController controller;

    public static boolean popupVisible = false; 
    
    
    /**
     * <p> Default constructor for simple error popups.
     * <p> This constructor will load, create and show a simple popup with a brief discription of the error that occoured.
     * 
     * @param textToShow - Text description to be showed on the popup.
     * @param activeNode - The node in wich the action caused the popup to be shown, 
     * if the error were not caused by a node action, 
     * any other active node can be passed for this parameter.
     * <b> The node passed will be disabled after the popup is showed
     * to prevent the same error to happen and to prevent multiple 
     * identical log files be created. </b>
     */
    public PopupError(String textToShow, Node activeNode) {
        this.node = activeNode;
        loadPopup();
        controller.setTextError(textToShow);
        if (!popupVisible) showPopup();
    }


    /**
     * Default method to show the popup
     */
    private void showPopup() {
        Popup popup = new Popup();

        popup.getContent().addAll(rootPane);
        popup.setHideOnEscape(false);
        popup.setAutoHide(false);
        
        popup.centerOnScreen();
        popup.show(node.getScene().getWindow());
        popupVisible = true;
    }



    /**
     * Load the popup FXML and controller
     */
    private void loadPopup() {
        try {
            FXMLLoader loader = new FXMLLoader(PopupError.class.getResource("/com/doideradev/doiderautils/views/popup.fxml"));
            rootPane = (Pane) loader.load();
            controller = loader.getController();

        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
            System.out.println(e.getMessage());
            System.out.println(e.getCause());
        }
    }
}
