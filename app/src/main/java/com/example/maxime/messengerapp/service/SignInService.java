package com.example.maxime.messengerapp.service;

import android.util.Log;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by victor on 19/10/16.
 */

public class SignInService {

    public static boolean signInResponse(User user){

        try {
            String parameters = user.getLogin()+ "/" + user.getPassword();
            String urlStr = "https://training.loicortola.com/chat-rest/1.0/connect/";
            String url = urlStr + parameters;
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(url)
                    .build();

            Response response = client.newCall(request).execute();
            return response.code() < 300;
        } catch(IOException e) {
            Log.e("HTTP GET:", e.toString());
        }
        return false;
    }
}
