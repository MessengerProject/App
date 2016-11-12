package com.example.maxime.messengerapp.utils.services;

import android.util.Log;

import com.example.maxime.messengerapp.model.Message;
import com.example.maxime.messengerapp.model.User;
import com.google.gson.Gson;


import java.io.IOException;

import okhttp3.Credentials;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by victor on 23/10/16.
 */
public class SendMessageService {

    private static final String TAG = SendMessageService.class.getName();
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public static boolean SendMessage(User user, Message message) {
        try {
            Gson gson = new Gson();
            String json = gson.toJson(message);
            Log.i(TAG, "SendMessage:" + json);
            String url = "https://training.loicortola.com/chat-rest/2.0/messages/";
            OkHttpClient client = new OkHttpClient();
            String credential = Credentials.basic(user.getLogin(), user.getPassword());
            RequestBody body = RequestBody.create(JSON, json);
            Request request = new Request.Builder()
                    .header("Authorization", credential)
                    .url(url)
                    .post(body)
                    .build();
            Response response = client.newCall(request).execute();
            Log.i(TAG, response.toString());
            Integer code = response.code();
            response.close();
            return code == 200;
        } catch (IOException e) {
            Log.e("HTTP POST:", e.toString());
        }
        return false;
    }
}
