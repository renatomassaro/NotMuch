package com.fernando.notmuch.Activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fernando.notmuch.Config.FireBaseDAO;
import com.fernando.notmuch.Helper.Base64Custom;
import com.fernando.notmuch.Helper.Preferences;
import com.fernando.notmuch.Model.Chat;
import com.fernando.notmuch.Model.User;
import com.fernando.notmuch.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    private EditText email;
    private EditText passwword;
    private Button signIn;
    private TextView signUp;
    private boolean askAgain;
    private String myName;
    private String userID;
    private ValueEventListener valueEventListener;
    private DatabaseReference firebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        isLogged();

        askAgain = true;
        email = findViewById(R.id.login_text_email);
        passwword = findViewById(R.id.login_text_password);
        signIn = findViewById(R.id.login_button_signin);
        signUp = findViewById(R.id.login_textView_signin);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Check if the email and password are right and go to the validation activity*/
                String pass =passwword.getText().toString();
                String emailText = email.getText().toString();

                String pattern = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
                Pattern p = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
                Matcher m = p.matcher(emailText);
                if(m.find() && !pass.matches("")){
                    //Make the login
                    User user = new User();
                    user.setEmail(emailText);
                    userID = Base64Custom.codeBase64(emailText);
                    firebase = FireBaseDAO.getFirebase()
                                           .child("usuarios")
                                           .child(userID);

                    valueEventListener = new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            User u = dataSnapshot.getValue(User.class);

                            Preferences preferences = new Preferences(LoginActivity.this);
                            preferences.savePreferences(userID, u.getName());

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    };

                    firebase.addListenerForSingleValueEvent(valueEventListener);

                    user.setPassword(passwword.getText().toString());
                    executeLogin(user);
                } else {
                    //Wrong email
                    Toast.makeText(LoginActivity.this, "E-mail is not valid", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int code, String[] perm, int[] result) {
        super.onRequestPermissionsResult(code, perm, result);
        for(int r : result) {
            if( r == PackageManager.PERMISSION_DENIED) {
                askAgain = false;
                return;
            }
        }
        askAgain = true;
    }

    private void isLogged() {
        if(FireBaseDAO.getFirebaseAuth().getCurrentUser() != null) {
            startApp();
        }
    }

    private void executeLogin(final User user) {
        FireBaseDAO.getFirebaseAuth().signInWithEmailAndPassword(
                user.getEmail(),
                user.getPassword()
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    //Preferences preferences = new Preferences(getApplicationContext());
                    //preferences.savePreferences(Base64Custom.codeBase64(user.getEmail()), user.getName());
                    startApp();
                } else {
                    Toast.makeText(LoginActivity.this,"Wrong e-mail or password", Toast.LENGTH_SHORT);
                }
            }
        });
    }

    private void startApp() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
