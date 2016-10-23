package com.example.maxime.messengerapp.task;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.maxime.messengerapp.model.Message;
import com.example.maxime.messengerapp.model.User;
import com.example.maxime.messengerapp.service.GetMessagesListService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by maxime on 23/10/16.
 */

public class GetMessagesListBGAsync extends AsyncTask<List<Message>, Void, Boolean> {
    private final String TAG = GetMessagesListBGAsync.class.getName();
    private Context mContext;
    private User user;
    public GetMessagesListListener getMessagesListListener;

    public GetMessagesListBGAsync(Context mContext, User user) {
        this.mContext = mContext;
        this.user = user;
    }

    public void setGetMessagesListListener(GetMessagesListListener getMessagesListListener){
        this.getMessagesListListener = getMessagesListListener;
    }


    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected List<Message> doInBackground(List<Message> messageList) {
        String stringMessagesList = GetMessagesListService.GetMessageListResponse(user);
        //Log.i(TAG, stringMessagesList.toString());
        if (stringMessagesList != "") {
            try {
                JSONObject JsonMessagesList = new JSONObject(stringMessagesList);
                Log.i(TAG, JsonMessagesList.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
        else {
            //return false;
        }
        return messageList;
    }

    public interface GetMessagesListListener{
        void onGetMessagesList(boolean result);
    }
}
