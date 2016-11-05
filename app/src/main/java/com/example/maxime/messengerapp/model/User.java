package com.example.maxime.messengerapp.model;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;

/**
 * Created by maxime on 19/10/16.
 */

public class User {
    String login;
    String password;
    String email;
    Image picture;

    public User() {}
    public User(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public User(String login, String password, String email) {
        this.login = login;
        this.password = password;
        this.email = email;
        //this.picture = new Image("image/jpg","");
    }

    public User(String login, String password, String email, Image picture) {
        this.login = login;
        this.password = password;
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

    public Image getPicture() {
        return picture;
    }

    public void setPicture(Image picture) {
        this.picture = picture;
    }
}
