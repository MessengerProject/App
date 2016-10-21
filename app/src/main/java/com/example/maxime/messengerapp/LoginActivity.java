package com.example.maxime.messengerapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.maxime.messengerapp.service.User;

import java.util.concurrent.ExecutionException;

public class LoginActivity extends AppCompatActivity {

    private final String TAG = LoginActivity.class.getName();

    //Declare private all buttons and edittext
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button btnRegister = (Button)findViewById(R.id.ButtonRegister);
        Button btnLogin = (Button)findViewById(R.id.ButtonLogin);
        LoginActivity.this.findViewById(R.id.loadingPanel).setVisibility(View.GONE);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText loginET = (EditText)findViewById(R.id.login);
                EditText pwdET = (EditText)findViewById(R.id.pwd);
                String login = String.valueOf(loginET.getText());
                String pwd = String.valueOf(pwdET.getText());

                User user = new User(login,pwd);
                LoginBGAsync loginbgasync = new LoginBGAsync(LoginActivity.this);
                LoginBGAsync.LoginListener loginListener = new LoginBGAsync.LoginListener() {
                    @Override
                    public void onLogin(boolean result) {
                        //TODO Go to next page with
                        if(result == true) {
                            Intent intent = new Intent(LoginActivity.this.getApplication(), Messenger.class);
                            LoginActivity.this.startActivity(intent);
                            Toast.makeText(LoginActivity.this.getApplication(), "Connected!!", Toast.LENGTH_LONG).show();
                        }
                    }
                };

                loginbgasync.setLoginListener(loginListener);

                loginbgasync.execute(user);
                try {
                    loginListener.onLogin(loginbgasync.get());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText loginET = (EditText)findViewById(R.id.login);
                EditText pwdET = (EditText)findViewById(R.id.pwd);
                String login = String.valueOf(loginET.getText());
                String pwd = String.valueOf(pwdET.getText());

                User user = new User(login,pwd);
                RegisterBGAsync registerBGAsync = new RegisterBGAsync(LoginActivity.this);
                RegisterBGAsync.RegisterListener registerListener = new RegisterBGAsync.RegisterListener() {
                    @Override
                    public void onLogin(boolean result) {
                        //TODO Go to next page with
                        if(result == true) {
                            /*Intent intent = new Intent(LoginActivity.this.getApplication(), Messenger.class);
                            LoginActivity.this.startActivity(intent);*/
                            Toast.makeText(LoginActivity.this.getApplication(), "Registered!!", Toast.LENGTH_LONG).show();
                        }
                    }
                };

                registerBGAsync.setRegisterListener(registerListener);

                registerBGAsync.execute(user);
                try {
                    registerListener.onLogin(registerBGAsync.get());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
