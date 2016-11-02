package com.example.maxime.messengerapp.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.maxime.messengerapp.R;
import com.example.maxime.messengerapp.model.Image;
import com.example.maxime.messengerapp.model.User;
import com.example.maxime.messengerapp.task.ProfileUploadBGAsync;
import com.example.maxime.messengerapp.task.SendMessageBGAsync;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;

/**
 * Created by victor on 29/10/16.
 */

public class ProfileConfigActivity extends AppCompatActivity implements View.OnClickListener {
    private final String TAG = ProfileConfigActivity.class.getName();
    private Button btnImage, btnSave;
    private EditText emailET, pwdET,pwdETConf;
    private ImageView imageView;
    private final String SHARED_PREFS = "prefs";
    private Context context;
    private User user;
    private Image imageProfile;
    private static final int GET_FROM_GALLERY = 3;
    private static final int REQUEST_IMAGE_CAPTURE = 1;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        setContentView(R.layout.activity_profileconfig);
        btnImage = (Button) findViewById(R.id.ButtonImage);
        btnSave =(Button) findViewById(R.id.ButtonSave);
        emailET = (EditText) findViewById(R.id.email);
        pwdET = (EditText) findViewById(R.id.pwd);
        pwdETConf = (EditText) findViewById(R.id.pwdConf);
        btnImage.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menutoolbar, menu);
        MenuItem profileAccess = menu.findItem(R.id.action_my_contacts);
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
                user.setPicture(imageProfile);

                ProfileUploadBGAsync profileUploadBGAsync = new ProfileUploadBGAsync(context, user, lastPassword);

                ProfileUploadBGAsync.profileUploadListener profileUploadListener = new ProfileUploadBGAsync.profileUploadListener() {
                    @Override
                    public void onSend(boolean result) {
                        if (!result) {
                            Toast.makeText(getApplication(), "Error to send message to server", Toast.LENGTH_LONG).show();
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
                startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI),GET_FROM_GALLERY);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Detects request codes
        if(requestCode==GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            Log.i(TAG, "onActivityResult: "+data.getData().getPath());
            imageView = (ImageView) findViewById(R.id.imageProfile);
            Glide.with(context).load(selectedImage).into(imageView);

            /*String photoPath = Environment.getExternalStorageDirectory()+"/17.jpg";
            Log.i(TAG, "onActivityResult: "+ photoPath);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bm = BitmapFactory.decodeFile(photoPath, options);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.PNG, 100, baos); //bm is the bitmap object
            byte[] b = baos.toByteArray();
            String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);

            imageProfile = new Image("image/png", encodedImage);*/
        }
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageView = (ImageView) findViewById(R.id.imageProfile);
            imageView.setImageBitmap(imageBitmap);
        }

    }
}
