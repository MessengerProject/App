package com.example.maxime.messengerapp.service;

import android.util.Log;

import com.example.maxime.messengerapp.model.Message;
import com.example.maxime.messengerapp.model.User;
import com.google.gson.Gson;


import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by victor on 23/10/16.
 */
public class SendMessageService {

        private static final String TAG = RegisterService.class.getName();
        public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public static boolean SendMessage(User user, Message message) {
        try {
            String param = user.getLogin()+"/"+user.getPassword();
            Gson gson = new Gson();
            String json = gson.toJson(message);
            Log.i(TAG, "SendMessage: "+json);
            String url = "https://training.loicortola.com/chat-rest/1.0/messages/"+param;
            OkHttpClient client = new OkHttpClient();
            RequestBody body = RequestBody.create(JSON, json);
            Request request = new Request.Builder()
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
