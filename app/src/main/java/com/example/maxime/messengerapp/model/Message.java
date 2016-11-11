package com.example.maxime.messengerapp.model;

import java.util.UUID;

/**
 * Created by victor on 23/10/16.
 */

public class Message {
    public String uuid;
    public String login;
    public String message;
    public String[] images = new String[1];
    public Attachment[] attachments = new Attachment[1];


    public Message(String msg, String login, Attachment attachment, String images) {
        this.message = msg;
        this.login = login;
        this.uuid = UUID.randomUUID().toString();
        //this.images[0] = attachment;
        attachment.setData(images);
        this.attachments[0] = attachment;
        this.images[0] = images;
    }

    public Message(String msg, String login) {
        this.message = msg;
        this.login = login;
        this.uuid = UUID.randomUUID().toString();
        Attachment attachment = new Attachment();
        this.attachments[0] = attachment;
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

    public Attachment[] getAttachments() {
        return attachments;
    }

    public void setAttachments(Attachment[] attachments) {
        this.attachments = attachments;
    }

    @Override
    public String toString() {
        return "Message{" +
                "uuid='" + uuid + '\'' +
                ", login='" + login + '\'' +
                ", message='" + message + '\'' +
                ", attachments=" + attachments +
                '}';
    }
}
