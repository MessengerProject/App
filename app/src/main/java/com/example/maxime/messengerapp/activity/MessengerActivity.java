package com.example.maxime.messengerapp.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.maxime.messengerapp.R;
import com.example.maxime.messengerapp.adapter.MessageAdapter;
import com.example.maxime.messengerapp.model.Message;
import com.example.maxime.messengerapp.model.User;
import com.example.maxime.messengerapp.task.GetMessagesListBGAsync;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maxime on 18/10/16.
 */

public class MessengerActivity extends AppCompatActivity {
    private final String TAG = MessengerActivity.class.getName();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_messenger);
        final Context mContext = getApplicationContext();
        SharedPreferences sharedPref = mContext.getSharedPreferences("prefs", mContext.MODE_PRIVATE);
        final String login = sharedPref.getString("login", "error");
        final String pwd = sharedPref.getString("pwd", "error");
        final User user = new User(login, pwd);
        final List<Message> messages = new ArrayList<>();

        final Button btnSend = (Button)findViewById(R.id.ButtonSend);
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerViewMsg);
        final EditText msgET = (EditText) findViewById(R.id.message);
        final Button btnRefresh = (Button)findViewById(R.id.ButtonRefresh);


        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);


        // specify an adapter
        final MessageAdapter adapter = new MessageAdapter(messages);
        recyclerView.setAdapter(adapter);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {handleClick(v, "send");}

            // HOW TO PUT IT OUTSIDE OF THE SetOnClickListener ??
            private void handleClick(View v, String button) {
                switch (button){
                    case "send":
                        String msg = String.valueOf(msgET.getText());
                        Message message = new Message(msg, login);
                        Log.i(TAG, message.toString());
                        //TODO Here add call to SendMsgBGAsync
                        messages.add(message);
                        adapter.notifyDataSetChanged();
                        msgET.getText().clear();

                }
            }
        });
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetMessagesListBGAsync getMessagesListBGAsync = new GetMessagesListBGAsync(mContext, user);
                GetMessagesListBGAsync.GetMessagesListListener getMessagesListListener = new GetMessagesListBGAsync.GetMessagesListListener() {
                    @Override
                    public void onGetMessagesList(boolean result) {

                    }
                };
                getMessagesListBGAsync.setGetMessagesListListener(getMessagesListListener);
                getMessagesListBGAsync.execute(messages);

            }
        });
        }

    }







