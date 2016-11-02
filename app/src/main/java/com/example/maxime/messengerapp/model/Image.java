package com.example.maxime.messengerapp.model;

/**
 * Created by victor on 02/11/16.
 */

public class Image {

    String mimeType;
    String data;

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Image(String mimeType, String data) {
        this.mimeType = mimeType;
        this.data = data;
    }
}
