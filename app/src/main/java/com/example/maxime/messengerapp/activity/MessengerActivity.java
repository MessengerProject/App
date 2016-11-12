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
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.maxime.messengerapp.R;
import com.example.maxime.messengerapp.adapter.MessageAdapter;
import com.example.maxime.messengerapp.model.Attachment;
import com.example.maxime.messengerapp.model.Message;
import com.example.maxime.messengerapp.model.User;
import com.example.maxime.messengerapp.task.GetImageProfileAsync;
import com.example.maxime.messengerapp.task.SendMessageBGAsync;
import com.example.maxime.messengerapp.task.GetMessagesListBGAsync;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by maxime on 18/10/16.
 */

public class MessengerActivity extends AppCompatActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = MessengerActivity.class.getName();
    private static final String SHARED_PREFS = "prefs";
    private static final int GET_FROM_GALLERY = 3;

    private SharedPreferences sharedPref;
    private Context context;

    private Button btnSend, btnImage, btnProfile;
    private ImageView imageView;
    private RecyclerView recyclerView;
    private EditText msgET;
    private SwipeRefreshLayout swipeRefreshLayout;

    private LinearLayoutManager linearLayoutManager;
    private MessageAdapter adapter;
    private List<Message> messages = new ArrayList<>();
    private Bitmap imageBitmap;
    private User user;
    private String encodedImage;
    private ByteArrayOutputStream baos;
    private Attachment attachmentMessage;
    private byte[] b;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Window params
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        context = getApplicationContext();
        setContentView(R.layout.activity_messenger);

        sharedPref = context.getSharedPreferences(SHARED_PREFS, context.MODE_PRIVATE);
        final String login = sharedPref.getString("login", "error");
        final String pwd = sharedPref.getString("pwd", "error");
        user = new User(login, pwd);//comment mettre un user permanent sur la session

        //Retrieve views from XML
        btnSend = (Button) findViewById(R.id.ButtonSend);
        btnImage = (Button) findViewById(R.id.ButtonCamera);
        btnProfile = (Button) findViewById(R.id.action_my_contacts);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewMsg);
        imageView = (ImageView) findViewById(R.id.imageProfileTop);
        msgET = (EditText) findViewById(R.id.message);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);

        //RecyclerView
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new MessageAdapter(messages);
        recyclerView.setAdapter(adapter);

        //Button listeners
        btnSend.setOnClickListener(this);
        btnImage.setOnClickListener(this);
        btnProfile.setOnClickListener(this);
        swipeRefreshLayout.setOnRefreshListener(this);

        //Async image
        //ASYNC TASK GET IMAGE FOR PROFILE
        GetImageProfileAsync getImageProfileAsync = new GetImageProfileAsync(context, user, user.getPassword());
        GetImageProfileAsync.GetImageProfileListener getImageProfileListener = new GetImageProfileAsync.GetImageProfileListener() {
            @Override
            public void onGetImageProfile(Bitmap result) {
                if (result != null) {
                    Log.i(TAG, "onGetImageProfile: result different than null");
                    imageView.setImageBitmap(Bitmap.createScaledBitmap(result, 80, 80, false));
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
        getImageProfileAsync.cancel(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ButtonSend: {
                String msg = String.valueOf(msgET.getText());

                Message message = new Message(msg, user.getLogin());
                Log.i(TAG, message.toString());
                messages.add(message);
                adapter.notifyDataSetChanged();

                SendMessageBGAsync sendMessage_bg_async = new SendMessageBGAsync(context, user, message);

                SendMessageBGAsync.sendMessageListener sendMessageListener = new SendMessageBGAsync.sendMessageListener() {
                    @Override
                    public void onSend(boolean result) {
                        if (!result) {
                            Toast.makeText(getApplication(), "Error to send message to server", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getApplication(), "Message sent", Toast.LENGTH_LONG).show();
                            msgET.getText().clear();
                        }
                    }
                };
                sendMessage_bg_async.setSendMessageListener(sendMessageListener);
                sendMessage_bg_async.execute();
                try {
                    sendMessageListener.onSend(sendMessage_bg_async.get());
                    //Toast.makeText(getApplication(), login_bg_async.get().toString(), Toast.LENGTH_LONG).show();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                recyclerView.smoothScrollToPosition(messages.size() - 1);
                sendMessage_bg_async.cancel(true);
                break;
            }
            case R.id.ButtonCamera: {
                Intent takePictureIntent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent,GET_FROM_GALLERY);
                }
                break;
            }
            case R.id.action_my_contacts: {
                Log.i(TAG, "onClick: here we are");
                Intent intent = new Intent(getApplication(), ProfileConfigActivity.class);
                SharedPreferences sharedPref = context.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("login", user.getLogin());
                editor.putString("pwd", user.getPassword());
                editor.commit();
                startActivity(intent);
                Toast.makeText(getApplication(), "Profile config!!", Toast.LENGTH_LONG).show();
                break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GET_FROM_GALLERY && resultCode == RESULT_OK) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            //Encode for user
            imageBitmap = BitmapFactory.decodeFile(picturePath);
            baos = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
            b = baos.toByteArray();
            encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
            attachmentMessage = new Attachment("image/png", encodedImage);
            String textMessage = msgET.getText().toString()+" ";
            Message message = new Message(textMessage, user.getLogin(), attachmentMessage, encodedImage);
            Log.i(TAG, message.toString());

            SendMessageBGAsync sendMessage_bg_async = new SendMessageBGAsync(context, user, message);

            SendMessageBGAsync.sendMessageListener sendMessageListener = new SendMessageBGAsync.sendMessageListener() {
                @Override
                public void onSend(boolean result) {
                    if (!result) {
                        Toast.makeText(getApplication(), "Error to send message to server", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplication(), "Message sent", Toast.LENGTH_LONG).show();
                        msgET.getText().clear();
                    }
                }
            };
            sendMessage_bg_async.setSendMessageListener(sendMessageListener);
            sendMessage_bg_async.execute();
            try {
                sendMessageListener.onSend(sendMessage_bg_async.get());
                //Toast.makeText(getApplication(), login_bg_async.get().toString(), Toast.LENGTH_LONG).show();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            sendMessage_bg_async.cancel(true);
            messages.add(message);
            adapter.notifyDataSetChanged();
            recyclerView.smoothScrollToPosition(messages.size() - 1);
        }
    }

    @Override
    public void onRefresh() {
        GetMessagesListBGAsync getMessagesListBGAsync = new GetMessagesListBGAsync(context, user, messages);
        GetMessagesListBGAsync.GetMessagesListListener getMessagesListListener = new GetMessagesListBGAsync.GetMessagesListListener() {
            @Override
            public void onGetMessagesList(boolean result) {
                adapter.notifyDataSetChanged();
            }
        };
        getMessagesListBGAsync.setGetMessagesListListener(getMessagesListListener);
        getMessagesListBGAsync.execute();
        try {
            getMessagesListListener.onGetMessagesList(getMessagesListBGAsync.get());
        } catch (Exception e) {
            Log.i(TAG, e.toString());
        }
        Log.i(TAG, "onRefresh: here we are");
        swipeRefreshLayout.setRefreshing(false);
        recyclerView.smoothScrollToPosition(messages.size() - 2);
        getMessagesListBGAsync.cancel(true);
    }
}







