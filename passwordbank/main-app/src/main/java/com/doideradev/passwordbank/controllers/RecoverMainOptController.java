package com.doideradev.passwordbank.controllers;

import com.doideradev.passwordbank.App;

import animatefx.animation.SlideInRight;
import animatefx.animation.ZoomIn;
import animatefx.animation.ZoomOut;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class RecoverMainOptController {
    
    
    @FXML private Button buttonConfirmEmail;
    @FXML private Label lHintAltEmail;
    @FXML private Label lHintMainEmail;
    @FXML private Label lHintPhoneNum;
    @FXML private Label labelAltEmail;
    @FXML private Label labelMainEmail;
    @FXML private Label labelPhoneNum;
    @FXML private TextField tFieldAltEmail;
    @FXML private TextField tFieldMainEmail;
    @FXML private TextField tFieldPhoneNum;
    @FXML private Text textAltEmail;
    @FXML private Text textEmalDesc;
    @FXML private Text textMainEmail;
    @FXML private Text textPhoneNum;
    @FXML private VBox vBoxMain;
    @FXML private VBox vBoxPhoneNum;

    
    @FXML private Button buttonNextQuestion;
    @FXML private ChoiceBox<String> cBoxQuestions;
    @FXML private GridPane gPaneQuestions;
    @FXML private Label labelHintAnswer;
    @FXML private TextArea tAreaAnswers;
    @FXML private Text textDescQuest;
    @FXML private VBox vBoxQuestions;

    public RecoverAccountController RecAccController;
    private String[] questions = {App.user.getQuestion1(), App.user.getQuestion2(), App.user.getQuestion3()};
    private String[] answers = {App.user.getAnswer1(), App.user.getAnswer2(), App.user.getAnswer3()};
    private int currentIndex = 0;

    

    public void initialize() {
        firstPageActions();
    }


    private void firstPageActions() {
        buttonConfirmEmail.getStyleClass().setAll("button-Confirm");
        buttonConfirmEmail.setText("Confirm");
        buttonConfirmEmail.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER) && verifyUserInfo()) {
                RecAccController.changePage(RecAccController.panePage2, true);
                secondPageActions();
            }
        });
        buttonConfirmEmail.setOnMouseClicked(event -> {
            if (verifyUserInfo()) {
                RecAccController.changePage(RecAccController.panePage2, true);
                secondPageActions();
            }
        });
        if (App.user.getMobileNumber() == null || App.user.getMobileNumber().isBlank()) {
            vBoxMain.getChildren().remove(vBoxPhoneNum);
        }
        
        textEmalDesc.setText("Confirm your registered emails in the fields bellow. ");

        labelMainEmail.setText("Your main Email");
        labelAltEmail.setText("Your alternative Email");
        lHintAltEmail.setVisible(false);
        lHintMainEmail.setVisible(false);
        lHintPhoneNum.setVisible(false);
        tFieldAltEmail.setOnKeyTyped(event -> {
            tFieldAltEmail.setStyle(null);
            lHintAltEmail.setStyle(null);
            lHintAltEmail.setVisible(false);
        });
        tFieldMainEmail.setOnKeyTyped(event -> {
            tFieldMainEmail.setStyle(null);
            lHintMainEmail.setStyle(null);
            lHintMainEmail.setVisible(false);
        });
        tFieldPhoneNum.setOnKeyTyped(event -> {
            tFieldPhoneNum.setStyle(null);
            lHintPhoneNum.setStyle(null);
            lHintPhoneNum.setVisible(false);
        });
        
        textMainEmail.setText(changeEmail(App.user.getMainEmail()));
        textAltEmail.setText(changeEmail(App.user.getAltEmail()));

    }
    private void secondPageActions() {
        buttonNextQuestion.getStyleClass().setAll("button-RightArrow");
        buttonNextQuestion.setText("Next");
        buttonNextQuestion.setOnKeyPressed(event -> {if(event.getCode().equals(KeyCode.ENTER))buttonQuestionsAction();});
        buttonNextQuestion.setOnMouseClicked(event -> buttonQuestionsAction());

        cBoxQuestions.setValue(questions[currentIndex]);
        tAreaAnswers.setOnKeyTyped(event -> {
            tAreaAnswers.setStyle(null);
            labelHintAnswer.setStyle(null);
            labelHintAnswer.setVisible(false);
        });
        labelHintAnswer.setVisible(false);
        textDescQuest.setText("Please provide the answer for the question " + (currentIndex+1));
    }


    private void buttonQuestionsAction() {
        if (verifyAnswer()) {
            currentIndex++;
            if (currentIndex < 3) {
                ZoomOut zoomOut = new ZoomOut(vBoxQuestions);
                ZoomIn zoomIn = new ZoomIn(vBoxQuestions);
                zoomOut.setSpeed(1.5);
                zoomOut.play();
                zoomIn.setSpeed(1.5);
                zoomIn.play();
                tAreaAnswers.clear();
                cBoxQuestions.setValue(questions[currentIndex]);
                textDescQuest.setText("Please provide the answer for the question " + (currentIndex+1));
            }
        }

        if (currentIndex == 3) {
            GridPane pane = (GridPane) gPaneQuestions.getParent();
            pane.getChildren().remove(gPaneQuestions);
            ColumnConstraints col0 = pane.getColumnConstraints().get(0);
            ColumnConstraints col1 = pane.getColumnConstraints().get(1);
            pane.getColumnConstraints().set(0, col1);
            pane.getColumnConstraints().set(1, col0);
            pane.add(gPaneQuestions, 0, 0);
            gPaneQuestions.getChildren().remove(vBoxQuestions);
            SlideInRight slide = new SlideInRight(pane);
            slide.play();

            textDescQuest.setText("All the information provided were analized and seems to be correct, " + 
                                  "you will be redirected to the page where you can see and modify your account information if you will");
            buttonNextQuestion.getStyleClass().setAll("button-Finish");
            buttonNextQuestion.setText("Finish");
            
            buttonNextQuestion.setOnMouseClicked(event2 -> {
                App.setDefAppSize();
                App.changePage("base");
                App.baseCtrlInstance.nextPage(App.baseCtrlInstance.loadPane("userInfo"));
            });
            buttonNextQuestion.setOnKeyPressed(event2 -> {
                if (event2.getCode().equals(KeyCode.ENTER)) {
                    App.setDefAppSize();
                    App.changePage("base");
                    App.baseCtrlInstance.nextPage(App.baseCtrlInstance.loadPane("userInfo"));
                }
            });
        }
    }


    private boolean verifyUserInfo() {
        if (tFieldMainEmail.getText().isBlank()) {
            tFieldMainEmail.setStyle("-fx-border-color: red; -fx-border-radius: 5px");
            lHintMainEmail.setStyle("-fx-text-fill: red;");
            lHintMainEmail.setText("Empty field");
            lHintMainEmail.setVisible(true);
            return false;    
        }
        if (!tFieldMainEmail.getText().equals(App.user.getMainEmail())) {
            tFieldMainEmail.setStyle("-fx-border-color: red; -fx-border-radius: 5px");
            lHintMainEmail.setStyle("-fx-text-fill: red;");
            lHintMainEmail.setText("Wrong email");
            lHintMainEmail.setVisible(true);
            return false;    
        }
        
        if (tFieldAltEmail.getText().isBlank()) {
            tFieldAltEmail.setStyle("-fx-border-color: red; -fx-border-radius: 5px");
            lHintAltEmail.setStyle("-fx-text-fill: red;");
            lHintAltEmail.setText("Empty field");
            lHintAltEmail.setVisible(true);
            return false;
        }
        if (!tFieldAltEmail.getText().equals(App.user.getAltEmail())) {
            tFieldAltEmail.setStyle("-fx-border-color: red; -fx-border-radius: 5px");
            lHintAltEmail.setStyle("-fx-text-fill: red;");
            lHintAltEmail.setText("Wrong email");
            lHintAltEmail.setVisible(true);
            return false;
        }
        
        if (vBoxMain.getChildren().contains(vBoxPhoneNum)) {
            if (!tFieldPhoneNum.getText().isBlank()) {
                tFieldAltEmail.setStyle("-fx-border-color: red; -fx-border-radius: 5px");
                lHintAltEmail.setStyle("-fx-text-fill: red;");
                lHintAltEmail.setText("Empty field");
                lHintAltEmail.setVisible(true);
                return false;
            }
            if (!tFieldPhoneNum.getText().equals(App.user.getMobileNumber())) {
                tFieldAltEmail.setStyle("-fx-border-color: red; -fx-border-radius: 5px");
                lHintAltEmail.setStyle("-fx-text-fill: red;");
                lHintAltEmail.setText("Wrong number");
                lHintAltEmail.setVisible(true);
                return false;
            }
        }
        return true;
    }


    private boolean verifyAnswer() {
        if (tAreaAnswers.getText().isBlank()) {
            tAreaAnswers.setStyle("-fx-border-radius: 5px; -fx-border-color: red;");
            labelHintAnswer.setText("The answer cannot be empty");
            labelHintAnswer.setStyle("-fx-text-fill: red");
            labelHintAnswer.setVisible(true);
            return false;
        }
        if (!tAreaAnswers.getText().equals(answers[currentIndex])) {
            tAreaAnswers.setStyle("-fx-border-radius: 5px; -fx-border-color: red;");
            labelHintAnswer.setText("The answer does not match the registered answer");
            labelHintAnswer.setStyle("-fx-text-fill: red");
            labelHintAnswer.setVisible(true);
            return false;
        }
        return true;
    }


    private String changeEmail(String email) {
        int at = email.indexOf("@");
        String toReplace = email.substring(3, at);
        String star = "";
        for (int i = 0; i < toReplace.length(); i++) {
            star += "*";
        }
        String emailMod = email.replace(toReplace, star);
        
        return emailMod;
    }
}