package com.example.maxime.messengerapp;

import android.os.AsyncTask;
import android.util.Log;

/**
 * Created by maxime on 12/10/16.
 */

public class Login_BG_Async extends AsyncTask <String, Void, Boolean>  {

    private final String TAG = Login_BG_Async.class.getName();


    public LoginListener loginListener;
    //private boolean result;

    public void setLoginListener(LoginListener loginListener) {
        this.loginListener = loginListener;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        Log.i(TAG,result.toString());
        //loginListener.onLogin(result);
    }

    @Override
    protected Boolean doInBackground(String[] params) {
        Log.i(TAG, "doInBackground \n params :  " + params[0].toString() );
//        String login = params[0].toString();
//        String pwd = params[1].toString();

        //TODO : Check DB for User
        return true;
    }
    public interface LoginListener{
        void onLogin(boolean result);


    }
}
