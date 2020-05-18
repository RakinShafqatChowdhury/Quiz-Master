package com.example.quizmaster.model;

public class Question {

    private String answer;
    private boolean answerVerify;

    public Question(String answer, boolean answerVerify) {
        this.answer = answer;
        this.answerVerify = answerVerify;
    }

    public Question() {
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public boolean getAnswerVerify() {
        return answerVerify;
    }

    public void setAnswerVerify(boolean answerVerify) {
        this.answerVerify = answerVerify;
    }

    @Override
    public String toString() {
        return
                answer
                + answerVerify;

    }

}
