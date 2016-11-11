package com.example.maxime.messengerapp.service;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import com.example.maxime.messengerapp.model.Image;
import com.example.maxime.messengerapp.model.User;

import java.io.ByteArrayOutputStream;
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
    private static String encodedImage;
    private static ByteArrayOutputStream baos;
    public static Image imageMessage;
    private static byte[] b;

    public static Bitmap getImageMessageService(User user, String imageURL) {
        try {
            //Get Profile informations
            String url = imageURL;
            OkHttpClient client = new OkHttpClient();
            String credential = Credentials.basic(user.getLogin(), user.getPassword());
            Request request = new Request.Builder()
                    .header("Authorization", credential)
                    .url(url)
                    .build();

            Response response = client.newCall(request).execute();
            Log.i(TAG, response.toString());
            InputStream inputStream = response.body().byteStream();
            imageBitmap = BitmapFactory.decodeStream(inputStream);
            /*baos = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
            b = baos.toByteArray();
            encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
            imageMessage = new Image("image/png", encodedImage);
            response.close();*/
            return imageBitmap;

        } catch (IOException e) {
            Log.e("HTTP Get:", e.toString());
        }
        return null;
    }
}
