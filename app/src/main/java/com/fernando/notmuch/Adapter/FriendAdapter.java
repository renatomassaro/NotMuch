package com.fernando.notmuch.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.fernando.notmuch.Model.Friend;
import com.fernando.notmuch.R;

import java.util.ArrayList;

public class FriendAdapter extends ArrayAdapter<Friend>{

    private ArrayList<Friend> friends;
    private Context context;

    public FriendAdapter(@NonNull Context context, @NonNull ArrayList<Friend> objects) {
        super(context, 0, objects);
        this.friends = objects;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = null;

        if(friends != null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            view = inflater.inflate(R.layout.friends_list_layout, parent, false);

            TextView name = view.findViewById(R.id.textView_name);
            TextView something = view.findViewById(R.id.textView_something);
            name.setText(friends.get(position).getName());
            something.setText(friends.get(position).getEmail());

        }

        return view;
    }

    @Override
    public int getCount() {
        return friends.size();
    }
}
