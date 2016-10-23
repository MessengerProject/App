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

    @Override
    public String toString() {
        return "Message{" +
                "author='" + author + '\'' +
                ", elementMessage='" + elementMessage + '\'' +
                '}';
    }

    public String getAuthor() {
        return this.author;
    }


    public String getElementMessage() {
        return this.elementMessage;
    }




}
