package com.doideradev.doiderautils;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.doideradev.doiderautils.UtilsClasses.Answer;
import com.doideradev.doiderautils.UtilsClasses.Question;

public class QnASimpleList implements Serializable {

    private Map<Question, Answer> qnaList;
    private Map<Integer, Question> questionBook;
    private final int size;


    public QnASimpleList(int size) {
        if (size <= 0) throw new IllegalArgumentException("Size must be greater than 0");
        this.size = size;
        qnaList = new java.util.HashMap<>(size);
        questionBook = new java.util.HashMap<>(size);
    }


    public void add(int questionNumber, Question question, Answer answer) {
        validateQuestionNumber(questionNumber);
        qnaList.put(question, answer);
        questionBook.put(questionNumber, question);
    }


    public Pair<Question, Answer> get(int questionNumber) {
        validateQuestionNumber(questionNumber);
        var q = questionBook.get(questionNumber);
        var a = qnaList.get(q);
        return new Pair<>(q, a);
    }


    public void changeAnswer(int questionNumber, Answer newAnswer) {
        validateQuestionNumber(questionNumber);
        var q = questionBook.get(questionNumber);
        qnaList.put(q, newAnswer);
    }

    public void changeQuestion(int questionNumber, Question newQuestion, Answer answer) {
        validateQuestionNumber(questionNumber);
        var oldQ = questionBook.get(questionNumber);
        
        questionBook.remove(questionNumber);
        questionBook.put(questionNumber, newQuestion);
        
        qnaList.remove(oldQ);
        qnaList.put(newQuestion, answer);
    }


    public List<Pair<Question, Answer>> getAll() {
        List<Pair<Question, Answer>> list = new java.util.ArrayList<>();
        for (var entry : qnaList.entrySet()) {
            list.add(new Pair<>(entry.getKey(), entry.getValue()));
        }
        return list;
    }


    public boolean equals(QnASimpleList other) {
        if (this.size != other.size) return false;
        for (int i = 1; i <= size; i++) {
            var realPair = this.get(i);
            var compPair = other.get(i);
            
            var q1 = realPair.getKey();
            var a1 = realPair.getValue();

            var q2 = compPair.getKey();
            var a2 = compPair.getValue();

            if (!q1.getQuestion().equals(q2.getQuestion())) return false;
            if (!a1.getAnswer().equals(a2.getAnswer())) return false;
        }
        return true;
    }
    
    
    private void validateQuestionNumber(int questionNumber) {
        if (questionNumber <= 0 || questionNumber > size) {
            throw new IllegalArgumentException("Question number must be between 1 and " + size);
        }
    }
}
