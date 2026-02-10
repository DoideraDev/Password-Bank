package com.doideradev.updater.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import org.apache.commons.io.FileUtils;

import com.doideradev.updater.utilities.AppConfigs;


public abstract class Updater {

    private static AppConfigs configs;

    static {
        openConfigFile();
    }


    public static void updateApplication() {
        openConfigFile();
        downloadMSIFile();
        deleteOldVersion();
        installNewVersion();
    }
    

    public static boolean downloadMSIFile() {
        try {
            URL url = new URL(configs.getDownloadURL());
            Path savePath = Path.of(System.getProperty("user.dir") + "/update.msi");
            InputStream in = url.openStream(); 
            Files.copy(in, savePath, StandardCopyOption.REPLACE_EXISTING);
            
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println(e.getCause());
            e.printStackTrace();
            return false;
        }
        return true;
    }


    public static boolean installNewVersion() {
        Process process;
        try {
            process = new ProcessBuilder("msiexec", "/i", "update.msi", "/qn", "/log", "install.log").inheritIO().start();
            
            int exitCode = process.waitFor();
    
            if (exitCode == 0) {
                System.out.println("Installation succeeded.");
            } else {
                System.out.println("Installation failed with code: " + exitCode);
            }
            return exitCode == 0;

        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.out.println(e.getCause());
            e.printStackTrace();

        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
            System.out.println(e.getCause());
            e.printStackTrace();

        }
        return false;
    }


    public static boolean deleteOldVersion() {
        try {
            FileUtils.deleteDirectory(new File(configs.getInstallDir()));
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.out.println(e.getCause());
            e.printStackTrace();
            return false;
        }
        return true;
    }


    public static boolean openConfigFile() {
        File confFile = new File(System.getenv("APPDATA") + "\\Password Bank\\configs\\app.config");
        try {
            FileInputStream fileInput = new FileInputStream(confFile);
            ObjectInputStream objectInput = new ObjectInputStream(fileInput);
            configs = (AppConfigs) objectInput.readObject();
            objectInput.close();
            fileInput.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println(e.getCause());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static AppConfigs getConfigs() {return configs;}

    public static void revertUpdate() {
        try {
            Files.deleteIfExists(Path.of(System.getProperty("user.dir") + "/update.msi"));
            FileUtils.copyDirectory(new File(System.getProperty("user.dir")), new File(configs.getInstallDir()));
            
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.out.println(e.getCause());
            e.printStackTrace();
        }
    }
}