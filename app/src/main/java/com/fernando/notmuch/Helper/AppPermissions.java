package com.fernando.notmuch.Helper;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

public class AppPermissions {

    private static final AppPermissions _instance = new AppPermissions();
    public static AppPermissions getInstance() {
        return _instance;
    }

    public void getPermissions(int code, Activity act, String[] permissionsRequest) {
        for(String permission : permissionsRequest) {
            if(ContextCompat.checkSelfPermission(act, permission) !=
                    PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(act, new String[] {permission}, code);
            }
        }
    }
}
