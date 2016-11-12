package com.example.maxime.messengerapp.utils.services;

import android.util.Log;

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
 * Created by victor on 29/10/16.
 */

public class ProfileUploadService {
    private static final String TAG = ProfileUploadService.class.getName();
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public static boolean uploadProfile(User user, String lastPassword) {

        try {
            Gson gson = new Gson();
            String json = gson.toJson(user);
            Log.i(TAG, json);
            String url = "https://training.loicortola.com/chat-rest/2.0/profile" ;//;+ param;
            String credential = Credentials.basic(user.getLogin(), lastPassword);
            OkHttpClient client = new OkHttpClient();
            RequestBody body = RequestBody.create(JSON, json);
            Request request = new Request.Builder()
                    .header("Authorization",credential)
                    .post(body)
                    .url(url)
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
