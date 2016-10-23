package com.example.maxime.messengerapp.task;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.example.maxime.messengerapp.model.User;
import com.example.maxime.messengerapp.service.RegisterService;

/**
 * Created by maxime on 22/10/16.
 */
public class RegisterBGAsync extends AsyncTask<Void, Void, Boolean> {

    private final String TAG = RegisterListener.class.getName();
    private Context mContext;
    private User user;
    public RegisterListener registerListener;


    public RegisterBGAsync(Context mContext, User user) {
        this.mContext = mContext;
        this.user = user;
    }

    //private boolean result;

    public void setRegisterListener(RegisterListener registerListener) {
        this.registerListener = registerListener;
    }



    @Override
    protected void onPostExecute(Boolean result) {
        Log.i(TAG,result.toString());
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        Log.i(TAG, "doInBackground Register \n user :  " + user.getLogin()+ user.getPwd());
        return RegisterService.registerResponse(user);
    }
    public interface RegisterListener{
        void onRegister(boolean result);
    }
}
