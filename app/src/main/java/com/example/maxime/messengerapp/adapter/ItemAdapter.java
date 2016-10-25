package com.example.maxime.messengerapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.maxime.messengerapp.R;
import com.example.maxime.messengerapp.model.Message;

import java.util.List;

/**
 * Created by maxime on 21/10/16.
 */

public class ItemAdapter extends BaseAdapter {
    List<Message> messages;
    Context context;
    LayoutInflater layoutInflater;

    public ItemAdapter(List<Message> messages, Context context) {
        this.messages = messages;
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return messages.size();
    }

    @Override
    public Object getItem(int position) {
        return messages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.element_message, null);
        TextView txt = (TextView) convertView.findViewById(R.id.text);
        txt.setText(messages.get(position).getMessage());
        TextView author = (TextView) convertView.findViewById(R.id.author);
        author.setText(messages.get(position).getLogin());
        return convertView;    }
}
