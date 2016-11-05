package com.example.maxime.messengerapp.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.maxime.messengerapp.R;
import com.example.maxime.messengerapp.adapter.MessageAdapter;
import com.example.maxime.messengerapp.model.Image;
import com.example.maxime.messengerapp.model.Message;
import com.example.maxime.messengerapp.model.User;
import com.example.maxime.messengerapp.service.SetProfilPictureService;
import com.example.maxime.messengerapp.task.GetImageProfileAsync;
import com.example.maxime.messengerapp.task.SendMessageBGAsync;
import com.example.maxime.messengerapp.task.GetMessagesListBGAsync;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by maxime on 18/10/16.
 */

public class MessengerActivity extends AppCompatActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    private final String TAG = MessengerActivity.class.getName();
    private final String SHARED_PREFS = "prefs";
    private Context context;
    private Button btnSend, btnRefresh;
    private RecyclerView recyclerView;
    private EditText msgET;
    private User user;
    private LinearLayoutManager linearLayoutManager;
    private MessageAdapter adapter;
    private List<Message> messages = new ArrayList<>();
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        setContentView(R.layout.activity_messenger);
        SharedPreferences sharedPref = context.getSharedPreferences(SHARED_PREFS, context.MODE_PRIVATE);
        final String login = sharedPref.getString("login", "error");
        final String pwd = sharedPref.getString("pwd", "error");
        btnSend = (Button) findViewById(R.id.ButtonSend);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewMsg);
        msgET = (EditText) findViewById(R.id.message);
        context = getApplicationContext();
        btnRefresh = (Button) findViewById(R.id.ButtonRefresh);
        recyclerView.setHasFixedSize(true);
        user = new User(login, pwd);//comment mettre un user permanent sur la session
        // use a linear layout manager
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        // specify an adapter
        adapter = new MessageAdapter(messages);
        recyclerView.setAdapter(adapter);
        btnSend.setOnClickListener(this);
        btnRefresh.setOnClickListener(this);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setCustomView(R.layout.actionbar);
        final ImageView iv = (ImageView) actionBar.getCustomView().findViewById(R.id.imageProfileTop);

        //ASYNC TASK GET IMAGE FOR PROFILE
        GetImageProfileAsync getImageProfileAsync = new GetImageProfileAsync(context, user);
        GetImageProfileAsync.GetImageProfileListener getImageProfileListener = new GetImageProfileAsync.GetImageProfileListener() {
            @Override
            public void onGetImageProfile(Bitmap result) {
                if (result != null) {
                    adapter.notifyDataSetChanged();
                    iv.setImageBitmap(result);
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
        //End asynctask
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menutoolbar, menu);
        MenuItem profileAccess = menu.findItem(R.id.action_my_contacts);
        profileAccess.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(getApplication(),ProfileConfigActivity.class);
                SharedPreferences sharedPref = context.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("login", user.getLogin());
                editor.putString("pwd", user.getPassword());
                editor.commit();
                startActivity(intent);
                Toast.makeText(getApplication(), "Profile config!!", Toast.LENGTH_LONG).show();
                return true;
            }
        });
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ButtonSend: {
                String msg = String.valueOf(msgET.getText());
                Calendar c = Calendar.getInstance();
                System.out.println("Current time => " + c.getTime());

                //SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
                //String formattedDate = df.format(c.getTime());
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
                sendMessage_bg_async.cancel(true);
            break;
            }
            case R.id.ButtonRefresh : {
                GetMessagesListBGAsync getMessagesListBGAsync = new GetMessagesListBGAsync(context, user, messages);
                GetMessagesListBGAsync.GetMessagesListListener getMessagesListListener = new GetMessagesListBGAsync.GetMessagesListListener() {
                    @Override
                    public void onGetMessagesList(boolean result) {
                        if (result) {
                            adapter.notifyDataSetChanged();
                        }
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
            }

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
        getMessagesListBGAsync.cancel(true);
    }
}







