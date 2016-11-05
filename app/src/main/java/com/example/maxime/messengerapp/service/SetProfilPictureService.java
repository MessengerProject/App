package com.example.maxime.messengerapp.service;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import com.example.maxime.messengerapp.model.Image;
import com.example.maxime.messengerapp.model.User;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import okhttp3.Credentials;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by maxime on 25/10/16.
 */

public class SetProfilPictureService {
    private static final String TAG = SetProfilPictureService.class.getName();
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private String imageName;

    public static Bitmap SetProfilPicture(User user) {

        try {
            //Get Profile informations
            String url = "https://training.loicortola.com/chat-rest/2.0/profile/"+user.getLogin() ;//;+ param;
            String credential = Credentials.basic(user.getLogin(), user.getPassword());
            Log.i(TAG, credential);
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .header("Authorization",credential)
                    .url(url)
                    .build();

            Response response = client.newCall(request).execute();
            JSONObject jObject = new JSONObject(response.body().string());
            Iterator<?> keys = jObject.keys();
            while( keys.hasNext() ) {
                String key = (String)keys.next();
                if ( jObject.get(key) instanceof JSONObject ) {
                    Log.i(TAG, "SetProfilPicture: "+jObject.get(key));
                }
            }
            response.close();
        } catch (IOException e) {
            Log.e("HTTP POST:", e.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {

            //Get Profile informations
            String url = "https://training.loicortola.com/chat-rest/2.0/files/usr-aaa.png";
            String credential = Credentials.basic(user.getLogin(), user.getPassword());
            Log.i(TAG, credential);
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .header("Authorization",credential)
                    .url(url)
                    .build();

            Response response = client.newCall(request).execute();
            InputStream inputStream = response.body().byteStream();
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
            byte[] b = baos.toByteArray();
            String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
            Image imageProfile = new Image("image/png", encodedImage);
            user.setPicture(imageProfile);
            response.close();
            return bitmap;

        } catch (IOException e) {
            Log.e("HTTP POST:", e.toString());
        }
        return null;
    }
}
