package com.doideradev.passwordbank.utilities;

import java.io.Serializable;
import java.util.Date;

/**
 * App Configurations class
 * 
 * <p> Contains basic app configuration data
 */
public class AppConfigs implements Serializable {
    
    private static final Long serialVersionUID = 1110000001L;

    private final String UUID = "565f6d83-e522-421a-82ad-21704607de14";
    private String UPDATE_RELEASE_URL = "https://api.github.com/repos/DoideraDev/Password-Bank/releases/latest";
    private String UPDATE_DOWNLOAD_URL = "";
    private final Date appInstallDate;
    private final String installDir;
    private final String appVersion;

    public AppConfigs(String version) {
        this.appVersion = version;
        this.installDir = System.getProperty("user.dir");
        this.appInstallDate = new Date();
    }

    public String getUUID() {return UUID;}
    
    public String getAppVersion() {return appVersion;}
    
    public String getInstallDir() {return installDir;}
    
    public Date getappInstallDate() {return appInstallDate;}
    
    public Date getAppInstallDate() {return appInstallDate;}
    
    public String getReleaseURL() {return UPDATE_RELEASE_URL;}
    
    /** Sets the URL for checking updates - <b> Testing purposes </b> 
     * @param url - New release URL
    */
    public void setReleaseURL(String url) {UPDATE_RELEASE_URL = url;}
    
    public String getDownloadURL() {return UPDATE_DOWNLOAD_URL;}
    public void setDownloadURL(String url) {UPDATE_DOWNLOAD_URL = url;}
    
    public static Long getSerialversion() {return serialVersionUID;}
}