package com.acadview.www.aq;

public class Ranking {
    private String userName;
    private int score;
    private String profilepic;

    public Ranking(){

    }

    public Ranking(String userName, int score,String profilepic) {
        this.userName = userName;
        this.score = score;
        this.profilepic=profilepic;
    }

    public String getProfilepic() {
        return profilepic;
    }

    public void setProfilepic(String profilepic) {
        this.profilepic = profilepic;
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
