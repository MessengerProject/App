package com.example.maxime.messengerapp.model;

import java.util.UUID;

/**
 * Created by victor on 23/10/16.
 */

public class SimpleMessage {
        public String uuid;
        public String login;
        public String message;


        public SimpleMessage(String message, String uuid, User user) {
            super();
            this.login = user.getLogin();
            this.message = message;
            this.uuid = uuid;
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
