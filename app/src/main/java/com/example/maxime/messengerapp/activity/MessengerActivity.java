package com.example.maxime.messengerapp.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.maxime.messengerapp.R;
import com.example.maxime.messengerapp.adapter.MessageAdapter;
import com.example.maxime.messengerapp.model.Message;
import com.example.maxime.messengerapp.model.User;
import com.example.maxime.messengerapp.task.SendMessageBGAsync;
import com.example.maxime.messengerapp.task.GetMessagesListBGAsync;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by maxime on 18/10/16.
 */
public class MessengerActivity extends AppCompatActivity implements View.OnClickListener {
    private final String TAG = MessengerActivity.class.getName();
    final String SHARED_PREFS = "prefs";
    Button btnSend, btnRefresh;
    RecyclerView recyclerView;
    EditText msgET;
    User user;
    LinearLayoutManager linearLayoutManager;
    MessageAdapter adapter;
    List<Message> messages;
    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        setContentView(R.layout.activity_messenger);
        messages = new ArrayList<>();
        SharedPreferences sharedPref = context.getSharedPreferences("prefs", context.MODE_PRIVATE);
        final String login = sharedPref.getString("login", "error");
        final String pwd = sharedPref.getString("pwd", "error");
        btnSend = (Button) findViewById(R.id.ButtonSend);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewMsg);
        msgET = (EditText) findViewById(R.id.message);
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
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetMessagesListBGAsync getMessagesListBGAsync = new GetMessagesListBGAsync(context, user, messages);
                GetMessagesListBGAsync.GetMessagesListListener getMessagesListListener = new GetMessagesListBGAsync.GetMessagesListListener() {

                    @Override
                    public void onGetMessagesList() {
                        adapter.notifyDataSetChanged();
                    }
                };
                getMessagesListBGAsync.setGetMessagesListListener(getMessagesListListener);
                getMessagesListBGAsync.execute();
                try {
                    getMessagesListListener.onGetMessagesList();
                } catch (Exception e) {
                    Log.i(TAG, e.toString());
                }
            }
        });
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
                //TODO Here add call to SendMsgBGAsync
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


            }
            case R.id.ButtonRefresh: {
                GetMessagesListBGAsync getMessagesListBGAsync = new GetMessagesListBGAsync(context, user, messages);
                GetMessagesListBGAsync.GetMessagesListListener getMessagesListListener = new GetMessagesListBGAsync.GetMessagesListListener() {

                    @Override
                    public void onGetMessagesList() {
                        adapter.notifyDataSetChanged();
                    }
                };
                getMessagesListBGAsync.setGetMessagesListListener(getMessagesListListener);
                getMessagesListBGAsync.execute();
                try {
                    getMessagesListListener.onGetMessagesList();
                } catch (Exception e) {
                    Log.i(TAG, e.toString());
                }
            }

        }
    }
}






