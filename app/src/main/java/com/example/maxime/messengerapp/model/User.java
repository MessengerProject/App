package com.example.maxime.messengerapp.model;

/**
 * Created by maxime on 19/10/16.
 */

public class User {
    String login;
    String pwd;

    public User() {}

    public User(String pwd, String login) {
        this.pwd = pwd;
        this.login = login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getLogin() {
        return login;
    }



}
