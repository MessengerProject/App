package com.example.maxime.messengerapp.task;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.maxime.messengerapp.model.Message;
import com.example.maxime.messengerapp.model.User;
import com.example.maxime.messengerapp.service.SendMessageService;

/**
 * Created by victor on 23/10/16.
 */

public class SendMessageBGAsync extends AsyncTask<Void, Void, Boolean> {

    private final String TAG = com.example.maxime.messengerapp.task.RegisterBGAsync.RegisterListener.class.getName();
    private Context mContext;
    private User user;
    private Message message;
    public sendMessageListener sendMessageListener;


    public SendMessageBGAsync(Context mContext, User user, Message message) {
        this.mContext = mContext;
        this.user = user;
        this.message = message;
    }


    @Override
    protected Boolean doInBackground(Void... params) {
        return SendMessageService.SendMessage(user, message);
    }

    @Override
    protected void onPostExecute(Boolean result) {
        Log.i(TAG, result.toString());
    }


    //Interface sendMessageListener
    public interface sendMessageListener{
        void onSend(boolean result);
    }
    public void setSendMessageListener(SendMessageBGAsync.sendMessageListener sendMessageListener) {
        this.sendMessageListener = sendMessageListener;
    }
}

