package com.example.maxime.messengerapp.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.maxime.messengerapp.R;
import com.example.maxime.messengerapp.adapter.MessageAdapter;
import com.example.maxime.messengerapp.model.Message;
import com.example.maxime.messengerapp.model.User;
import com.example.maxime.messengerapp.task.LoginBGAsync;
import com.example.maxime.messengerapp.task.SendMessageBGAsync;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutionException;

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

        final List<Message> messages = new ArrayList<>();

        final Button btnSend = (Button)findViewById(R.id .ButtonSend);
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerViewMsg);
        final EditText msgET = (EditText) findViewById(R.id.message);
        final Context context = getApplicationContext();

        recyclerView.setHasFixedSize(true);
        final User user = new User(login, pwd);//comment mettre un user permanent sur la session

        // use a linear layout manager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        // specify an adapter
        final MessageAdapter adapter = new MessageAdapter(messages);
        recyclerView.setAdapter(adapter);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {handleClick(v, "send");}

            private void handleClick(View v, String button) {
                switch (button){
                    case "send":
                        String msg = String.valueOf(msgET.getText());
                        Calendar c = Calendar.getInstance();
                        System.out.println("Current time => " + c.getTime());

                        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
                        String formattedDate = df.format(c.getTime());
                        Message message = new Message(msg, formattedDate, user);
                        Log.i(TAG, message.toString());
                        messages.add(message);
                        adapter.notifyDataSetChanged();
                        msgET.getText().clear();
                        SendMessageBGAsync sendMessage_bg_async = new SendMessageBGAsync(context, user, message);

                        SendMessageBGAsync.sendMessageListener sendMessageListener= new SendMessageBGAsync.sendMessageListener() {
                            @Override
                            public void onSend(boolean result) {
                                if (!result)
                                {
                                    Toast.makeText(getApplication(), "Error to send message to server", Toast.LENGTH_LONG).show();
                                }
                                else
                                {
                                    Toast.makeText(getApplication(), "Message sended", Toast.LENGTH_LONG).show();

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

                }
            }
        });

    }






}
