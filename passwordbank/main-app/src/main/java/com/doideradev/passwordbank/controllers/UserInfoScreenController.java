package com.doideradev.passwordbank.controllers;

import com.doideradev.doiderautils.Controller;
import com.doideradev.passwordbank.App;
import com.doideradev.passwordbank.model.AppUser;
import com.doideradev.passwordbank.utilities.QuestionsList;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

public class UserInfoScreenController implements Controller {

    @FXML private Button buttonBack;
    @FXML private Button buttonDeleteAcc;
    @FXML private Button buttonLogout;
    @FXML private Button buttonSaveChanges;
    @FXML private Button buttonShowPass;

    @FXML private ChoiceBox<String> cBoxQ1;
    @FXML private ChoiceBox<String> cBoxQ2;
    @FXML private ChoiceBox<String> cBoxQ3;
    
    @FXML private GridPane gPaneUserInfo;
    
    @FXML private Label labelA1;
    @FXML private Label labelA2;
    @FXML private Label labelA3;
    @FXML private Label labelAltEmail;
    @FXML private Label labelHintPass;
    @FXML private Label labelHintUsername;
    @FXML private Label labelHintAltEmail;
    @FXML private Label labelHintQuestions;
    @FXML private Label labelMainEmail;
    @FXML private Label labelPassword;
    @FXML private Label labelPhoneNum;
    @FXML private Label labelQ1;
    @FXML private Label labelQ2;
    @FXML private Label labelQ3;
    @FXML private Label labelUsername;
    
    @FXML private PasswordField pFieldPassword;
    
    @FXML private TextArea tAreaA1;
    @FXML private TextArea tAreaA2;
    @FXML private TextArea tAreaA3;
    
    @FXML private TextField tFieldAltEmail;
    @FXML private TextField tFieldMainEmail;
    @FXML private TextField tFieldPassword;
    @FXML private TextField tFieldPhoneNum;
    @FXML private TextField tFieldusername;
    
    @FXML private Text textAccountInfo;
    @FXML private Text textQuestions;
    @FXML private Text textRecovery;
    @FXML private Text textUserInfo;

    
    private final String labelStyleBad    = "-fx-text-fill: red;";
    private final String tFieldSavetStyle = "-fx-border-color: #4BFE03; -fx-border-width: 2px";
    private final String tFieldErrorStyle = "-fx-border-color: red; -fx-border-width: 2px;";
    private final String emailRegexGroup  = "((?>@gmail\\.com)|(?>@outlook\\.com)|(?>@yahoo\\.com)|(?>@hotmail\\.com))$";
    private final String genericRegexText = "^[\\w^(.\\-_)]+";
    private final String defQuestString   = "Select an item in list";
    private final String emptyText = "The field cannot be empty";
    private final String questString = "Select a valid value for the question";
    private boolean[] recInfoTyped = new boolean[7];
    private boolean recInfoFinal = false;
    

    private final QuestionsList list = new QuestionsList();


    public void initialize() {
        setActions();
        setTexts();
        setButtonsStyle();
        setElementsStyle(App.darkMode.get());
        setFields();
    }


    
    private void setFields() {
        AppUser user = App.user;
        tFieldMainEmail.setText(user.getMainEmail());
        pFieldPassword.setText(user.getPassword().getPass());
        tFieldusername.setText(user.getUsername());
        if (user.hasRecoverInfo()) {
            tFieldAltEmail.setText(user.getAltEmail());
            if (user.getMobileNumber() != null) 
                tFieldPhoneNum.setText(user.getMobileNumber());
            cBoxQ1.setItems(list.getList());
            cBoxQ1.setValue(user.getQuestion1());
            cBoxQ2.setItems(list.getList());
            cBoxQ2.setValue(user.getQuestion2());
            cBoxQ3.setItems(list.getList());
            cBoxQ3.setValue(user.getQuestion3());
            tAreaA1.setText(user.getAnswer1());
            tAreaA2.setText(user.getAnswer2());
            tAreaA3.setText(user.getAnswer3());
        } else {
            cBoxQ1.setItems(list.getList());
            cBoxQ1.setValue(defQuestString);
            cBoxQ2.setItems(list.getList());
            cBoxQ2.setValue(defQuestString);
            cBoxQ3.setItems(list.getList());
            cBoxQ3.setValue(defQuestString);
        }
    }


    public void setActions() {
        buttonBack.setOnMouseClicked(event -> {
            App.baseCtrlInstance.prevPage();
        });
        buttonLogout.setOnMouseClicked(event -> {
            App.stayLoggedIn = false;
            App.getStage().hide();
            App.setMinAppSize();
            App.changePage("start");
        });

        buttonDeleteAcc.setOnMouseClicked(event -> {
            if (App.deleteAccount()) {
                App.changePage("start");
            } else App.changePage("base");
        });
        buttonShowPass.setOnMousePressed(event -> {
            buttonShowPass.getStyleClass().setAll("button-ViewPass");
            buttonShowPass.requestFocus();
            tFieldPassword.setText(pFieldPassword.getText());
            pFieldPassword.setVisible(false);
            tFieldPassword.setVisible(true);
        });
        buttonShowPass.setOnMouseReleased(event -> {
            buttonShowPass.getStyleClass().setAll("button-HidePass");
            buttonShowPass.focusedProperty().not();
            tFieldPassword.clear();
            tFieldPassword.setVisible(false);
            pFieldPassword.setVisible(true);
        });

        tFieldMainEmail.setEditable(false);
        labelHintQuestions.setVisible(false);

        tFieldusername.setOnKeyTyped(event -> {
            tFieldusername.setStyle(null);
            labelHintUsername.setVisible(false);
        });
        pFieldPassword.setOnKeyTyped(event -> {
            pFieldPassword.setStyle(null);
            labelHintPass.setVisible(false);
        });
        tFieldAltEmail.setOnKeyTyped(event -> {
            if (tFieldAltEmail.getText().isBlank()) recInfoTyped[0] = false;
            else recInfoTyped[0] = true;
            tFieldAltEmail.setStyle(null);
            labelHintAltEmail.setVisible(false);
        });

        cBoxQ1.valueProperty().addListener(listener -> {
            if (!cBoxQ1.getSelectionModel().getSelectedItem().equals(defQuestString)) {
                recInfoTyped[1] = true;
                cBoxQ1.setStyle(null);
                labelHintQuestions.setVisible(false);
            } else recInfoTyped[1] = false;
        });
        cBoxQ2.valueProperty().addListener(listener -> {
            if (!cBoxQ2.getSelectionModel().getSelectedItem().equals(defQuestString)) {
                recInfoTyped[2] = true;
                cBoxQ2.setStyle(null);
                labelHintQuestions.setVisible(false);
            } else recInfoTyped[2] = false; 
        });
        cBoxQ3.valueProperty().addListener(listener -> {
            if (!cBoxQ3.getSelectionModel().getSelectedItem().equals(defQuestString)) {
                recInfoTyped[3] = true;
                cBoxQ3.setStyle(null);
                labelHintQuestions.setVisible(false);
            } else recInfoTyped[3] = false;
        });
        
        
        tAreaA1.setOnKeyTyped(event -> {
            if (tAreaA1.getText().isBlank()) recInfoTyped[4] = false;
            else recInfoTyped[4] = true;
        });
        tAreaA2.setOnKeyTyped(event -> {
            if (tAreaA3.getText().isBlank()) recInfoTyped[5] = false;
            else recInfoTyped[5] = true;
        });
        tAreaA3.setOnKeyTyped(event -> {
            if (tAreaA3.getText().isBlank()) recInfoTyped[6] = false;
            else recInfoTyped[6] = true;
        });
        
        tAreaA1.setOnMouseClicked(event -> {
            if (tAreaA1.getText().equals(emptyText)) {
                tAreaA1.clear();
            }
            labelHintQuestions.setVisible(false);
            tAreaA1.setStyle(null);
        });
        tAreaA2.setOnMouseClicked(event -> {
            if (tAreaA2.getText().equals(emptyText)) {
                tAreaA2.clear();
            }
            labelHintQuestions.setVisible(false);
            tAreaA2.setStyle(null);
        });
        tAreaA3.setOnMouseClicked(event -> {
            if (tAreaA3.getText().equals(emptyText)) {
                tAreaA3.clear();
            }
            labelHintQuestions.setVisible(false);
            tAreaA3.setStyle(null);
        });
        

        buttonSaveChanges.setOnMouseClicked(event -> {
            if (verifyFields()) {
                confirmView();
                saveChanges();
            }
        });
    }
    

    private void saveChanges() {
        AppUser user;
        user = App.user;
        user.setUsername(tFieldusername.getText());
        user.setPassword(pFieldPassword.getText());

        if (recInfoFinal) {
            user.setAltEmail(tFieldAltEmail.getText());
            if (!(tFieldPhoneNum.getText() == null)) {
                user.setMobileNumber(tFieldPhoneNum.getText());
            }
            
            user.setQuestion1(cBoxQ1.getSelectionModel().getSelectedItem());
            user.setQuestion2(cBoxQ2.getSelectionModel().getSelectedItem());
            user.setQuestion3(cBoxQ3.getSelectionModel().getSelectedItem());
            user.setAnswer1(tAreaA1.getText());
            user.setAnswer2(tAreaA2.getText());
            user.setAnswer3(tAreaA3.getText());

            user.setRecoverInfo(true);
        }
    }


    private boolean verifyFields() {
        boolean retVal = true;

        if (tFieldusername.getText().isEmpty()) {
            labelHintUsername.setText("Username cannot be empty");
            animateFieldsError(tFieldusername, labelHintUsername);
            retVal = false;
        }

        if (pFieldPassword.getText().isBlank()) {
            labelHintPass.setText("Password field cannot be empty");
            animateFieldsError(pFieldPassword, labelHintPass);
            retVal = false;
        } else if (pFieldPassword.getText().length() < 6) {
            labelHintPass.setText("Password is too short");
            animateFieldsError(pFieldPassword, labelHintPass);
            retVal = false;
        }
        
        labelHintQuestions.setStyle(labelStyleBad);
        
        for (boolean info : recInfoTyped) {
            if (info) {
                recInfoFinal = true;
                break;
            } else recInfoFinal = false;
        }
        if (recInfoFinal) {
            
            if (tFieldAltEmail.getText().isBlank()) {
                labelHintAltEmail.setText("Email cannot be empty");
                animateFieldsError(tFieldAltEmail,labelHintAltEmail);
                retVal = false;
            } else if (!tFieldAltEmail.getText().matches(genericRegexText + emailRegexGroup)) {
                labelHintAltEmail.setText("Email format is wrong");
                animateFieldsError(tFieldAltEmail,labelHintAltEmail);
                retVal = false;
            } 
            
            if (cBoxQ1.getSelectionModel().getSelectedItem().equals(defQuestString)) {
                cBoxQ1.setStyle(tFieldErrorStyle);
                labelHintQuestions.setText(questString);
                labelHintQuestions.setVisible(true);
                retVal = false;
            }
            if (cBoxQ2.getSelectionModel().getSelectedItem().equals(defQuestString)) {
                cBoxQ2.setStyle(tFieldErrorStyle);
                labelHintQuestions.setText(questString);
                labelHintQuestions.setVisible(true);
                retVal = false;
            }
            if (cBoxQ3.getSelectionModel().getSelectedItem().equals(defQuestString)) {
                cBoxQ3.setStyle(tFieldErrorStyle);
                labelHintQuestions.setText(questString);
                labelHintQuestions.setVisible(true);
                retVal = false;
            }
            
    
            if (tAreaA1.getText().isBlank()) {
                tAreaA1.setStyle(tFieldErrorStyle + labelStyleBad);
                tAreaA1.setText(emptyText);
                retVal = false;
            }
            if (tAreaA2.getText().isBlank()) {
                tAreaA2.setStyle(tFieldErrorStyle + labelStyleBad);
                tAreaA2.setText(emptyText);
                retVal = false;
            }
            if (tAreaA3.getText().isBlank()) {
                tAreaA3.setStyle(tFieldErrorStyle + labelStyleBad);
                tAreaA3.setText(emptyText);
                retVal = false;
            }
        } else cleanView(false);

        return retVal;
    }


    private void confirmView() {
        tFieldusername.setStyle(tFieldSavetStyle);
        pFieldPassword.setStyle(tFieldSavetStyle);
        tFieldMainEmail.setStyle(tFieldSavetStyle);
        if (recInfoFinal) {
            tFieldAltEmail.setStyle(tFieldSavetStyle);
            tFieldPhoneNum.setStyle(tFieldSavetStyle);
            cBoxQ1.setStyle(tFieldSavetStyle);
            cBoxQ2.setStyle(tFieldSavetStyle);
            cBoxQ3.setStyle(tFieldSavetStyle);
            tAreaA1.setStyle(tFieldSavetStyle);
            tAreaA2.setStyle(tFieldSavetStyle);
            tAreaA3.setStyle(tFieldSavetStyle);
        }
        cleanView(true);
    }


    private void cleanView(boolean delay) {
        if (delay) {
            new Thread(() -> {
                try {
                    Thread.sleep(2000);
                    tFieldusername.setStyle(null);
                    pFieldPassword.setStyle(null);
                    tFieldMainEmail.setStyle(null);
                
                    tFieldAltEmail.setStyle(null);
                    tFieldPhoneNum.setStyle(null);
                    cBoxQ1.setStyle(null);
                    cBoxQ2.setStyle(null);
                    cBoxQ3.setStyle(null);
                    tAreaA1.setStyle(null);
                    tAreaA2.setStyle(null);
                    tAreaA3.setStyle(null);
                    labelHintQuestions.setVisible(false);
                    
                } catch (Exception e) {
                }
            }).start();
        } else {
            tFieldusername.setStyle(null);
            pFieldPassword.setStyle(null);
            tFieldMainEmail.setStyle(null);
        
            tFieldAltEmail.setStyle(null);
            tFieldPhoneNum.setStyle(null);
            cBoxQ1.setStyle(null);
            cBoxQ2.setStyle(null);
            cBoxQ3.setStyle(null);
            tAreaA1.setStyle(null);
            tAreaA2.setStyle(null);
            tAreaA3.setStyle(null);
            labelHintQuestions.setVisible(false);
        }
    }


    private void animateFieldsError(TextField tField, Label labelHint) {
        labelHint.setStyle(labelStyleBad);
        labelHint.setVisible(true);
        tField.setStyle(tFieldErrorStyle);
    }


    private void setButtonsStyle() {
        buttonBack.getStyleClass().setAll("button-Prev");
        buttonDeleteAcc.getStyleClass().setAll("button-Delete");
        buttonLogout.getStyleClass().setAll("button-Logout");
        buttonSaveChanges.getStyleClass().setAll("button-SaveChange");
        buttonShowPass.getStyleClass().setAll("button-HidePass");
    }
    

    private void setTexts() {
        buttonLogout.setText("Log out");
        buttonDeleteAcc.setText("Delete account");
        buttonSaveChanges.setText("Save changes");
        labelA1.setText("Answer 1");
        labelA2.setText("Answer 2");
        labelA3.setText("Answer 3");
        labelAltEmail.setText("Alternative email");
        labelHintPass.setVisible(false);
        labelHintUsername.setVisible(false);
        labelMainEmail.setText("Main email");
        labelPassword.setText("Password");
        labelPhoneNum.setText("Phone number");
        labelQ1.setText("Question 1");
        labelQ2.setText("Question 2");
        labelQ3.setText("Question 3");
    }



    @Override
    public void setElementsStyle(boolean darkMode) {
        setTheme(darkMode, gPaneUserInfo);
        setTextTheme(darkMode, new Text[] {textAccountInfo, textQuestions, textRecovery, textUserInfo}, 
                                    new Label[] {labelA1, labelA2, labelA3, labelAltEmail, labelHintPass, labelHintUsername, 
                                                 labelMainEmail, labelPassword, labelPhoneNum, labelQ1, labelQ2, labelQ3});
    }
}