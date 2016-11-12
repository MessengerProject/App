package com.example.maxime.messengerapp.task;

import android.os.AsyncTask;
import android.util.Log;

import com.example.maxime.messengerapp.model.Message;
import com.example.maxime.messengerapp.model.User;
import com.example.maxime.messengerapp.utils.services.SendMessageService;

/**
 * Created by victor on 23/10/16.
 */

public class SendMessageBGAsync extends AsyncTask<Void, Void, Boolean> {

    //Interface sendMessageListener
    public interface sendMessageListener{
        void onSend(boolean result);
    }

    private final String TAG = com.example.maxime.messengerapp.task.RegisterBGAsync.RegisterListener.class.getName();

    private User user;
    private Message message;
    protected sendMessageListener sendMessageListener;

    public SendMessageBGAsync( User user, Message message) {
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

    public void setSendMessageListener(SendMessageBGAsync.sendMessageListener sendMessageListener) {
        this.sendMessageListener = sendMessageListener;
    }
}

