package com.example.maxime.messengerapp.model;

/**
 * Created by maxime on 19/10/16.
 */

public class User {
    String login;
    String password;

    public User() {}
    public User(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLogin() {
        return login;
    }



}
