package com.doideradev.passwordbank.utilities;

import java.util.ArrayList;

import com.doideradev.passwordbank.model.AppUser;
import com.doideradev.passwordbank.model.Login;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


/**
 * <p> A custom list to manage Login objects associated with a specific AppUser.
 * <p> This class provides useful {@code static} methods to retrieve the most used and oldest edited logins.
 */
public final class LoginList extends ArrayList<Login>{

    private static final int value = 3;
    private final AppUser ownerUser;
    private static final long serialVersionUID = 100302L;
    private transient ObservableList<Login> observableList;

    /**
     * Default constructor for LoginList.
     * <p> This constructor initializes a new and empty login list for the specified user.
     * @param user the owner user of the login list
     */
    public LoginList(AppUser user) {
        this.ownerUser = user;
        newObservableList();
    }

    
    /**
     * Copy constructor for LoginList.
     * @param user the owner user of the login list
     * @param list the login list to copy
     */
    public LoginList(AppUser user, LoginList list){
        super(list);
        this.ownerUser = user;
        observableList = FXCollections.observableArrayList(this);
    }


    /**
     * Get the most used logins from the given login list.
     * @param loginList the login list to get the most used logins from
     * @return a login list containing the most used logins
     */
    public static LoginList getMostUsed(final LoginList loginList) {
        LoginList list = new LoginList(loginList.ownerUser);
        LoginList changeList = new LoginList(loginList.ownerUser, loginList);
        for (int i = 0; i < value; i++) {
            Login login = changeList.get(0);

            for (int j = 0; j < changeList.size(); j++) {
                if (changeList.get(j).getUseCount() > login.getUseCount() &&
                    !list.contains(changeList.get(j))) {
                    login = changeList.get(j);
                }
            }
            list.add(login);
            changeList.remove(login);
        }
        return list;
    }


    /**
     * Get the oldest edited logins from the given login list.
     * @param loginList the login list to get the oldest edited logins from
     * @return a login list containing the oldest edited logins
     */
    public static LoginList getOldestEdited(LoginList loginList) {
        LoginList list = new LoginList(loginList.ownerUser);
        LoginList changeList = new LoginList(loginList.ownerUser, loginList);
        for (int i = 0; i < value; i++) {
            Login login = changeList.get(0);

            for (int j = 0; j < changeList.size(); j++) {
                if (changeList.get(j).getLastEditDate().isBefore(login.getLastEditDate()) &&
                    !list.contains(changeList.get(j))) {
                    login = changeList.get(j);
                }
            }
            list.add(login);
            changeList.remove(login);
        }
        return list;
    }


    @Override
    public boolean add(Login element) {
        boolean added = super.add(element);
        if (added) observableList.add(element);
        return added;
    }


    @Override
    public boolean remove(Object element) {
        boolean removed = super.remove((Login)element);
        if (removed) observableList.remove(element);
        return removed;
    }


    public ObservableList<Login> newObservableList() {return observableList = FXCollections.observableArrayList(this);}
    
    public ObservableList<Login> getObservableList() {return observableList;}

    /**
     * Check if the given user is the owner of the login list.
     * @param user the user to check ownership
     * @return true if the user is the owner, false otherwise
     */
    public boolean checkPassOwnership(AppUser user) {return this.ownerUser.equals(user);}
}