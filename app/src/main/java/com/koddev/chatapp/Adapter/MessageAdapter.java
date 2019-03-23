package com.koddev.chatapp.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.koddev.chatapp.Model.Chat;
import com.koddev.chatapp.R;
import com.squareup.picasso.Picasso;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    public static  final int MSG_TYPE_LEFT = 0;
    public static  final int MSG_TYPE_RIGHT = 1;
    public static  final int MSG_TYPE_IMAGE_LEFT = 2;
    public static  final int MSG_TYPE_IMAGE_RIGHT = 3;
    public static  final int MSG_TYPE_VIDEO_LEFT = 4;
    public static  final int MSG_TYPE_VIDEO_RIGHT = 5;


    private Context mContext;
    private List<Chat> mChat;
    private String imageUrl;

    FirebaseUser fUser;

    public MessageAdapter(Context mContext, List<Chat> mChat, String imageUrl){
        this.mChat = mChat;
        this.mContext = mContext;
        this.imageUrl = imageUrl;
    }

    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == MSG_TYPE_RIGHT) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_right, parent, false);
                return new MessageAdapter.ViewHolder(view);
        }

        else if (viewType == MSG_TYPE_LEFT){
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_left, parent, false);
            return new MessageAdapter.ViewHolder(view);
        }

        else if (viewType == MSG_TYPE_IMAGE_RIGHT){
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_image_right, parent, false);
            return new MessageAdapter.ViewHolder(view);
        }

        else if (viewType == MSG_TYPE_IMAGE_LEFT){
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_image_left, parent, false);
            return new MessageAdapter.ViewHolder(view);
        }

        else if (viewType == MSG_TYPE_VIDEO_RIGHT){
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_video_right, parent, false);
            return new MessageAdapter.ViewHolder(view);
        }

        else if (viewType == MSG_TYPE_VIDEO_LEFT){
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_video_left, parent, false);
            return new MessageAdapter.ViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, int position) {
        Chat chat = mChat.get(position);
        if(chat.getMessage().startsWith("https://firebasestorage.googleapis.com")) {
            try {
                Picasso.with(mContext).load(chat.getMessage()).into(holder.showImage);
                holder.showImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        
                    }
                });
                //TODO: download image and give it an id, so that next time we open the screen we dont re-load it from the storage
            } catch (Exception ex) {
                ex.printStackTrace();
                //TODO: Handle error
            }
        }/* else if(chat.getMessage().startsWith("https://firebasestorage.googleapis.com")) {
            try {
                holder.bind(chat);
                //holder.bind(chat);
                //TODO: download image and give it an id, so that next time we open the screen we dont re-load it from the storage
            } catch (Exception ex) {
                ex.printStackTrace();
                //TODO: Handle error
            }
        }
*/
        else if (!chat.isMedia()){
            holder.show_message.setText(chat.getMessage());

            if (imageUrl.equals("default")){
                holder.profile_image.setImageResource(R.mipmap.ic_launcher);
            } else {
                Glide.with(mContext).load(imageUrl).into(holder.profile_image);
            }

            if (position == mChat.size()-1){
                if (chat.isIsseen()){
                    holder.txt_seen.setText("Seen");
                } else {
                    holder.txt_seen.setText("Delivered");
                }
            } else {
                holder.txt_seen.setVisibility(View.GONE);
            }
        }
    }


    public Bitmap loadImageBitmap(Context context, String imageName) {
        Bitmap bitmap = null;
        FileInputStream fiStream;
        try {
            fiStream    = context.openFileInput(imageName);
            bitmap      = BitmapFactory.decodeStream(fiStream);
            fiStream.close();
        } catch (Exception e) {
            Log.d("saveImage", "Exception 3, Something went wrong!");
            e.printStackTrace();
        }
        return bitmap;
    }

    public void saveImage(Context context, Bitmap b, String imageName) {
        FileOutputStream foStream;
        try {
            foStream = context.openFileOutput(imageName, Context.MODE_PRIVATE);
            b.compress(Bitmap.CompressFormat.PNG, 100, foStream);
            foStream.close();
        } catch (Exception e) {
            Log.d("saveImage", "Exception 2, Something went wrong!");
            e.printStackTrace();
        }
    }

    public boolean fileExist(String fname){
        File file = mContext.getFileStreamPath(fname);
        return file.exists();
    }

    @Override
    public int getItemCount() {
        return mChat.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder{

        public TextView show_message;
        public ImageView profile_image, showImage;
        public TextView txt_seen;
        public VideoView showVideo;
        MediaController mediaController;


        public ViewHolder(View itemView) {
            super(itemView);

            show_message = itemView.findViewById(R.id.show_message);
            profile_image = itemView.findViewById(R.id.profile_image);
            showImage = itemView.findViewById(R.id.show_image);
            showVideo = itemView.findViewById(R.id.show_video);
            txt_seen = itemView.findViewById(R.id.txt_seen);
            mediaController = new MediaController(mContext);
        }

        public void bind(Chat chat) {
            if(chat.getMessage() != null)
            {
                Uri uri = Uri.parse(chat.getMessage());
                showVideo.setVideoURI(uri);
                showVideo.requestFocus();
                showVideo.setMediaController(mediaController);
                showVideo.setZOrderOnTop(true);
                showVideo.start();
            }
        }
    }

    @Override
    public int getItemViewType(int position) {

        fUser = FirebaseAuth.getInstance().getCurrentUser();

        if(mChat.get(position).getSender().equals(fUser.getUid()) && mChat.get(position).getMessage().startsWith("https://firebasestorage.googleapis.com")){
            mChat.get(position).setMedia(true);
            return MSG_TYPE_IMAGE_RIGHT;
        }
        else if(mChat.get(position).getMessage().startsWith("https://firebasestorage.googleapis.com")){
            mChat.get(position).setMedia(true);
            return MSG_TYPE_IMAGE_LEFT;
        }

        else if (mChat.get(position).getSender().equals(fUser.getUid()) && mChat.get(position).getMessage().startsWith("https://firebasestorage.googleapis.com")) {
            mChat.get(position).setMedia(true);
            return MSG_TYPE_VIDEO_RIGHT;
        }
        else if(mChat.get(position).getMessage().startsWith("https://firebasestorage.googleapis.com")){
            mChat.get(position).setMedia(true);
            return MSG_TYPE_VIDEO_LEFT;
        }

        else if (mChat.get(position).getSender().equals(fUser.getUid())){
            return MSG_TYPE_RIGHT;
        }
        else {
            return MSG_TYPE_LEFT;
        }
    }

}