package com.example.maxime.messengerapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;


/**
 * Created by maxime on 18/10/16.
 */

public class Messenger extends AppCompatActivity {
    private final String TAG = Messenger.class.getName();

    private ListView listViewMsg;
    //private final String TAG = AppCompatActivity.class.getName();
    //Message msg = new Message("Bob","L'eponge");
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messenger);

        final ArrayList<Message> message_data = new ArrayList<Message>();
        listViewMsg = (ListView)findViewById(R.id.listViewMsg);
        View header = getLayoutInflater().inflate(R.layout.view_message, null);
        listViewMsg.addHeaderView(header);
        final MessageAdapter msg_array_adapter = new MessageAdapter(this, R.layout.view_message, message_data);
        listViewMsg.setAdapter(msg_array_adapter);
        Button btnSend = (Button)findViewById(R.id.ButtonSend);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText  msgET = (EditText) findViewById(R.id.message);
                String msg = String.valueOf(msgET.getText());
                String author = "max";
                Message message = new Message(msg, author);
                message_data.add(message);
                //msgArray.add(msg);
                Log.i(TAG, message_data.toString());
                //msg_array_adapter.add(msg);
                msg_array_adapter.notifyDataSetChanged();
                //Log.i(TAG, message.elementMessage);
                //TODO: implement message from Message class and push it to server (and add it to listViewMsg for the moment)

            }
        });

    }






}
