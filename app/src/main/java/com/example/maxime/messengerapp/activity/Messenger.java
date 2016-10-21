package com.example.maxime.messengerapp.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.maxime.messengerapp.adapter.ItemAdapter;
import com.example.maxime.messengerapp.model.Message;
import com.example.maxime.messengerapp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maxime on 18/10/16.
 */

public class Messenger extends AppCompatActivity {
    private final String TAG = Messenger.class.getName();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_messenger);
        final Context mContext = getApplicationContext();

        SharedPreferences sharedPref = mContext.getSharedPreferences("prefs", mContext.MODE_PRIVATE);
        final String login = sharedPref.getString("login", "error");

        final List<Message> messages = new ArrayList<>();


        //Log.i(TAG, sharedPref.getString("login","error" ));
        ListView listView = (ListView) findViewById(R.id.listViewMsg);

        final ItemAdapter adapter = new ItemAdapter(messages, mContext);
        listView.setAdapter(adapter);

        Button btnSend = (Button)findViewById(R.id .ButtonSend);


        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText  msgET = (EditText) findViewById(R.id.message);
                String msg = String.valueOf(msgET.getText());
                Message message = new Message(msg, login);
                Log.i(TAG, message.toString());
                messages.add(message);
                adapter.notifyDataSetChanged();
                //TODO: implement message from Message class and push it to server (and add it to listViewMsg for the moment)

            }
        });

    }






}
