package com.example.maxime.messengerapp.task;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.example.maxime.messengerapp.model.User;

import com.example.maxime.messengerapp.service.SetProfilePictureService;

/**
 * Created by victor on 05/11/16.
 */

public class GetImageProfileAsync extends AsyncTask<Void, Void, Bitmap> {
    private final String TAG = GetImageProfileAsync.class.getName();
    private Context mContext = null;
    private User user;
    private String lastPassword;
    public GetImageProfileAsync.GetImageProfileListener getImageProfileListener;

    public GetImageProfileAsync(Context mContext, User user, String lastPassword) {
        this.mContext = mContext;
        this.user = user;
        this.lastPassword = lastPassword;
    }

    public void setGetImageProfileListener(GetImageProfileAsync.GetImageProfileListener getImageProfileListener){
        this.getImageProfileListener = getImageProfileListener;
    }

    @Override
    protected Bitmap doInBackground(Void... params) {
        Bitmap response = SetProfilePictureService.SetProfilPicture(user, lastPassword);
        return response;
    }
    public interface GetImageProfileListener{
        void onGetImageProfile(Bitmap result);
    }
}
