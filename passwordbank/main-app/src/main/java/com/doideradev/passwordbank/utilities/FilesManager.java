package com.doideradev.passwordbank.utilities;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import com.doideradev.passwordbank.App;
import com.doideradev.passwordbank.model.AppUser;

public abstract class FilesManager {

    private static final String rootDataDirectory = System.getenv("APPDATA") + "\\Password Bank";
    private static final String filesDirectory = rootDataDirectory + "\\data\\";
    private static final String configsDirectory = rootDataDirectory + "\\configs\\";
    private static final String fileType = ".data";
    private static final String userFN = "User";
    private static final String passFN = "Pass";
    private static final String configFile = "app.config";

    private static AppUser user = null;
    private static LoginList logs = null;
    private static AppConfigs configs = null;
    private static File userFile;
    private static File passFile;
    private static File confFile;
    public static boolean haveUser;
    public static boolean havePass;
    public static boolean isConfigured;


    
    public static void loadFiles() {
        File configDir = new File(configsDirectory);
        File appDir = new File(filesDirectory);
        
        userFile = new File(filesDirectory, userFN + fileType);
        haveUser = userFile.exists();
        
        if (!configDir.exists()) 
            try {Files.createDirectories(configDir.toPath());} 
            catch (Exception e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        if (!appDir.exists()) 
            try {Files.createDirectories(appDir.toPath());} 
            catch (IOException e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }

        passFile = new File(filesDirectory, passFN + fileType);
        havePass = passFile.exists();

        confFile = new File(configsDirectory, configFile);
        isConfigured = confFile.exists();
    }



    public static AppUser openUserFile() {
        try 
        {
            user = null;
            FileInputStream fileInput = new FileInputStream(userFile);
            ObjectInputStream objectInput = new ObjectInputStream(fileInput);
            AppUser newUser = (AppUser) objectInput.readObject();
 
            objectInput.close();
            fileInput.close();
            user = newUser;
        } 
        catch (Exception e) 
        {
            System.out.println("Exception in FilesManager.openUserFile(): " + e.getMessage());
            if (e instanceof FileNotFoundException) {
                System.out.println("The specified file was not found in your system. \n");
                e.printStackTrace();
            }
            if (e instanceof IOException) {
                System.out.println("It was not possible to read the Object from the input. \n");
                e.printStackTrace();
            }
            if (e instanceof ClassNotFoundException) {
                System.out.println("Could not convert the extracted object to the desired Object Type. \n");
                e.printStackTrace();
            }

            System.out.println("==========================================================================\n");
        }
        return user;
    }

    private static void saveUserFile() {
        try 
        {
            FileOutputStream fileOutput = new FileOutputStream(userFile);
            BufferedOutputStream buffOut = new BufferedOutputStream(fileOutput);
            ObjectOutputStream objectOutput = new ObjectOutputStream(buffOut);
            objectOutput.writeObject(user);
            objectOutput.close();
            fileOutput.close();
        } 
        catch (IOException e) {
            System.out.println(e.getMessage());
            System.out.println(e.getCause());
            e.printStackTrace();
        }
    }


    public static LoginList openPassFile() {
        try 
        {
            FileInputStream fileInput = new FileInputStream(passFile);
            ObjectInputStream objectInput = new ObjectInputStream(fileInput);
            logs = (LoginList) objectInput.readObject();
            objectInput.close();
            fileInput.close();
        } 
        catch (Exception e) 
        {
            if (e instanceof FileNotFoundException) {
                System.out.println(e.getMessage());
            }
            if (e instanceof IOException) {
                System.out.println(e.getMessage());
            }
            if (e instanceof ClassNotFoundException) {
                System.out.println(e.getMessage());
            }
        } 
        return logs;
    }

    private static void savePassFile() {
        try 
        {
            FileOutputStream fileOutput = new FileOutputStream(passFile);
            ObjectOutputStream objectOutput = new ObjectOutputStream(fileOutput);
            objectOutput.writeObject(logs);
            objectOutput.close();
            fileOutput.close();
        } 
        catch (IOException e) {
            System.out.println(e.getMessage());
            System.out.println(e.getCause());
            e.printStackTrace();
        }
    }


    public static AppConfigs getConfigs() {
        try 
        {
            FileInputStream fileInput = new FileInputStream(confFile);
            ObjectInputStream objectInput = new ObjectInputStream(fileInput);
            configs = (AppConfigs) objectInput.readObject();
            objectInput.close();
            fileInput.close();
            return configs;
        } 
        catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println(e.getCause());
            e.printStackTrace();
            // new PopupError("There was a problem trying to access Password Bank's configuration data", App.baseCtrlInstance.sampleNode());
        }
        return null;
    }

    public static void saveConfigs() {
        try 
        {
            if (!App.configs.equals(configs)) {
                FileOutputStream fileOUtput = new FileOutputStream(confFile);
                ObjectOutputStream objectOutput = new ObjectOutputStream(fileOUtput);
                objectOutput.writeObject(App.configs);
                objectOutput.close();
                fileOUtput.close();
            }
        } 
        catch (Exception e) {
            System.out.println("Error saving configs file");
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }


    /**
     * Delete all files related to the application.
     * @return true if all files were deleted, false if any of the files could not be deleted.
     */
    public static boolean deleteFiles() {
        try 
        {
            boolean passDeleted = false;
            boolean dirDeleted = false;
            boolean userDeleted = Files.deleteIfExists(userFile.toPath());
            if (userDeleted) passDeleted = deletePasswordFiles();
            if (passDeleted) dirDeleted = Files.deleteIfExists(Path.of(filesDirectory));
            return dirDeleted;
        } 
        catch (IOException e) {
            System.out.println("Error: An error happened trying to delete the files");
        }
        return false;
    }

    
    /**
     * Delete all passwords stored by the application.
     * 
     * @return true if the file was deleted, false if it was not possible to delete it, or if the file does not exist.
     */
    private static boolean deletePasswordFiles() {
        try {return Files.deleteIfExists(passFile.toPath());} 
        catch (IOException e) {
            System.out.println(e.getMessage());
            System.out.println(e.getCause());
            e.printStackTrace();
        }
        return false;
    }


    /**
     * Close all files related to the application
     * 
     * @param aUser - the user of the application
     * @param logins - the list of logins registered in the application
     */
    public static void closeFiles(AppUser aUser, LoginList logins) {
        user = aUser;
        logs = logins;
        if (!isConfigured) saveConfigs();
        if (user != null) saveUserFile();
        
        if (logs != null) {savePassFile();}
    }
}