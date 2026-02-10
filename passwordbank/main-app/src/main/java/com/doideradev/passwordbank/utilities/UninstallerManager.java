package com.doideradev.passwordbank.utilities;

import java.io.IOException;

import com.doideradev.passwordbank.App;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class UninstallerManager {

    private Node callerNode;
    private Pane rootPane;

    public UninstallerManager(Node callerNode) {
        this.callerNode = callerNode;
        loadUninstaller();
    }

    private void loadUninstaller() {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("views/uninstaller.fxml"));
            rootPane = (Pane) loader.load();

        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
            System.out.println(e.getMessage());
            System.out.println(e.getCause());
        }
    }

    public void showUninstaller() {
        Stage modalStage = new Stage();
        modalStage.setResizable(false);
        modalStage.initModality(Modality.APPLICATION_MODAL);
        modalStage.initStyle(StageStyle.TRANSPARENT);
        modalStage.setTitle("Password Bank Uninstaller");
        

        Scene modalScene;
        modalScene = new Scene(rootPane);
        modalScene.setFill(Color.rgb(0, 0, 0, 0.01));
        modalStage.setScene(modalScene);
        modalStage.sizeToScene();
        
        Stage appWindow = (Stage) callerNode.getScene().getWindow();
        double modalX, modalY;
        modalX = ((appWindow.getX() + appWindow.getWidth()/2) - 200 /* <- half modal width */);
        modalY = ((appWindow.getY() + appWindow.getHeight()/2) - 200 /* <- half modal height*/);
        modalStage.setX(modalX);
        modalStage.setY(modalY);

        modalStage.showAndWait();
    }
}
