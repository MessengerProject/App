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

public class LoginActivity extends AppCompatActivity {
    private final String TAG = LoginActivity.class.getName();

    //User user = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        final Button btnLogin = (Button)findViewById(R.id.ButtonLogin);
        final Button btnRegister = (Button)findViewById(R.id.ButtonRegister);
        final EditText loginET = (EditText)findViewById(R.id.login);
        final EditText pwdET = (EditText)findViewById(R.id.pwd);

        final String SHARED_PREFS = "prefs";
        final Context context = getApplicationContext();
        final User user = new User();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                user.setLogin(String.valueOf(loginET.getText()));
                user.setPwd(String.valueOf(pwdET.getText()));
                Log.i(TAG,user.getLogin() + "   " + user.getPwd());


                LoginBGAsync login_bg_async = new LoginBGAsync(context, user);

                LoginBGAsync.LoginListener loginListener = new LoginBGAsync.LoginListener() {
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
                            editor.putString("pwd", user.getPwd());
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


            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                user.setLogin(String.valueOf(loginET.getText()));
                user.setPwd(String.valueOf(pwdET.getText()));
                Log.i(TAG,user.getLogin() + "   " + user.getPwd());


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
                            editor.putString("pwd", user.getPwd());
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
        });
    }
}
