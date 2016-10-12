package com.example.maxime.messengerapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button btnLogin = (Button)findViewById(R.id.ButtonLogin);


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText login = (EditText)findViewById(R.id.login);
                EditText pwd = (EditText)findViewById(R.id.pwd);

                Login_BG_Async login_bg_async = new Login_BG_Async();
                Login_BG_Async.LoginListener loginListener = new Login_BG_Async.LoginListener() {
                    @Override
                    public void onLogin(boolean result) {

                        if (result == false)
                        {
                            Toast.makeText(getApplication(), "Unknown User", Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            //TODO Go to next page with
                            //Intent intent = new Intent(getApplication(),Login.class);
                            //startActivity(intent);
                        }
                    }
                };
                login_bg_async.setLoginListener(loginListener);
                login_bg_async.execute(login, pwd);
                try {
                    loginListener.onLogin((Boolean) login_bg_async.get());
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
