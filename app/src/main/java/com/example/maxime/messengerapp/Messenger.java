package com.example.maxime.messengerapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

/**
 * Created by maxime on 18/10/16.
 */

public class Messenger extends AppCompatActivity {

    //private final String TAG = AppCompatActivity.class.getName();
    //Message msg = new Message("Bob","L'eponge");
    ListView  listViewMsg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messenger);

        Button btnSend = (Button)findViewById(R.id.ButtonSend);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText  msgET = (EditText) findViewById(R.id.message);
                String msg = String.valueOf(msgET.getText());
                //TODO: implement message from Message class and push it to server (and add it to listViewMsg for the moment)
            }
        });
    }






}
