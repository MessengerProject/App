package com.example.maxime.messengerapp.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
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
import com.example.maxime.messengerapp.model.TextValidator;
import com.example.maxime.messengerapp.model.User;
import com.example.maxime.messengerapp.task.GetImageProfileAsync;
import com.example.maxime.messengerapp.task.ProfileUploadBGAsync;

import java.io.ByteArrayOutputStream;
import java.util.concurrent.ExecutionException;

/**
 * Created by victor on 29/10/16.
 */

public class ProfileConfigActivity extends AppCompatActivity implements View.OnClickListener {
    private final String TAG = ProfileConfigActivity.class.getName();
    private final String pwdValidationString =  "\n" +
            "A digit must occur at least once\n" +
            "A lower case letter must occur at least once\n" +
            "An upper case letter must occur at least once\n" +
            "A special character must occur at least once\n" +
            "No whitespace allowed in the entire string\n" +
            "At least 8 characters\n";
    private final String PwdConfValidationString ="Error: Not the same password";
    private final String patternPwd = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}";
    private final String emailValidationString = "exemple@exemple.com";

    private ActionProcessButton btnImage, btnSave;
    private EditText emailET, pwdET,pwdETConf;
    private ImageView imageView, imageViewTop;
    private final String SHARED_PREFS = "prefs";
    private Context context;
    private User user;
    private Attachment attachmentProfile;
    private static final int GET_FROM_GALLERY = 3;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        setContentView(R.layout.activity_profileconfig);
        btnImage = (ActionProcessButton) findViewById(R.id.ButtonImage);
        btnImage.setVisibility(View.VISIBLE);
        btnSave =(ActionProcessButton) findViewById(R.id.ButtonSave);
        btnSave.setVisibility(View.VISIBLE);
        emailET = (EditText) findViewById(R.id.email);
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
        pwdET = (EditText) findViewById(R.id.pwd);
        pwdET.addTextChangedListener(new TextValidator(pwdET) {
            @Override
            public void validate(TextView textView, String text) {

                if (!text.matches(patternPwd)) {
                    pwdET.setError(pwdValidationString);
                }

            }
        });
        pwdETConf = (EditText) findViewById(R.id.pwdConf);
        pwdETConf.addTextChangedListener(new TextValidator(pwdET) {
            @Override
            public void validate(TextView textView, String text) {
                if (!text.matches(pwdET.getText().toString()))
                {
                    pwdETConf.setError(PwdConfValidationString);
                }
            }
        });
        imageView = (ImageView) findViewById(R.id.imageProfile);
        imageViewTop = (ImageView) findViewById(R.id.imageProfileTop);
        btnImage.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        //Attachment profile
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.actionbar, null);
        actionBar.setCustomView(v);
        final ImageView iv = (ImageView) actionBar.getCustomView().findViewById(R.id.imageProfileTop);
        //ASYNC TASK GET IMAGE FOR PROFILE
        SharedPreferences sharedPref = context.getSharedPreferences(SHARED_PREFS, context.MODE_PRIVATE);
        final String login = sharedPref.getString("login", "error");
        final String pwd = sharedPref.getString("pwd", "error");
        user = new User(String.valueOf(login), String.valueOf(pwd));
        GetImageProfileAsync getImageProfileAsync = new GetImageProfileAsync(context, user, user.getPassword());
        GetImageProfileAsync.GetImageProfileListener getImageProfileListener = new GetImageProfileAsync.GetImageProfileListener() {
            @Override
            public void onGetImageProfile(Bitmap result) {
                if (result != null) {
                    iv.setImageBitmap(Bitmap.createScaledBitmap(result, 80, 80, false));
                    //iv.setImageBitmap(result);
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
                Log.i(TAG, "onClick: here we are");
                SharedPreferences sharedPref = context.getSharedPreferences(SHARED_PREFS, context.MODE_PRIVATE);
                final String login = sharedPref.getString("login", "error");
                final String lastPassword = sharedPref.getString("pwd", "error");
                final String pwd = pwdET.getText().toString();
                final String email = emailET.getText().toString();
                user = new User(String.valueOf(login), String.valueOf(pwd), String.valueOf(email));
                user.setPicture(attachmentProfile);

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
                Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("attachments/*");
                startActivityForResult(intent,GET_FROM_GALLERY);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Log.i(TAG, "onActivityResult: "+requestCode);
            if (requestCode == GET_FROM_GALLERY) {
                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };
                Cursor cursor = getContentResolver().query(selectedImage,filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();
                Glide.with(this).load(picturePath).placeholder(R.mipmap.ic_launcher).fallback(R.mipmap.ic_launcher).into(imageView);
                //Encode for user
                Bitmap bm = BitmapFactory.decodeFile(picturePath);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
                byte[] b = baos.toByteArray();
                String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
                attachmentProfile = new Attachment("attachments/png", encodedImage);
            }

        }
    }
}
