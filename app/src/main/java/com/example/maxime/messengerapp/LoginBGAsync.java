package com.example.maxime.messengerapp;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.example.maxime.messengerapp.service.SignInService;
import com.example.maxime.messengerapp.service.User;

/**
 * Created by maxime on 12/10/16.
 */

public class LoginBGAsync extends AsyncTask <String, Void, Boolean>  {

    private final String TAG = LoginBGAsync.class.getName();

    private Activity myactivity = null;
    public LoginListener loginListener;

    public LoginBGAsync(Activity myactivity) {
        this.myactivity = myactivity;
    }

    public void setLoginListener(LoginListener loginListener) {
        this.loginListener = loginListener;
    }


    @Override
    protected void onPreExecute() {
        myactivity.findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
    }

    @Override
    protected Boolean doInBackground(String[] params) {
        String login = params[0].toString();
        String pwd = params[1].toString();
        User user = new User(login,pwd);
        return SignInService.signInResponse(user);
    }

    @Override
    protected void onPostExecute(Boolean result) {
        myactivity.findViewById(R.id.loadingPanel).setVisibility(View.GONE);
    }

    public interface LoginListener{
        void onLogin(boolean result);
    }
}
