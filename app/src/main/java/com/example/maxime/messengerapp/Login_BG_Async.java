package com.example.maxime.messengerapp;

import android.os.AsyncTask;
import android.util.Log;

/**
 * Created by maxime on 12/10/16.
 */

public class Login_BG_Async extends AsyncTask {

    private final String TAG = Login_BG_Async.class.getName();


    public LoginListener loginListener;
    //private boolean result;

    public void setLoginListener(LoginListener loginListener) {
        this.loginListener = loginListener;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        Log.i(TAG,o.toString());
        loginListener.onLogin((Boolean)o);
    }

    @Override
    protected Boolean doInBackground(Object[] params) {
        Log.i(TAG, "doInBackground");
        String login = params[0].toString();
        String pwd = params[1].toString();
        //TODO : Check DB for User
        return true;
    }
    public interface LoginListener{
        void onLogin(boolean result);
    }
}
