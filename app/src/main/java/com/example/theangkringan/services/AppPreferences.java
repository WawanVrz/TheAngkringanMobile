package com.example.theangkringan.services;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class AppPreferences {

    String KEY_USER_TOKEN ="access_token";
    String KEY_USER_UNIQUE ="user_unique";
    String PREFS_NAME ="login";

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    public AppPreferences(Context context) {
        preferences = context.getSharedPreferences(PREFS_NAME, context.MODE_PRIVATE);
        editor = preferences.edit();

    }
    public void storeUserToken(Context context, String accessToken){
        editor.putString(KEY_USER_TOKEN, accessToken);
        editor.commit();
    }

    public String getUserToken(Context context){
        return preferences.getString(KEY_USER_TOKEN,"");
    }

    public void storeUserUnique(Context context, String userUnique){
        editor.putString(KEY_USER_UNIQUE, userUnique);
        editor.commit();
    }

    public String getUserUnique(Context context){
        return preferences.getString(KEY_USER_UNIQUE,"");
    }

    public void clearUserToken (Context context){
        editor.putString(KEY_USER_UNIQUE, "");
        editor.putString(KEY_USER_TOKEN, "");
        editor.commit();
    }
}
