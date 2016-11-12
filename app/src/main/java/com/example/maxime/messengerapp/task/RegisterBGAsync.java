package com.example.maxime.messengerapp.task;

import android.os.AsyncTask;
import android.util.Log;

import com.example.maxime.messengerapp.model.User;
import com.example.maxime.messengerapp.utils.services.RegisterService;

/**
 * Created by maxime on 22/10/16.
 */
public class RegisterBGAsync extends AsyncTask<Void, Void, Boolean> {

    //Interface RegisterListener
    public interface RegisterListener{
        void onRegister(boolean result);
    }

    private final String TAG = RegisterListener.class.getName();
    private User user;
    protected RegisterListener registerListener;


    public RegisterBGAsync( User user) {
        this.user = user;
    }

    public void setRegisterListener(RegisterListener registerListener) {
        this.registerListener = registerListener;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        Log.i(TAG,result.toString());
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        return RegisterService.registerResponse(user);
    }
}
