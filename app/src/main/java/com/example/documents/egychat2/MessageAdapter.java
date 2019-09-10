package com.example.documents.egychat2;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.documents.egychat2.Settings_Pakage.Chat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    public static final int msg_type_left = 0;
    public static final int msg_type_right = 1;
    FirebaseUser fuser;
    private Context context;
    private List<Chat> mChat;
    private String imgUrl;

    public MessageAdapter(Context context, List<Chat> chat, String imgUrl) {
        this.context = context;
        this.mChat = chat;
        this.imgUrl = imgUrl;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == msg_type_right) {
            View view = LayoutInflater.from(context).inflate(R.layout.chat_item_right, parent, false);
            return new MessageAdapter.ViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.chat_ittem_left, parent, false);
            return new MessageAdapter.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Chat chat = mChat.get(position);
        holder.showMsg.setText(chat.getMessage());

        if (imgUrl == "default") {
            holder.profileImg_msg.setImageResource(R.mipmap.ic_launcher);
        } else {
            Picasso.with(context).load(imgUrl).into(holder.profileImg_msg);
        }
    }

    @Override
    public int getItemCount() {
        return mChat.size();
    }

    @Override
    public int getItemViewType(int position) {
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        if (mChat.get(position).getSender().equals(fuser.getUid())) {
            return msg_type_left;
        } else
            return msg_type_right;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView showMsg;
        ImageView profileImg_msg;

        public ViewHolder(View itemView) {
            super(itemView);

            showMsg = itemView.findViewById(R.id.show_message);
            profileImg_msg = itemView.findViewById(R.id.profile_chat_img);
        }
    }
}
