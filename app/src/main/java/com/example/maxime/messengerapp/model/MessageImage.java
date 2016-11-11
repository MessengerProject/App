package com.example.maxime.messengerapp.model;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * Created by victor on 11/11/16.
 */

public class MessageImage {

    public String uuid;
    public String login;
    public String message;
    public String[] images;


    public MessageImage(String msg, String login, String image) {
        this.message = msg;
        this.login = login;
        this.uuid = UUID.randomUUID().toString();
        this.images[0] = image;
    }

    public MessageImage(String msg, String login) {
        this.message = msg;
        this.login = login;
        this.uuid = UUID.randomUUID().toString();
        String image = " ";
        this.images[0] = image;
    }


    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String[] getAttachments() {
        return images;
    }

    public void setAttachments( String[] attachments) {
        this.images = attachments;
    }

    @Override
    public String toString() {
        return "MessageImage{" +
                "uuid='" + uuid + '\'' +
                ", login='" + login + '\'' +
                ", message='" + message + '\'' +
                ", images=" + images +
                '}';
    }
}

