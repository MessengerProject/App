package com.example.maxime.messengerapp.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import com.dd.processbutton.iml.ActionProcessButton;
import com.example.maxime.messengerapp.R;
import com.example.maxime.messengerapp.model.Attachment;
import com.example.maxime.messengerapp.utils.TextValidator;
import com.example.maxime.messengerapp.model.User;
import com.example.maxime.messengerapp.task.GetImageProfileAsync;
import com.example.maxime.messengerapp.task.ProfileUploadBGAsync;
import com.example.maxime.messengerapp.utils.Util;

import java.io.ByteArrayOutputStream;
import java.util.concurrent.ExecutionException;

/**
 * Created by victor on 29/10/16.
 */

public class ProfileConfigActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = ProfileConfigActivity.class.getName();
    private static final String pwdValidationString =  "\n" +
            "A digit must occur at least once\n" +
            "A lower case letter must occur at least once\n" +
            "An upper case letter must occur at least once\n" +
            "A special character must occur at least once\n" +
            "No whitespace allowed in the entire string\n" +
            "At least 8 characters\n";

    private static final String PwdConfValidationString ="Error: Not the same password";
    private static final String patternPwd = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}";
    private static final String emailValidationString = "exemple@exemple.com";
    private static final String SHARED_PREFS = "prefs";
    private static final int GET_FROM_GALLERY = 3;

    private ActionProcessButton btnImage, btnSave;
    private EditText emailET, pwdET,pwdETConf;
    private ImageView imageView, imageViewTop;
    private Context context;
    private User user;
    private String login, pwd, email;
    private String lastPassword;
    private Attachment attachmentProfile;
    private SharedPreferences sharedPref;

    //Image
    private String encodedImage;
    private String imagePath;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Window params
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        context = getApplicationContext();
        setContentView(R.layout.activity_profileconfig);

        //Retrieve views from XML
        btnImage = (ActionProcessButton) findViewById(R.id.ButtonImage);
        btnImage.setVisibility(View.VISIBLE);
        btnSave =(ActionProcessButton) findViewById(R.id.ButtonSave);
        btnSave.setVisibility(View.VISIBLE);
        emailET = (EditText) findViewById(R.id.email);
        imageViewTop = (ImageView) findViewById(R.id.imageProfileTop);
        imageView = (ImageView) findViewById(R.id.imageProfile);
        pwdETConf = (EditText) findViewById(R.id.pwdConf);
        pwdET = (EditText) findViewById(R.id.pwd);

        //Button listeners
        btnImage.setOnClickListener(this);
        btnSave.setOnClickListener(this);

        //Toolbar
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        emailET.addTextChangedListener(new TextValidator(emailET) {
            @Override
            public void validate(TextView textView, String text) {
                if (!text.matches(String.valueOf(android.util.Patterns.EMAIL_ADDRESS))) {
                    emailET.setError(emailValidationString);
                    btnSave.setProgress(-1);
                    btnSave.setEnabled(false);
                }
                else
                {
                    btnSave.setProgress(0);
                    btnSave.setEnabled(true);

                }
            }
        });
        pwdET.addTextChangedListener(new TextValidator(pwdET) {
            @Override
            public void validate(TextView textView, String text) {

                if (!text.matches(patternPwd)) {
                    pwdET.setError(pwdValidationString);
                }

            }
        });
        pwdETConf.addTextChangedListener(new TextValidator(pwdET) {
            @Override
            public void validate(TextView textView, String text) {
                if (!text.matches(pwdET.getText().toString()))
                {
                    pwdETConf.setError(PwdConfValidationString);
                    btnSave.setEnabled(false);
                }
                else {
                    btnSave.setEnabled(true);
                }
            }
        });

        //User create
        sharedPref = context.getSharedPreferences(SHARED_PREFS, context.MODE_PRIVATE);
        login = sharedPref.getString("login", "error");
        pwd = sharedPref.getString("pwd", "error");
        user = new User(String.valueOf(login), String.valueOf(pwd));

        //ASYNC TASK GET IMAGE FOR PROFILE
        GetImageProfileAsync getImageProfileAsync = new GetImageProfileAsync(context, user, user.getPassword());
        GetImageProfileAsync.GetImageProfileListener getImageProfileListener = new GetImageProfileAsync.GetImageProfileListener() {
            @Override
            public void onGetImageProfile(Bitmap result) {
                if (result != null) {
                    imageViewTop.setImageBitmap(Bitmap.createScaledBitmap(result, 80, 80, false));
                }
            }
        };
        getImageProfileAsync.setGetImageProfileListener(getImageProfileListener);
        getImageProfileAsync.execute();
        try {
            getImageProfileListener.onGetImageProfile(getImageProfileAsync.get());
        } catch (Exception e) {
            Log.i(TAG, e.toString());
        }
        Log.i(TAG, "onRefresh: here we are");
        getImageProfileAsync.cancel(true);
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ButtonSave: {

                sharedPref = context.getSharedPreferences(SHARED_PREFS, context.MODE_PRIVATE);
                login = sharedPref.getString("login", "error");
                lastPassword = sharedPref.getString("pwd", "error");
                pwd = pwdET.getText().toString();
                email = emailET.getText().toString();
                user = new User(String.valueOf(login), String.valueOf(pwd), String.valueOf(email));
                user.setImage(attachmentProfile);

                //ProfileSave Async
                ProfileUploadBGAsync profileUploadBGAsync = new ProfileUploadBGAsync(context, user, lastPassword);

                ProfileUploadBGAsync.profileUploadListener profileUploadListener = new ProfileUploadBGAsync.profileUploadListener() {
                    @Override
                    public void onSend(boolean result) {
                        if (!result) {
                            btnImage.setProgress(-1);
                            Toast.makeText(getApplication(), "Can't connect server or image too big", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getApplication(), "Profile changed", Toast.LENGTH_LONG).show();
                        }
                    }
                };
                profileUploadBGAsync.setProfileUploadListener(profileUploadListener);
                profileUploadBGAsync.execute();
                try {
                    profileUploadListener.onSend(profileUploadBGAsync.get());
                    //Toast.makeText(getApplication(), login_bg_async.get().toString(), Toast.LENGTH_LONG).show();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                profileUploadBGAsync.cancel(true);
                break;

            }
            case R.id.ButtonImage: {
                Intent intent = Util.openCameraIntent();
                startActivityForResult(intent,GET_FROM_GALLERY);
                break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == GET_FROM_GALLERY) {

                //Get imagepath
                imagePath = Util.getimagePath(data.getData(),this.getContentResolver());

                Glide.with(this).load(imagePath).placeholder(R.mipmap.ic_launcher).fallback(R.mipmap.ic_launcher).into(imageView);

                //Decode for user
                encodedImage = Util.pathToEncodedImage(imagePath);
                attachmentProfile = new Attachment("attachments/png", encodedImage);
            }

        }
    }
}
