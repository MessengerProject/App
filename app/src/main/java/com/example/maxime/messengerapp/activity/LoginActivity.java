package com.example.maxime.messengerapp.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.maxime.messengerapp.model.User;
import com.example.maxime.messengerapp.task.LoginBGAsync;
import com.example.maxime.messengerapp.R;
import com.example.maxime.messengerapp.task.RegisterBGAsync;

import java.util.concurrent.ExecutionException;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    private final String TAG = LoginActivity.class.getName();
    Button btnLogin, btnRegister;
    EditText loginET, pwdET;
    User user;
    LoginBGAsync login_bg_async;
    LoginBGAsync.LoginListener loginListener;
    final String SHARED_PREFS = "prefs";
    public Context context;
    //User user = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        setContentView(R.layout.activity_login);
        btnLogin = (Button)findViewById(R.id.ButtonLogin);
        btnRegister = (Button)findViewById(R.id.ButtonRegister);
        loginET = (EditText)findViewById(R.id.login);
        pwdET = (EditText)findViewById(R.id.pwd);
        btnLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ButtonLogin:{
                user = new User(String.valueOf(loginET.getText()), String.valueOf(pwdET.getText()));
                Log.i(TAG,user.getLogin() + "   " + user.getPassword());
                login_bg_async = new LoginBGAsync(context, user);
                loginListener = new LoginBGAsync.LoginListener() {
                    @Override
                    public void onLogin(boolean result) {
                        if (!result)
                        {
                            Toast.makeText(getApplication(), "Unknown User", Toast.LENGTH_LONG).show();
                        }
                        else
                        {

                            Intent intent = new Intent(getApplication(),MessengerActivity.class);
                            SharedPreferences sharedPref = context.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString("login", user.getLogin());
                            editor.putString("pwd", user.getPassword());
                            editor.commit();
                            startActivity(intent);
                            Toast.makeText(getApplication(), "Connected!!", Toast.LENGTH_LONG).show();

                        }
                    }
                };

                login_bg_async.setLoginListener(loginListener);
                login_bg_async.execute();
                try {
                    loginListener.onLogin(login_bg_async.get());
                    //Toast.makeText(getApplication(), login_bg_async.get().toString(), Toast.LENGTH_LONG).show();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                login_bg_async.cancel(true);
            }
            case R.id.ButtonRegister:{
                user.setLogin(String.valueOf(loginET.getText()));
                user.setPassword(String.valueOf(pwdET.getText()));
                Log.i(TAG,user.getLogin() + "   " + user.getPassword());


                RegisterBGAsync register_bg_async = new RegisterBGAsync(context, user);
                RegisterBGAsync.RegisterListener registerListener = new RegisterBGAsync.RegisterListener(){
                    @Override
                    public void onRegister(boolean result) {
                        if (!result)
                        {
                            Toast.makeText(getApplication(), "Unknown User", Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            Intent intent = new Intent(getApplication(),MessengerActivity.class);
                            SharedPreferences sharedPref = context.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString("login", user.getLogin());
                            editor.putString("pwd", user.getPassword());
                            editor.commit();
                            startActivity(intent);
                            Toast.makeText(getApplication(), "Connected!!", Toast.LENGTH_LONG).show();

                        }
                    }
                };
                register_bg_async.setRegisterListener(registerListener);
                register_bg_async.execute();
                try {
                    registerListener.onRegister(register_bg_async.get());
                    //Toast.makeText(getApplication(), login_bg_async.get().toString(), Toast.LENGTH_LONG).show();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

            }
        }

    }
}
