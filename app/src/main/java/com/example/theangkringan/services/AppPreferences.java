package com.example.theangkringan.services;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class AppPreferences {

    static final String KEY_USER_TOKEN ="access_token";
    static final String KEY_USER_UNIQUE ="user_unique";
    static final String PREFS_NAME ="login";

    private final SharedPreferences preferences;

    public AppPreferences(Context context) {
        preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }
//    private static SharedPreferences getSharedPreference(Context context){
//        return context.getSharedPreferences(KEY_LOGIN, Context.MODE_PRIVATE);
//    }

    public void storeUserToken(Context context, String accessToken){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(KEY_USER_TOKEN, accessToken);
        editor.apply();
    }

    public String getUserToken(Context context){
        return preferences.getString(KEY_USER_TOKEN,"");
    }

    public void storeUserUnique(Context context, String userUnique){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(KEY_USER_UNIQUE, userUnique);
        editor.apply();
    }

    public String getUserUnique(Context context){
        return preferences.getString(KEY_USER_UNIQUE,"");
    }

    public void clearUserToken (Context context){
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }
}
