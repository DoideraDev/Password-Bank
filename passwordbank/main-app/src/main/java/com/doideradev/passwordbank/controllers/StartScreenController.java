package com.doideradev.passwordbank.controllers;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import com.doideradev.passwordbank.App;
import com.doideradev.passwordbank.model.AppUser;
import com.doideradev.passwordbank.model.Password;
import com.doideradev.passwordbank.utilities.QuestionsList;
import com.doideradev.doiderautils.Controller;
import com.doideradev.doiderautils.SceneManager;
import com.doideradev.doiderautils.popup.PopupError;

import java.awt.Desktop;

import animatefx.animation.Shake;
import animatefx.animation.SlideAnimation;
import animatefx.animation.SlideInDown;
import animatefx.animation.SlideInLeft;
import animatefx.animation.SlideInRight;
import animatefx.animation.SlideInUp;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class StartScreenController implements Controller {

    /*
     * Start screen components
     */
    @FXML private Button bViewPass;
    @FXML private Button buttonClose;
    @FXML private Button buttonLogReg;
    @FXML private CheckBox cBoxStayLogged;
    @FXML private Label lHintPasswordStart;
    @FXML private Label lHintUsernameStart;
    @FXML private Label lPassword;
    @FXML private Label lUsername;
    @FXML protected Pane pLogin;
    @FXML private PasswordField pFPassword;
    @FXML private StackPane stackPaneMain;
    @FXML private TextField tFPassword;
    @FXML private TextField tFUsername;
    @FXML private Text tForgotPass;
    @FXML private Text textUserGuide;
    @FXML private Text tWelcome;
    @FXML private VBox vBoxUserInfo;

    /*
     * Register screen 1 components
     */
    @FXML private Button bViewPassReg;
    @FXML private Button bViewPassRepReg;
    @FXML private Button buttonBackStartPage;
    @FXML private Button buttonNextPageReg;
    @FXML private Label lEmailReg;
    @FXML private Label lHintEmailReg;
    @FXML private Label lHintPassReg;
    @FXML private Label lHintPassRepReg;
    @FXML private Label lPasswordReg;
    @FXML private Label lPasswordRepReg;
    @FXML private PasswordField pFPasswordReg;
    @FXML private PasswordField pFPasswordRepReg;
    @FXML private TextField tFEmailReg;
    @FXML private TextField tFPasswordReg;
    @FXML private TextField tFPasswordRepReg;
    @FXML private Text textRegInfo;

    /*
     * Register screen 2 components
     */
    @FXML private Button buttonBackPageReg;
    @FXML private Button buttonNextPageRecovery;
    @FXML private CheckBox cBoxUsername;
    @FXML private Label lHintUsernameReg;
    @FXML private Label labelUsernameReg;
    @FXML private Pane paneRegister;
    @FXML private TextField tFUsernameReg;
    @FXML private Text textRegInfo2;

    /*
     * Recovery information screen presentation
     */
    @FXML private Button buttonNextPageRecovery2;
    @FXML private Button buttonSkipRecovery;
    @FXML private Text textRegRec1;

    /*
     * Recovery information screen 1
     */
    @FXML private Button buttonNextPageRecovery3;
    @FXML private CheckBox cBoxAltEmail;
    @FXML private Label lAltEmailRec;
    @FXML private Label lHintAltEmailRec;
    @FXML private Label lHintMainEmailRec;
    @FXML private Label lHintPhoneNum;
    @FXML private Label lMainEmailRec;
    @FXML private Label lPhoneNum;
    @FXML private TextField tFAltEmailRec;
    @FXML private TextField tFMainEmailRec;
    @FXML private TextField tFPhoneNum;
    @FXML private Text textRegRec2;

    /*
     * Recovery information screen 2
     */
    @FXML private Button buttonBackPageRec3;
    @FXML private Button buttonFinish;
    @FXML private ChoiceBox<String> cBoxQuestion1;
    @FXML private ChoiceBox<String> cBoxQuestion2;
    @FXML private ChoiceBox<String> cBoxQuestion3;
    @FXML private Label lAnswer1;
    @FXML private Label lAnswer2;
    @FXML private Label lAnswer3;
    @FXML private Label lHintQuestions;
    @FXML private Label lQuestion1;
    @FXML private Label lQuestion2;
    @FXML private Label lQuestion3;
    @FXML private TextArea tAAnswer1;
    @FXML private TextArea tAAnswer2;
    @FXML private TextArea tAAnswer3;
    @FXML private Text textRegRec3;


    private Pane panePage1;     private Pane panePage2;
    private Pane panePage3;     private Pane panePage4;
    private Pane panePage5;     private Pane forgotPage;

    private final String tFieldErrorStyle = "-fx-border-color: red;";
    private final String labelStyleGood   = "-fx-text-fill: green;";
    private final String labelStyleBad    = "-fx-text-fill: red;";
    private final String emailRegexGroup  = "((?>@gmail\\.com)|(?>@outlook\\.com)|(?>@yahoo\\.com)|(?>@hotmail\\.com))$";
    private final String genericRegexText = "^[\\w^(.\\-_)]+";
    private final String defQuestString   = "Select an item in list";
    private AppUser user;       private String password;
    private String username;
    private String mainEmail;   private String altEmail;
    private String phoneNum;
    private String question1;   private String answer1;
    private String question2;   private String answer2;
    private String question3;   private String answer3;

    private QuestionsList qList = new QuestionsList();
    

    public void initialize() {
        App.getStage().setOnShown(event -> {
            if (!App.haveUser) loadNextPages();
            else {user = App.user; loadForgotPasswordScene();}
        });
        setActions();
    }

    
    


    @Override
    public void setActions() {
        {// Start page actions
            buttonLogReg.getStyleClass().setAll("button-LogReg");
            bViewPass.getStyleClass().setAll("button-HidePass");
    
            bViewPass.setOnMousePressed(event -> {
                bViewPass.requestFocus();
                String pass = pFPassword.getText();
                tFPassword.setText(pass);
                pFPassword.setVisible(false);
                tFPassword.setVisible(true);
                bViewPass.getStyleClass().setAll("button-ViewPass");
            });
            bViewPass.setOnMouseReleased(event -> {
                bViewPass.focusedProperty().not();
                String pass = tFPassword.getText();
                pFPassword.setText(pass);
                tFPassword.setVisible(false);
                pFPassword.setVisible(true);
                bViewPass.getStyleClass().setAll("button-HidePass");
            });
            buttonClose.setOnMouseClicked(event -> Platform.exit());
            buttonClose.setOnMouseMoved(event -> buttonClose.setCursor(Cursor.HAND));
             
            {// Set start page layout and actions based on the user existence
                if (App.haveUser) {
                tWelcome.setText("Enter your login information");
                textUserGuide.setVisible(false);
                buttonLogReg.setText("Login");
                buttonLogReg.setOnMouseClicked(event -> {if (verifyStartFields()) goToApp();});
                buttonLogReg.setOnKeyPressed(event -> {
                    if (event.getCode().equals(KeyCode.ENTER) && verifyStartFields()) 
                        {goToApp();}
                });
                } else {
                    tWelcome.setLayoutY(280);
                    tWelcome.setText("Create an account to start using the application!");
                    vBoxUserInfo.setVisible(false);
                    textUserGuide.setVisible(true);
                    buttonLogReg.setLayoutY(400);
                    buttonLogReg.setText("Create account");
                    buttonLogReg.setOnMouseClicked(event -> goToPage1());
                    buttonLogReg.setOnKeyPressed(event -> {if (event.getCode().equals(KeyCode.ENTER)) goToPage1();});
                }
            }
    
            tFUsername.setOnKeyTyped(event -> {
                tFUsername.setStyle(null);
                lHintUsernameStart.setVisible(false);
            });
            pFPassword.setOnKeyTyped(event -> {
                pFPassword.setStyle(null);
                tFPassword.setStyle(null);
                lHintPasswordStart.setVisible(false);
            });
            cBoxStayLogged.setOnMouseClicked(event -> {
                if (cBoxStayLogged.isSelected()) {
                    App.stayLoggedIn = true;
                } else App.stayLoggedIn = false;
            });
            cBoxStayLogged.setOnKeyReleased(event -> {
                if (event.getCode().equals(KeyCode.ENTER) ||
                    event.getCode().equals(KeyCode.SPACE) ) {
                    if (cBoxStayLogged.isSelected()) {
                        App.stayLoggedIn = true;
                    } else App.stayLoggedIn = false;
                }
            });
            tForgotPass.setOnMousePressed(event -> tForgotPass.requestFocus());
            tForgotPass.focusedProperty().addListener((a, b, c) -> {
                if (c) tForgotPass.setFill(Color.BLUE);
                else tForgotPass.setFill(Color.WHITE);
            });
            tForgotPass.setOnKeyPressed(event -> {
                if (event.getCode().equals(KeyCode.ENTER)) {
                    changePage(true, forgotPage);
                }
            });
            tForgotPass.setOnMouseClicked(event -> {
                changePage(true, forgotPage);
            });
            textUserGuide.setText("User guide");
            textUserGuide.setOnMouseClicked(event -> openUserGuide());
            textUserGuide.setOnMousePressed(event -> textUserGuide.requestFocus());
            textUserGuide.setOnMouseReleased(event -> tWelcome.requestFocus());
            textUserGuide.focusedProperty().addListener((a, b, c) -> {
                if (c) textUserGuide.setFill(Color.BLUE);
                else textUserGuide.setFill(Color.WHITE);
            });
    
            lHintPasswordStart.setVisible(false);
            lHintUsernameStart.setVisible(false);
        }

        {// Register page 1 actions
            bViewPassReg.getStyleClass().setAll("button-HidePass");
            bViewPassRepReg.getStyleClass().setAll("button-HidePass");
            buttonBackStartPage.getStyleClass().setAll("button-LeftArrow");
            buttonNextPageReg.getStyleClass().setAll("button-RightArrow");
            
            bViewPassReg.setOnMousePressed(event -> {
                bViewPassReg.getStyleClass().setAll("button-ViewPass");
                bViewPassReg.requestFocus();
                tFPasswordReg.setText(pFPasswordReg.getText());
                pFPasswordReg.setVisible(false);
                tFPasswordReg.setVisible(true);
            });
            bViewPassReg.setOnMouseReleased(event -> {
                bViewPassReg.getStyleClass().setAll("button-HidePass");
                bViewPassReg.focusedProperty().not();
                tFPasswordReg.clear();
                tFPasswordReg.setVisible(false);
                pFPasswordReg.setVisible(true);
            });
            bViewPassRepReg.setOnMousePressed(event -> {
                bViewPassRepReg.getStyleClass().setAll("button-ViewPass");
                bViewPassRepReg.requestFocus();
                tFPasswordRepReg.setText(pFPasswordRepReg.getText());
                pFPasswordRepReg.setVisible(false);
                tFPasswordRepReg.setVisible(true);
            });
            bViewPassRepReg.setOnMouseReleased(event -> {
                bViewPassRepReg.getStyleClass().setAll("button-HidePass");
                bViewPassRepReg.focusedProperty().not();
                tFPasswordRepReg.clear();
                tFPasswordRepReg.setVisible(false);
                pFPasswordRepReg.setVisible(true);
            });
            buttonBackStartPage.setOnMouseClicked(event -> {
                App.getStage().setWidth(pLogin.getWidth());
                changePage(pLogin, false);
            });
            buttonNextPageReg.setOnMouseClicked(event -> goToPage2());
            buttonNextPageReg.setOnKeyPressed(event -> {if (event.getCode().equals(KeyCode.ENTER)) goToPage2();});
            tFEmailReg.setOnKeyTyped(event -> {
                tFEmailReg.setStyle(null);
                lHintEmailReg.setStyle(null);
                lHintEmailReg.setVisible(false);
            });
            pFPasswordReg.setOnKeyTyped(event -> {
                pFPasswordReg.setStyle(null);
                lHintPassReg.setStyle(null);
                lHintPassReg.setVisible(false);
            });
            pFPasswordRepReg.setOnKeyTyped(event -> {
                pFPasswordRepReg.setStyle(null);
                lHintPassRepReg.setStyle(null);
                lHintPassRepReg.setVisible(false);
                if (pFPasswordRepReg.getText().equals(pFPasswordReg.getText()) &&
                    !pFPasswordReg.getText().isBlank()) {
                    lHintPassRepReg.setText("the password matches");
                    lHintPassRepReg.setStyle(labelStyleGood);
                    lHintPassRepReg.setVisible(true);
                }
            });
            lHintEmailReg.setVisible(false);
            lHintPassReg.setVisible(false);
            lHintPassRepReg.setVisible(false);
            textRegInfo.setText("Please enter the following informations to register");
        }

        {// Register page 2 actions
            buttonBackPageReg.getStyleClass().setAll("button-LeftArrow");
            buttonNextPageRecovery.getStyleClass().setAll("button-RightArrow");

            buttonBackPageReg.setOnMouseClicked(event -> {
                changePage(panePage1, false);
            });
            buttonNextPageRecovery.setOnMouseClicked(event -> goToPage3());
            buttonNextPageRecovery.setOnKeyPressed(event -> {if (event.getCode().equals(KeyCode.ENTER)) goToPage3();});
            tFUsernameReg.setOnKeyTyped(event -> {
                tFUsernameReg.setStyle(null);
                lHintUsernameReg.setStyle(null);
                lHintUsernameReg.setVisible(false);
            });
            cBoxUsername.setOnMouseClicked(event -> checkBoxSelect1());
            cBoxUsername.setOnKeyReleased(event -> {
                if (event.getCode().equals(KeyCode.ENTER) || 
                    event.getCode().equals(KeyCode.SPACE)) {
                    checkBoxSelect1();
                }
            });
            labelUsernameReg.requestFocus();
            lHintUsernameReg.setVisible(false);
            textRegInfo2.setText("You can set an Username to use while logging instead of your email");
        }

        {// Recovery information page actions
            buttonSkipRecovery.getStyleClass().setAll("button-Skip");
            buttonNextPageRecovery2.getStyleClass().setAll("button-RightArrow");

            buttonSkipRecovery.setOnMouseClicked(event -> {createAccount();            goToApp();});
            buttonSkipRecovery.setOnKeyPressed(event -> {
                if(event.getCode().equals(KeyCode.ENTER)) {createAccount();            goToApp();}});
            buttonNextPageRecovery2.setOnMouseClicked(event -> goToPage4());
            buttonNextPageRecovery2.setOnKeyPressed(event -> {
                if (event.getCode().equals(KeyCode.ENTER)) goToPage4();});
            textRegRec1.setText("Please, fill the following fields to help secure your account. \nDon't worry, you can do it latter");
        }

        {// Recovery page 1 actions
            buttonNextPageRecovery3.getStyleClass().setAll("button-RightArrow");

            buttonNextPageRecovery3.setOnMouseClicked(event -> goToPage5());
            buttonNextPageRecovery3.setOnKeyPressed(event -> {
                if (event.getCode().equals(KeyCode.ENTER)) goToPage5();});
            tFAltEmailRec.setOnKeyTyped(event -> {
                tFAltEmailRec.setStyle(null);
                lHintAltEmailRec.setStyle(null);
                lHintAltEmailRec.setVisible(false);
            });
            tFMainEmailRec.setOnKeyTyped(event -> {
                tFMainEmailRec.setStyle(null);
                lHintMainEmailRec.setStyle(null);
                lHintMainEmailRec.setVisible(false);
            });
            tFPhoneNum.setOnKeyTyped(event -> {
                tFPhoneNum.setStyle(null);
                lHintPhoneNum.setStyle(null);
                lHintPhoneNum.setVisible(false);
            });
            cBoxAltEmail.setOnMouseClicked(event -> checkBoxSelect2());
            cBoxAltEmail.setOnKeyReleased(event -> {
                if (event.getCode().equals(KeyCode.ENTER) || 
                    event.getCode().equals(KeyCode.SPACE)) {
                    checkBoxSelect2();
                }
            });
            tFPhoneNum.setOnKeyTyped(event -> {
                if (tFPhoneNum.getText().isEmpty()) lHintPhoneNum.setVisible(true);
                else lHintPhoneNum.setVisible(false);
            });
            lMainEmailRec.requestFocus();
            tFMainEmailRec.setText(mainEmail);
            lHintAltEmailRec.setVisible(false);
            lHintMainEmailRec.setVisible(false);
            lHintPhoneNum.setVisible(true);
            textRegRec2.setText("Fill theses fields carefully, this will be needed to recovery your account");
        }

        {// Recovery page 2 actions
            buttonBackPageRec3.getStyleClass().setAll("button-LeftArrow");
            buttonFinish.getStyleClass().setAll("button-Finish");

            buttonBackPageRec3.setOnMouseClicked(event -> {
                App.getStage().setHeight(600);
                changePage(panePage4, false);
            });
            buttonFinish.setOnMouseClicked(event -> {if (verifyQuestionFields()) finishRegistration();});
            buttonFinish.setOnKeyTyped(event ->     {if (verifyQuestionFields()) finishRegistration();});
            tAAnswer1.setOnMouseClicked(event -> {
                tAAnswer1.setStyle(null);
                tAAnswer1.clear();
                lHintQuestions.setVisible(false);
            });
            tAAnswer2.setOnMouseClicked(event -> {
                tAAnswer2.setStyle(null);
                tAAnswer2.clear();
                lHintQuestions.setVisible(false);
            });
            tAAnswer3.setOnMouseClicked(event -> {
                tAAnswer3.setStyle(null);
                tAAnswer3.clear();
                lHintQuestions.setVisible(false);
            });
            cBoxQuestion1.valueProperty().addListener(listener -> {
                if (!cBoxQuestion1.getSelectionModel().getSelectedItem().equals(defQuestString)) {
                    cBoxQuestion1.setStyle(null);
                    lHintQuestions.setVisible(false);
                }
            });
            cBoxQuestion2.valueProperty().addListener(listener -> {
                if (!cBoxQuestion2.getSelectionModel().getSelectedItem().equals(defQuestString)) {
                    cBoxQuestion2.setStyle(null);
                    lHintQuestions.setVisible(false);
                }
            });
            cBoxQuestion3.valueProperty().addListener(listener -> {
                if (!cBoxQuestion3.getSelectionModel().getSelectedItem().equals(defQuestString)) {
                    cBoxQuestion3.setStyle(null);
                    lHintQuestions.setVisible(false);
                }
            });

            cBoxQuestion1.setItems(qList.getList());
            cBoxQuestion1.setValue(defQuestString);
            cBoxQuestion2.setItems(qList.getList());
            cBoxQuestion2.setValue(defQuestString);
            cBoxQuestion3.setItems(qList.getList());
            cBoxQuestion3.setValue(defQuestString);
            lHintQuestions.setVisible(false);
            textRegRec3.setText("Select a question and give an answer to each one, you will need this when recovering your account");

        }
    }
    

    private void goToPage1() {
        App.getStage().setWidth(500);
        changePage(panePage1, true);
        textRegInfo.requestFocus();
    }

    private void goToPage2() {
        if (verifyRegisterFields()) {
            mainEmail = tFEmailReg.getText();
            password  = pFPasswordReg.getText();
            changePage(panePage2, true);
        }
    }

    private void goToPage3() {
        if (tFUsernameReg.getText().isBlank() && !cBoxUsername.isSelected()) {
            lHintUsernameReg.setText("Please, fill the field or select the checkbox");
            animateFieldsError(tFUsernameReg, null, lHintUsernameReg, null);
        } else {
            username = tFUsernameReg.getText();
            changePage(panePage3, true);
        }
    }

    private void goToPage4() {
        changePage(panePage4, true);
    }

    private void goToPage5() {
        if (verifyRecoverFields()) {
            mainEmail = tFMainEmailRec.getText();
            if (cBoxAltEmail.isSelected()) {altEmail = mainEmail;} 
            else altEmail = tFAltEmailRec.getText();
            
            if (!tFPhoneNum.getText().isBlank()) {phoneNum = tFPhoneNum.getText();}
            App.getStage().setHeight(720);
            changePage(panePage5, true);
        }
    }


    private void createAccount() {
        this.user = new AppUser();
        App.user = user;
        App.haveUser = true;
        user.setMainEmail(mainEmail);
        user.setPassword(password);
        user.setUsername(username);
        user.setRecoverInfo(false);
    }


    private void finishRegistration() {
        question1 = cBoxQuestion1.getSelectionModel().getSelectedItem();
        question2 = cBoxQuestion2.getSelectionModel().getSelectedItem();
        question3 = cBoxQuestion3.getSelectionModel().getSelectedItem();
        answer1 = tAAnswer1.getText();
        answer2 = tAAnswer2.getText();
        answer3 = tAAnswer3.getText();
        phoneNum = tFPhoneNum.getText();
        createAccount();
        user.setAltEmail(altEmail);
        user.setAnswer1(answer1);
        user.setAnswer2(answer2);
        user.setAnswer3(answer3);
        user.setQuestion1(question1);
        user.setQuestion2(question2);
        user.setQuestion3(question3);
        user.setRecoverInfo(true);
        if (!tFPhoneNum.getText().isBlank()) {
            user.setMobileNumber(phoneNum);
        }
        goToApp();
    }

    
    private void goToApp() {
        App.setDefAppSize();  
        App.changePage("base");
    }


    private void checkBoxSelect1() {
        if (cBoxUsername.isSelected()) {
            tFUsernameReg.setText(mainEmail);
            username = mainEmail;
            tFUsernameReg.setStyle(null);
            lHintUsernameReg.setVisible(false);
        } else {tFUsernameReg.clear();  username = null;}
    }

    private void checkBoxSelect2() {
        if (cBoxAltEmail.isSelected()) {
            tFAltEmailRec.setText(mainEmail);
            tFAltEmailRec.setEditable(false);
        } else {
            tFAltEmailRec.clear();
            tFAltEmailRec.setEditable(true);
        }
    }


    private boolean verifyStartFields() {
        if (tFUsername.getText().isEmpty()) {
            lHintUsernameStart.setText("Username cannot be empty");
            animateFieldsError(tFUsername, null, lHintUsernameStart, null);
            return false;
        } else if (!tFUsername.getText().equals(user.getUsername())) {
            lHintUsernameStart.setText("Username is wrong");
            animateFieldsError(tFUsername, null, lHintUsernameStart, null);
            return false;
        }

        if (pFPassword.getText().isBlank()) {
            lHintPasswordStart.setText("Password field cannot be empty");
            animateFieldsError(tFPassword, pFPassword, lHintPasswordStart, bViewPass);
            return false;
        } else if (!Password.comparePasswords(pFPassword.getText(), user.getPassword())) {
            lHintPasswordStart.setText("Wrong password");
            animateFieldsError(tFPassword, pFPassword, lHintPasswordStart, bViewPass);
            return false;
        }
        return true;
    }


    private boolean verifyRegisterFields() {
        if (tFEmailReg.getText().isBlank()) {
            lHintEmailReg.setText("Email cannot be empty");
            animateFieldsError(tFEmailReg, null, lHintEmailReg, null);
            return false;
        } else if (!tFEmailReg.getText().matches(genericRegexText+emailRegexGroup)) {
            lHintEmailReg.setText("Email format incorrect");
            animateFieldsError(tFEmailReg, null, lHintEmailReg, null);
            return false;
        } 
        
        if (pFPasswordReg.getText().isBlank()) {
            lHintPassReg.setText("Password cannot be empty");
            animateFieldsError(tFPasswordReg, pFPasswordReg, lHintPassReg, bViewPassReg);
            return false;
        } else if (pFPasswordReg.getText().length() < 6) {
            lHintPassReg.setText("Password too short");
            animateFieldsError(tFPasswordReg, pFPasswordReg, lHintPassReg, bViewPassReg);
            return false;
        }
        
        if (pFPasswordRepReg.getText().isBlank()) {
            lHintPassRepReg.setText("Password cannot be empty");
            animateFieldsError(tFPasswordRepReg, pFPasswordRepReg, lHintPassRepReg, bViewPassRepReg);
            return false;
        } else if (!pFPasswordRepReg.getText().equals(pFPasswordReg.getText())) {
            lHintPassRepReg.setText("Password does not match");
            animateFieldsError(tFPasswordRepReg, pFPasswordRepReg, lHintPassRepReg, bViewPassRepReg);
            return false;
        }
        return true;
    }


    private boolean verifyRecoverFields() {
        if (tFMainEmailRec.getText().isBlank()) {
            lHintMainEmailRec.setText("Email cannot be empty");
            animateFieldsError(tFMainEmailRec, null, lHintMainEmailRec, null);
            return false;
        } else if (!tFMainEmailRec.getText().matches(genericRegexText+emailRegexGroup)){
            lHintMainEmailRec.setText("Email format is not valid");
            animateFieldsError(tFMainEmailRec, null, lHintMainEmailRec, null);
            return false;
        }
        
        if (tFAltEmailRec.getText().isBlank() && !cBoxAltEmail.isSelected()) {
            lHintAltEmailRec.setText("Email cannot be empty");
            animateFieldsError(tFAltEmailRec, null, lHintAltEmailRec, null);
            return false;
        } else if (!tFAltEmailRec.getText().matches(genericRegexText+emailRegexGroup)){
            lHintAltEmailRec.setText("Email format is not valid");
            animateFieldsError(tFAltEmailRec, null, lHintAltEmailRec, null);
            return false;
        }
        return true;
    }


    private boolean verifyQuestionFields() {
        String emptyText = "The field cannot be empty";
        String questString = "Select a valid item for question ";

        if (cBoxQuestion1.getSelectionModel().getSelectedItem().equals(defQuestString)) {
            cBoxQuestion1.setStyle(tFieldErrorStyle);
            lHintQuestions.setText(questString + "1");
            lHintQuestions.setStyle(labelStyleBad);
            lHintQuestions.setVisible(true);
            return false;
        }
        if (cBoxQuestion2.getSelectionModel().getSelectedItem().equals(defQuestString)) {
            cBoxQuestion2.setStyle(tFieldErrorStyle);
            lHintQuestions.setText(questString + "2");
            lHintQuestions.setStyle(labelStyleBad);
            lHintQuestions.setVisible(true);
            return false;
        }
        if (cBoxQuestion3.getSelectionModel().getSelectedItem().equals(defQuestString)) {
            cBoxQuestion3.setStyle(tFieldErrorStyle);
            lHintQuestions.setText(questString + "3");
            lHintQuestions.setStyle(labelStyleBad);
            lHintQuestions.setVisible(true);
            return false;
        }

        

        if (tAAnswer1.getText().isBlank()) {
            Shake shake = new Shake(tAAnswer1);
            tAAnswer1.setStyle(tFieldErrorStyle + labelStyleBad);
            tAAnswer1.setText(emptyText);
            lHintQuestions.setText("First answer is empty");
            lHintQuestions.setStyle(labelStyleBad);
            lHintQuestions.setVisible(true);
            shake.play();
            return false;
        }
        if (tAAnswer2.getText().isBlank()) {
            Shake shake = new Shake(tAAnswer2);
            tAAnswer2.setStyle(tFieldErrorStyle + labelStyleBad);
            tAAnswer2.setText(emptyText);
            lHintQuestions.setText("Second answer is empty");
            lHintQuestions.setStyle(labelStyleBad);
            lHintQuestions.setVisible(true);
            shake.play();
            return false;
        }
        if (tAAnswer3.getText().isBlank()) {
            Shake shake = new Shake(tAAnswer3);
            tAAnswer3.setStyle(tFieldErrorStyle + labelStyleBad);
            tAAnswer3.setText(emptyText);
            lHintQuestions.setText("Third answer is empty");
            lHintQuestions.setStyle(labelStyleBad);
            lHintQuestions.setVisible(true);
            shake.play();
            return false;
        }
        return true;
    }


    private void animateFieldsError(TextField tField, PasswordField pField, Label labelHint, Button button) {
        Shake shake = new Shake(tField);
        labelHint.setStyle(labelStyleBad);
        labelHint.setVisible(true);
        if (pField != null) {
            Shake shake2 = new Shake(pField);
            Shake shake3 = new Shake(button);
            pField.setStyle(tFieldErrorStyle);
            shake2.play();
            shake3.play();
        } else {tField.setStyle(tFieldErrorStyle);}
        shake.play();
    }



    private void openUserGuide() {
        try (InputStream input = App.class.getResourceAsStream("User guide.pdf")) { 
            Path tempFile = Files.createTempFile("User guide", ".pdf");
            Path newPath = Paths.get(tempFile.getParent() + "/User guide.pdf");

            Files.copy(input, tempFile, StandardCopyOption.REPLACE_EXISTING);
            Path path = Files.move(tempFile, newPath, StandardCopyOption.REPLACE_EXISTING);
            File userGuide = new File(path.toUri());

            Desktop desktop = Desktop.getDesktop();
            if (Desktop.isDesktopSupported()) {
                desktop.open(userGuide);
                userGuide.deleteOnExit();
            }

        } catch (Exception e) {
            new PopupError("Failed to open user guide - " + e.getMessage(), textUserGuide);
        }
    }


    /**
     * <p> Method used to animate transition betweeen pages
     * , this method is solely to be used inside the class.
     * @param pageToGo - the destination page (page to be animated)
     * @param isForward - determines if the transition is forward or not
     */
    private void animatePageTransition(Pane pageToGo, boolean isForward) {
        if (isForward) {
            SlideAnimation slide = new SlideInRight(pageToGo);
            slide.setSpeed(1.5);
            slide.play();
            stackPaneMain.getChildren().clear();
            stackPaneMain.getChildren().add(pageToGo);
        } else {
            SlideAnimation slide = new SlideInLeft(pageToGo);
            slide.setSpeed(1.5);
            slide.play();
            stackPaneMain.getChildren().clear();
            stackPaneMain.getChildren().add(pageToGo);
        }
    }
    

    /**
     * <p> Method used to animate transition betweeen pages
     * , although this method is {@code private} 
     * it is designed to be used by dependent classes.
     * 
     * <p> It is encapsulated by {@link com.doideradev.passwordbank.controllers.StartScreenController#changePage(boolean, Pane) changePage()}
     * 
     * @param pageToGo - the destination page (page to be animated)
     * @param isForward - determines if the transition is forward or not
     */
    private void animatePageTransition(boolean isForward, Pane pageToGo) {
        if (isForward) {
            App.getStage().setWidth(500);
            SlideAnimation slide = new SlideInUp(pageToGo);
            // slide.setSpeed(1.5);
            stackPaneMain.getChildren().clear();
            stackPaneMain.getChildren().add(pageToGo);
            slide.play();
        } else {
            App.getStage().setWidth(400);
            SlideAnimation slide = new SlideInDown(pageToGo);
            // slide.setSpeed(1.5);
            stackPaneMain.getChildren().clear();
            stackPaneMain.getChildren().add(pageToGo);
            slide.play();
        }
    }


    /**
     * <p> Method responsible to change the pages of this controller.
     * 
     * @param pageToGo - the page to change to.
     * @param isForward - determines if the changing is forward or not.
     */
    private void changePage(Pane pageToGo, boolean isForward) {
        animatePageTransition(pageToGo, isForward);
    }
    
    
    protected void changePage(boolean isForward, Pane pageToGo) {
        animatePageTransition(isForward, pageToGo);
    }




    private void loadNextPages() {
        SceneManager.setController("register1", this);
        var root1 = SceneManager.loadPage(App.class, "register1");
        panePage1 = (Pane) root1;

        SceneManager.setController("register2", this);
        var root2 = SceneManager.loadPage(App.class, "register2");
        panePage2 = (Pane) root2;

        SceneManager.setController("recoveryInfoPres", this);
        var root3 = SceneManager.loadPage(App.class, "recoveryInfoPres");
        panePage3 = (Pane) root3;

        SceneManager.setController("recoveryInfo1", this);
        var root4 = SceneManager.loadPage(App.class, "recoveryInfo1");
        panePage4 = (Pane) root4;
        
        SceneManager.setController("recoveryInfo2", this);
        var root5 = SceneManager.loadPage(App.class, "recoveryInfo2");
        panePage5 = (Pane) root5;
    }


    private void loadForgotPasswordScene() {
        var root = SceneManager.loadPage(App.class, "recoverPres");
        var controller = (RecoverAccountController) SceneManager.getController("recoveryPres");
        controller.startController = this;
        forgotPage = (Pane) root;
    }



    @Override
    public void setElementsStyle(boolean darkMode) {
        // no-op, the start screen has its own stylesheets and is not affected by the dark mode setting
    }
}