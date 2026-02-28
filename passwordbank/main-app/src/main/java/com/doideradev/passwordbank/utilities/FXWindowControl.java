package com.doideradev.passwordbank.utilities;

import java.util.ArrayList;
import java.util.List;

import com.doideradev.passwordbank.App;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class FXWindowControl {

    private final Stage primaryStage;
    private final double mouseResizeOffset = 2;
    private final double mouseDragOffset = mouseResizeOffset + 25;
    private final Button bMinimize, bMaximize, bClose;
    private final Node icon;
    private BorderPane parentNode;
    private Scene activeScene;

    
    ContextMenu menu;

    
    List<Node> borders = new ArrayList<>();
    private Node topBorder;
    private Node rightBorder;
    private Node bottomBorder;
    private Node leftBorder;
    private Node centerBorder;
    private String topStyle;
    private String rightStyle;
    private String bottomStyle;
    private String leftStyle;
    private String centerStyle;
    

    private boolean moveNorth,      moveSouth;
    private boolean moveEast,       moveWest;
    private boolean isResizable,    isDraggable;
    
    private double mouseXPos,         mouseYPos;
    private double stageWidth,      stageHeight;
    private double lastMouseXPos, lastMouseYPos;

    /**
     * This variable are used to track the last (x, y) position of the stage relative to the screen.
    */
    private double lastStageXPos, lastStageYPos;

    /**
     * These variables are used to track the last width and height of the stage, which is useful for restoring the stage to its previous size when it is maximized and then restored.
     */
    private double lastStageWidth, lastStageHeight;
    private double moveOffSetX,     moveOffSetY;
    private ResizeDirection dir;




    
    public FXWindowControl(Button minimize, Button maximize, Button close, Node icon) {
        bClose = close; bMaximize = maximize; bMinimize = minimize;
        this.icon = icon;
        primaryStage = App.getStage();
        activeScene = primaryStage.getScene();
        parentNode = (BorderPane) activeScene.getRoot();
        getBorders(parentNode);
        setScenePadding(true);
        setActions();
        tips();
    }



    /**
     * This method is responsible for setting the tooltips for the buttons in the tittle bar.
     */
    private void tips() {
        Tooltip closeTip = new Tooltip("Close");
        closeTip.setShowDelay(Duration.millis(500));
        closeTip.setAutoHide(false);
        bClose.setTooltip(closeTip);

        Tooltip maxTip = new Tooltip("Maximize");
        maxTip.setShowDelay(Duration.millis(500));
        maxTip.setAutoHide(false);
        bMaximize.setTooltip(maxTip);
        
        Tooltip minTip = new Tooltip("Minimize");
        minTip.setShowDelay(Duration.millis(500));
        minTip.setAutoHide(false);
        bMinimize.setTooltip(minTip);
    }



    private void setActions() {
        bClose.getStyleClass().setAll("button-close");
        bMaximize.getStyleClass().setAll("button-maximize");
        bMinimize.getStyleClass().setAll("button-minimize");

        windowMainOperations();
        createIconMenu();

        parentNode.setOnMouseMoved(moveHandle());
        parentNode.setOnMouseClicked(toggleMaximize());
        parentNode.setOnMouseDragged(dragHandle());
        parentNode.setOnMousePressed(event -> {
            lastMouseXPos = event.getScreenX();
            lastMouseYPos = event.getScreenY();
            lastStageXPos = primaryStage.getX();
            lastStageYPos = primaryStage.getY();
            stageHeight = primaryStage.getHeight();
            stageWidth = primaryStage.getWidth();

            moveOffSetX = (lastStageXPos - lastMouseXPos);
            moveOffSetY = (lastStageYPos - lastMouseYPos);
        });
        parentNode.setOnMouseReleased(event -> {
            if (primaryStage.getHeight() < App.defH) {
                primaryStage.setHeight(App.defH);
            }
            if (primaryStage.getWidth() < App.defW) {
                primaryStage.setWidth(App.defW);
            }
        });
        icon.setOnMouseClicked(event -> {
            if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
                parentNode.setOnMouseClicked(null);
                closeApplication();
            }
            if (event.getButton().equals(MouseButton.SECONDARY)) {
                if (primaryStage.isMaximized()) {
                    menu.getItems().get(0).setDisable(true);
                    menu.getItems().get(2).setDisable(false);
                } else {
                    menu.getItems().get(0).setDisable(false);
                    menu.getItems().get(2).setDisable(true);
                }
                menu.show(icon, event.getScreenX()+15, event.getScreenY()+15);
            }
        });
    }
    
    

    /**
     * Create the context menu for the icon in the title bar, which contains the options to maximize, minimize, restore and close the application.
     */
    private void createIconMenu() {
        MenuItem maxItem = new MenuItem("maximize");
        maxItem.setOnAction(event -> doMaximizing());

        MenuItem minItem = new MenuItem("minimize");
        minItem.setOnAction(event -> primaryStage.setIconified(true));

        MenuItem restore = new MenuItem("restore");
        restore.setOnAction(event -> doMaximizing());
        
        MenuItem closeItem = new MenuItem("close");
        closeItem.setOnAction(event -> closeApplication());
        
        menu = new ContextMenu();
        menu.getItems().addAll(maxItem, minItem, restore, closeItem);
    }



    /**
     * Set the main operations of the window, such as closing, maximizing and minimizing. Also set the key combination for closing the application (Alt + F4).
     */
    private void windowMainOperations() {
        activeScene.setOnKeyPressed(event -> {
            if (event.isAltDown() && 
                event.getCode().equals(KeyCode.F4)) 
                closeApplication();
        });
        bClose.setOnMouseClicked(event -> closeApplication());
        bMaximize.setOnMouseClicked(event -> doMaximizing());
        bMinimize.setOnMouseClicked(event -> primaryStage.setIconified(true));
    }



    /**
     * This method is responsible for toggling the maximized state of the stage when the user double clicks on the title bar. It checks if the click count is 2, if the click is within the first 25 pixels of the scene (which is where the title bar is located) and if the primary mouse button was used. If all these conditions are met, it calls the doMaximizing() method to toggle the maximized state of the stage.
     * @return an EventHandler that handles the mouse click event for toggling the maximized state of the stage.
     */
    private EventHandler<MouseEvent> toggleMaximize() {
        return arg0 -> {
            if ((arg0.getClickCount() == 2) && 
                (arg0.getSceneY() <= 25) &&
                (arg0.getButton() == MouseButton.PRIMARY)) {
                doMaximizing();
            }
        };
    }


    /**
     * This method is responsible for maximizing and restoring the stage. When the stage is maximized, it removes the padding from the scene and removes the round corners from the borders. When the stage is restored, it restores the attributes that were removed when maximizing.
     */
    private void doMaximizing() {
        if (primaryStage.isMaximized()) {
            primaryStage.setWidth(lastStageWidth);
            primaryStage.setHeight(lastStageHeight);
            setScenePadding(true);
            restoreRoundCorners();
            primaryStage.setMaximized(false);
        } else {
            lastStageWidth = primaryStage.getWidth();
            lastStageHeight = primaryStage.getHeight();
            setScenePadding(false);
            removeRoundCorners();
            primaryStage.setMaximized(true);
        }

    }


    private final void closeApplication() {Platform.exit();}
    

    /**
     * This method is responsible for handling the mouse movement over the stage to determine if the user is trying to resize or drag the stage. It checks the position of the mouse relative to the edges of the stage and sets the appropriate flags for resizing and dragging. It also updates the cursor to indicate the possible actions (resizing or dragging) that can be performed based on the mouse position.
     * 
     * @return an EventHandler that handles the mouse movement event for determining the resizing and dragging actions for the stage.
     */
    private EventHandler<MouseEvent> moveHandle() {
        return event -> {
            {
                isResizable = isDraggable = false;
                moveNorth = moveEast = moveSouth = moveWest = false;
                parentNode.setCursor(Cursor.DEFAULT);
            }
            
            stageHeight = primaryStage.getHeight();
            stageWidth = primaryStage.getWidth();
            mouseXPos = event.getSceneX();
            mouseYPos = event.getSceneY();
            lastMouseXPos = event.getScreenX();
            lastMouseYPos = event.getScreenY();

            if (!primaryStage.isMaximized()) {
                if ((mouseYPos <= mouseDragOffset) && 
                    (mouseYPos > mouseResizeOffset)) {
                    isDraggable = true;
                    return;
                }

                // upper edge
                if (mouseYPos <= mouseResizeOffset) {
                    isResizable = moveNorth = true;
                    moveSouth = !moveNorth;
                } else moveNorth = false;

                // lower edge
                if (stageHeight - mouseYPos <= mouseResizeOffset) {
                    isResizable = moveSouth = true;
                    moveNorth = !moveSouth;
                } else moveSouth = false;
                
                // left edge
                if (mouseXPos <= mouseResizeOffset) {
                    isResizable = moveWest = true;
                    moveEast = !moveWest;
                } else moveWest = false;

                // right edge
                if ((stageWidth - mouseXPos) <= mouseResizeOffset) {
                    isResizable = moveEast = true;
                    moveWest = !moveEast;
                } else moveEast = false;
            }

            setDirectionToResize();
        };
        
    }


    /**
     * This method ir responsible for handling hte dragging and resizing of the stage when the user clicks and drags on the edges or corners of the stage.
     * @return an EventHandler that handles the mouse drag event for dragging and resizing the stage.
     */
    private EventHandler<MouseEvent> dragHandle() {
        return event -> {
            if (isResizable && (event.getButton() == MouseButton.PRIMARY)) {
                double newHeight, newWidth;
                double offset;
                switch (dir) {
                    case N:
                        newHeight = stageHeight - (event.getScreenY() - lastMouseYPos);
                        
                        if (newHeight > App.defH) {
                            primaryStage.setY(event.getScreenY());
                            primaryStage.setHeight(newHeight);
                            lastStageYPos = event.getScreenY();
                        } else {
                            primaryStage.setY(lastStageYPos);
                            primaryStage.setHeight(newHeight);
                            if (event.getScreenY() == lastStageYPos) {
                                primaryStage.setHeight(App.defH);
                            }
                        }
                        stageHeight = primaryStage.getHeight();
                        lastMouseYPos = event.getScreenY();
                        break;
    
                    case E:
                        newWidth = stageWidth + (event.getScreenX() - lastMouseXPos);
                        
                        if (newWidth > App.defW) {
                            primaryStage.setWidth(newWidth);
                            lastMouseXPos = event.getScreenX();
                        }
                        stageWidth = primaryStage.getWidth();
                        
                        break;
                        
                    case S:
                        offset = event.getScreenY() - lastMouseYPos;
                        
                        if (offset == 0) break;
                        newHeight = stageHeight + (offset);
                        
                        if (newHeight > App.defH) {
                            primaryStage.setHeight(newHeight);
                            lastMouseYPos = event.getScreenY();
                        }
                        
                        stageHeight = primaryStage.getHeight();
                        break;
                        
                    case W:
                        offset = event.getScreenX() - lastMouseXPos;

                        if (offset == 0) break;
                        newWidth = primaryStage.getWidth() - offset;
                        
                        if (newWidth > App.defW) {
                            primaryStage.setX(offset + primaryStage.getX());
                            primaryStage.setWidth(newWidth);
                            lastMouseXPos = event.getScreenX();
                        }
                        
                        break;
    
                    case NE:
                        newHeight = stageHeight - (event.getScreenY() - lastMouseYPos);
                        newWidth  = stageWidth  + (event.getScreenX() - lastMouseXPos);
    
                        if (newHeight > App.defH) {
                            primaryStage.setY(event.getScreenY());
                            primaryStage.setHeight(newHeight);
                            lastStageYPos = event.getScreenY();
                        } else {
                            primaryStage.setY(lastStageYPos);
                            primaryStage.setHeight(newHeight);
                            if (event.getScreenY() == lastStageYPos) {
                                primaryStage.setHeight(App.defH);
                            }
                        }
    
                        if (newWidth > App.defW) primaryStage.setWidth(newWidth);
                        else primaryStage.setWidth(newWidth);
    
                        stageHeight = primaryStage.getHeight();
                        stageWidth  = primaryStage.getWidth();
                        lastMouseYPos = event.getScreenY();
                        lastMouseXPos = event.getScreenX();
                        break;
    
                    case NW:
                        newHeight = stageHeight - (event.getScreenY() - lastMouseYPos);
                        newWidth  = stageWidth  - (event.getScreenX() - lastMouseXPos);
                        
                        if (newHeight > App.defH) {
                            primaryStage.setY(event.getScreenY());
                            primaryStage.setHeight(newHeight);
                            lastStageYPos = event.getScreenY();
                        } else {
                            primaryStage.setY(lastStageYPos);
                            primaryStage.setHeight(newHeight);
                            if (event.getScreenY() == lastStageYPos) {
                                primaryStage.setHeight(App.defH);
                            }
                        }
    
                        if (newWidth > App.defW) {
                            primaryStage.setX(event.getScreenX());
                            primaryStage.setWidth(newWidth);
                            lastStageXPos = event.getScreenX();
                        } else {
                            primaryStage.setX(lastStageXPos);
                            primaryStage.setWidth(newWidth);
                        }  
                        
                        stageHeight = primaryStage.getHeight();
                        stageWidth  = primaryStage.getWidth();
                        lastMouseXPos = event.getScreenX();
                        lastMouseYPos = event.getScreenY();
                        break;
    
                    case SE:
                        newHeight = stageHeight + (event.getScreenY() - lastMouseYPos);
                        newWidth = stageWidth + (event.getScreenX() - lastMouseXPos);
    
                        if (newHeight > App.defH) primaryStage.setHeight(newHeight);
                        else primaryStage.setHeight(newHeight);
                        if (newWidth > App.defW) primaryStage.setWidth(newWidth);
                        else primaryStage.setWidth(newWidth);
                        
                        stageHeight = primaryStage.getHeight();
                        stageWidth  = primaryStage.getWidth();
                        lastMouseXPos = event.getScreenX();
                        lastMouseYPos = event.getScreenY();
                        break;
    
                    case SW:
                        newHeight = stageHeight + (event.getScreenY() - lastMouseYPos);
                        newWidth = stageWidth - (event.getScreenX() - lastMouseXPos);
    
                        
                        if (newHeight > App.defH) primaryStage.setHeight(newHeight);
                        else primaryStage.setHeight(newHeight);
    
                        if (newWidth > App.defW) {
                            primaryStage.setX(event.getScreenX());
                            primaryStage.setWidth(newWidth);
                            lastStageXPos = event.getScreenX();
                        } else {
                            primaryStage.setX(lastStageXPos);
                            primaryStage.setWidth(newWidth);
                        }
                        
                        
                        stageHeight = primaryStage.getHeight();
                        stageWidth = primaryStage.getWidth();
                        lastMouseXPos = event.getScreenX();
                        lastMouseYPos = event.getScreenY();
                        break;
    
                    default:
                        break;
                };
            }
    
            if (isDraggable && event.getButton().equals(MouseButton.PRIMARY)) {
                primaryStage.setX(event.getScreenX() + moveOffSetX);
                primaryStage.setY(event.getScreenY() + moveOffSetY);
            }

        };
    }



    /**
     * This method is responsible for setting the padding of the scene when the stage is maximized or restored. When the stage is maximized, it removes the padding from the scene to allow the stage to occupy the entire screen. When the stage is restored, it sets the padding back to its original value to allow for resizing and dragging of the stage.
     * @param set a boolean value that indicates whether to set the padding (true) or remove it (false).
     */
    private void setScenePadding(boolean set) {
        if (set) parentNode.setPadding(new Insets(mouseResizeOffset));
        else     parentNode.setPadding(new Insets(0));
    }



    /**
     * This method is responsible for removing the round corners from the borders of the stage when it is maximized. This is done by iterating through the list of borders and setting their style to remove the border radius and background radius. This allows the stage to have sharp corners when it is maximized, which is a common behavior for maximized windows in most operating systems.
     */
    private void removeRoundCorners() {
        for (Node node : borders) {
            if (node != null) {
                node.setStyle(node.getStyle() + "-fx-border-radius: 0; -fx-background-radius: 0;");
            }
        }
    }
    



    private void restoreRoundCorners() {
        if (topBorder != null) topBorder.setStyle(topStyle);
        if (rightBorder != null) rightBorder.setStyle(rightStyle);
        if (bottomBorder != null) bottomBorder.setStyle(bottomStyle);
        if (leftBorder != null) leftBorder.setStyle(leftStyle);
        if (centerBorder != null) centerBorder.setStyle(centerStyle);
    }




    private void getBorders(BorderPane parentNode) {
        topBorder = parentNode.getTop();
        if (topBorder != null) {
            borders.add(topBorder);
            topStyle = topBorder.getStyle();
        }

        rightBorder = parentNode.getRight();
        if (rightBorder != null) {
            borders.add(rightBorder);
            rightStyle = rightBorder.getStyle();
        }

        bottomBorder = parentNode.getBottom();
        if (bottomBorder != null) {
            borders.add(bottomBorder);
            bottomStyle = bottomBorder.getStyle();
        }

        leftBorder = parentNode.getLeft();
        if (leftBorder != null) {
            borders.add(leftBorder);
            leftStyle = leftBorder.getStyle();
        }

        centerBorder = parentNode.getCenter();
        if (centerBorder != null) {
            borders.add(centerBorder);
            centerStyle = centerBorder.getStyle();
        }
    }




    private void setDirectionToResize() {
        if (moveNorth) {
            dir = ResizeDirection.N;
            parentNode.setCursor(Cursor.N_RESIZE);
            if (moveEast) {
                dir = ResizeDirection.NE;
                parentNode.setCursor(Cursor.NE_RESIZE);
            }
            if (moveWest) {
                dir = ResizeDirection.NW;
                parentNode.setCursor(Cursor.NW_RESIZE);
            }
            return;
        }
        if (moveSouth) {
            dir = ResizeDirection.S;
            parentNode.setCursor(Cursor.S_RESIZE);
            if (moveEast) {
                dir = ResizeDirection.SE;
                parentNode.setCursor(Cursor.SE_RESIZE);
            }  
            if (moveWest) {
                dir = ResizeDirection.SW;
                parentNode.setCursor(Cursor.SW_RESIZE);
            }
            return;
        }
        if (moveEast) {
            dir = ResizeDirection.E;
            parentNode.setCursor(Cursor.E_RESIZE);
        }
        if (moveWest) {
            dir = ResizeDirection.W;
            parentNode.setCursor(Cursor.W_RESIZE);
        }
        return;
    }



    
    private enum ResizeDirection {
        N, E, S, W, 
        NE, NW, SE, SW;
    }
}