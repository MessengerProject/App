package com.example.maxime.messengerapp.task;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.example.maxime.messengerapp.model.User;

import com.example.maxime.messengerapp.service.SetProfilPictureService;

/**
 * Created by victor on 05/11/16.
 */

public class GetImageProfileAsync extends AsyncTask<Void, Void, Bitmap> {
    private final String TAG = GetImageProfileAsync.class.getName();
    private Context mContext = null;
    private User user;
    public GetImageProfileAsync.GetImageProfileListener getImageProfileListener;

    public GetImageProfileAsync(Context mContext, User user) {
        this.mContext = mContext;
        this.user = user;
    }

    public void setGetImageProfileListener(GetImageProfileAsync.GetImageProfileListener getImageProfileListener){
        this.getImageProfileListener = getImageProfileListener;
    }

    @Override
    protected Bitmap doInBackground(Void... params) {
        Bitmap response = SetProfilPictureService.SetProfilPicture(user);
        return response;
    }
    public interface GetImageProfileListener{
        void onGetImageProfile(Bitmap result);
    }
}
