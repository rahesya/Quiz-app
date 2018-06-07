package com.acadview.www.aq;

public class QuestionScore {

    private String question_Category;
    private String user;
    private String score;
    private String categoryId;
    private String categoryName;

    QuestionScore(){

    }

    public QuestionScore(String question_Category, String user, String score, String categoryId, String categoryName) {
        this.question_Category = question_Category;
        this.user = user;
        this.score = score;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }

    public String getQuestion_Category() {
        return question_Category;
    }

    public void setQuestion_Category(String question_Category) {
        this.question_Category = question_Category;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}

