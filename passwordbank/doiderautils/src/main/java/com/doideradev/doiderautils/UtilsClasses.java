package com.doideradev.doiderautils;

public class UtilsClasses implements java.io.Serializable {
    public class Question implements java.io.Serializable {
        private final String questionValue;

        public Question(String questionValue) {
            this.questionValue = questionValue;
        }

        public String getQuestion() {return questionValue;}


        public boolean equals(Question other) {
            return this.questionValue.equals(other.getQuestion());
        }

        public int hashCode() {
            return questionValue.hashCode();
        }
    }

    public class Answer implements java.io.Serializable {
        private String answerValue;

        public Answer(String answerValue) {
            this.answerValue = answerValue;
        }

        public String getAnswer() {return answerValue;}
        public void setAnswer(String answerValue) {this.answerValue = answerValue;}

        public boolean equals(Answer other) {
            return this.answerValue.equals(other.getAnswer());
        }

        public int hashCode() {
            return answerValue.hashCode();
        }
    }
}
