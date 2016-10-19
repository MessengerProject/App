package com.example.maxime.messengerapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by maxime on 18/10/16.
 */

public class Messenger extends AppCompatActivity {
    private final String TAG = AppCompatActivity.class.getName();

    //private final String TAG = AppCompatActivity.class.getName();
    //Message msg = new Message("Bob","L'eponge");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messenger);
        //listViewMsg = (ListView)findViewById(R.id.listViewMsg);
        final List<String> msgArray = new ArrayList<String>();

        Button btnSend = (Button)findViewById(R.id.ButtonSend);
        final ArrayAdapter<String> msg_array_adapter;
        msg_array_adapter = new ArrayAdapter<String>(this, R.layout.view_message, msgArray);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText  msgET = (EditText) findViewById(R.id.message);
                String msg = String.valueOf(msgET.getText());
                String author = "max";
                Message message = new Message(msg, author);

                msgArray.add(msg);
                Log.i(TAG, msgArray.toString());
                msg_array_adapter.notifyDataSetChanged();
                //Log.i(TAG, message.elementMessage);
                //TODO: implement message from Message class and push it to server (and add it to listViewMsg for the moment)

            }
        });

    }






}
