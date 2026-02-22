package com.doideradev.doiderautils;

import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;

/**
 * Interface for controllers in the application. It defines methods for setting the style and actions of the controller.
 */
public interface Controller {

    /**
     * Set the style of the controller based on the dark mode value.
     * @param darkMode the value of the dark mode property - true if dark mode is enabled, false otherwise.
     */
    public void setElementsStyle(boolean darkMode);


    /**
     * Set the texts of the controller based on the dark mode value.
     * @param darkMode the value of the dark mode property - true if dark mode is enabled, false otherwise.
     */
    default void setTextsStyle(boolean darkMode) {};


    /**
     * Set the actions of the controller.
     */
    public void setActions();


    default void setTextTheme(boolean darkMode, Text[] texts, Label... labels) {
        if (darkMode) {
            for (Label label : labels)  {label.setTextFill(AppColorScheme.WHITE);}
            for (Text text : texts)     {text.setFill(AppColorScheme.WHITE);}
        } else {
            for (Label label : labels)  {label.setTextFill(AppColorScheme.BLACK);}
            for (Text text : texts)     {text.setFill(AppColorScheme.BLACK);}
        }
    }


    default void setTheme(boolean darkMode, Region... regions) {
        if (darkMode) {
            for (Region region : regions) {
                region.setBackground(Background.fill(AppColorScheme.BLACK));
            }
        } else {
            for (Region region : regions) {
                region.setBackground(Background.fill(AppColorScheme.WHITE));
            }
        }
    }
}
