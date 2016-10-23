package com.example.maxime.messengerapp.model;

import java.util.Date;
import java.util.UUID;

/**
 * Created by maxime on 18/10/16.
 */

public class Message {
    public User user;
    public String elementMessage;
    public String date;
    String uuid;

    public Message(String elementMessage, String date, User user) {
        super();
        this.elementMessage = elementMessage;
        this.date =date;
        this.user = user;
        this.uuid = UUID.randomUUID().toString();
    }

    @Override
    public String toString() {
        return "Message{" +
                "user='" + user.toString() + '\'' +
                ", elementMessage='" + elementMessage + '\'' +
                ", date='" + date + '\'' +
                '}';
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getElementMessage() {
        return elementMessage;
    }

    public void setElementMessage(String elementMessage) {
        this.elementMessage = elementMessage;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
