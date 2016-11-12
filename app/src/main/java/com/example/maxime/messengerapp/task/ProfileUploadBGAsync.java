package com.example.maxime.messengerapp.task;

import android.os.AsyncTask;
import android.util.Log;

import com.example.maxime.messengerapp.model.User;
import com.example.maxime.messengerapp.utils.services.ProfileUploadService;

/**
 * Created by victor on 29/10/16.
 */

public class ProfileUploadBGAsync extends AsyncTask<Void, Void, Boolean>{

    //Interface
    public interface profileUploadListener{
        void onSend(boolean result);
    }

    private final String TAG = LoginBGAsync.class.getName();
    private User user;
    private String lastPassword;
    private profileUploadListener profileUploadListener;

    public ProfileUploadBGAsync( User user, String lastPassword) {
        this.user = user;
        this.lastPassword = lastPassword;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        //TODO : Check DB for User
        return ProfileUploadService.uploadProfile(user,lastPassword);
    }

    @Override
    protected void onPostExecute(Boolean result) {
        Log.i(TAG,result.toString());
    }

    public void setProfileUploadListener(ProfileUploadBGAsync.profileUploadListener profileUploadListener) {
        this.profileUploadListener = profileUploadListener;
    }
}
