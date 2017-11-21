package com.fernando.notmuch.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.fernando.notmuch.Activities.ChatActivity;
import com.fernando.notmuch.Adapter.ChatAdapter;
import com.fernando.notmuch.Adapter.FriendAdapter;
import com.fernando.notmuch.Config.FireBaseDAO;
import com.fernando.notmuch.Helper.Base64Custom;
import com.fernando.notmuch.Helper.Preferences;
import com.fernando.notmuch.Model.Chat;
import com.fernando.notmuch.Model.Friend;
import com.fernando.notmuch.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends Fragment {

    private ListView listView;
    private ChatAdapter adapter;
    private ArrayList<Chat> chats;
    private ValueEventListener valueEventListener;
    private DatabaseReference firebase;

    public ChatFragment() {
        // Required empty public constructor
    }


    @Override
    public void onStart() {
        super.onStart();
        firebase.addValueEventListener(valueEventListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        firebase.removeEventListener(valueEventListener);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        listView = view.findViewById(R.id.lv_chats);
        chats = new ArrayList<>();
        adapter = new ChatAdapter(getActivity(), chats);

        listView.setAdapter(adapter);

        Preferences preferences = new Preferences(getActivity());
        String userId = preferences.getUserId();

        firebase = FireBaseDAO.getFirebase()
                .child("chats")
                .child(userId);

        //Listener to get the friends
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                chats.clear();
                for(DataSnapshot data : dataSnapshot.getChildren()) {
                    Chat chat = data.getValue(Chat.class);
                    chats.add(chat);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                Chat chat = chats.get(position);
                if(chat != null) {
                    Intent intent = new Intent(getActivity(), ChatActivity.class);
                    intent.putExtra("name", chat.getName());
                    String email = Base64Custom.decodeBase64(chat.getUserId());
                    intent.putExtra("email", email);
                    startActivity(intent);
                }
            }
        });

        return view;
    }

}
