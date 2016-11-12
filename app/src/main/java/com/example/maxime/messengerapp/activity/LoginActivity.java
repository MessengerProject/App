package com.example.maxime.messengerapp.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dd.processbutton.iml.ActionProcessButton;
import com.example.maxime.messengerapp.model.User;
import com.example.maxime.messengerapp.task.LoginBGAsync;
import com.example.maxime.messengerapp.R;
import com.example.maxime.messengerapp.task.RegisterBGAsync;
import com.example.maxime.messengerapp.utils.TextValidator;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.concurrent.ExecutionException;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = LoginActivity.class.getName();
    private final String pwdValidationString =
            "A digit must occur at least once\n" +
                    "A lower case letter must occur at least once\n" +
                    "An upper case letter must occur at least once\n" +
                    "A special character must occur at least once\n" +
                    "No whitespace allowed in the entire string\n" +
                    "At least 8 characters\n";

    private static final String patternPwd = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}";
    private static final String loginValidationString = "Must be longer than 5";
    private static final String SHARED_PREFS = "prefs";

    private Context context;

    private ActionProcessButton btnLogin, btnRegister;
    private EditText loginET, pwdET;
    private User user;

    private LoginBGAsync login_bg_async;
    private RegisterBGAsync register_bg_async;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Window params
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        setContentView(R.layout.activity_login);
        context = getApplicationContext();

        //Retrieve views from XML
        btnLogin = (ActionProcessButton) findViewById(R.id.ButtonLogin);
        btnRegister = (ActionProcessButton) findViewById(R.id.ButtonRegister);
        loginET = (EditText) findViewById(R.id.login);
        pwdET = (EditText) findViewById(R.id.pwd);

        //Button listeners
        btnLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
        btnRegister.setVisibility(View.GONE);
        setTextButtonsListeners();
        btnLogin.setMode(ActionProcessButton.Mode.ENDLESS);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public void setTextButtonsListeners() {
        loginET.addTextChangedListener(new TextValidator(loginET) {
            @Override
            public void validate(TextView textView, String text) {
                if (text.length() < 5) {
                    loginET.setError(loginValidationString);
                    btnLogin.setProgress(-1);
                    btnRegister.setProgress(-1);
                } else {
                    btnLogin.setProgress(0);
                    btnRegister.setProgress(0);
                }

            }
        });

        pwdET.addTextChangedListener(new TextValidator(pwdET) {
            @Override
            public void validate(TextView textView, String text) {
                if (!text.matches(patternPwd)) {
                    pwdET.setError(pwdValidationString);
                    btnRegister.setProgress(-1);
                    btnLogin.setProgress(-1);
                } else {
                    btnLogin.setProgress(0);
                    btnRegister.setProgress(0);

                }
            }
        });
    }

    @Override
    protected void onStart() {

        //Params Onstart
        btnLogin.setProgress(0);
        btnLogin.setVisibility(View.VISIBLE);
        btnRegister.setProgress(0);
        btnRegister.setVisibility(View.GONE);

        loginET.setEnabled(true);
        pwdET.setEnabled(true);
        btnLogin.setEnabled(true);
        loginET.setText("");
        pwdET.setText("");

        super.onStart();// ATTENTION: This was auto-generated to implement the App Indexing API.
// See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    protected void onPause() {
        if (login_bg_async != null) {
            login_bg_async.cancel(true);
        }
        if (register_bg_async != null) {
            register_bg_async.cancel(true);
        }
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();// ATTENTION: This was auto-generated to implement the App Indexing API.
// See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        btnLogin.setProgress(0);
        btnLogin.setEnabled(true);
        loginET.setEnabled(true);
        pwdET.setEnabled(true);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.disconnect();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ButtonLogin: {
                //Button params
                btnLogin.setProgress(0);
                btnLogin.setEnabled(false);
                loginET.setEnabled(false);
                pwdET.setEnabled(false);

                user = new User(String.valueOf(loginET.getText()), String.valueOf(pwdET.getText()));

                //Login Async
                login_bg_async = new LoginBGAsync( user);
                LoginBGAsync.LoginListener loginListener = new LoginBGAsync.LoginListener() {
                    @Override
                    public void onLogin(boolean result) {
                        if (!result) {
                            //Button params
                            btnLogin.setProgress(-1);
                            btnLogin.setEnabled(true);
                            loginET.setEnabled(true);
                            pwdET.setEnabled(true);
                            btnRegister.setVisibility(View.VISIBLE);

                            Toast.makeText(getApplication(), "Unknown User", Toast.LENGTH_LONG).show();
                        } else {
                            openMessengerActivity(btnLogin);
                        }
                    }
                };

                login_bg_async.setLoginListener(loginListener);
                login_bg_async.setBtn(btnLogin);
                login_bg_async.execute();
                try {
                    loginListener.onLogin(login_bg_async.get());
                    //Toast.makeText(getApplication(), login_bg_async.get().toString(), Toast.LENGTH_LONG).show();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
                login_bg_async.cancel(true);
                break;
            }

            case R.id.ButtonRegister: {
                user.setLogin(String.valueOf(loginET.getText()));
                user.setPassword(String.valueOf(pwdET.getText()));


                register_bg_async = new RegisterBGAsync(user);
                RegisterBGAsync.RegisterListener registerListener = new RegisterBGAsync.RegisterListener() {
                    @Override
                    public void onRegister(boolean result) {
                        if (!result) {
                            btnLogin.setVisibility(View.VISIBLE);
                            btnRegister.setVisibility(View.GONE);

                            Toast.makeText(getApplication(), "Can't register", Toast.LENGTH_SHORT).show();
                        } else {
                            openMessengerActivity(btnRegister);
                        }
                    }
                };
                register_bg_async.setRegisterListener(registerListener);
                register_bg_async.execute();
                try {
                    registerListener.onRegister(register_bg_async.get());
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    public void openMessengerActivity(ActionProcessButton button) {
        button.setProgress(100);

        Intent intent = new Intent(getApplication(), MessengerActivity.class);
        SharedPreferences sharedPref = context.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("login", user.getLogin());
        editor.putString("pwd", user.getPassword());
        editor.apply();
        startActivity(intent);
        Toast.makeText(getApplication(), "Connected!!", Toast.LENGTH_LONG).show();
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Login Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }
}
