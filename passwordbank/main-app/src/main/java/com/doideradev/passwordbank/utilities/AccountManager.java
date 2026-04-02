package com.doideradev.passwordbank.utilities;

import java.util.Collections;

import com.doideradev.doiderautils.UtilsClasses.Answer;
import com.doideradev.doiderautils.UtilsClasses.Question;
import com.doideradev.passwordbank.App;
import com.doideradev.passwordbank.model.AppUser;
import com.doideradev.passwordbank.model.Login;


public abstract class AccountManager {

    protected static AppUser user;
    protected static LoginList logs;
    protected static AppConfigs configs;
    
    public static boolean haveUser = false;
    public static boolean havePass = false;
    public static boolean stayLoggedIn = false;


    /**
     * loads all data from files, such as: AppUser, Configs, LoginList.
     */
    public static void loadAccount() {
        FilesManager.loadFiles();

        if (FilesManager.haveUser) {
            haveUser = true;
            user = FilesManager.openUserFile();
            user.getPassword().retrievePass();
            App.darkMode.set(user.isDarkMode());
            stayLoggedIn = user.isStayLoggedIn();
        }
        if (!FilesManager.isConfigured)
            configs = new AppConfigs(App.appVersion);
        else configs = FilesManager.getConfigs();

        if (FilesManager.havePass) {
            var loadedLogs = FilesManager.openPassFile();
            if (loadedLogs.checkPassOwnership(user)) {
                logs = loadedLogs;
                logs.newObservableList();
                Collections.sort(logs);
                for (Login login : logs) {
                    login.getPassword().retrievePass();
                }
            }
        }
    }


    /**
     * Creates and activates a new user account from provided credentials and recovery information, associating it with security questions and answers.
     * @param pUser - A template user object containing the primary account details (email, username, password) and optionally recovery information (alternate email, mobile number).
     * @param questions - An array of security questions to be associated with the new account. Each question is indexed sequentially.
     * @param answers - An array of answers corresponding to the provided questions. The i‑th answer matches the i‑th question.

     * @return Returns the newly created AppUser
     */
    public static AppUser createAccount(AppUser pUser, Question[] questions, Answer[] answers) {
        final var candUser = new AppUser();
        candUser.setMainEmail(pUser.getMainEmail());
        candUser.setUsername(pUser.getUsername());
        candUser.setPassword(pUser.getPassword().getPass());
        if (pUser.hasRecoverInfo()) {
            candUser.setRecoverInfo(pUser.hasRecoverInfo());
            candUser.setAltEmail(pUser.getAltEmail());
            candUser.setMobileNumber(pUser.getMobileNumber());
            candUser.setStayLogged(false);
            
            int count = 1;

            for (var question : questions) {
                candUser.setQuestion(count, question, answers[count-1]);
                count++;
            }
        }
        user = candUser;
        haveUser = true;
        return user;
    }



    /**
     * Updates the existing user account with new information. This method is designed to selectively overwrite attributes of the current user object based on the non‑null values provided in the updatedUser parameter. In practice, the updatedUser <b> should have all attributes set to null except those that are intended to be changed </b>.

     * @param updatedUser - A user object containing only the fields to be updated. Attributes left as null will not affect the current account. If recovery information is present, security questions, answers, alternate email, and mobile number will also be updated.

     */
    public static void updateAccountData(AppUser updatedUser) {
        var username = updatedUser.getUsername();
        var mainEmail = updatedUser.getMainEmail();
        var password = updatedUser.getPassword();

        if (username != null) user.setUsername(username);
        if (mainEmail != null) user.setMainEmail(mainEmail);
        if (password != null) user.setPassword(password.getPass());

        
        if (updatedUser.hasRecoverInfo()) {
            var q1 = updatedUser.getQuestion(1);
            var q2 = updatedUser.getQuestion(2);
            var q3 = updatedUser.getQuestion(3);
            
            var a1 = updatedUser.getAnswer(1);
            var a2 = updatedUser.getAnswer(2);
            var a3 = updatedUser.getAnswer(3);
            
            var altEmail = updatedUser.getAltEmail();
            var phoneNum = updatedUser.getMobileNumber();

            user.setQuestion(1, q1, a1);
            user.setQuestion(2, q2, a2);
            user.setQuestion(3, q3, a3);

            user.setAltEmail(altEmail);
            user.setMobileNumber(phoneNum);

            user.setRecoverInfo(true);
        }
    }




    public static boolean deleteAccount() {
        var deleted = FilesManager.deleteFiles();
        if (deleted) {
            user = null;
            logs = null;
            havePass = false;
            haveUser = false;
        }
        return deleted;
    }

    public static void saveAccountData() {
        logs = App.logs;
        stayLoggedIn = App.stayLoggedIn;
        user.getPassword().protectPassword();
        if (logs != null) {
            Collections.sort(logs);
            for (Login login : logs) {
                login.getPassword().protectPassword();
            }
        }
        user.setDarkMode(App.darkMode.get());
        user.setStayLogged(stayLoggedIn);
        FilesManager.closeFiles(user, logs);
    }


    public static AppUser user() {return user;}
    public static AppConfigs configs() {return configs;}
    public static LoginList logins() {return logs;}
}
