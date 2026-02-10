package com.doideradev.doiderautils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class AppErrorLogger {

    private Exception exception;
    private List<String> logText;
    private String logFileName;
    private final static String rootLogDirectory = System.getenv("APPDATA") + "\\Password Bank\\logs\\";
    
    /**
     * Constructor for the AppErrorLogger class
     * @param e - Exception to log
     * @param logFileName - Name of the log file
     */
    public AppErrorLogger(Exception e, String logFileName) {
        this.exception = e;
        this.logFileName = logFileName;
        createLogContent();
        createLogFile();
    }


    /**
     * Create the log file in the logs directory
     */
    private void createLogFile() {
        try {
            File errorLog = new File(rootLogDirectory + logText.get(0) + ".txt");
            logText.remove(0);
            if (errorLog.createNewFile()) {
                FileWriter writer = new FileWriter(errorLog);
                for (String string : logText) {
                    writer.write(string);
                }
                writer.close();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.out.println(e.getCause());
            e.printStackTrace();
        }
    }


    /**
     * Create the content of the log file
     */
    private void createLogContent() {
        logText = new java.util.ArrayList<>();
        logText.add(logFileName + "_" + System.currentTimeMillis());
        logText.add("An error occurred in the application.\n\n");
        logText.add("Exception Message: " + exception.getMessage() + "\n");
        logText.add("Exception Cause: " + exception.getCause() + "\n\n");
        logText.add("Stack Trace:\n");
        for (StackTraceElement element : exception.getStackTrace()) {
            logText.add(element.toString() + "\n");
        }
    }

}
