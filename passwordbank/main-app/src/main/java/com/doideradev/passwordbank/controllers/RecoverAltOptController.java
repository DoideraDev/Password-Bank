package com.doideradev.passwordbank.controllers;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

import com.doideradev.passwordbank.App;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class RecoverAltOptController {


    @FXML private Button buttonNext;
    @FXML private Label labelEmail;
    @FXML private Label labelHintEmail;
    @FXML private Label labelHintUsername;
    @FXML private Label labelUsername;
    @FXML private TextField tFieldEmail;
    @FXML private TextField tFieldUsername;
    @FXML private Text textDescription;
    @FXML private Text textEmail;


    @FXML private Button buttonConfirmCode;
    @FXML private Label lHintCode;
    @FXML private TextField tFieldCode;
    @FXML private Text textDescCode;

    private String code;
    private final String desktop = System.getProperty("user.home") + "/Desktop/";
    private final String recoverFile = "password.txt";
    public RecoverAccountController controller;


    public void initialize() {
        firstPageActions();
    }

    private void firstPageActions() {
        buttonNext.setOnMouseClicked(event -> {
            if (verifyFields() && createFile()) {
                controller.changePage(controller.panePage2, true);
                secondPageActions();
            }
        });
        tFieldEmail.setOnKeyTyped(event -> {
            tFieldEmail.setStyle(null);
            labelHintEmail.setVisible(false);
        });
        tFieldUsername.setOnKeyTyped(event -> {
            tFieldUsername.setStyle(null);
            labelHintUsername.setVisible(false);
        });

        labelHintEmail.setVisible(false);
        labelHintUsername.setVisible(false);

        textDescription.setText("Please, provide the following information to recover your account");
        labelUsername.setText("Type your username");
        labelEmail.setText("Type your email");
        textEmail.setText("Registered email: " + changeEmail());
    }
    private void secondPageActions() {
        textDescCode.setText("A text file was created in your desktop, copy the 8-characters code and paste on the field bellow.");
        buttonConfirmCode.setOnMouseClicked(event -> {
            if (verifyCode()) {
                deleteFile();
                App.setDefAppSize();
                App.changePage("base");
                App.baseCtrlInstance.nextPage(App.baseCtrlInstance.loadPane("userInfo"));
            }
        });
        lHintCode.setVisible(false);
    }



    private boolean verifyCode() {
        if (tFieldCode.getText().isBlank()) {
            tFieldCode.setStyle("-fx-border-color: red; -fx-border-radius: 5px");
            lHintCode.setStyle("-fx-text-fill: red;");
            lHintCode.setText("The field is empty");
            lHintCode.setVisible(true);
            return false;
        }
        if (!tFieldCode.getText().equals(code)) {
            tFieldCode.setStyle("-fx-border-color: red; -fx-border-radius: 5px");
            lHintCode.setStyle("-fx-text-fill: red;");
            lHintCode.setText("The code is wrong");
            lHintCode.setVisible(true);
            return false;
        }
        return true;
    }


    private boolean verifyFields() {
        if (!tFieldUsername.getText().isBlank()) {
            tFieldUsername.setStyle("-fx-border-color: red; -fx-border-radius: 5px");
            labelHintUsername.setStyle("-fx-text-fill: red;");
            labelHintUsername.setText("Empty field");
            labelHintUsername.setVisible(true);
            return false;
        }
        if (!tFieldUsername.getText().equals(App.user.getUsername())) {
            tFieldUsername.setStyle("-fx-border-color: red; -fx-border-radius: 5px");
            labelHintUsername.setStyle("-fx-text-fill: red;");
            labelHintUsername.setText("Wrong username");
            labelHintUsername.setVisible(true);
            return false;
        }
        
        if (!tFieldEmail.getText().isBlank()) {
            tFieldEmail.setStyle("-fx-border-color: red; -fx-border-radius: 5px");
            labelHintEmail.setStyle("-fx-text-fill: red;");
            labelHintEmail.setText("Empty field");
            labelHintEmail.setVisible(true);
            return false;
        }
        if (!tFieldEmail.getText().equals(App.user.getMainEmail())) {
            tFieldEmail.setStyle("-fx-border-color: red; -fx-border-radius: 5px");
            labelHintEmail.setStyle("-fx-text-fill: red;");
            labelHintEmail.setText("Wrong email");
            labelHintEmail.setVisible(true);
            return false;
        }
        return true;
    }

    
    private String changeEmail() {
        int at = App.user.getMainEmail().indexOf("@");
        String email = App.user.getMainEmail();
        String toReplace = email.substring(3, at);
        String star = "";
        for (int i = 0; i < toReplace.length(); i++) {
            star += "*";
        }
        email = email.replace(toReplace, star);
        
        return email;
    }


    private String generateCode() {
        String code = "";
        Random rand = new Random();
        for (int i = 0; i < 8; i++) {
            code += (char) rand.nextInt(33, 127);
        }
        return code;
    }


    String writeError = "There was a problem trying to write the file";

    private boolean createFile() {
        try {
            File txtFile = new File(desktop + recoverFile);
            if (txtFile.createNewFile()) {return writeFile();}
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.out.println(e.getCause());
            e.printStackTrace();
        }
        return false;
    }


    private boolean writeFile() throws IOException {
        FileWriter txtFile;
        try {
            txtFile = new FileWriter(desktop + recoverFile);
            code = generateCode();
            txtFile.write("Your recovery code: " + code);
            txtFile.close();
            return true;
        } catch (Exception e) {throw new IOException(writeError);}

    }

    private void deleteFile() {
        File txtFile = new File(desktop + recoverFile);
        txtFile.delete();
    }
}
