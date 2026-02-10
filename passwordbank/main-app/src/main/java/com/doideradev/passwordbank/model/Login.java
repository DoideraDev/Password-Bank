package com.doideradev.passwordbank.model;

import java.io.Serializable;
import java.time.LocalDate;

public final class Login implements Serializable, Comparable<Login> {

    private static final long serialVersionUID = 100301L;

    private String identifier;
    private String userName;
    private Password password;
    private final LocalDate creationDate;
    private LocalDate lastEditDate;
    private int useCount;


    public Login() {
        this.creationDate = LocalDate.now();
        setLastEditDate(creationDate);
    }

    public Login(String identifier, String userName, String password) {
        this.creationDate = LocalDate.now();
        
        setIdentifier(identifier);
        setUserName(userName);
        setPassword(password);
    }


    public String getIdentifier() {
        return this.identifier;
    }
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }


    public Password getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = new Password(password);
        setLastEditDate(LocalDate.now());
    }


    public LocalDate getCreationDate() {
        return creationDate;
    }


    public LocalDate getLastEditDate() {
        return lastEditDate;
    }
    private void setLastEditDate(LocalDate lastEditDate) {
        this.lastEditDate = lastEditDate;
    }


    public int getUseCount() {
        return this.useCount;
    }
    protected void setUseCount(int useCount) {
        this.useCount = useCount;
    }

    /**
     * Increase the use count by one
     */
    public void increaseCount(){
        this.useCount++;
    }

    
    @Override
    public int compareTo(Login login) {
        return this.getIdentifier().compareTo(login.getIdentifier());
    }
}