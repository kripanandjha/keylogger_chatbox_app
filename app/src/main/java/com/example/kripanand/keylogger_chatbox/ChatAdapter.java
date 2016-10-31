package com.example.kripanand.keylogger_chatbox;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.List;

/**
 * Created by vinuth on 23/10/16.
 */

public class ChatAdapter extends ArrayAdapter<Message> {

    private String mUserID;

    public ChatAdapter(Context context, String userID, List<Message> messages) {
        super(context, 0, messages);

        mUserID = userID;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.chat_row, parent, false);
            final ViewHolder holder = new ViewHolder();
            holder.imageLeft = (ImageView) convertView.findViewById(R.id.ProfileLeft);
            holder.imageRight = (ImageView) convertView.findViewById(R.id.ProfileRight);
            holder.body = (TextView) convertView.findViewById(R.id.tvBody);
            convertView.setTag(holder);
        }

        final Message message = (Message)getItem(position);
        final ViewHolder holder = (ViewHolder) convertView.getTag();
        final boolean isMe = message.getUserID().equals(mUserID);

        if (isMe) {
            holder.imageRight.setVisibility(View.VISIBLE);
            holder.imageLeft.setVisibility(View.GONE);
            holder.body.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
        } else{
            holder.imageRight.setVisibility(View.GONE);
            holder.imageLeft.setVisibility(View.VISIBLE);
            holder.body.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
        }

        final ImageView profileView = isMe ? holder.imageRight : holder.imageLeft;
        Picasso.with(getContext()).load(getProfileGravatar(message.getUserID())).into(profileView);
        holder.body.setText(message.getBody());

        return convertView;
    }

    private static String getProfileGravatar(final String userID){
        String hex = "";
        try {
            final MessageDigest digest = MessageDigest.getInstance("MD5");
            final byte[] hash = digest.digest(userID.getBytes());
            final BigInteger bigInt = new BigInteger(hash);
        } catch(Exception e){
            e.printStackTrace();
        }

        return "http://www.gravatar.com/avatar/" + hex + "?d=identicon";
    }

    class ViewHolder{
        public ImageView imageLeft;
        public ImageView imageRight;
        public TextView body;
    }
}
