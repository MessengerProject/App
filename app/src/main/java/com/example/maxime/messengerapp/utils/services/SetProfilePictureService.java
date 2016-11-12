package com.example.maxime.messengerapp.utils.services;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import com.example.maxime.messengerapp.model.Attachment;
import com.example.maxime.messengerapp.model.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import okhttp3.Credentials;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by maxime on 25/10/16.
 */

public class SetProfilePictureService {
    private static final String TAG = SetProfilePictureService.class.getName();
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static String imageURL;
    private static Bitmap imageBitmap;
    private static String encodedImage;
    private static ByteArrayOutputStream baos;
    private static Attachment attachmentProfile;
    private static byte[] b;

    public static Bitmap SetProfilPicture(User user, String lastPassword) {
        try {
            //Get Profile informations
            String url = "https://training.loicortola.com/chat-rest/2.0/profile/" + user.getLogin();//;+ param;
            String credential = Credentials.basic(user.getLogin(), user.getPassword());
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .header("Authorization", credential)
                    .url(url)
                    .build();

            Response response = client.newCall(request).execute();
            String data = "";
            data = response.body().string();
            JSONObject jsonObj = new JSONObject(data);
            Map<String, String> map = new HashMap<>();
            Iterator iter = jsonObj.keys();
            while (iter.hasNext()) {
                String key = (String) iter.next();
                String value = jsonObj.getString(key);
                map.put(key, value);
            }
            Log.i(TAG, "SetProfilPicture: " + map.toString());
            imageURL = map.get("picture");

            response.close();
        } catch (IOException e) {
            Log.e("HTTP POST:", e.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (imageURL != null) {
            try {

                //Get Profile informations
                String url = imageURL;
                String credential = Credentials.basic(user.getLogin(), user.getPassword());
                Log.i(TAG, credential);
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .header("Authorization", credential)
                        .url(url)
                        .build();

                Response response = client.newCall(request).execute();
                InputStream inputStream = response.body().byteStream();
                imageBitmap = BitmapFactory.decodeStream(inputStream);
                baos = new ByteArrayOutputStream();
                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
                b = baos.toByteArray();
                encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
                attachmentProfile = new Attachment("attachments/png", encodedImage);
                user.setImage(attachmentProfile);
                response.close();
                return imageBitmap;

            } catch (IOException e) {
                Log.e("HTTP POST:", e.toString());
            }
        }
        return null;
    }
}
