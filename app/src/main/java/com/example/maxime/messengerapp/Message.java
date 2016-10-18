package com.example.maxime.messengerapp;

import java.util.Date;

/**
 * Created by maxime on 18/10/16.
 */

public class Message {
    public String author;
    public String elementMessage;

    public Message(String elementMessage, String author) {
        this.elementMessage = elementMessage;
        this.author = author;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getElementMessage() {
        return elementMessage;
    }

    public void setElementMessage(String elementMessage) {
        this.elementMessage = elementMessage;
    }



}
