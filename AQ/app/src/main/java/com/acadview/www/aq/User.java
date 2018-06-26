package com.acadview.www.aq;

public class User {
    private String password;
    private String username;
    private String questionsAttempted;
    private String correctAttempts;
    private String totalScore;
    private String pathtobackimage;
    private String pathtoprofileimage;
    private String phone;

    public User(){

    }

    public User(String password, String username, String questionsAttempted, String correctAttempts, String totalScore, String pathtobackimage, String pathtoprofileimage, String phone) {
        this.password = password;
        this.username = username;
        this.questionsAttempted = questionsAttempted;
        this.correctAttempts = correctAttempts;
        this.totalScore = totalScore;
        this.pathtobackimage = pathtobackimage;
        this.pathtoprofileimage = pathtoprofileimage;
        this.phone = phone;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getQuestionsAttempted() {
        return questionsAttempted;
    }

    public void setQuestionsAttempted(String questionsAttempted) {
        this.questionsAttempted = questionsAttempted;
    }

    public String getCorrectAttempts() {
        return correctAttempts;
    }

    public void setCorrectAttempts(String correctAttempts) {
        this.correctAttempts = correctAttempts;
    }

    public String getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(String totalScore) {
        this.totalScore = totalScore;
    }

    public String getPathtobackimage() {
        return pathtobackimage;
    }

    public void setPathtobackimage(String pathtobackimage) {
        this.pathtobackimage = pathtobackimage;
    }

    public String getPathtoprofileimage() {
        return pathtoprofileimage;
    }

    public void setPathtoprofileimage(String pathtoprofileimage) {
        this.pathtoprofileimage = pathtoprofileimage;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
