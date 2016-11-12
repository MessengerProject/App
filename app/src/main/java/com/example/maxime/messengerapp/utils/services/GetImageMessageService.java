package com.example.maxime.messengerapp.utils.services;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.example.maxime.messengerapp.model.User;

import java.io.IOException;
import java.io.InputStream;

import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by victor on 11/11/16.
 */

public class GetImageMessageService {

    private static final String TAG = GetImageMessageService.class.getName();
    private static Bitmap imageBitmap;

    public static Bitmap getImageMessageService(User user, String imageURL) {
        try {
            //Get Profile informations
            OkHttpClient client = new OkHttpClient();
            String credential = Credentials.basic(user.getLogin(), user.getPassword());
            Request request = new Request.Builder()
                    .header("Authorization", credential)
                    .url(imageURL)
                    .build();

            Response response = client.newCall(request).execute();
            InputStream inputStream = response.body().byteStream();
            imageBitmap = BitmapFactory.decodeStream(inputStream);
            return imageBitmap;


        } catch (IOException e) {
            Log.e("HTTP Get:", e.toString());
        }
        return null;
    }
}
