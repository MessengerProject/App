package com.example.maxime.messengerapp.service;

import android.util.Log;

import com.example.maxime.messengerapp.model.User;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by maxime on 23/10/16.
 */

public class GetMessagesListService {
    private static final String TAG = GetMessagesListService.class.getName();
    //public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public static String GetMessageListResponse(User user){

        try {
            String parameters = user.getLogin()+ "/" + user.getPwd();
            String urlStr = "https://training.loicortola.com/chat-rest/1.0/messages/";
            String url = urlStr + parameters;
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(url)
                    .build();

            Response response = client.newCall(request).execute();

            if (response.code() < 300){
                Log.i(TAG,response.body().string());
                return response.body().string();
            }
        } catch(IOException e) {
            Log.e("HTTP GET:", e.toString());
        }
        return null;
    }
}
