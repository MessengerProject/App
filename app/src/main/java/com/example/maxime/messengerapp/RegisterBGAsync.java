package com.example.maxime.messengerapp;

import android.app.Activity;
import android.os.AsyncTask;
import android.view.View;

import com.example.maxime.messengerapp.service.RegisterService;
import com.example.maxime.messengerapp.service.User;

/**
 * Created by victor on 21/10/16.
 */

public class RegisterBGAsync extends AsyncTask<User, Void, Boolean>{

    private final String TAG = LoginBGAsync.class.getName();

    private Activity myactivity = null;
    public RegisterListener registerListener;

    public RegisterBGAsync(Activity myactivity) {
        this.myactivity = myactivity;
    }

    public void setRegisterListener(RegisterListener registerListener) {
        this.registerListener = registerListener;
    }


    @Override
    protected void onPreExecute() {
        myactivity.findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
    }

    @Override
    protected Boolean doInBackground(User[] user) {
        return RegisterService.registerResponse(user[0]);
    }

    @Override
    protected void onPostExecute(Boolean result) {
        myactivity.findViewById(R.id.loadingPanel).setVisibility(View.GONE);
    }

    public interface RegisterListener {
        void onLogin(boolean result);
    }
}
