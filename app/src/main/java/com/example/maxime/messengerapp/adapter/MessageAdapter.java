package com.example.maxime.messengerapp.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.maxime.messengerapp.R;
import com.example.maxime.messengerapp.model.Attachment;
import com.example.maxime.messengerapp.model.Message;

import java.util.List;


/**
 * Created by maxime on 21/10/16.
 */

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    private static String TAG = MessageAdapter.class.getName();
    private List<Message> messages;

    // Provide a reference to the views for each data item
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewAuthor;
        public TextView textViewTxt;
        public ImageView imageView;
        public RelativeLayout relative, relative1;
        public View RelativeLayoutMessage;

        public ViewHolder(View v) {
            super(v);
            textViewAuthor = (TextView) v.findViewById(R.id.author);
            textViewTxt = (TextView) v.findViewById(R.id.textMessage);
            imageView = (ImageView) v.findViewById(R.id.imageMessage);
            relative = (RelativeLayout) v.findViewById(R.id.messageLayout);
            RelativeLayoutMessage = v.findViewById(R.id.RelativeLayoutMessage);
            relative1 = (RelativeLayout) v.findViewById(R.id.RelativeLayoutMessage);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MessageAdapter(List<Message> messages) {
        this.messages = messages;
        /*for (int i = 0 ; i < messages.size(); i++){
            Log.i(TAG, "MessageAdapter: "+messages.get(i).getAttachments().get(0).getData().toString());
        }*/
    }

    // Create new views (invoked by the layout manager)

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.element_message, parent, false);
        return new ViewHolder(v);

    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from messages at this position
        // - replace the contents of the view with that element
        /*RelativeLayout relative1 = holder.relative1;
        relative1.setHorizontalGravity(RelativeLayout.ALIGN_PARENT_RIGHT);
        relative1.setHorizontalGravity(RelativeLayout.ALIGN_PARENT_END);
        relative1.requestLayout();*/

        holder.textViewTxt.setText(messages.get(position).getMessage());
        holder.textViewAuthor.setText(messages.get(position).getLogin());
        Log.i(TAG, "ADAPTER"+messages.get(position).toString());
        Bitmap bitmap = null;
        if (messages.get(position).getAttachments() != null) {
            Attachment attachmentMessage = messages.get(position).getAttachments()[0];
            if (attachmentMessage != null) {
                String imageString = attachmentMessage.getData();
                try {
                    byte[] decodedString = Base64.decode(imageString, Base64.DEFAULT);
                    bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                } catch (Exception e) {
                    e.getMessage();
                }
                holder.imageView.setImageBitmap(bitmap);
                holder.textViewTxt.measure(0, 0);
                int width = holder.textViewTxt.getMeasuredWidth();
                holder.imageView.measure(0, 0);
                width = width + holder.imageView.getMeasuredWidth();
                RelativeLayout layout = holder.relative;
                layout.getLayoutParams().width = width;
                layout.requestLayout();
            } else {
                holder.textViewTxt.measure(0, 0);
                int width = holder.textViewTxt.getMeasuredWidth();
                RelativeLayout layout = holder.relative;
                layout.getLayoutParams().width = width + 20;
                layout.requestLayout();
            }
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return messages.size();
    }
}