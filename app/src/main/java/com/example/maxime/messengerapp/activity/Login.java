package com.example.maxime.messengerapp.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.maxime.messengerapp.LoginBGAsync;
import com.example.maxime.messengerapp.Messenger;
import com.example.maxime.messengerapp.R;

import java.util.concurrent.ExecutionException;

public class Login extends AppCompatActivity {
    private final String TAG = Login.class.getName();

    //User user = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        final Button btnLogin = (Button)findViewById(R.id.ButtonLogin);
        final EditText loginET = (EditText)findViewById(R.id.login);
        final EditText pwdET = (EditText)findViewById(R.id.pwd);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String login = String.valueOf(loginET.getText());
                String pwd = String.valueOf(pwdET.getText());

                Log.i(TAG,login + "   " + pwd);

                String params[] = {login, pwd};
                LoginBGAsync login_bg_async = new LoginBGAsync();
                LoginBGAsync.LoginListener loginListener = new LoginBGAsync.LoginListener() {
                    @Override
                    public void onLogin(boolean result) {
                        if (!result)
                        {
                            Toast.makeText(getApplication(), "Unknown User", Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            Intent intent = new Intent(getApplication(),Messenger.class);
                            intent.putExtra("login", login);
                            startActivity(intent);
                            Toast.makeText(getApplication(), "Connected!!", Toast.LENGTH_LONG).show();

                        }
                    }
                };


                login_bg_async.setLoginListener(loginListener);
                login_bg_async.execute(params);
                try {
                    loginListener.onLogin(login_bg_async.get());
                    //Toast.makeText(getApplication(), login_bg_async.get().toString(), Toast.LENGTH_LONG).show();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }


            }
        });

    }
}
