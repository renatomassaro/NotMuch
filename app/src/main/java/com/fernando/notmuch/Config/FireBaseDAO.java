package com.fernando.notmuch.Config;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public final class FireBaseDAO {
    private static DatabaseReference fbreference;
    private static FirebaseAuth firebaseAuth;

    public static DatabaseReference getFirebase() {
        if(fbreference == null)
            fbreference = FirebaseDatabase.getInstance().getReference();
        return fbreference;
    }

    public static FirebaseAuth getFirebaseAuth() {
        if(firebaseAuth == null)
            firebaseAuth = FirebaseAuth.getInstance();
        return firebaseAuth;
    }
}
