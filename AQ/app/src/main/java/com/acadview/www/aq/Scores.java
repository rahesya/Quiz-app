package com.acadview.www.aq;

public class Scores {
    private String CategoryName;
    private int Score;

    Scores(){

    }

    public String getCategoryName() {
        return CategoryName;
    }

    public void setCategoryName(String categoryName) {
        CategoryName = categoryName;
    }

    public int getScore() {
        return Score;
    }

    public void setScore(int score) {
        Score = score;
    }

    public Scores(String categoryName, int score) {
        CategoryName = categoryName;
        Score = score;
    }

}
