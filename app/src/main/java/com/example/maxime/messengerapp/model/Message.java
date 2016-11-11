package com.example.maxime.messengerapp.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by victor on 23/10/16.
 */

public class Message {
    public String uuid;
    public String login;
    public String message;
    public ArrayList<Image> images;


    public Message(String msg, String login, Image image) {
        this.message = msg;
        this.login = login;

        this.uuid = UUID.randomUUID().toString();
        this.images = new ArrayList<>(10);
        this.images.add(0,image);
    }

    public Message(String msg, String login) {
        this.message = msg;
        this.login = login;
        this.uuid = UUID.randomUUID().toString();
        Image image = new Image();
        this.images = new ArrayList<>(10);
        this.images.add(image);
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

    public ArrayList<Image> getAttachments() {
        return images;
    }

    public void setAttachments(ArrayList<Image> attachments) {
        this.images = attachments;
    }

    @Override
    public String toString() {
        return "Message{" +
                "uuid='" + uuid + '\'' +
                ", login='" + login + '\'' +
                ", message='" + message + '\'' +
                ", attachments=" + images +
                '}';
    }
}
