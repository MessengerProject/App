package com.example.maxime.messengerapp.task;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import com.example.maxime.messengerapp.model.Attachment;
import com.example.maxime.messengerapp.model.Message;
import com.example.maxime.messengerapp.model.User;
import com.example.maxime.messengerapp.utils.services.GetImageMessageService;
import com.example.maxime.messengerapp.utils.services.GetMessagesListService;
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
        int TypeOfGet = params[2];
        Log.i(TAG, "limit: "+ limit + "\noffset:  " + offset + "\nTypeOfGet: "+ TypeOfGet);
        String stringMessagesList = GetMessagesListService.GetMessageListResponse(user, limit, offset);
        Log.i(TAG, "MessagesList: " + stringMessagesList);
        //Service
        Gson messagesList = new Gson();
        messagesList.toJson(stringMessagesList);
        Type listType = new TypeToken<List<Message>>() {
        }.getType();

        List<Message> messagesTmp = messagesList.fromJson(stringMessagesList, listType);
        //messages.clear();
        for (int i = messagesTmp.size()-1 ; i > 0 ;  i--) {
            Bitmap image = null;
            try
            {
                image = GetImageMessageService.getImageMessageService(user, messagesTmp.get(i).getImages()[0]);
            }
            catch (Exception e)
            {
                Log.i(TAG, e.toString());
            }

            if (image != null) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                image.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
                byte[] b = baos.toByteArray();
                String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
                Attachment attachmentMessage = new Attachment("image/png", encodedImage);
                Attachment[] attachments = new Attachment[1];
                attachments[0] = attachmentMessage;
                Message message = new Message(messagesTmp.get(i).getMessage().toString(), messagesTmp.get(i).getLogin().toString(), attachmentMessage, encodedImage);
                switch (TypeOfGet){
                    case 0:
                    {   messages.add(0,message);
                        Log.i(TAG, "messages.size():  " + messages.size());
                        messages.remove(messages.size() - 1);
                        Log.i(TAG, "messages.size():  " + messages.size());

                    }
                    case 1:
                    {
                        messages.add(message);
                        //messages.remove(0);

                    }
                    case 2:
                    {
                        messages.add(message);
                    }
                    messagesTmp.get(i).getImages()[0] = encodedImage;
                    messagesTmp.get(i).setAttachments(attachments);
                }

            } else {
                Attachment attachmentMessage = new Attachment("image/png", "");
                Message message = new Message(messagesTmp.get(i).getMessage().toString(), messagesTmp.get(i).getLogin().toString(), attachmentMessage, "");
                switch (TypeOfGet) {
                    case 0: {
                        messages.add(0, message);
                        Log.i(TAG, "messages.size():  " + messages.size());
                        messages.remove(messages.size()-1);
                        Log.i(TAG, "messages.size():  " + messages.size());
                    }
                    case 1: {
                        messages.add(message);
                        //messages.remove(0);
                    }
                    case 2: {
                        messages.add(message);
                    }
                }
            }

        }
        return true;
    }

    public interface GetMessagesListListener {
        void onGetMessagesList(boolean result);
    }
}