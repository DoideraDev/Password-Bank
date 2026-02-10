package com.doideradev.passwordbank.controllers;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import com.doideradev.doiderautils.popup.PopupError;
import com.doideradev.passwordbank.App;

import java.awt.Desktop;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Duration;

public class AboutScreenController {

    @FXML private Button buttonBack;
    @FXML private Button buttonGitHub;
    @FXML private Button buttonPDF;
    @FXML private GridPane gPaneAbout;
    @FXML private Text textAbout;
    @FXML private Text textAboutDescription;
    @FXML private TextFlow textFlowApp;
    @FXML private Text textGuideDescription;
    @FXML private Text textProjectGitHub;
    @FXML private Text textUserGuide;


    public void initialize() {
        setActions();
        setButtonsStyle();
        setText();
        setTextTheme();
        buttonPDF.setText("Open user guide");
        BaseController.setStyle(gPaneAbout);

        gPaneAbout.widthProperty().addListener((a, b, c) -> {
            textAboutDescription.setWrappingWidth(c.doubleValue()-580);
            textGuideDescription.setWrappingWidth(c.doubleValue()-580);
        });
    }
    
    
    private void setActions() {
        buttonBack.setOnMouseClicked(event -> App.baseCtrlInstance.prevPage());
        Tooltip gitTip = new Tooltip("Github project page");
        gitTip.setShowDelay(Duration.millis(150));
        gitTip.setShowDuration(Duration.millis(800));
        gitTip.setAutoHide(true);
        buttonGitHub.setTooltip(gitTip);
        buttonGitHub.setOnMouseClicked(event -> openGitHubPage());
        buttonPDF.setOnMouseClicked(event -> openPDFFile());
    }


    private void openGitHubPage() {
        try {
            String url = "https://github.com/Doiderazada/Password-Bank";
            Desktop desktop = Desktop.getDesktop();
            if (Desktop.isDesktopSupported()) {
                desktop.browse(new URI(url));
            }
        } catch(IOException | URISyntaxException e) {
            new PopupError("There was an error opening the GitHub Project page", buttonGitHub);
            System.out.println(e.getMessage());
            System.out.println(e.getCause());
            e.printStackTrace();
        }
    }


    private void openPDFFile() {
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
            new PopupError("There was an error opening the User Guide", buttonPDF);
            System.out.println(e.getMessage());
            System.out.println(e.getCause());
            e.printStackTrace();
        }
    }


    private void setButtonsStyle() {
        if (App.darkMode) {
            buttonPDF.getStyleClass().setAll("button-PDF-white");
            buttonGitHub.getStyleClass().setAll("button-GitHub-white");
        } else {
            buttonPDF.getStyleClass().setAll("button-PDF-black");
            buttonGitHub.getStyleClass().setAll("button-GitHub-black");
        }
        buttonBack.getStyleClass().setAll("button-Prev");
    }

    
    private void setTextTheme() {
        if (App.darkMode) buttonPDF.setTextFill(Color.WHITE);
        else buttonPDF.setTextFill(Color.BLACK);
        BaseController.setTextTheme(new Text[] {textAbout, textAboutDescription, textGuideDescription,
                                                textProjectGitHub, textUserGuide});
        var children = textFlowApp.getChildren();
        Text childList[] = new Text[children.size()];
        for (javafx.scene.Node node : children) {
            int index = children.indexOf(node);
            childList[index] = (Text) node;
        }
        BaseController.setTextTheme(childList);
    }


    private void setText() {
        String description = "This program is part of a public, personal project developed by Doidera, student of the Computer Science course at Universidade Federal Rural do Semi-Árido \n\n" +
                             "This program was developed using Java 17 and the graphical library JavaFX version 21 \n\n" +
                             "Although the program needs access to your internet conection for some funcionalities, "  + 
                             "all passwords created and stored in the program are saved locally and thus, cannot be accessed by any other user or the developers itself";

        String userGuide = "For a complete guide on how to use the application on its full, read the User Guide Document bellow";
        String gitText = "Check out the project page";

        textFlowApp.getChildren().add(new Text("Developed by "));
        textFlowApp.getChildren().add(new Text("Doidera, "));
        textFlowApp.getChildren().add(new Text(App.appVersion));
        
        for (javafx.scene.Node node : textFlowApp.getChildren()) {
            Text txt = (Text) node;
            txt.setOpacity(0.7);
        }
        Text txt = (Text) textFlowApp.getChildren().get(1);
        var font = txt.getFont();
        txt.setFont(Font.font(font.getFamily(), FontWeight.BOLD, font.getSize()));

        textAboutDescription.setText(description);
        textGuideDescription.setText(userGuide);
        textProjectGitHub.setText(gitText);
    }
}