package com.example.maxime.messengerapp.utils;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;

import com.example.maxime.messengerapp.model.Attachment;
import com.example.maxime.messengerapp.model.Message;
import com.example.maxime.messengerapp.model.User;
import com.example.maxime.messengerapp.utils.services.GetImageMessageService;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.util.List;

import static android.support.v4.app.ActivityCompat.startActivityForResult;

/**
 * Created by victor on 12/11/16.
 */

public class Util {

    public static String pathToEncodedImage(String imagePath){
        Bitmap imageBitmap = BitmapFactory.decodeFile(imagePath);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
        byte[] b = baos.toByteArray();
        String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
        return encodedImage;
    }

    public static String getimagePath(Uri data, ContentResolver contentResolver){
        Uri selectedImage = data;
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = contentResolver.query(selectedImage, filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String imagepath = cursor.getString(columnIndex);
        cursor.close();
        return imagepath;
    }

    public static String BitmapToEncodedImage(Bitmap image){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
        byte[] b = baos.toByteArray();
        String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
        return encodedImage;
    }

    public static Intent openCameraIntent(){
        Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        return intent;
    }

    public static Gson StringToJson(String messagesList){
        Gson messageList = new Gson();
        messageList.toJson(messagesList);
        return messageList;
    }

    public static List<Message> updateMessageList(List<Message> messagesTmp, List<Message> messages, User user){
        for (int i = messagesTmp.size()-1 ; i > 0 ;  i--) {
            Bitmap image = null;
            try
            {
                image = GetImageMessageService.getImageMessageService(user, messagesTmp.get(i).getImages()[0]);
            }
            catch (Exception e)
            {
                Log.i(null,  e.toString());
            }

            if (image != null) {
                String encodedImage = Util.BitmapToEncodedImage(image);

                //Set image inside message
                Attachment attachmentMessage = new Attachment("image/png", encodedImage);
                Attachment[] attachments = new Attachment[1];
                attachments[0] = attachmentMessage;
                Message message = new Message(messagesTmp.get(i).getMessage().toString(), messagesTmp.get(i).getLogin().toString(), attachmentMessage, encodedImage);
                messages.add(0,message);
                messagesTmp.get(i).getImages()[0] = encodedImage;
                messagesTmp.get(i).setAttachments(attachments);
            } else {
                //Set default image inside message
                Attachment attachmentMessage = new Attachment("image/png", "");
                Message message = new Message(messagesTmp.get(i).getMessage().toString(), messagesTmp.get(i).getLogin().toString(), attachmentMessage, "");
                messages.add(0,message);
            }
        }
        return messages;
    }
}
