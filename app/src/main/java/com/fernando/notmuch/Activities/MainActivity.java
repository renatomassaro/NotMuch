package com.fernando.notmuch.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.fernando.notmuch.Adapter.TabAdapter;
import com.fernando.notmuch.Config.FireBaseDAO;
import com.fernando.notmuch.Helper.Base64Custom;
import com.fernando.notmuch.Helper.Preferences;
import com.fernando.notmuch.Helper.SlidingTabLayout;
import com.fernando.notmuch.Model.Friend;
import com.fernando.notmuch.Model.User;
import com.fernando.notmuch.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private SlidingTabLayout slidingTabLayout;
    private ViewPager viewPager;
    private String friendId;
    private DatabaseReference firebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setToolbar();
        setFragments();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.action_logout:
                logOut();
                return true;
            case R.id.action_person_add:
                addNewFriend();
                return true;
            case R.id.action_search:
            case R.id.actions_settings:
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void logOut() {
        FireBaseDAO.getFirebaseAuth().signOut();
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void setToolbar() {
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Not Much");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
    }

    private void setFragments() {
        slidingTabLayout = findViewById(R.id.sld_tabs);
        viewPager = findViewById(R.id.vp_page);
        //Fill all the space
        slidingTabLayout.setDistributeEvenly(true);
        slidingTabLayout.setSelectedIndicatorColors(ContextCompat.getColor(this, R.color.gray));
        //Set adapter
        TabAdapter adapter = new TabAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        //set view pager
        slidingTabLayout.setViewPager(viewPager);
    }

    private void addNewFriend() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.Theme_AppCompat_Light_Dialog_Alert));

        dialog.setTitle("New friend =)");
        dialog.setMessage("Type your friend's e-mail");
        dialog.setCancelable(false);

        final EditText editText = new EditText(getApplicationContext());
        dialog.setView(editText);

        dialog.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String emailFriend = editText.getText().toString();
                if(!emailFriend.isEmpty()) {
                    friendId = Base64Custom.codeBase64(emailFriend).replace("\n","");
                    //Get user from DB
                    firebase = FireBaseDAO.getFirebase().child("usuarios").child(friendId);
                    firebase.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getValue() != null) {
                                User user = dataSnapshot.getValue(User.class);
                                firebase = FireBaseDAO.getFirebase();
                                Preferences preferences = new Preferences(getApplicationContext());
                                String id = preferences.getUserId().replace("\n","");
                                if (id != null) {
                                    firebase = firebase.child("friends").child(id).child(friendId);

                                    Friend friend = new Friend();
                                    friend.setId(friendId);
                                    friend.setName(user.getName());
                                    friend.setEmail(user.getEmail());

                                    firebase.setValue(friend);
                                } else {
                                    Toast.makeText(MainActivity.this, "This user hasn't an account", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                } else {
                    Toast.makeText(MainActivity.this, "Email is not valid", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        dialog.create();
        dialog.show();
    }
}
