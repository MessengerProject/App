package com.example.maxime.messengerapp.task;

import android.os.AsyncTask;

import com.dd.processbutton.iml.ActionProcessButton;
import com.example.maxime.messengerapp.model.User;
import com.example.maxime.messengerapp.utils.services.LoginService;

import static java.lang.Integer.getInteger;

/**
 * Created by maxime on 12/10/16.
 */

public class LoginBGAsync extends AsyncTask<Void, Void, Boolean> {

    //Interface LoginListener
    public interface LoginListener {
        void onLogin(boolean result);
    }

    private final String TAG = LoginBGAsync.class.getName();

    private User user;
    private ActionProcessButton btn;


    public LoginBGAsync( User user) {
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


    public void setLoginListener(LoginListener loginListener) {
        LoginListener loginListener1 = loginListener;
    }
}
