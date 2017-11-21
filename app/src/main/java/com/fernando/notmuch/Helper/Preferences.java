package com.fernando.notmuch.Helper;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

public class Preferences {
    private Context context;
    private SharedPreferences preferences;
    private final String FILE_NAME = "notmuch.preferencies";
    private final int MODE = 0; //Just my app can access this preferences
    private final String USER_ID = "userIdLogged";
    private final String USER_NAME = "userNameLogged";
    private SharedPreferences.Editor editor;

    public Preferences(Context context) {
        this.context = context;
        preferences = context.getSharedPreferences(FILE_NAME, MODE);
        editor = preferences.edit();
    }

    public void savePreferences(String userId, String name ) {
        editor.putString(USER_ID, userId);
        editor.putString(USER_NAME, name);
        editor.apply();
    }

    public String getUserId() {
        return preferences.getString(USER_ID, null).replace("\n", "");
    }

    public String getUserName() {
        return preferences.getString(USER_NAME, null);
    }
}
