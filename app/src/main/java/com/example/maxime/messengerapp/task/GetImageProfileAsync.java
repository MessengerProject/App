package com.example.maxime.messengerapp.task;

import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.example.maxime.messengerapp.model.User;

import com.example.maxime.messengerapp.utils.services.SetProfilePictureService;

/**
 * Created by victor on 05/11/16.
 */

public class GetImageProfileAsync extends AsyncTask<Void, Void, Bitmap> {
    private final String TAG = GetImageProfileAsync.class.getName();
    private User user;
    private GetImageProfileAsync.GetImageProfileListener getImageProfileListener;

    public GetImageProfileAsync( User user) {
        this.user = user;
    }

    public void setGetImageProfileListener(GetImageProfileAsync.GetImageProfileListener getImageProfileListener){
        this.getImageProfileListener = getImageProfileListener;
    }

    @Override
    protected Bitmap doInBackground(Void... params) {
        return SetProfilePictureService.SetProfilPicture(user);
    }
    public interface GetImageProfileListener{
        void onGetImageProfile(Bitmap result);
    }
}
