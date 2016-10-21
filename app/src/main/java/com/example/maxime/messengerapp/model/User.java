package com.example.maxime.messengerapp.model;

/**
 * Created by maxime on 19/10/16.
 */

public class User {
    //String uuid;
    String login;
    String pwd;

    public User() {}

//    public String getUuid() {
//        return uuid;
//    }

    public User(String login, String pwd) {
        this.login = login;
        this.pwd = pwd;
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
