package com.doideradev.passwordbank.model;

import java.io.Serializable;
import com.doideradev.doiderautils.QnASimpleList;
import com.doideradev.doiderautils.UtilsClasses.Answer;
import com.doideradev.doiderautils.UtilsClasses.Question;


public class AppUser implements Serializable {

    private static final long serialVersionUID = 100201L; 

    private Password password;
    private String mainEmail;
    private String username;
    private String altEmail;
    private String mobileNumber;
    private boolean darkMode;
    private boolean stayLoggedIn;
    private boolean haveRecoverInfo;
    private QnASimpleList recoveryQA;
    private boolean[] haveQA;
    private final int MAX_QA_PAIRS = 3;


    public AppUser() {    
        recoveryQA = new QnASimpleList(MAX_QA_PAIRS);
        haveQA = new boolean[MAX_QA_PAIRS];
        for (int i = 0; i < MAX_QA_PAIRS; i++) haveQA[i] = false;
    }

    

    public AppUser(String mainEmail, String password, String username, String altEmail, String mobileNumber, Question[] questions, Answer[] answers, boolean darkMode) {
        recoveryQA = new QnASimpleList(MAX_QA_PAIRS);
        haveQA = new boolean[MAX_QA_PAIRS];
        for (int i = 0; i < MAX_QA_PAIRS; i++) haveQA[i] = false;

        setMainEmail(mainEmail);
        setPassword(password);
        setUsername(username);
        setMobileNumber(mobileNumber);
        setDarkMode(darkMode);
        setQuestions(questions, answers);
    }



    public String getMainEmail() {return mainEmail;}
    public void setMainEmail(String userEmail) {this.mainEmail = userEmail;}

    public Password getPassword() {return this.password;}
    public void setPassword(String password) {this.password = new Password(password);}

    public String getUsername() {return this.username;}
    public void setUsername(String username) {this.username = username;}

    public String getAltEmail() {return this.altEmail;}
    public void setAltEmail(String altEmail) {this.altEmail = altEmail;}

    public String getMobileNumber() {return this.mobileNumber;}
    public void setMobileNumber(String number) {this.mobileNumber = number;}

    public boolean isDarkMode() {return darkMode;}
    public void setDarkMode(boolean darkMode) {this.darkMode = darkMode;}

    public boolean isStayLoggedIn() {return this.stayLoggedIn;}
    public void setStayLogged(boolean stayLogged) {this.stayLoggedIn = stayLogged;}

    public boolean hasRecoverInfo() {return haveRecoverInfo;}
    public void setRecoverInfo(boolean recoverInfo) {this.haveRecoverInfo = recoverInfo;}

    private void setQuestions(Question[] questions, Answer[] answers) {
        for (int i = 0; i < MAX_QA_PAIRS; i++) setQuestion(i + 1, questions[i], answers[i]);
    }
    
    public void setQuestion(int questNum, Question question, Answer answer) {
        if ((questNum > 0 && questNum <= MAX_QA_PAIRS)) {
            if (haveQA[questNum-1]) {
                recoveryQA.changeQuestion(questNum, question, answer);
                return;
            }
            recoveryQA.add(questNum, question, answer);
            haveQA[questNum-1] = true;
        }  
    }

    public void setAnswer(int questNum, Answer answer) {
        if ((questNum > 0 && questNum <= MAX_QA_PAIRS) && haveQA[questNum-1]) {
            recoveryQA.changeAnswer(questNum, answer);
        }  
    }

    public Question getQuestion(int questNum) {
        return recoveryQA.get(questNum).getKey();
    }

    public Answer getAnswer(int questNum) {
        return recoveryQA.get(questNum).getValue();
    }



    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof AppUser)) return false;
        AppUser user = (AppUser) obj;
        int count = 0;
        user.getPassword().retrievePass(); 
        String canditatePass = user.getPassword().getPass();
        this.getPassword().retrievePass();
        String actualPass = this.getPassword().getPass();

        // Basic checks - non null attributes
        count = (this.getUsername().equals(user.getUsername())) ? count + 1 : count;
        count = (actualPass.equals(canditatePass)) ? count + 1 : count;
        count = (this.getMainEmail().equals(user.getMainEmail())) ? count + 1 : count;
        if (count < 3) return false;

        // Secondary checks - other attributes - optional
        if (this.hasRecoverInfo())
        { 
            count = (this.getAltEmail().equals(user.getAltEmail())) ? count + 1 : count; 
            count = (this.getMobileNumber().equals(user.getMobileNumber())) ? count + 1 : count;
            if (count < 5) return false;
            
            // Recovery Q&A checks - optional
            for (int i = 1; i <= MAX_QA_PAIRS; i++) {
                if (haveQA[i-1]) {
                    if (user.haveQA[i-1]) {
                        count = (this.getQuestion(i).equals(user.getQuestion(i))) ? count + 1 : count;
                        count = (this.getAnswer(i).equals(user.getAnswer(i))) ? count + 1 : count;
                    }
                }
            }
            if (count < 11) return false;
        }
        return true;
    } 
}