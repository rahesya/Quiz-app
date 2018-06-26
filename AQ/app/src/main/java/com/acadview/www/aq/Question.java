package com.acadview.www.aq;
import java.util.Comparator;

public class Question{
    private String Question,AnswerA,AnswerB,AnswerC,AnswerD,CorrectAnswer,CategoryId,IsImageQuestion,NoOfCorrectAttempts,NoOfAttempts,Qno,Difficulty;

    public Question(String question, String answerA, String answerB, String answerC, String answerD, String correctAnswer, String categoryId, String isImageQuestion, String noOfCorrectAttempts, String noOfAttempts, String qno, String difficulty) {
        Question = question;
        AnswerA = answerA;
        AnswerB = answerB;
        AnswerC = answerC;
        AnswerD = answerD;
        CorrectAnswer = correctAnswer;
        CategoryId = categoryId;
        IsImageQuestion = isImageQuestion;
        NoOfCorrectAttempts = noOfCorrectAttempts;
        NoOfAttempts = noOfAttempts;
        Qno = qno;
        Difficulty = difficulty;
    }

    public String getQuestion() {
        return Question;
    }

    public void setQuestion(String question) {
        Question = question;
    }

    public String getAnswerA() {
        return AnswerA;
    }

    public void setAnswerA(String answerA) {
        AnswerA = answerA;
    }

    public String getAnswerB() {
        return AnswerB;
    }

    public void setAnswerB(String answerB) {
        AnswerB = answerB;
    }

    public String getAnswerC() {
        return AnswerC;
    }

    public void setAnswerC(String answerC) {
        AnswerC = answerC;
    }

    public String getAnswerD() {
        return AnswerD;
    }

    public void setAnswerD(String answerD) {
        AnswerD = answerD;
    }

    public String getCorrectAnswer() {
        return CorrectAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        CorrectAnswer = correctAnswer;
    }

    public String getCategoryId() {
        return CategoryId;
    }

    public void setCategoryId(String categoryId) {
        CategoryId = categoryId;
    }

    public String getIsImageQuestion() {
        return IsImageQuestion;
    }

    public void setIsImageQuestion(String isImageQuestion) {
        IsImageQuestion = isImageQuestion;
    }

    public String getNoOfCorrectAttempts() {
        return NoOfCorrectAttempts;
    }

    public void setNoOfCorrectAttempts(String noOfCorrectAttempts) {
        NoOfCorrectAttempts = noOfCorrectAttempts;
    }

    public String getNoOfAttempts() {
        return NoOfAttempts;
    }

    public void setNoOfAttempts(String noOfAttempts) {
        NoOfAttempts = noOfAttempts;
    }

    public String getQno() {
        return Qno;
    }

    public void setQno(String qno) {
        Qno = qno;
    }

    public String getDifficulty() {
        return Difficulty;
    }

    public void setDifficulty(String difficulty) {
        Difficulty = difficulty;
    }

    public Question(){

    }

    public static Comparator<Question> Questionratio = new Comparator<Question>() {

        public int compare(Question s1, Question s2) {

            int ratio1 = Integer.parseInt(s1.NoOfAttempts)*100/Integer.parseInt(s1.getNoOfCorrectAttempts());
            int ratio2 = Integer.parseInt(s2.NoOfAttempts)*100/Integer.parseInt(s2.getNoOfCorrectAttempts());

            return ratio1-ratio2;

        }};
}
