package com.doideradev.passwordbank.utilities;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import org.apache.commons.io.FileUtils;

import com.doideradev.passwordbank.App;

import javafx.application.Platform;

public abstract class UninstallerInitializer {


    /**
     * Start the uninstaller process for the application and close the application.
     * 
     * <p> The uninstaller application will be copied to a temporary folder and 
     * started when the application is closed to prevent file access conflicts during 
     * the deletion of the application files.
     * 
     * <p> If any error occurs during the process, a popup message will be shown
     * 
     */
    public static void initializeUninstaller() {
        Path currentDir = Path.of(System.getProperty("user.dir"));
        Path tempAppDir = Path.of(System.getProperty("user.home") + "/AppData/Local/Temp/Password Bank");

        deleteExisting(tempAppDir);

        try {
            tempAppDir = Files.createTempDirectory("Password Bank");
            Path tempAppDirFinal = tempAppDir.resolveSibling("Password Bank");
            tempAppDir = Files.move(tempAppDir, tempAppDirFinal, StandardCopyOption.REPLACE_EXISTING);
            FileUtils.copyDirectory(currentDir.toFile(), tempAppDirFinal.toFile());

        } catch (IllegalArgumentException | IOException e) {
            System.out.println(e.getMessage());
            System.out.println(e.getCause());
            e.printStackTrace();
        }
        
        App.getStage().setOnCloseRequest(event -> {
            try {
                ProcessBuilder builder = new ProcessBuilder(System.getProperty("user.home") + "\\AppData\\Local\\Temp\\Password Bank" + "\\Uninstaller");
                builder.start();
            } catch (IOException e) {
                System.out.println(e.getMessage());
                System.out.println(e.getCause());
                e.printStackTrace();
            }
        });
        Platform.exit();
    }

    

    /**
     * <p> Tries to delete a file of directory and all of its sub-directories
     * <p> The method performs a deletion on the file by the given path if the path is an actual file.
     * If the path given is a directory the files and sub-directories inside will be deleted recursively. 
     * 
     * <p> This method does not throw any exception, instead it will pop a message if any error occour during the deletion.
     * 
     * @param filePath - Path to the File/directory do be deleted.
     */
    private static void deleteExisting(Path filePath) {
        File fileToDelete = filePath.toFile();

        if (fileToDelete.exists()) {
            try {
                FileUtils.deleteDirectory(fileToDelete);
            } catch (IOException e) {
                System.out.println(e.getMessage());
                System.out.println(e.getCause());
                e.printStackTrace();
            }
        }
    }
}
