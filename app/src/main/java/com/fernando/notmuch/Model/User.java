package com.fernando.notmuch.Model;


import android.support.annotation.NonNull;

import com.fernando.notmuch.Config.FireBaseDAO;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.Exclude;

public class User {

    private String name;
    private String email;
    private String password;
    private String id;

    public User() {
        name = email = password = "";
    }

    public void save() {
        FireBaseDAO.getFirebase().child("usuarios").child(getId()).setValue(this).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {

                }else{
                    try {
                        throw new Exception();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Exclude
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Exclude
    public String getId() {
        return id.replace("\n","");
    }

    public void setId(String id) {
        this.id = id.replace("\n","");
    }
}
