package com.example.maxime.messengerapp.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.maxime.messengerapp.R;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by victor on 29/10/16.
 */

public class ProfileConfigActivity extends AppCompatActivity implements View.OnClickListener {
    private final String TAG = ProfileConfigActivity.class.getName();
    Button btnImage;
    EditText emailET, pwdET;
    ImageView imageView;
    final String SHARED_PREFS = "prefs";
    public Context context;
    public static final int GET_FROM_GALLERY = 3;
    public static final int REQUEST_IMAGE_CAPTURE = 1;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        setContentView(R.layout.activity_profileconfig);
        btnImage = (Button) findViewById(R.id.ButtonImage);
        emailET = (EditText) findViewById(R.id.email);
        pwdET = (EditText) findViewById(R.id.pwd);
        btnImage.setOnClickListener(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
            case R.id.ButtonImage: {
                Log.i(TAG, "onClick: here we are");
                startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI),GET_FROM_GALLERY);
            }
            break;
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
        }
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageView = (ImageView) findViewById(R.id.imageProfile);
            imageView.setImageBitmap(imageBitmap);
        }

    }
}
