package com.fernando.notmuch.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.fernando.notmuch.Helper.Preferences;
import com.fernando.notmuch.Model.Message;
import com.fernando.notmuch.R;

import java.util.ArrayList;


public class MessageAdapter extends ArrayAdapter<Message> {

    private ArrayList<Message> messages;
    private Context context;
    private String myId;

    public MessageAdapter(@NonNull Context context, @NonNull ArrayList<Message> objects) {
        super(context, 0, objects);
        this.context = context;
        this.messages = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = null;
        Preferences preferences = new Preferences(context);
        myId = preferences.getUserId();
        if(messages != null) {
            //Set layout
            Message message = messages.get(position);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            if (message.getUserId().equals(myId)) {
                view = inflater.inflate(R.layout.message_right, parent, false);
            } else {
                view = inflater.inflate(R.layout.message_left, parent, false);
            }
            //Set the message and return
            TextView textView = view.findViewById(R.id.msg_left);
            textView.setText(message.getMessage());
        }


        return view;
    }
}
