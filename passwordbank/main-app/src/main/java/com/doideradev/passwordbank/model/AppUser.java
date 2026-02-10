package com.doideradev.passwordbank.model;

import java.io.Serializable;

public class AppUser implements Serializable {

    private static final long serialVersionUID = 100201L; 

    private Password password;
    private String mainEmail;
    private String username;
    private String altEmail;
    private String mobileNumber;
    private String question1;
    private String question2;
    private String question3;
    private String answer1;
    private String answer2;
    private String answer3;
    private boolean darkMode;
    private boolean stayLoggedIn;
    private boolean haveRecoverInfo;




    public AppUser() {    
    }

    

    public AppUser(String mainEmail, String password, String username, String altEmail, String mobileNumber, String question1,
                   String question2, String question3, String answer1, String answer2, String answer3, boolean darkMode) {
        setMainEmail(mainEmail);
        setPassword(password);
        setUsername(username);
        setMobileNumber(mobileNumber);
        setQuestion1(question1);
        setQuestion2(question2);
        setQuestion3(question3);
        setAnswer1(answer1);
        setAnswer2(answer2);
        setAnswer3(answer3);
        setDarkMode(darkMode);
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

    public String getQuestion1() {return question1;}
    public void setQuestion1(String question1) {this.question1 = question1;}

    public String getQuestion2() {return question2;}
    public void setQuestion2(String question2) {this.question2 = question2;}

    public String getQuestion3() {return question3;}
    public void setQuestion3(String question3) {this.question3 = question3;}

    public String getAnswer1() {return answer1;}
    public void setAnswer1(String answer1) {this.answer1 = answer1;}

    public String getAnswer2() {return answer2;}
    public void setAnswer2(String answer2) {this.answer2 = answer2;}

    public String getAnswer3() {return answer3;}
    public void setAnswer3(String answer3) {this.answer3 = answer3;}

    public boolean isDarkMode() {return darkMode;}
    public void setDarkMode(boolean darkMode) {this.darkMode = darkMode;}

    public boolean isStayLoggedIn() {return this.stayLoggedIn;}
    public void setStayLogged(boolean stayLogged) {this.stayLoggedIn = stayLogged;}

    public boolean hasRecoverInfo() {return haveRecoverInfo;}
    public void setRecoverInfo(boolean recoverInfo) {this.haveRecoverInfo = recoverInfo;}

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
            count = (this.getQuestion1().equals(user.getQuestion1())) ? count + 1 : count;
            count = (this.getQuestion2().equals(user.getQuestion2())) ? count + 1 : count;
            count = (this.getQuestion3().equals(user.getQuestion3())) ? count + 1 : count;
            if (count < 8) return false;
            
            count = (this.getAnswer1().equals(user.getAnswer1())) ? count + 1 : count;
            count = (this.getAnswer2().equals(user.getAnswer2())) ? count + 1 : count;
            count = (this.getAnswer3().equals(user.getAnswer3())) ? count + 1 : count;
            if (count < 11) return false;
        }
        return true;
    } 
}