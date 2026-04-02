package com.doideradev.passwordbank.utilities;

import java.util.ArrayList;
import java.util.Arrays;

import com.doideradev.doiderautils.UtilsClasses;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


/**
 * <p>Class that contains the list of security questions to choose from when creating an account
 * <p>This class is used to create an observable list of questions that can be used in the UI
 * <p>The questions are stored in an array and then added to an observable list that can be accessed by the UI
 */
public final class QuestionsList {

    private final ObservableList<String> questions;
    private ArrayList<String> questionsList;

    public QuestionsList() {
        questionsList = new ArrayList<>();
        questionsList.addAll(Arrays.asList(questions()));
        questions = FXCollections.observableArrayList(questionsList);
    }

    
    public ObservableList<String> getList() {
        return FXCollections.unmodifiableObservableList(questions);
    }


    private final String[] questions() {
        UtilsClasses utils = new UtilsClasses();
        var question1 = utils.new Question("What's your father/mother's maiden name");
        var question2 = utils.new Question("What's the name of your first pet");
        var question3 = utils.new Question("What's the name of your first boyfriend/girlfriend");
        var question4 = utils.new Question("What's the name of the first city you lived");
        var question5 = utils.new Question("What's the number of your first house/apartment");
        var question6 = utils.new Question("What's your blood type");
        var question7 = utils.new Question("What's the name of your first school");
        var question8 = utils.new Question("What's the model of your first vehicle");
        var question9 = utils.new Question("What's your oldest son's name");
        var question10 = utils.new Question("What's your youngest son's name");
        var question11 = utils.new Question("When is your wedding anniversery");
        var question12 = utils.new Question("When is your first son's birthday");

        String[] questions = {question1.getQuestion(), question2.getQuestion(), question3.getQuestion(), question4.getQuestion(), 
                             question5.getQuestion(), question6.getQuestion(), question7.getQuestion(), question8.getQuestion(), 
                             question9.getQuestion(), question10.getQuestion(), question11.getQuestion(), question12.getQuestion()};

        return questions;
    }
}
