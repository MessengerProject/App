package com.example.maxime.messengerapp.task;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.dd.processbutton.iml.ActionProcessButton;
import com.example.maxime.messengerapp.model.User;
import com.example.maxime.messengerapp.service.LoginService;

import static java.lang.Integer.getInteger;

/**
 * Created by maxime on 12/10/16.
 */

public class LoginBGAsync extends AsyncTask<Void, Void, Boolean> {

    private final String TAG = LoginBGAsync.class.getName();
    private Context mContext;
    private User user;
    public LoginListener loginListener;
    public ActionProcessButton btn;


    public LoginBGAsync(Context context, User user) {
        this.mContext = context;
        this.user = user;
    }

    public void setBtn(ActionProcessButton btn) {
        this.btn = btn;
    }

    @Override
    protected void onPreExecute() {
        btn.setProgress(1);
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        //TODO : Check DB for User
        return LoginService.loginResponse(user);
    }

    @Override
    public void onPostExecute(Boolean result) {
        if (result) {
            btn.setProgress(-1);
        }
    }

    //Interface
    public interface LoginListener {
        void onLogin(boolean result);
    }

    public void setLoginListener(LoginListener loginListener) {
        this.loginListener = loginListener;
    }
}
