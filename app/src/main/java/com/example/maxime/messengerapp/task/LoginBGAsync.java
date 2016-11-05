package com.example.maxime.messengerapp.task;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.example.maxime.messengerapp.R;
import com.example.maxime.messengerapp.model.User;
import com.example.maxime.messengerapp.service.LoginService;
import com.example.maxime.messengerapp.service.SetProfilPictureService;

import static android.content.Context.MODE_PRIVATE;
import static java.lang.Integer.getInteger;

/**
 * Created by maxime on 12/10/16.
 */

public class LoginBGAsync extends AsyncTask <Void, Void, Boolean>  {

    private final String TAG = LoginBGAsync.class.getName();
    private Context mContext;
    private User user;
    public LoginListener loginListener;



    public LoginBGAsync (Context context, User user){
        this.mContext = context;
        this.user = user;
    }
    //private boolean result;

    public void setLoginListener(LoginListener loginListener) {
        this.loginListener = loginListener;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        Log.i(TAG,result.toString());
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        //TODO : Check DB for User
        return LoginService.loginResponse(user);
    }
    public interface LoginListener{
        void onLogin(boolean result);
    }
}
