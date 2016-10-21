package com.example.maxime.messengerapp.model;

import java.util.Date;

/**
 * Created by maxime on 18/10/16.
 */

public class Message {
    public String author;
    public String elementMessage;

    public Message(String elementMessage, String author) {
        super();
        this.elementMessage = elementMessage;
        this.author = author;
    }

    public String getAuthor() {
        return this.author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getElementMessage() {
        return this.elementMessage;
    }

    public void setElementMessage(String elementMessage) {
        this.elementMessage = elementMessage;
    }



}
