package com.example.maxime.messengerapp.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.maxime.messengerapp.R;
import com.example.maxime.messengerapp.adapter.MessageAdapter;
import com.example.maxime.messengerapp.model.Attachment;
import com.example.maxime.messengerapp.model.Message;
import com.example.maxime.messengerapp.model.User;
import com.example.maxime.messengerapp.task.GetImageProfileAsync;
import com.example.maxime.messengerapp.task.SendMessageBGAsync;
import com.example.maxime.messengerapp.task.GetMessagesListBGAsync;
import com.example.maxime.messengerapp.utils.Util;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by maxime on 18/10/16.
 */

public class MessengerActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = MessengerActivity.class.getName();
    private static final String SHARED_PREFS = "prefs";
    private static final int GET_FROM_GALLERY = 3;

    private SharedPreferences sharedPref;
    private Context context;

    private Button btnSend, btnImage, btnProfile;
    private ImageView imageView;
    private RecyclerView recyclerView;
    private EditText msgET;

    private LinearLayoutManager linearLayoutManager;
    private MessageAdapter adapter;
    private List<Message> messages = new ArrayList<>();
    private SwipeRefreshLayout swipeRefreshLayout;

    private User user;
    private String login;
    private String pwd;
    private String msg;
    private Message message;
    private String url;

    //Image
    private String encodedImage;
    private Bitmap imageBitmap;
    private ByteArrayOutputStream baos;
    private Attachment attachmentMessage;
    private byte[] b;

    private int nbMessageToUpload = 6;
    private int nbMessageToUploadOnCreate = 12;
    private int firstVisibleItem, totalItemCount, lastVisibleItem;
    private GetMessagesListBGAsync getMessagesListBGAsync;
    private GetImageProfileAsync getImageProfileAsync;
    private SendMessageBGAsync sendMessage_bg_async;
    private GetMessagesListBGAsync.GetMessagesListListener getMessagesListListener;
    private GetImageProfileAsync.GetImageProfileListener getImageProfileListener;
    private SendMessageBGAsync.sendMessageListener sendMessageListener;


    private int compteur = 1;

    @Override
    protected void onStart() {

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(final RecyclerView recyclerView, int dy) {
                super.onScrollStateChanged(recyclerView, dy);
                totalItemCount = linearLayoutManager.getItemCount();
                firstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();

                Log.i(TAG,"totalItemCount:  " +totalItemCount);
                Log.i(TAG, "firstVisibleItem:  " + firstVisibleItem);
                Log.i(TAG, "lastVisibleItem:  " + lastVisibleItem);

                if (!(getMessagesListBGAsync != null && getMessagesListBGAsync.getStatus().equals(AsyncTask.Status.RUNNING))) {
                    if (dy > 0) {
                        Log.i(TAG, "\n\n dy >>>>>>>> 0 \n\n");
                        //Getmessages Async
                        //if (compteur>=0) {
                            if (firstVisibleItem < 12) {
                                getMessagesListBGAsync = new GetMessagesListBGAsync(user, messages);
                                GetMessagesListBGAsync.GetMessagesListListener getMessagesListListener = new GetMessagesListBGAsync.GetMessagesListListener() {
                                    @Override
                                    public void onGetMessagesList(boolean result) {
                                        adapter.notifyDataSetChanged();
                                        //recyclerView.smoothScrollToPosition(messages.size());
                                    }
                                };
                                getMessagesListBGAsync.setGetMessagesListListener(getMessagesListListener);
                                getMessagesListBGAsync.execute(nbMessageToUpload, totalItemCount, 0);
                                try {
                                    //recyclerView.smoothScrollToPosition(nbMessageToUpload);
                                    getMessagesListListener.onGetMessagesList(getMessagesListBGAsync.get());
                                    //Log.i(TAG, "\n\ncompteur" + compteur);
                                    compteur++;

                                    getMessagesListBGAsync.cancel(true);
                                } catch (Exception e) {
                                    Log.i(TAG, e.toString());
                                }
                                Log.i(TAG, "onRefresh: here we are");
                                //swipeRefreshLayout.setRefreshing(false);
                                recyclerView.scrollToPosition(12);

                                swipeRefreshLayout.setEnabled(false);
                            }
                        //}


                    }
                    /*if (dy < 0) {
                        Log.i(TAG, "\n\n dy <<<<<<<<<<< 0 \n\n");
                        //Getmessages Async
                        if (firstVisibleItem > (totalItemCount - 20)) {
                            getMessagesListBGAsync = new GetMessagesListBGAsync(context, user, messages);
                            GetMessagesListBGAsync.GetMessagesListListener getMessagesListListener = new GetMessagesListBGAsync.GetMessagesListListener() {
                                @Override
                                public void onGetMessagesList(boolean result) {
                                    adapter.notifyDataSetChanged();
                                    //recyclerView.smoothScrollToPosition(messages.size());
                                }
                            };
                            getMessagesListBGAsync.setGetMessagesListListener(getMessagesListListener);
                            getMessagesListBGAsync.execute(nbMessageToUpload, totalItemCount, 1);
                            try {
                                //recyclerView.smoothScrollToPosition(nbMessageToUpload);
                                getMessagesListListener.onGetMessagesList(getMessagesListBGAsync.get());
                                compteur--;
                            } catch (Exception e) {
                                Log.i(TAG, e.toString());
                            }
                            Log.i(TAG, "onRefresh: here we are");
                            //swipeRefreshLayout.setRefreshing(false);
                            //recyclerView.scrollToPosition();
                            getMessagesListBGAsync.cancel(true);
                            swipeRefreshLayout.setEnabled(false);
                        }
                if (getMessagesListBGAsync != null && getMessagesListBGAsync.getStatus().equals(AsyncTask.Status.RUNNING)) {
                    getMessagesListBGAsync.cancel(true);
                }
                if (firstVisibleItem < visibleThreshold) {
                    //Getmessages Async
                    getMessagesListBGAsync = new GetMessagesListBGAsync( user, messages);
                    GetMessagesListBGAsync.GetMessagesListListener getMessagesListListener = new GetMessagesListBGAsync.GetMessagesListListener() {
                        @Override
                        public void onGetMessagesList(boolean result) {
                            adapter.notifyDataSetChanged();
                        }
                    };
                    getMessagesListBGAsync.setGetMessagesListListener(getMessagesListListener);
                    getMessagesListBGAsync.execute(nbMessageToUpload, totalItemCount - 1);
                    try {
                        getMessagesListListener.onGetMessagesList(getMessagesListBGAsync.get());
                    } catch (Exception e) {
                        Log.i(TAG, e.toString());
                    }
                    recyclerView.scrollToPosition(10);
                    getMessagesListBGAsync.cancel(true);
                    swipeRefreshLayout.setEnabled(false);
>>>>>>> fdd95c565535346158693bc162319cf67eae44e4


                    }

*/
                }
            }
        });
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Window params
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        context = getApplicationContext();
        setContentView(R.layout.activity_messenger);

        sharedPref = context.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        login = sharedPref.getString("login", "error");
        pwd = (sharedPref.getString("pwd", "error"));
        user = new User(login, pwd);//comment mettre un user permanent sur la session

        //Retrieve views from XML
        btnSend = (Button) findViewById(R.id.ButtonSend);
        btnImage = (Button) findViewById(R.id.ButtonCamera);
        btnProfile = (Button) findViewById(R.id.action_my_contacts);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewMsg);
        imageView = (ImageView) findViewById(R.id.imageProfileTop);
        msgET = (EditText) findViewById(R.id.message);
        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipeRefreshLayout);
        context = getApplicationContext();

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

        if (getMessagesListBGAsync != null && getMessagesListBGAsync.getStatus().equals(AsyncTask.Status.RUNNING)) {
            getMessagesListBGAsync.cancel(true);
        }

        getMessagesListBGAsync = new GetMessagesListBGAsync(user, messages);
        GetMessagesListBGAsync.GetMessagesListListener getMessagesListListener = new GetMessagesListBGAsync.GetMessagesListListener() {
            @Override
            public void onGetMessagesList(boolean result) {
                adapter.notifyDataSetChanged();
            }

        };
        getMessagesListBGAsync.setGetMessagesListListener(getMessagesListListener);
        getMessagesListBGAsync.execute(nbMessageToUploadOnCreate, 0, 2);
        try {
            getMessagesListListener.onGetMessagesList(getMessagesListBGAsync.get());
            recyclerView.scrollToPosition(messages.size()-1);


        } catch (Exception e) {
            Log.i(TAG, e.toString());
        }
        getMessagesListBGAsync.cancel(true);

        //Async getImageProfile
        //ASYNC TASK GET IMAGE FOR PROFILE
        getImageProfileAsync = new GetImageProfileAsync( user);
        getImageProfileListener = new GetImageProfileAsync.GetImageProfileListener() {
            @Override
            public void onGetImageProfile(Bitmap result) {
                if (result != null) {
                    Log.i(TAG, "onGetImageProfile: result different than null");
                    imageView.setImageBitmap(Bitmap.createScaledBitmap(result, 80, 80, false));
                    //url = result;
                }
            }
        };
        //Glide.with(this).load(url).placeholder(R.mipmap.ic_launcher).fallback(R.mipmap.ic_launcher).into(imageView);
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
                msg = String.valueOf(msgET.getText());
                message = new Message(msg, user.getLogin());
                messages.add(message);
                adapter.notifyDataSetChanged();
                recyclerView.smoothScrollToPosition(messages.size()-1);

                //SendMessage Async
                sendMessage_bg_async = new SendMessageBGAsync( user, message);

                sendMessageListener = new SendMessageBGAsync.sendMessageListener() {
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
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                sendMessage_bg_async.cancel(true);

                break;
            }

            case R.id.ButtonCamera: {
                Intent intent = Util.openCameraIntent();
                startActivityForResult(intent,GET_FROM_GALLERY);
                break;
            }

            case R.id.action_my_contacts: {
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
            //Get imagepath
            String imagepath = Util.getimagePath(data.getData(),this.getContentResolver());

            //Encode for user
            encodedImage = Util.pathToEncodedImage(imagepath);
            attachmentMessage = new Attachment("image/png", encodedImage);
            String textMessage = msgET.getText().toString() + " ";
            Message message = new Message(textMessage, user.getLogin(), attachmentMessage, encodedImage);


            //SendMessage Async
            SendMessageBGAsync sendMessage_bg_async = new SendMessageBGAsync( user, message);

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
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            sendMessage_bg_async.cancel(true);
            messages.add(message);
            adapter.notifyDataSetChanged();
            recyclerView.smoothScrollToPosition(messages.size()-1);

        }
    }

    /*public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }*/
}







