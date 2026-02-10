package com.doideradev.uninstaller.utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.apache.commons.io.FileUtils;

// import com.doideradev.doiderautils.*;
import com.doideradev.uninstaller.controllers.UninstallerController;

import javafx.application.Platform;

public abstract class UninstallerManager {

    
    private static final String userPath = System.getProperty("user.home");
    private static String startMenuFolder = System.getenv("APPDATA") + "\\Microsoft\\Windows\\Start Menu\\Programs\\Password Bank";
    private static String installedFolder;

    private static AppConfigs configs;

    private static boolean initialized = false;


    /**
     * Starts the uninstallation process of Password Bank
     * 
     */
    public static void uninstallApp() {
        if (!initialized) {
            new Thread(() -> {
                    configs = openConfigFile();
                    installedFolder = configs.getInstallDir();
                    int count = 1;

                    try {
                        initialized = true;
                        
                        count = (deleteApplication()) ? count+1 : 1;    // step 1
                        Platform.runLater(() -> UninstallerController.setUninstallProgress(.60));
                        
                        count = (deleteShortcuts()) ? count+1 : 1;      // step 2
                        Platform.runLater(() -> UninstallerController.setUninstallProgress(.80));
                        
                        deleteExtras();                                 // step 3
                        Platform.runLater(() -> UninstallerController.setUninstallProgress(.95));
                        count++;
    
                        deleteRegistry();                               // step 4
                        Platform.runLater(() -> {
                            UninstallerController.setUninstallProgress(1.0);
                            UninstallerController.uninstallComplete = true;
                        });

                    } 
                    catch (Exception e) 
                    {
                        List<String> error = PopupError.createErrorList(e.getCause(), e.getMessage(), e.getStackTrace());
                        new PopupError("Uninstallation failed. Reverting changes...", null, error);
                        revertUninstall(count);
                    }
                    autoDelete();
            }).start();
        }
    }


    /**
     * Deletes the shortcuts of Password Bank from the Start Menu 
     * @return true if successful 
     * @throws IOException
     */
    private static boolean deleteShortcuts() throws IOException {
        File folder = Path.of(startMenuFolder).toFile();
        // File shortcut = new File(userPath + "\\" + startMenuFolder + "\\Password Bank.lnk");
        
        if (folder.exists()) {
            // shortcut.delete();
            FileUtils.deleteDirectory(folder);
            return true;
        }
        else return true;
    }


    /**
     * Deletes the main application folder of Password Bank
     * @throws IOException
     */
    private static boolean deleteApplication() throws IOException {
        Path appFolder = Path.of(installedFolder);
        File app = appFolder.toFile();
        FileUtils.deleteDirectory(app);
        return true;
    }


    /**
     * Deletes extra files like Desktop shortcut if there are any
     * @throws IOException
     */
    private static void deleteExtras() throws IOException {
        Path desktopShortcut = Path.of(userPath, "Desktop", "Password Bank.lnk");
        Files.exists(desktopShortcut);
        Files.deleteIfExists(desktopShortcut);
    }


    /**
     * Schedules the deletion of the uninstaller folder on exit
     */
    private static void autoDelete() {
        File actualDir = Path.of(System.getProperty("user.dir")).toFile();
        actualDir.deleteOnExit();
    }


    /**
     * Reverts the uninstallation process based on the last completed step
     * @param lastStep - The last completed step of the uninstallation process
     */
    private static void revertUninstall(int lastStep) {
        try {       // Undo the deletion of the Application
            Path passBankTempFolder = Path.of(userPath, "AppData\\Local\\Temp\\Passwor Bank");
            File destFolder = new File(installedFolder);
            File srcFolder = passBankTempFolder.toFile();
            FileUtils.copyDirectory(srcFolder, destFolder);
        } catch (IOException e) {
            System.out.println("There was a problem recovering the application from temporary folder.");
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        if (lastStep == 1) return;

        try {       // Undo the deletion of Start Menu folder
            Files.createDirectory(Path.of(startMenuFolder));
            Path source = Path.of(installedFolder, "Password Bank.exe");
            Path dest   = Path.of(startMenuFolder, "Password Bank.exe");
            Files.createSymbolicLink(dest, source);
            source = Path.of(installedFolder, "Uninstaller.exe");
            dest   = Path.of(startMenuFolder, "Uninstaller.exe");
            Files.createSymbolicLink(dest, source);
        } catch (IOException e) {
            System.out.println("There was a problem creating the new shortcut for Password Bank application.");
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        if (lastStep == 2) return;

        try {       // Undo the deletion of Desktop application shortcut
            Path source = Path.of(installedFolder, "Password Bank.exe");
            Path dest   = Path.of(userPath, "Desktop\\Password Bank.exe");
            Files.createSymbolicLink(dest, source);
        } catch (IOException e) {
            System.out.println("There was a problem creating the new Desktop shortcut for Password Bank application.");
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }


    /**
     * Opens the configuration file of Password Bank
     * @return AppConfigs object with the configurations stored in the file
     */
    private static AppConfigs openConfigFile() {
        try (InputStream fileIn = new FileInputStream(System.getenv("APPDATA") + "\\Password Bank\\configs\\app.config");
             ObjectInputStream in = new ObjectInputStream(fileIn)) {
            configs = (AppConfigs) in.readObject();
            return configs;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println(e.getLocalizedMessage());
            System.out.println(e.getCause());
            e.printStackTrace();
        }
        return null;
    }


    /**
     * Deletes the registry entries of Password Bank
     * @return true if successful
     */
    private static boolean deleteRegistry() {
        String[] lines = new String[10];
        try {
            ProcessBuilder builder = new ProcessBuilder(System.getProperty("user.dir") + "/app/data/" + "Registry-deletion.bat " , configs.getUUID(), " /va /f");
            Process p = builder.start();
            String line;
            InputStream input = p.getInputStream();
            BufferedReader buffer = new BufferedReader(new InputStreamReader(input));
            int index = 0;  
            while ((line = buffer.readLine()) != null) {
                System.out.println(line);
                lines[index++] = line;
            }
            p.waitFor(); p.destroy();
            return true;
        } 
        catch (IOException | InterruptedException e) 
        {
            List<String> error = PopupError.createErrorList(e.getCause(), e.getMessage(), e.getStackTrace());
            for (String string : lines) error.add(string);
            new PopupError("Could not delete the registry file of Password bank", null, error);
        }
        return false;
    }
    

    public static final AppConfigs getConfigs() {
        return configs;
    }
}
