package com.example.maxime.messengerapp.service;

import android.util.Log;

import com.example.maxime.messengerapp.LoginActivity;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by victor on 21/10/16.
 */

public class RegisterService {

    private static final String TAG = LoginActivity.class.getName();
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public static boolean registerResponse(User user){

        try {
            String param = user.getLogin() + "/" + user.getPassword();
            Gson gson = new Gson();
            String json =gson.toJson(user);
            String url = "https://training.loicortola.com/chat-rest/1.0/register/"+param;

            OkHttpClient client = new OkHttpClient();
            RequestBody body = RequestBody.create(JSON, json);
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();

            Response response = client.newCall(request).execute();

            Log.i(TAG, response.toString());
            Log.i(TAG, json);
            response.body().close();

            Integer code = response.code();
            return code == 200;
        } catch(IOException e) {
            Log.e("HTTP GET:", e.toString());
        }
        return false;
    }
}
