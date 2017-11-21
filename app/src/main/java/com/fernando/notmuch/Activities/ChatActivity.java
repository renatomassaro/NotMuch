package com.fernando.notmuch.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.fernando.notmuch.Adapter.MessageAdapter;
import com.fernando.notmuch.Config.FireBaseDAO;
import com.fernando.notmuch.Helper.Base64Custom;
import com.fernando.notmuch.Helper.Preferences;
import com.fernando.notmuch.Model.Chat;
import com.fernando.notmuch.Model.Message;
import com.fernando.notmuch.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText messageArea;
    private ImageButton sendMessage;
    private ListView messages;
    private DatabaseReference firebase;
    private ArrayList<Message> msgs;
    private MessageAdapter arrayAdapter;
    private ValueEventListener eventListener;

    private String friendName;
    private String friendEmail;
    private String friendId;

    private String userId;
    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        //Get view references
        messageArea = findViewById(R.id.message_text);
        sendMessage = findViewById(R.id.send_message);
        messages = findViewById(R.id.message_list);

        //Get user data
        Preferences preferences = new Preferences(getApplicationContext());
        userId = preferences.getUserId();
        userName = preferences.getUserName();

        //Get information from another activity
        Bundle extra = getIntent().getExtras();
        if(extra != null) {
            friendName = extra.getString("name");
            friendEmail = extra.getString("email");
        }
        friendId = Base64Custom.codeBase64(friendEmail);

        //Set up toolbar
        setToolbar();

        //Create listview and adapter
        msgs = new ArrayList<>();
        arrayAdapter = new MessageAdapter(getApplicationContext(), msgs);
        messages.setAdapter(arrayAdapter);

        //Get messages from firebase
        firebase = FireBaseDAO.getFirebase()
                .child("messages")
                .child(userId)
                .child(friendId);

        //Set up the messages listener and add it into firebase
        eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                msgs.clear();
                for(DataSnapshot data : dataSnapshot.getChildren()) {
                    Message anotherMessage = data.getValue(Message.class);
                    if(anotherMessage.getMessage() != null)
                        msgs.add(anotherMessage);
                }
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        firebase.addValueEventListener(eventListener);

        //Build click action sending the messages
        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = messageArea.getText().toString();
                if(!msg.isEmpty()) {
                    Message msgModel = new Message();
                    msgModel.setUserId(userId);
                    msgModel.setMessage(msg);

                    //Save the message for the user and for friend
                    if(saveMessage(userId, friendId, msgModel) ) {
                        messageArea.setText("");
                        saveMessage(friendId, userId, msgModel);
                    } else {
                        Toast.makeText(getApplicationContext(),  "Cannot send the message",
                                Toast.LENGTH_SHORT).show();
                    }
                }

                //Save the chat
                Chat chat = new Chat();
                chat.setUserId(friendId);
                chat.setName(friendName);
                chat.setMessage(msg);
                saveChat(userId, friendId, chat);

                chat.setUserId(userId);
                chat.setName(userName);
                saveChat(friendId, userId, chat);
            }
        });
    }

    private void setToolbar() {
        toolbar = findViewById(R.id.toolbar_chat);
        toolbar.setTitle(friendName);
        toolbar.setNavigationIcon(R.drawable.ic_action_arrow_left);
        setSupportActionBar(toolbar);
    }

    private boolean saveMessage(String userId, String friendId, Message msg) {
        try {
            firebase = FireBaseDAO.getFirebase().child("messages");
            firebase.child(userId)
                    .child(friendId).
                    push().setValue(msg);
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebase.removeEventListener(eventListener);
    }

    private boolean saveChat(String userId, String friendId, Chat chat) {
        try {
            firebase = FireBaseDAO.getFirebase().child("chats");
            firebase.child(userId)
                    .child(friendId)
                    .setValue(chat);
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

}
