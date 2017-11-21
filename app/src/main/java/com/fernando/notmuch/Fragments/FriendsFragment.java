package com.fernando.notmuch.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.fernando.notmuch.Activities.ChatActivity;
import com.fernando.notmuch.Adapter.FriendAdapter;
import com.fernando.notmuch.Config.FireBaseDAO;
import com.fernando.notmuch.Helper.Preferences;
import com.fernando.notmuch.Model.Friend;
import com.fernando.notmuch.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FriendsFragment extends Fragment {

    private ListView listView;
    private FriendAdapter adapter;
    private ArrayList<Friend> friends;
    private ValueEventListener valueEventListener;
    private DatabaseReference firebase;

    public FriendsFragment() {
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

        View view = inflater.inflate(R.layout.fragment_friends, container, false);
        listView = view.findViewById(R.id.lv_friends);
        friends = new ArrayList<>();
        adapter = new FriendAdapter(getActivity(), friends);

        listView.setAdapter(adapter);

        firebase = FireBaseDAO.getFirebase();
        Preferences preferences = new Preferences(getActivity());
        String userId = preferences.getUserId();

        firebase = firebase.child("friends").child(userId);

        //Listener to get the friends
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                friends.clear();
                for(DataSnapshot data : dataSnapshot.getChildren()) {
                    Friend friend = data.getValue(Friend.class);
                    friends.add(friend);
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

                Friend friend = friends.get(position);
                if(friend != null) {
                    Intent intent = new Intent(getActivity(), ChatActivity.class);
                    intent.putExtra("name", friend.getName());
                    intent.putExtra("email", friend.getEmail());
                    startActivity(intent);
                }
            }
        });

        return view;
    }

}
