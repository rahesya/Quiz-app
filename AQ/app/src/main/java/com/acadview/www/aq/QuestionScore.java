package com.acadview.www.aq;

public class QuestionScore {

    private String categoryId;
    private String user;
    private String score;
    private String categoryName;

    public QuestionScore(String categoryId, String user, String score, String categoryName) {
        this.categoryId = categoryId;
        this.user = user;
        this.score = score;
        this.categoryName = categoryName;
    }

    QuestionScore(){

    }


    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
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

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}

