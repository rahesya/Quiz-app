package com.acadview.www.aq;

public class User {
    private String password;
    private String username;
    private String Phone;

    public User(String password, String username, String phone) {
        this.password = password;
        this.username = username;
        Phone = phone;
    }

    public User(){

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

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }
}
