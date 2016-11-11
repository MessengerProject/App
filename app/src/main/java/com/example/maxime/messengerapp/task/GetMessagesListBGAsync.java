package com.example.maxime.messengerapp.task;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import com.example.maxime.messengerapp.model.Image;
import com.example.maxime.messengerapp.model.Message;
import com.example.maxime.messengerapp.model.MessageImage;
import com.example.maxime.messengerapp.model.User;
import com.example.maxime.messengerapp.service.GetImageMessageService;
import com.example.maxime.messengerapp.service.GetMessagesListService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by maxime on 23/10/16.
 */

public class GetMessagesListBGAsync extends AsyncTask<Void, Void, Boolean> {
    private final String TAG = GetMessagesListBGAsync.class.getName();
    private Context mContext = null;
    private User user;
    public GetMessagesListListener getMessagesListListener;
    public List<Message> messages;

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
        //Service
        Bitmap image = GetImageMessageService.getImageMessageService(user, "https://training.loicortola.com/chat-rest/2.0/files/msg-a545901e-814a-44cf-b96a-7675d711755c/0.png");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
        byte[] b = baos.toByteArray();
        String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
        Image imageMessage = new Image("image/png", encodedImage);
        Gson messagesList = new Gson();
        messagesList.toJson(stringMessagesList);
        Type listType = new TypeToken<List<Message>>(){}.getType();
        Log.i(TAG, "MessageList: "+messagesList);
        List<MessageImage> messagesTmp = messagesList.fromJson(stringMessagesList, listType);
        List<Message> messages = new ArrayList<>(20);
        for (int i = 0; i< messagesTmp.size(); i++){
            Message message = new Message(messagesTmp.get(i).getMessage().toString(), messagesTmp.get(i).getLogin().toString(), imageMessage);
            messages.add(message);
        }
        //messagesTmp.get(0).getAttachments().add(imageMessage);
        this.messages.clear();
        this.messages.addAll(messages);
        Log.i(TAG, messages.toString());
        return true;
    }
    public interface GetMessagesListListener{
        void onGetMessagesList(boolean result);
    }
}