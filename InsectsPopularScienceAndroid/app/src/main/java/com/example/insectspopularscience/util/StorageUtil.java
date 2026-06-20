package com.example.insectspopularscience.util;

import android.content.Context;
import android.content.SharedPreferences;

public class StorageUtil {
    private static final String PREFS_NAME = "insects_app_prefs";
    private static final String KEY_TOKEN = "token";
    private static final String KEY_USER = "user";

    private SharedPreferences prefs;
    private static StorageUtil instance;

    private StorageUtil(Context context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public static StorageUtil getInstance(Context context) {
        if (instance == null) {
            instance = new StorageUtil(context);
        }
        return instance;
    }

    public void setToken(String token) {
        prefs.edit().putString(KEY_TOKEN, token).apply();
    }

    public String getToken() {
        return prefs.getString(KEY_TOKEN, null);
    }

    public void setUser(String userJson) {
        prefs.edit().putString(KEY_USER, userJson).apply();
    }

    public String getUser() {
        return prefs.getString(KEY_USER, null);
    }

    public void clear() {
        prefs.edit().clear().apply();
    }
}

