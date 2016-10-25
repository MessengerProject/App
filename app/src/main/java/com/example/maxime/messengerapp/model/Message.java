package com.example.maxime.messengerapp.model;

import java.util.UUID;

/**
 * Created by victor on 23/10/16.
 */

public class Message {
        public String uuid;
        public String login;
        public String message;



    public Message(String msg, String login) {
        this.message = msg;
        this.login = login;
        this.uuid = UUID.randomUUID().toString();
    }

    @Override
    public String toString() {
        return "SimpleMessage{" +
                "uuid='" + uuid + '\'' +
                ", login='" + login + '\'' +
                ", message='" + message + '\'' +
                '}';
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
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
}
