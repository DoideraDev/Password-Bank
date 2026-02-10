package com.doideradev.passwordbank.utilities;

import com.doideradev.passwordbank.App;
import com.doideradev.passwordbank.controllers.HomeScreenController;
import com.doideradev.passwordbank.model.Login;
import com.doideradev.passwordbank.utilities.AppEnums.FillType;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class PasswordFXElement {

    private Button buttonEdit;
    private Button buttonDelete;
    private Button bViewPass;
    private Button bCopyLogin;
    private Button bCopyPass;
    private HBox hBoxLogin;
    private HBox hBoxPass;
    private HBox hBoxButtons;
    private Label labelPass;
    private Label labelUser;
    private static Rectangle mainRectangle;
    private StackPane mainStackPane;
    private StackPane passStackPane;
    private PasswordField pFPass;
    private Text textIdentifier;
    private TextField tFPass;
    private TextField tFUser;

    /**
     * Main VBox containing all elements
     */
    private VBox mainVBox;

    /**
     * Inner VBox composed by user and password VBoxes
     */
    private VBox fieldVBox;

    private VBox userVBox;
    private VBox passVBox;

    private final double tFieldW = 250;
    private final double tFieldH = 30; 
    private final double innVBoxW = 300;
    private final double innVBoxH = 60;
    private boolean showDelete;

    private final String cssFile = "styles/passwords-buttons.css";
    private Login login;
    private FillType type = FillType.NORMAL;
    private HomeScreenController home;





    public PasswordFXElement(Login login, boolean showDelete) {
        this.login = login;
        this.showDelete = showDelete;
        createComponents();
        setComponents();
        completeFields();
        setActions();
    }

    /**
     * <p> Create a PasswordFXElement to be shown in the Home Screen.
     * 
     * <p> <b> NOTE: </b> This Constructor should be called only from HomeScreenController class.
     * @param login - Login object to be used to fill the fields of the element
     * @param showDelete - boolean to indicate if the delete button should be shown
     * @param fill - FillType to indicate the fill style of the element
     * @param home - HomeScreenController instance to be used for actions that require HomeScreenController methods
     */
    public PasswordFXElement(Login login, boolean showDelete, FillType fill,HomeScreenController home) {
        this(login, showDelete);
        this.type = fill;
        setRectFill();
        this.home = home;
    }


    /**
     * Instantiate all components for the PasswordFXElement
     */
    private void createComponents() {
        buttonDelete  = new Button();
        buttonEdit    = new Button();
        bViewPass     = new Button();
        bCopyLogin    = new Button();
        bCopyPass     = new Button();
        hBoxLogin     = new HBox();
        hBoxPass      = new HBox();
        hBoxButtons   = new HBox();
        labelPass     = new Label();
        labelUser     = new Label();
        mainRectangle = new Rectangle();
        mainStackPane = new StackPane();
        passStackPane = new StackPane();
        pFPass        = new PasswordField();
        textIdentifier = new Text();
        tFPass         = new TextField();
        tFUser         = new TextField();
        mainVBox       = new VBox();
        fieldVBox      = new VBox();
        userVBox       = new VBox();
        passVBox       = new VBox();
    }

    /**
     * Set all the components for the PasswordFXElement
     */
    private void setComponents() {
        mainStackPane.setAlignment(Pos.CENTER);
        mainStackPane.setPrefSize(350, 250);
        mainStackPane.setMinHeight(mainStackPane.getPrefHeight());
        mainStackPane.setMinWidth(mainStackPane.getPrefWidth());
        mainStackPane.setMaxHeight(mainStackPane.getPrefHeight());
        mainStackPane.setMaxWidth(mainStackPane.getPrefWidth());
        mainStackPane.setFocusTraversable(false);
        mainStackPane.setPrefSize(350, 250);

        mainRectangle.setWidth(350);
        mainRectangle.setHeight(250);
        mainRectangle.setFocusTraversable(false);
        mainRectangle.setArcWidth(20);
        mainRectangle.setArcHeight(20);
        setRectFill();

        setStokeByTheme();
        mainRectangle.setStrokeWidth(1.5);
        mainRectangle.setStrokeType(StrokeType.INSIDE);
        mainRectangle.setSmooth(true);
        
        mainVBox.setAlignment(Pos.TOP_LEFT);
        mainVBox.setSpacing(15);
        
        VBox.setMargin(textIdentifier, new Insets(10, 0, 0, 15));
        textIdentifier.setText("Identifier");
        textIdentifier.setFont(Font.font(null, FontWeight.BOLD, 20));
        textIdentifier.setFocusTraversable(false);

        fieldVBox.setAlignment(Pos.CENTER);
        fieldVBox.setSpacing(15);
        
        userVBox.setPrefSize(innVBoxW, innVBoxH);
        userVBox.setMinWidth(innVBoxW);
        userVBox.setMinHeight(innVBoxH);
        userVBox.setMaxWidth(innVBoxW);
        userVBox.setMaxHeight(innVBoxH);
        
        labelUser.setFocusTraversable(false);
        labelUser.setText("Login");
        labelUser.setFont(Font.font(15));
        
        tFUser.setPrefSize(tFieldW, tFieldH);
        tFUser.setMinSize(tFieldW, tFieldH);
        tFUser.setMaxSize(tFieldW, tFieldH);
        tFUser.setEditable(false);
        tFUser.setFocusTraversable(false);
        
        passVBox.setPrefSize(innVBoxW, innVBoxH);
        passVBox.setMinWidth(innVBoxW);
        passVBox.setMinHeight(innVBoxH);
        passVBox.setMaxWidth(innVBoxW);
        passVBox.setMaxHeight(innVBoxH);

        labelPass.setFocusTraversable(false);
        labelPass.setText("Password");
        labelPass.setFont(Font.font(15));

        passStackPane.setAlignment(Pos.CENTER_LEFT);
        passStackPane.setPrefSize(tFieldW, tFieldH);
        passStackPane.setMaxSize(tFieldW, tFieldH);
        
        tFPass.setPrefSize(tFieldW, tFieldH);
        tFPass.setMinSize(tFieldW, tFieldH);
        tFPass.setMaxSize(tFieldW, tFieldH);
        tFPass.setEditable(false);
        tFPass.setFocusTraversable(false);
        
        pFPass.setPrefSize(tFieldW, tFieldH);
        pFPass.setMinSize(tFieldW, tFieldH);
        pFPass.setMaxSize(tFieldW, tFieldH);
        pFPass.setText("Minha princess");
        pFPass.setEditable(false);
        pFPass.setFocusTraversable(false);

        bViewPass.setPrefSize(tFieldH, tFieldH);
        bViewPass.getStylesheets().setAll(App.class.getResource(cssFile).toExternalForm());
        bViewPass.getStyleClass().setAll("button-HidePass");
        bViewPass.setFocusTraversable(false);
        StackPane.setAlignment(bViewPass, Pos.CENTER_RIGHT);

        hBoxButtons.setAlignment(Pos.CENTER_RIGHT);
        hBoxButtons.setSpacing(20);
        hBoxButtons.setPadding(new Insets(0, 25, 0, 0));

        buttonDelete.setPrefSize(30, 30);
        buttonDelete.getStylesheets().setAll(App.class.getResource(cssFile).toExternalForm());
        buttonDelete.getStyleClass().setAll("button-Delete");
        buttonDelete.setFocusTraversable(false);
        buttonEdit.setPrefSize(30, 30);
        buttonEdit.getStylesheets().setAll(App.class.getResource(cssFile).toExternalForm());
        buttonEdit.getStyleClass().setAll("button-Edit");
        buttonEdit.setFocusTraversable(false);

        bCopyLogin.setPrefSize(30, 30);
        bCopyLogin.getStylesheets().setAll(App.class.getResource(cssFile).toExternalForm());
        bCopyLogin.getStyleClass().setAll("button-Copy");
        bCopyLogin.setFocusTraversable(false);

        bCopyPass.setPrefSize(30, 30);
        bCopyPass.getStylesheets().setAll(App.class.getResource(cssFile).toExternalForm());
        bCopyPass.getStyleClass().setAll("button-Copy");
        bCopyPass.setFocusTraversable(false);

        hBoxLogin.setSpacing(15);
        hBoxLogin.getChildren().addAll(tFUser, bCopyLogin);

        hBoxPass.setSpacing(15);
        hBoxPass.getChildren().addAll(passStackPane, bCopyPass);

        mainStackPane.getChildren().addAll(mainRectangle, mainVBox);
        mainVBox.getChildren().addAll(textIdentifier, fieldVBox);
        fieldVBox.getChildren().addAll(userVBox, passVBox, hBoxButtons);
        userVBox.getChildren().addAll(labelUser,  hBoxLogin);
        passVBox.getChildren().addAll(labelPass, hBoxPass);
        if (showDelete) {
            hBoxButtons.getChildren().addAll(buttonEdit, buttonDelete);
        } else hBoxButtons.getChildren().addAll(buttonEdit);
        passStackPane.getChildren().addAll(tFPass, pFPass, bViewPass);
    }


    /**
     * Set the fill color of the main rectangle according to the FillType
     * @see FillType
     */
    private void setRectFill() {
        switch (type) {
            case NORMAL: mainRectangle.setFill(Color.valueOf("#8A59EE"));
            break;
            case RED: mainRectangle.setFill(Color.valueOf("#FF0000"));
            break;
            case PUR: mainRectangle.setFill(Color.valueOf("#A843B3"));
            break;
        }
    }


    /**
     * Set the actions for all elements in the PasswordFXElement
     */
    private void setActions() {

        bViewPass.setOnMousePressed(event -> {
            this.login.increaseCount();
            tFPass.setText(pFPass.getText());
            pFPass.setVisible(false);
            bViewPass.getStyleClass().setAll("button-ViewPass");
        });
        bViewPass.setOnMouseReleased(event -> {
            tFPass.clear();
            pFPass.setVisible(true);
            bViewPass.getStyleClass().setAll("button-HidePass");
        });

        tFUser.setOnMouseDragged(event -> {tFUser.selectRange(0, 0);});
        tFUser.setOnMousePressed(event -> {tFUser.selectRange(0, 0);});
        pFPass.setOnMouseDragged(event -> {pFPass.selectRange(0, 0);});
        pFPass.setOnMousePressed(event -> {pFPass.selectRange(0, 0);});

        bCopyLogin.setOnMouseClicked(event -> {
            this.login.increaseCount();
            Clipboard clipboard = Clipboard.getSystemClipboard();
            ClipboardContent content = new ClipboardContent();
            content.putString(tFUser.getText());
            clipboard.setContent(content);

            Tooltip tip = new Tooltip("user copied to clipboard");
            Tooltip.install(tFUser, tip);
            tip.setAutoHide(true);
            tip.setShowDuration(Duration.millis(800));
            tip.show(bViewPass, event.getScreenX(), event.getScreenY());
        });
        bCopyPass.setOnMouseClicked(event -> {
            Clipboard clipboard = Clipboard.getSystemClipboard();
            ClipboardContent content = new ClipboardContent();
            content.putString(pFPass.getText());
            clipboard.setContent(content);
            
            Tooltip tip = new Tooltip("password copied to clipboard");
            Tooltip.install(pFPass, tip);
            tip.setAutoHide(true);
            tip.setShowDuration(Duration.millis(800));
            tip.show(bViewPass, event.getScreenX(), event.getScreenY());
            
            this.login.increaseCount();
        });

        buttonEdit.setOnMouseClicked(event -> {
            ModalManager modal = new ModalManager(this.login, bViewPass, ModalState.EDIT);
            modal.showModal();
            this.login = modal.getLoginUpdated();
            completeFields();
        });
        buttonDelete.setOnMouseClicked(event -> {
            GridPane.clearConstraints(this.mainStackPane);
            GridPane parent = (GridPane) this.mainStackPane.getParent();
            parent.getChildren().remove(this.mainStackPane);

            App.logs.remove(login);
            login = null;
            this.mainStackPane = null;
        });
    }

    /**
     * Fill all fields with the login data
     */
    private void completeFields() {
        textIdentifier.setText(login.getIdentifier());
        tFUser.setText(login.getUserName());
        pFPass.setText(login.getPassword().getPass());
        if (home != null) home.findOldestPass();
    }


    public Node getRoot() {return this.mainStackPane;}


    /**
     * Set the stroke color of the main rectangle according to the current theme
     */
    public static void setStokeByTheme() {
        if (App.darkMode) 
             mainRectangle.setStroke(Color.WHITE);
        else mainRectangle.setStroke(Color.BLACK);
    }
}