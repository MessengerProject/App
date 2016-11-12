package com.example.maxime.messengerapp.task;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.example.maxime.messengerapp.model.Attachment;
import com.example.maxime.messengerapp.model.Message;
import com.example.maxime.messengerapp.model.MessageImage;
import com.example.maxime.messengerapp.model.User;
import com.example.maxime.messengerapp.service.GetImageMessageService;
import com.example.maxime.messengerapp.service.GetMessagesListService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by maxime on 23/10/16.
 */

public class GetMessagesListBGAsync extends AsyncTask<Integer, Void, Boolean> {
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

    public void setGetMessagesListListener(GetMessagesListListener getMessagesListListener) {
        this.getMessagesListListener = getMessagesListListener;
    }

    @Override
    protected Boolean doInBackground(Integer... params) {
        int limit = params[0];
        int offset = params[1];
        String stringMessagesList = GetMessagesListService.GetMessageListResponse(user, limit, offset);
        //Log.i(TAG, "MessagesList: " + stringMessagesList);
        //Service
        Gson messagesList = new Gson();
        messagesList.toJson(stringMessagesList);
        Type listType = new TypeToken<List<MessageImage>>() {
        }.getType();
        List<MessageImage> messagesTmp = messagesList.fromJson(stringMessagesList, listType);
        Log.i(TAG, messagesTmp.toString());
        //messages.clear();
        for (int i = messagesTmp.size()-1;i >= 0 ; i--)
            try {
                if (messagesTmp.get(i).getAttachments()[0].length() > 20) {
                    Log.i(TAG, "doInBackground: " + messagesTmp.get(i).getAttachments()[0]);
                    Bitmap image = GetImageMessageService.getImageMessageService(user, messagesTmp.get(i).getAttachments()[0]);
                    if (image != null) {
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        image.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
                        byte[] b = baos.toByteArray();
                        String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
                        Attachment attachmentMessage = new Attachment("attachments/png", encodedImage);
                        Message message = new Message(messagesTmp.get(i).getMessage().toString(), messagesTmp.get(i).getLogin().toString(), attachmentMessage, encodedImage);
                        messages.add(0, message);
                    } else {
                        Message message = new Message(messagesTmp.get(i).getMessage().toString(), messagesTmp.get(i).getLogin().toString());
                        messages.add(0, message);
                    }
                } else {
                    Message message = new Message(messagesTmp.get(i).getMessage().toString(), messagesTmp.get(i).getLogin().toString());
                    messages.add(message);
                }
            }
            catch (Exception e){
                Log.i(TAG, e.toString());
            }
        //messagesTmp.get(0).getAttachments().add(imageMessage);
        //this.messages.clear();
        //messages.addAll(messages);
        //Log.i(TAG, messages.toString());
        return true;
    }

    public interface GetMessagesListListener {
        void onGetMessagesList(boolean result);
    }
}