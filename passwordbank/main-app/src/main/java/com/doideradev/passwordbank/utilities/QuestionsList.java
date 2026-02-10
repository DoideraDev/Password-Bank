package com.doideradev.passwordbank.utilities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public final class QuestionsList extends ArrayList<String>{

    public QuestionsList() {
        super.addAll(Arrays.asList(questions()));
    }

    
    
    
    /**
     * <p> This method return {@code false} by default.
     * 
     * <p> No element can be added in this list.
     * 
     * @param element - the element to be added (the element will not be added)
     * @return this method will always return {@code false} because this list is intended to be unmodifiable 
     */
    @Deprecated
    @Override
    public boolean add(String element) {
        return false;
    }

    
    

    /**
     * <p> This method does nothing.
     * 
     * <p> No element can be added in this list.
     * 
     * @param index - index in wich the element will be added (this action will not be done)
     * @param element - the element to be added (the element will not be added)
     */
    @Deprecated
    @Override
    public void add(int index, String element) {
        // no-op
    }




    /**
     * <p> This method return {@code false} by default.
     * 
     * <p> No element can be added in this list.
     * 
     * @param index - index in wich the elements will start to be added (this action will no be done)
     * @param elements - the list of elements to be added (the elements will not be added)
     * @return this method will always return {@code false} because this list is intended to be unmodifiable
     */
    @Deprecated
    @Override
    public boolean addAll(int index, Collection<? extends String> elements) {
        return false;
    }
    



    /**
     * <p> This method return {@code false} by default.
     * 
     * <p> No element can be added in this list.
     * 
     * @param elements - the list of elements to be added (the elements will not be added)
     * @return this method will always return {@code false} because this list is intended to be unmodifiable
     */
    @Deprecated
    @Override
    public boolean addAll(Collection<? extends String> elements) {
        return false;
    }




    /**
     * This method inplementation return {@code null} by default.
     * <p>
     * No element in this list can be removed.
     * 
     * @param i - the index of the element to be removed (the element will not be removed)
     * @return this method will always return {@code null} because the elements cannot be removed
     */
    @Deprecated
    @Override
    public String remove(int i) {
        return null;
    }
    


    
    /**
     * This method inplementation return {@code false} by default.
     * <p>
     * No element in this list can be removed.
     * 
     * @param s - the String value to remove (the element will not be removed)
     * @return this method will always return {@code false}, the items in this list cannot be removed
     */
    @Deprecated
    @Override
    public boolean remove(Object s) {
        return false;
    }
    


    
    /**
     * This method inplementation return {@code false} by default.
     * <p>
     * No element in this list can be removed.
     * 
     * @param elements - the list of elments to be removed (the elements will not be removed)
     * @return this method will always return {@code false}, the items in this list cannot be removed
     */
    @Deprecated
    @Override
    public boolean removeAll(Collection<? extends Object> elements) {
        return false;
    }
    


    
    /**
     * This method inplementation return {@code false} by default.
     * <p>
     * No element in this list can be removed.
     * 
     * @param filter - a predicate to remove elements in this list (the elements will not be removed)
     * @return this method will always return {@code false}, the items in this list cannot be removed
     */
    @Deprecated
    @Override
    public boolean removeIf(Predicate<? super String> filter) {
        return false;
    }




    /**
     * This method does nothing.
     * <p>
     * This list cannot be modified.
     * 
     * @param operator - the operator to apply to each element (this action will not be done)
     */
    @Deprecated
    @Override
    public void replaceAll(UnaryOperator<String> operator) {
        // no-op
    }




    /**
     * This method returns {@code false} by default.
     * <p>
     * No element in this list can be removed.
     * 
     * @param elements - list of elements to retain in list (this action will not be done)
     * @return {@code false}. This list is intended to be unmodifiable 
     */
    @Deprecated
    @Override
    public boolean retainAll(Collection<?> elements) {
        return false;
    }




    /**
     * This method returns {@code null} by default.
     * <p>
     * No elemt in this list can be modified.
     * 
     * @param index - the index of the element to be replaced (this action will not be done)
     * @param element = the new element to be stored in this list (the element will not be stored in list)
     * @return this method will always {@code false}, because this list is intended to be unmodifiable
     */
    @Deprecated
    @Override
    public String set(int index, String element) {
        return null;
    }




    /**
     * <p> This method does nothing, because this list cannot be cleared
     * 
     * <p> This list is intended to not be modifiable;
     */
    @Deprecated
    @Override
    public void clear() {
        // no-op;
    }



    public ObservableList<String> getList() {
        return FXCollections.observableArrayList(this);
    }


    private final String[] questions() {
        String question1 = "What's your father/mother's maiden name";
        String question2 = "What's the name of your first pet";
        String question3 = "What's the name of your first boyfriend/girlfriend";
        String question4 = "What's the name of the first city you lived";
        String question5 = "What's the number of your first house/apartment";
        String question6 = "What's your blood type";
        String question7 = "What's the name of your first school";
        String question8 = "What's the model of your first vehicle";
        String question9 = "What's your oldest son's name";
        String question10 = "What's your youngest son's name";
        String question11 = "When is your wedding anniversery";
        String question12 = "When is your first son's birthday";

        String[] questions = {question1, question2, question3, question4, question5, 
                              question6, question7, question8, question9, question10, 
                              question11, question12};

        return questions;
    }


    // class InvalidMethodException extends Exception {

    //     public InvalidMethodException() {
    //         super("Cannot delete any element from this list");
    //     }
    // }
}
