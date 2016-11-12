package com.example.maxime.messengerapp.task;

import android.os.AsyncTask;

import com.example.maxime.messengerapp.model.Message;
import com.example.maxime.messengerapp.model.User;
import com.example.maxime.messengerapp.utils.Util;
import com.example.maxime.messengerapp.utils.services.GetMessagesListService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by maxime on 23/10/16.
 */

public class GetMessagesListBGAsync extends AsyncTask<Integer, Void, Boolean> {
    private final String TAG = GetMessagesListBGAsync.class.getName();
    private User user;
    private GetMessagesListListener getMessagesListListener;
    private List<Message> messages;

    public GetMessagesListBGAsync( User user, List<Message> messages) {
        this.user = user;
        this.messages = messages;
    }

    public void setGetMessagesListListener(GetMessagesListListener getMessagesListListener) {
        this.getMessagesListListener = getMessagesListListener;
    }

    @Override
    protected Boolean doInBackground(Integer... params) {
        int limit = params[0];
        int offset = params[1];
        String stringMessagesList = GetMessagesListService.GetMessageListResponse(user, limit, offset);

        Gson messagesList = Util.StringToJson(stringMessagesList);

        Type listType = new TypeToken<List<Message>>() {}.getType();
        List<Message> messagesTmp = messagesList.fromJson(stringMessagesList, listType);

        //Update MessageList to include images
        messages = Util.updateMessageList(messagesTmp, messages, user);
        return true;
    }

    public interface GetMessagesListListener {
        void onGetMessagesList(boolean result);
    }
}