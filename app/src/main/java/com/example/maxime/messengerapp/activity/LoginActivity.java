package com.example.maxime.messengerapp.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dd.processbutton.iml.ActionProcessButton;
import com.example.maxime.messengerapp.model.User;
import com.example.maxime.messengerapp.task.LoginBGAsync;
import com.example.maxime.messengerapp.R;
import com.example.maxime.messengerapp.task.RegisterBGAsync;
import com.example.maxime.messengerapp.model.TextValidator;
import java.util.concurrent.ExecutionException;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    private final String TAG = LoginActivity.class.getName();
    private final String pwdValidationString =
            "A digit must occur at least once\n" +
            "A lower case letter must occur at least once\n" +
            "An upper case letter must occur at least once\n" +
            "A special character must occur at least once\n" +
            "No whitespace allowed in the entire string\n" +
            "At least 8 characters\n";
    private final String patternPwd = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}";

    private final String loginValidationString= "Must be longer than 5";
    ActionProcessButton btnLogin, btnRegister;
    EditText loginET, pwdET;
    User user;
    LoginBGAsync login_bg_async;
    LoginBGAsync.LoginListener loginListener;
    final String SHARED_PREFS = "prefs";
    public Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        setContentView(R.layout.activity_login);
        btnLogin = (ActionProcessButton) findViewById(R.id.ButtonLogin);
        btnRegister = (ActionProcessButton)findViewById(R.id.ButtonRegister);
        btnRegister.setVisibility(View.GONE);
        loginET = (EditText)findViewById(R.id.login);
        loginET.addTextChangedListener(new TextValidator(loginET) {
            @Override
            public void validate(TextView textView, String text) {
                if (text.length() < 5){
                    loginET.setError(loginValidationString);
                    btnLogin.setProgress(-1);
                    btnRegister.setProgress(-1);
                }
                else {
                    btnLogin.setProgress(0);
                    btnRegister.setProgress(0);
                }

            }
        });
        pwdET = (EditText)findViewById(R.id.pwd);
        pwdET.addTextChangedListener(new TextValidator(pwdET) {
            @Override
            public void validate(TextView textView, String text) {

                if (!text.matches(patternPwd)) {
                    pwdET.setError(pwdValidationString);
                    btnRegister.setProgress(-1);
                    btnLogin.setProgress(-1);
                }
                else{
                    btnLogin.setProgress(0);
                    btnRegister.setProgress(0);

                }

            }
        });
        btnLogin.setOnClickListener(this);
        btnLogin.setMode(ActionProcessButton.Mode.ENDLESS);
        btnRegister.setOnClickListener(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    @Override
    protected void onStart() {
        btnLogin.setProgress(0);
        btnLogin.setVisibility(View.VISIBLE);
        btnRegister.setProgress(0);
        btnRegister.setVisibility(View.GONE);
        loginET.setEnabled(true);
        pwdET.setEnabled(true);
        btnLogin.setEnabled(true);
        loginET.setText("");
        pwdET.setText("");
        super.onStart();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ButtonLogin:{
                btnLogin.setProgress(0);
                btnLogin.setEnabled(false);
                loginET.setEnabled(false);
                pwdET.setEnabled(false);
                user = new User(String.valueOf(loginET.getText()), String.valueOf(pwdET.getText()));
                Log.i(TAG,user.getLogin() + " " + user.getPassword());
                login_bg_async = new LoginBGAsync(context, user);
                loginListener = new LoginBGAsync.LoginListener() {
                    @Override
                    public void onLogin(boolean result) {
                        if (!result)
                        {
                            btnLogin.setProgress(-1);
                            btnLogin.setEnabled(true);
                            loginET.setEnabled(true);
                            pwdET.setEnabled(true);
                            btnRegister.setVisibility(View.VISIBLE);
                            Toast.makeText(getApplication(), "Unknown User", Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            btnLogin.setProgress(100);
                            Intent intent = new Intent(getApplication(),MessengerActivity.class);
                            SharedPreferences sharedPref = context.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString("login", user.getLogin());
                            editor.putString("pwd", user.getPassword());
                            editor.commit();
                            startActivity(intent);
                            //Toast.makeText(getApplication(), "Connected!!", Toast.LENGTH_LONG).show();

                        }
                    }
                };

                login_bg_async.setLoginListener(loginListener);
                login_bg_async.setBtn(btnLogin);
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
                break;
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
                            Toast.makeText(getApplication(), "Can't register", Toast.LENGTH_SHORT).show();
                            btnLogin.setVisibility(View.VISIBLE);
                            btnRegister.setVisibility(View.GONE);
                        }
                        else
                        {
                            btnRegister.setProgress(100);
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
                break;

            }
        }

    }

}
