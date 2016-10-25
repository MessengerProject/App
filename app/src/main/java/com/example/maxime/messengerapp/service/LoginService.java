package com.example.maxime.messengerapp.service;

import android.util.Log;

import com.example.maxime.messengerapp.model.User;

import java.io.IOException;

import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by maxime on 23/10/16.
 */

public class LoginService {
    private static final String TAG = LoginService.class.getName();

    public static boolean loginResponse(User user){

        try {
            String url = "https://training.loicortola.com/chat-rest/2.0/connect";
            String credential = Credentials.basic(user.getLogin(), user.getPassword());

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .header("Authorization",credential)
                    .url(url)
                    .build();

            Response response = client.newCall(request).execute();
            Log.i(TAG, response.toString());
            return response.code() < 300;
        } catch(IOException e) {
            Log.e("HTTP GET:", e.toString());
        }
        return false;
    }
}
