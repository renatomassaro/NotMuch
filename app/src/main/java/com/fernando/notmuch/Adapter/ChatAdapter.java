package com.fernando.notmuch.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.fernando.notmuch.Model.Chat;
import com.fernando.notmuch.R;

import java.util.ArrayList;

public class ChatAdapter extends ArrayAdapter<Chat> {

    ArrayList<Chat> chats;
    Context context;

    public ChatAdapter(@NonNull Context context, @NonNull ArrayList<Chat> objects) {
        super(context, 0, objects);
        this.chats = objects;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = null;
        if(chats != null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            view = inflater.inflate(R.layout.chats_list_layout, parent, false);

            TextView name = view.findViewById(R.id.textView_name);
            TextView something = view.findViewById(R.id.textView_something);

            name.setText(chats.get(position).getName());
            something.setText(chats.get(position).getMessage());
        }
        return view;
    }

    @Override
    public int getCount() {
        return chats.size();
    }
}
