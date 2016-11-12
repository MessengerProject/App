package com.example.maxime.messengerapp.service;

import android.util.Log;

import com.example.maxime.messengerapp.model.User;

import java.io.IOException;

import okhttp3.Credentials;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by maxime on 23/10/16.
 */

public class GetMessagesListService {
    private static final String TAG = GetMessagesListService.class.getName();
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static String url;
    private static OkHttpClient client;
    private static String credential;
    private static String data = "";

    public static String GetMessageListResponse(User user, int limit,int offset ){
        try {

            url = "https://training.loicortola.com/chat-rest/2.0/messages?limit=" + limit + "&offset=" + offset;
            client= new OkHttpClient();
            credential = Credentials.basic(user.getLogin(), user.getPassword());

            Request request = new Request.Builder()
                    .header("Authorization", credential)
                    .url(url)
                    .build();

            Response response = client.newCall(request).execute();
            if (response.code() < 300){
                data = response.body().string();
                response.close();
            }
        } catch(IOException e) {
            Log.e("HTTP GET:", e.toString());
        }
        //Log.i(TAG, data);
        return data;
    }
}
