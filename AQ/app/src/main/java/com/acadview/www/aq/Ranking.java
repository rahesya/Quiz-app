package com.acadview.www.aq;

public class Ranking {
    private String userName;
    private int score;
    public Ranking(){

    }

    public Ranking(String userName, int score) {
        this.userName = userName;
        this.score = score;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public long getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
