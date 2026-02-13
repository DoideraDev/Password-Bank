package com.doideradev.passwordbank.utilities;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

import org.apache.commons.io.FileUtils;

import com.doideradev.passwordbank.App;
import com.doideradev.doiderautils.popup.PopupError;

import java.io.*;
import java.net.*;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Pair;

public abstract class UpdaterManager {

    private static boolean updateConfirmed = false;
    /** For testing purposes */
    public static boolean updateAvailable = false; 
    
    
    /**
     * Look for updates by checking the latest release version from the GitHub API.
     * <p> If a new version is found, the {@code updateAvailable} property in the App class is set to true.
     * @param configs - AppConfigs instance containing the release URL
     */
    public static void lookForUpdates(AppConfigs configs) {
        CompletableFuture.runAsync(() -> {
            try {
                JsonObject jsonRelease = fetchReleaseJson(configs.getReleaseURL());
                var releaseInfo = parseRelease(jsonRelease);
                String latestVersion = releaseInfo.getKey();
                String downloadUrl = releaseInfo.getValue();
    
                if (!App.appVersion.equals(latestVersion) && downloadUrl != null) {
                    configs.setDownloadURL(downloadUrl);
                    updateAvailable = true;
                    Platform.runLater(() -> App.updateAvailable.set(true));
                } else System.out.println("You are up to date.");
    
            } catch (Exception e) {
                boolean isConnectionError = e instanceof java.net.UnknownHostException || 
                e instanceof java.net.ConnectException ||
                e instanceof java.net.SocketException ||
                e instanceof java.net.SocketTimeoutException;
                if (isConnectionError) {
                    Platform.runLater(() -> new PopupError("Unable to find updates, please connect to the internet.", App.baseCtrlInstance.sampleNode()));
                } else {
                    System.out.println("An error occurred while checking for updates: " + e.getMessage() + "\n");
                    System.out.println("Cause: " + e.getCause());
                    System.out.println("Stack trace: ");
                    e.printStackTrace();
                }
            }
        });
    }


    /**
     * Fetch the latest release JSON from the GitHub API.
     * 
     * @param url - URL of the GitHub releases API endpoint
     * @return JsonObject representing the latest release
     * @throws IOException if an I/O error occurs
     */
    private static JsonObject fetchReleaseJson(String url) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setRequestProperty("Accept", "application/vnd.github.v3+json");
        try (InputStream is = conn.getInputStream();
             JsonReader reader = Json.createReader(is)) {
            return reader.readObject();
        }
    }



    /**
     * Parse the JSON release object to extract the latest version and download URL.
     * 
     * @param release - JSON object representing the release
     * @return A {@code Pair<String, String>} containing the latest version (key) and download URL (value)
     */
    private static Pair<String, String> parseRelease(JsonObject release) {
        String latestVersion = release.getString("tag_name", "");
        String downloadUrl = null;
        JsonArray assets = release.getJsonArray("assets");
        if (assets != null && !assets.isEmpty()) {
            downloadUrl = assets.getJsonObject(0).getString("browser_download_url", null);
        }
        return new Pair<String,String>(latestVersion, downloadUrl); 
    }





    /**
     * Notify the user about the availability of an update.
     * <p> A popup window will be shown to the user with the option to update now or later.
     * 
     * @return boolean true if the user confirmed the update, false otherwise
     */
    public static boolean notifyUpdateAvailability() {
        var updatePopup = createUpdateWindow();
        updatePopup.showAndWait();

        if (updateConfirmed) new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                    Platform.exit();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
        return updateConfirmed;
    }


    /**
     * Start the update process by downloading the installer and starting the updater application.
     * 
     * <p> The updater application will be copied to a temporary folder and 
     * started when the application is closed to prevent file access conflicts during
     */
    public static void startUpdateInitialSteps() {
        Path currentDir = Path.of(System.getProperty("user.dir"));
        try {
            
            // Create a temporary directory for the update process
            Path updateTempFolder = Files.createTempDirectory("Password-Bank-Temp-Folder");
            FileUtils.copyDirectory(currentDir.toFile(), updateTempFolder.toFile());
            ///////////////////////////////////////////////////////////////////////////////
            
            // Start the updater process
            ProcessBuilder updateProcess = new ProcessBuilder(updateTempFolder.toString(), "\\Updater");
            updateProcess.start();
            ////////////////////////////////////////////////////////////////////////////////

        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }


    /**
     * Create update notification window and set all its properties, styles and actions.
     * 
     * @return Stage update window
     */
    private static Stage createUpdateWindow() {
        Stage window = new Stage();
        StackPane content = new StackPane();
        VBox vBox = new VBox();
        Text infoText = new Text();
        HBox bbox = new HBox();
        Button updateBtn = new Button();
        Button laterBtn = new Button();
        
        window.setWidth(350);
        window.setHeight(200);

        bbox.setSpacing(50);
        bbox.setAlignment(Pos.CENTER);
        vBox.setSpacing(40);
        vBox.setAlignment(Pos.CENTER);
        vBox.setFillWidth(true);
        
        updateBtn.setText("Update");
        updateBtn.setPrefWidth(80);
        updateBtn.setFont(Font.font("arial", FontWeight.NORMAL, 12.));
        laterBtn.setText("Later");
        laterBtn.setPrefWidth(80);
        laterBtn.setFont(Font.font("arial", FontWeight.NORMAL, 12.));

        updateBtn.setStyle("-fx-background-color: rgba(34, 187, 34, 1); -fx-text-fill: white;");
        updateBtn.setOnMouseEntered(e -> {updateBtn.setCursor(Cursor.HAND);});
        updateBtn.setOnMouseExited(e -> {updateBtn.setCursor(Cursor.DEFAULT);});
        laterBtn.setStyle("-fx-background-color: rgba(255, 47, 47, 1); -fx-text-fill: white;");
        laterBtn.setOnMouseEntered(e -> {laterBtn.setCursor(Cursor.HAND);});
        laterBtn.setOnMouseExited(e -> {laterBtn.setCursor(Cursor.DEFAULT);});

        /* button actions set */
        updateBtn.setOnMouseClicked(event -> {updateConfirmed = true; window.close();});
        laterBtn.setOnMouseClicked(event -> window.close());
        

        infoText.setWrappingWidth(250);
        infoText.setTextAlignment(TextAlignment.CENTER);
        infoText.setFont(Font.font("Arial", FontWeight.BOLD, 18.0));
        infoText.setText("A new update is available!" + "\nWould you like to update now?");
        
        bbox.getChildren().addAll(updateBtn, laterBtn);
        vBox.getChildren().addAll(infoText, bbox);
        
        String windowStyle = 
            " -fx-background-color: white;" + " -fx-border-color: #8A8AEE;" + 
            " -fx-border-width: 2;" +" -fx-border-radius: 5;" + " -fx-background-radius: 5;";

        content.setStyle(windowStyle);

        content.getChildren().addAll(vBox);
        content.setPrefSize(window.getWidth(), window.getHeight());
        content.setAlignment(Pos.CENTER);

        window.toFront();
        window.setAlwaysOnTop(true);
        window.setResizable(false);
        window.initModality(Modality.APPLICATION_MODAL);
        window.initStyle(StageStyle.TRANSPARENT);
        window.centerOnScreen();
        window.requestFocus();

        javafx.scene.Scene scene = new javafx.scene.Scene(content);
        scene.setFill(javafx.scene.paint.Color.rgb(0, 0, 0, 0.01));
        window.setScene(scene);
        
        return window;
    }
}
