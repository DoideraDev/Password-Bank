package com.doideradev.passwordbank.utilities;

import java.io.IOException;

import com.doideradev.passwordbank.App;
import com.doideradev.passwordbank.controllers.ModalController;
import com.doideradev.passwordbank.model.Login;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ModalManager {

    private Login managedLogin;
    private Pane modalRoot;
    private Node rootNode;
    private ModalState state;
    private ModalController controller;



    public ModalManager(Login login, Node rootNode, ModalState state) {
        loadModal();
        this.state = state;
        this.managedLogin = login;
        this.rootNode = rootNode;
    }


    private void loadModal() {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("views/modal.fxml"));
            modalRoot = (Pane) loader.load();
            controller = loader.getController();
        } catch (IllegalStateException | IOException e) {}
    }


    public void showModal() {
        Stage modalStage = new Stage();
        modalStage.setResizable(false);
        modalStage.initModality(Modality.APPLICATION_MODAL);
        modalStage.initStyle(StageStyle.TRANSPARENT);

        Scene modalScene;
        modalScene = new Scene(modalRoot);
        modalScene.setFill(Color.rgb(0, 0, 0, 0.01));
        modalStage.setScene(modalScene);
        modalStage.sizeToScene();
        
        Stage appWindow = (Stage) rootNode.getScene().getWindow();
        double modalX, modalY;
        modalX = ((appWindow.getX() + appWindow.getWidth()/2) - 200 /* <- half modal width */);
        modalY = ((appWindow.getY() + appWindow.getHeight()/2) - 200 /* <- half modal height*/);
        modalStage.setX(modalX);
        modalStage.setY(modalY);


        switch (state) {
            case CREATE:
                modalStage.showAndWait();
                if (controller.getLogin() != null) {
                    setLoginUpdated(controller.getLogin());
                }
                break;
                
                case EDIT:
                controller.setLoginToShow(managedLogin);
                controller.setFields();
                modalStage.showAndWait();
                setLoginUpdated(controller.getLogin());
                controller.setLoginToShow(null);
            break;
        }
    }


    public Login getLoginUpdated() {
        return this.managedLogin;
    }

    private void setLoginUpdated(Login updatedLogin) {
        this.managedLogin = updatedLogin;
    }
}
