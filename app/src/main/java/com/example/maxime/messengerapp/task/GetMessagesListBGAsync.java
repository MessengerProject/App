package com.example.maxime.messengerapp.task;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.maxime.messengerapp.model.Message;
import com.example.maxime.messengerapp.model.User;
import com.example.maxime.messengerapp.service.GetMessagesListService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by maxime on 23/10/16.
 */

public class GetMessagesListBGAsync extends AsyncTask<Void, Void, Boolean> {
    private final String TAG = GetMessagesListBGAsync.class.getName();
    private Context mContext = null;
    private User user;
    public GetMessagesListListener getMessagesListListener;
    private List<Message> messages;

    public GetMessagesListBGAsync(Context mContext, User user, List<Message> messages) {
        this.mContext = mContext;
        this.user = user;
        this.messages = messages;
    }

    public void setGetMessagesListListener(GetMessagesListListener getMessagesListListener){
        this.getMessagesListListener = getMessagesListListener;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        String stringMessagesList = GetMessagesListService.GetMessageListResponse(user);
        Log.i(TAG, stringMessagesList);
        Gson messagesList = new Gson();
        messagesList.toJson(stringMessagesList);
        Type listType = new TypeToken<List<Message>>(){}.getType();
        Log.i(TAG, "MessageList: "+messagesList);
        List<Message> messagesTmp = (List<Message>) messagesList.fromJson(stringMessagesList, listType);
        this.messages.clear();
        this.messages.addAll(messagesTmp);
        return true;
    }
    public interface GetMessagesListListener{
        void onGetMessagesList();
    }
}