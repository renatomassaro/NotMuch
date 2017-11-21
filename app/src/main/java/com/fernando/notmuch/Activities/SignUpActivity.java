package com.fernando.notmuch.Activities;

import android.content.Intent;
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
import com.fernando.notmuch.Model.User;
import com.fernando.notmuch.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class SignUpActivity extends AppCompatActivity {

    private EditText name;
    private EditText email;
    private EditText password;
    private TextView cancel;
    private Button create;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        name = findViewById(R.id.signup_name);
        email = findViewById(R.id.signup_email);
        password = findViewById(R.id.signup_password);
        create = findViewById(R.id.signup_create);
        cancel = findViewById(R.id.signup_cancel);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { finish(); }});

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Verify data

                user = new User();
                user.setName(name.getText().toString());
                user.setEmail(email.getText().toString());
                user.setPassword(password.getText().toString());

                register();
            }
        });
    }

    private void register() {
        FireBaseDAO.getFirebaseAuth().createUserWithEmailAndPassword(user.getEmail(),
                user.getPassword()).addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if( task.isSuccessful()) {
                    user.setId(Base64Custom.codeBase64(user.getEmail()));
                    user.save();

                    //FireBaseDAO.getFirebaseAuth().signOut();
                    finish();
                } else {
                    String message = "Registration has failed";
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthWeakPasswordException e) {
                        message = "Password is too weak";
                    } catch( FirebaseAuthInvalidCredentialsException e) {
                        message = "This e-mail is not valid";
                    } catch(FirebaseAuthUserCollisionException e) {
                        message = "This user already exists";
                    } catch(Exception e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
