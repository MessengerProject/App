package com.example.maxime.messengerapp.model;


/**
 * Created by maxime on 19/10/16.
 */

public class User {
    String login;
    String password;
    String email;
    Attachment picture;

    public User() {}
    public User(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public User(String login, String password, String email) {
        this.login = login;
        this.password = password;
        this.email = email;
        //this.picture = new Attachment("attachments/jpg","");
    }

    public User(String login, String password, String email, Attachment picture) {
        this.login = login;
        this.password = password;
        this.email = email;
        this.picture = picture;
    }
    public User(String login, String email, Attachment picture) {
        this.login = login;
        this.email = email;
        this.picture = picture;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Attachment getPicture() {
        return picture;
    }

    public void setPicture(Attachment picture) {
        this.picture = picture;
    }
}
