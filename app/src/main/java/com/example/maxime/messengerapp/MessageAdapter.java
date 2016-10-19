package com.example.maxime.messengerapp;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by maxime on 19/10/16.
 */

public class MessageAdapter extends ArrayAdapter<Message> {
    Context context;
    int layoutResourceId;
    ArrayList<Message> data=null;

    public MessageAdapter(Context context, int layoutResourceId, ArrayList<Message> msg) {
        super(context, layoutResourceId, msg);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.data = msg;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View row = convertView;
        MessageHolder holder = null;
        if (row == null){
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new MessageHolder();
            holder.elementMessage = (TextView)row.findViewById(R.id.textMessage);
            holder.author = (TextView)row.findViewById(R.id.author);

            row.setTag(holder);
        }
        else{
            holder = (MessageHolder)row.getTag();
        }
        Message message = data.get(position);
        holder.author.setText(message.author);
        holder.elementMessage.setText(message.elementMessage);

        return row;
    }

    static class MessageHolder{
        TextView author;
        TextView elementMessage;
    }
}
