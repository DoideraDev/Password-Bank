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
    private double lastStageXPos, lastStageYPos;
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

        parentNode.setOnMouseMoved(positionControl());
        parentNode.setOnMousePressed(event -> {
            lastMouseXPos = event.getScreenX();
            lastMouseYPos = event.getScreenY();
            lastStageXPos = primaryStage.getX();
            lastStageYPos = primaryStage.getY();
            stageHeight = primaryStage.getHeight();
            stageWidth = primaryStage.getWidth();
            moveOffSetX = primaryStage.getX() - event.getScreenX();
            moveOffSetY = primaryStage.getY() - event.getScreenY();
        });
        parentNode.setOnMouseDragged(doStageAction());
        parentNode.setOnMouseReleased(event -> {
            if (primaryStage.getHeight() < App.defH) {
                primaryStage.setHeight(App.defH);
            }
            if (primaryStage.getWidth() < App.defW) {
                primaryStage.setWidth(App.defW);
            }
        });
        parentNode.setOnMouseClicked(toggleMaximize());
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



    private EventHandler<MouseEvent> toggleMaximize() {
        return new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent arg0) {
                if ((arg0.getClickCount() == 2) && 
                    (arg0.getSceneY() <= 25) &&
                    (arg0.getButton() == MouseButton.PRIMARY)) {
                    doMaximizing();
                }
            }
        };
    }


    private void doMaximizing() {
        if (primaryStage.isMaximized()) {
            primaryStage.setWidth(App.defW);
            primaryStage.setHeight(App.defH);
            setScenePadding(true);
            restoreRoundCorners();
            primaryStage.setMaximized(false);
        } else {
            setScenePadding(false);
            removeRoundCorners();
            primaryStage.setMaximized(true);
        }
    }


    private final void closeApplication() {
        Platform.exit();
    }
    

    private EventHandler<MouseEvent> positionControl() {
        return new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent arg0) {
                {
                    isResizable = isDraggable = false;
                    moveNorth = moveEast = moveSouth = moveWest = false;
                    parentNode.setCursor(Cursor.DEFAULT);
                }
                
                stageHeight = primaryStage.getHeight();
                stageWidth = primaryStage.getWidth();
                mouseXPos = arg0.getSceneX();
                mouseYPos = arg0.getSceneY();
                

                if (!primaryStage.isMaximized()) {
                    if ((mouseYPos <= mouseDragOffset) && 
                        (mouseYPos > mouseResizeOffset)) {
                        isDraggable = true;
                        return;
                    }

                    if (mouseYPos <= mouseResizeOffset) {
                        isResizable = moveNorth = true;
                        moveSouth = !moveNorth;
                    } else moveNorth = false;

                    if (stageHeight - mouseYPos <= mouseResizeOffset) {
                        isResizable = moveSouth = true;
                        moveNorth = !moveSouth;
                    } else moveSouth = false;
                    
                    if (mouseXPos <= mouseResizeOffset) {
                        isResizable = moveWest = true;
                        moveEast = !moveWest;
                    } else moveWest = false;

                    if ((stageWidth - mouseXPos) <= mouseResizeOffset) {
                        isResizable = moveEast = true;
                        moveWest = !moveEast;
                    } else moveEast = false;
                }

                setDirectionToResize();
            };
        };
    }



    private EventHandler<MouseEvent> doStageAction() {
        return new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent arg0) {
                if (isResizable && (arg0.getButton() == MouseButton.PRIMARY)) {
                    double newHeight, newWidth;
                    switch (dir) {
                        case N:
                            newHeight = stageHeight - (arg0.getScreenY() - lastMouseYPos);
                            
                            if (newHeight > App.defH) {
                                primaryStage.setY(arg0.getScreenY());
                                primaryStage.setHeight(newHeight);
                                lastStageYPos = arg0.getScreenY();
                            } else {
                                primaryStage.setY(lastStageYPos);
                                primaryStage.setHeight(newHeight);
                                if (arg0.getScreenY() == lastStageYPos) {
                                    primaryStage.setHeight(App.defH);
                                }
                            }
                            stageHeight = primaryStage.getHeight();
                            lastMouseYPos = arg0.getScreenY();
                            break;

                        case E:
                            newWidth = stageWidth + (arg0.getScreenX() - lastMouseXPos);

                            if (newWidth > App.defW) primaryStage.setWidth(newWidth);
                            else primaryStage.setWidth(newWidth);

                            stageWidth = primaryStage.getWidth();
                            lastMouseXPos = arg0.getScreenX();
                            break;

                        case S:
                            newHeight = stageHeight + (arg0.getScreenY() - lastMouseYPos);

                            if (newHeight > App.defH) primaryStage.setHeight(newHeight);
                            else primaryStage.setHeight(newHeight);
                            
                            stageHeight = primaryStage.getHeight();
                            lastMouseYPos = arg0.getScreenY();
                            break;

                        case W:
                            newWidth = primaryStage.getWidth() - (arg0.getScreenX() - lastMouseXPos);
                            
                            if (newWidth > App.defW) {
                                primaryStage.setX(arg0.getScreenX());
                                primaryStage.setWidth(newWidth);
                                lastStageXPos = arg0.getScreenX();
                            } else {
                                primaryStage.setX(lastStageXPos);
                                primaryStage.setWidth(newWidth);
                            }
                            
                            lastMouseXPos = arg0.getScreenX();
                            break;

                        case NE:
                            newHeight = stageHeight - (arg0.getScreenY() - lastMouseYPos);
                            newWidth  = stageWidth  + (arg0.getScreenX() - lastMouseXPos);

                            if (newHeight > App.defH) {
                                primaryStage.setY(arg0.getScreenY());
                                primaryStage.setHeight(newHeight);
                                lastStageYPos = arg0.getScreenY();
                            } else {
                                primaryStage.setY(lastStageYPos);
                                primaryStage.setHeight(newHeight);
                                if (arg0.getScreenY() == lastStageYPos) {
                                    primaryStage.setHeight(App.defH);
                                }
                            }

                            if (newWidth > App.defW) primaryStage.setWidth(newWidth);
                            else primaryStage.setWidth(newWidth);

                            stageHeight = primaryStage.getHeight();
                            stageWidth  = primaryStage.getWidth();
                            lastMouseYPos = arg0.getScreenY();
                            lastMouseXPos = arg0.getScreenX();
                            break;

                        case NW:
                            newHeight = stageHeight - (arg0.getScreenY() - lastMouseYPos);
                            newWidth  = stageWidth  - (arg0.getScreenX() - lastMouseXPos);
                            
                            if (newHeight > App.defH) {
                                primaryStage.setY(arg0.getScreenY());
                                primaryStage.setHeight(newHeight);
                                lastStageYPos = arg0.getScreenY();
                            } else {
                                primaryStage.setY(lastStageYPos);
                                primaryStage.setHeight(newHeight);
                                if (arg0.getScreenY() == lastStageYPos) {
                                    primaryStage.setHeight(App.defH);
                                }
                            }

                            if (newWidth > App.defW) {
                                primaryStage.setX(arg0.getScreenX());
                                primaryStage.setWidth(newWidth);
                                lastStageXPos = arg0.getScreenX();
                            } else {
                                primaryStage.setX(lastStageXPos);
                                primaryStage.setWidth(newWidth);
                            }  
                            
                            stageHeight = primaryStage.getHeight();
                            stageWidth  = primaryStage.getWidth();
                            lastMouseXPos = arg0.getScreenX();
                            lastMouseYPos = arg0.getScreenY();
                            break;

                        case SE:
                            newHeight = stageHeight + (arg0.getScreenY() - lastMouseYPos);
                            newWidth = stageWidth + (arg0.getScreenX() - lastMouseXPos);

                            if (newHeight > App.defH) primaryStage.setHeight(newHeight);
                            else primaryStage.setHeight(newHeight);
                            if (newWidth > App.defW) primaryStage.setWidth(newWidth);
                            else primaryStage.setWidth(newWidth);
                            
                            stageHeight = primaryStage.getHeight();
                            stageWidth  = primaryStage.getWidth();
                            lastMouseXPos = arg0.getScreenX();
                            lastMouseYPos = arg0.getScreenY();
                            break;

                        case SW:
                            newHeight = stageHeight + (arg0.getScreenY() - lastMouseYPos);
                            newWidth = stageWidth - (arg0.getScreenX() - lastMouseXPos);

                            
                            if (newHeight > App.defH) primaryStage.setHeight(newHeight);
                            else primaryStage.setHeight(newHeight);

                            if (newWidth > App.defW) {
                                primaryStage.setX(arg0.getScreenX());
                                primaryStage.setWidth(newWidth);
                                lastStageXPos = arg0.getScreenX();
                            } else {
                                primaryStage.setX(lastStageXPos);
                                primaryStage.setWidth(newWidth);
                            }
                            
                            
                            stageHeight = primaryStage.getHeight();
                            stageWidth = primaryStage.getWidth();
                            lastMouseXPos = arg0.getScreenX();
                            lastMouseYPos = arg0.getScreenY();
                            break;

                        default:
                            break;
                    };
                }

                if (isDraggable && arg0.getButton().equals(MouseButton.PRIMARY)) {
                    primaryStage.setX(arg0.getScreenX() + moveOffSetX);
                    primaryStage.setY(arg0.getScreenY() + moveOffSetY);
                }
            };
        };
    }




    private void setScenePadding(boolean set) {
        if (set) parentNode.setPadding(new Insets(mouseResizeOffset));
        else     parentNode.setPadding(new Insets(0));
    }


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