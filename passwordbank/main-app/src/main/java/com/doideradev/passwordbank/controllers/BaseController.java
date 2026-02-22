package com.doideradev.passwordbank.controllers;

import com.doideradev.doiderautils.Controller;
import com.doideradev.doiderautils.SceneManager;
import com.doideradev.passwordbank.App;
import com.doideradev.passwordbank.utilities.FXWindowControl;

import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class BaseController implements Controller {

    @FXML private BorderPane bPaneMain;
    @FXML private Button buttonClose;
    @FXML private Button buttonHome;
    @FXML private Button buttonMaximize;
    @FXML private Button buttonMenu;
    @FXML private Button buttonMinimize;
    @FXML private Button buttonPassword;
    @FXML private Button buttonSettings;
    @FXML private GridPane gPaneMenu;
    @FXML private GridPane gPaneTitleBar;
    @FXML private GridPane gPaneTop;
    @FXML private ImageView appIcon;
    @FXML private ScrollPane sPaneMain;
    @FXML private StackPane stackPaneMain;

    private HomeScreenController homeCtrl;      private Pane homePane;
    private PasswordScreenController passCtrl;  private Pane passPane;
    private Pane settPane;
    
    private final Text menuText = new Text();
    private final Text homeText = new Text();
    private final Text passText = new Text();
    private final Text settText = new Text();
    
    private boolean menuMaximized = false;
    private final double minXMenu = 50;
    private final double maxXMenu = 230;


    public void initialize() {
        App.baseCtrlInstance = this;
        loadMainPages();
        changePage(homePane);
        setActions();
        createMenuText();
        setButtonsStyle();
        setElementsStyle(App.darkMode.get());
        editMenuButtonsSize(menuMaximized);

        App.getStage().setOnShowing(event -> {
            new FXWindowControl(buttonMinimize, buttonMaximize, buttonClose, appIcon);
        });
    }


    /**
     * Set the actions for the main buttons in the application
     */
    public void setActions() {
        buttonHome.setOnMouseClicked(event -> {
            if (App.logs != null) homeCtrl.loadHomePanels();
            changePage(homePane);
        });
        buttonPassword.setOnMouseClicked(event -> {
            changePage(passPane);
            passCtrl.printLogins();
        });
        buttonSettings.setOnMouseClicked(event -> changePage(settPane));
        
        buttonMenu.setOnMouseClicked(event -> {
            menuMaximized = !menuMaximized;
            if (menuMaximized) 
                 {gPaneMenu.setPrefWidth(maxXMenu);} 
            else {gPaneMenu.setPrefWidth(minXMenu);}
            editMenuButtonsSize(menuMaximized);
        });

        buttonMenu.setOnMouseMoved(event -> buttonMenu.setCursor(Cursor.HAND));
        buttonHome.setOnMouseMoved(event -> buttonHome.setCursor(Cursor.HAND));
        buttonPassword.setOnMouseMoved(event -> buttonPassword.setCursor(Cursor.HAND));
        buttonSettings.setOnMouseMoved(event -> buttonSettings.setCursor(Cursor.HAND));
    }


    /**
     * <p> Load the main pages (home, passwords and settings) and their controllers
    */
    private void loadMainPages() {
        Parent root1 = SceneManager.loadPage(App.class, "home");
        homeCtrl = (HomeScreenController) SceneManager.getController("home");
        homePane = (Pane) root1;
        
        Parent root2 = SceneManager.loadPage(App.class, "passwords");
        passCtrl = (PasswordScreenController) SceneManager.getController("passwords");
        passPane = (Pane) root2;
        
        Parent root3 = SceneManager.loadPage(App.class, "settings");
        settPane = (Pane) root3;
    }


    /**
     * Load a FXML file page and return it as a Pane object
     * @param pageName - Name of the FXML file to be loaded (without the .fxml extension)
     * @return The loaded page as a Pane object
    */
   protected Pane loadPane(String pageName) {
       var parent = SceneManager.loadPage(App.class, pageName);
        return (Pane) parent;
    }
    

    /**
     * Edit the size of the menu buttons and add/remove text depending on the menu state
     * @param isMenuMaximized - true if the menu is maximized, false if minimized
     */
    private void editMenuButtonsSize(boolean isMenuMaximized) {
        if (isMenuMaximized) {
            buttonHome.setPrefSize(maxXMenu-10, 40);
            buttonMenu.setPrefSize(maxXMenu-10, 40);
            buttonPassword.setPrefSize(maxXMenu-10, 40);
            buttonSettings.setPrefSize(maxXMenu-10, 40);
            buttonHome.setGraphic(homeText);
            buttonMenu.setGraphic(menuText);
            buttonPassword.setGraphic(passText);
            buttonSettings.setGraphic(settText);
        } else {
            buttonHome.setPrefSize(50, 40);
            buttonMenu.setPrefSize(50, 40);
            buttonPassword.setPrefSize(50, 40);
            buttonSettings.setPrefSize(50, 40);
            buttonHome.setGraphic(null);
            buttonMenu.setGraphic(null);
            buttonPassword.setGraphic(null);
            buttonSettings.setGraphic(null);
        }
    }


    /**
     * Create the text elements for the menu buttons
     */
    private void createMenuText(){
        menuText.setText("Menu");
        menuText.setFont(Font.font("Georgia", FontWeight.BOLD, FontPosture.REGULAR, 24));
        menuText.setFill(Color.WHITE);
        homeText.setText("Home");
        homeText.setFont(Font.font("Georgia", FontWeight.BOLD, FontPosture.REGULAR, 24));
        homeText.setFill(Color.WHITE);
        passText.setText("Passwords");
        passText.setFont(Font.font("Georgia", FontWeight.BOLD, FontPosture.REGULAR, 24));
        passText.setFill(Color.WHITE);
        settText.setText("Settings");
        settText.setFont(Font.font("Georgia", FontWeight.BOLD, FontPosture.REGULAR, 24));
        settText.setFill(Color.WHITE);
    }

    /**
     * Change the current page to the selected one
     * @param nextPane - Pane to be shown
     */
    private void changePage(Pane nextPane) {
        stackPaneMain.getChildren().clear();
        stackPaneMain.getChildren().add(nextPane);
    }

    /**
     * Go back to the previous page
     */
    protected void prevPage() {
        stackPaneMain.getChildren().remove(1);
    }

    /**
     * Go to the next selected page
     * @param page - Pane to be shown
     */
    protected void nextPage(Pane page) {
        stackPaneMain.getChildren().add(page);
    }

    /**
     * Set the style for the buttons in the application
     */
    private void setButtonsStyle(){
        buttonClose.getStyleClass().setAll("button-close");
        buttonMaximize.getStyleClass().setAll("button-maximize");
        buttonMinimize.getStyleClass().setAll("button-minimize");
        buttonHome.getStyleClass().setAll("button-home");
        buttonMenu.getStyleClass().setAll("button-menu");
        buttonPassword.getStyleClass().setAll("button-password");
        buttonSettings.getStyleClass().setAll("button-settings");
    }

    /**
     * Set the style of the main elements of the screen depending on the active theme
     */
    public void setElementsStyle(boolean darkMode) {
        bPaneMain.setBackground(Background.fill(Color.TRANSPARENT));
        gPaneTitleBar.setStyle("-fx-background-color: #292929aa; -fx-background-radius: 8 8 0 0; -fx-border-radius: 8 8 0 0;");
        gPaneTop.setStyle("-fx-background-color: #804DE5; -fx-background-radius: 8 8 0 0; -fx-border-radius: 8 8 0 0;");
        if (darkMode) {
            sPaneMain.setStyle(sPaneMain.getStyle() + "-fx-background-color: #292929;");
            gPaneMenu.setStyle("-fx-background-color: #3B3B3B;" +
            "-fx-background-radius: 0 0 0 8;" + 
            "-fx-border-radius: 0 0 0 8;");
        }
        else {
            sPaneMain.setStyle(sPaneMain.getStyle() + "-fx-background-color: WHITE;");
            gPaneMenu.setStyle("-fx-background-color: #8A8A8A;" +
                                "-fx-background-radius: 0 0 0 8;" + 
                                "-fx-border-radius: 0 0 0 8;");
        }
        setTheme(darkMode, stackPaneMain);
        
        Text[] texts = {menuText, homeText, passText, settText};
        setTextTheme(darkMode, texts);
    }


    /**
     * Node to be used as a reference for popups and error messages
     * @return A node in the current scene that is always present
     * <p> and does not interact with user actions
     */
    public Node sampleNode() {return appIcon;}
}