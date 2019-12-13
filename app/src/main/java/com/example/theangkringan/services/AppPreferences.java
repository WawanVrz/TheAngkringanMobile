package com.example.theangkringan.services;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class AppPreferences {

    static final String KEY_USER_TOKEN ="access_token";

    private static SharedPreferences getSharedPreference(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static void storeUserToken(Context context, String accessToken){
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.putString(KEY_USER_TOKEN, accessToken);
        editor.apply();
    }

    public static String getUserToken(Context context){
        return getSharedPreference(context).getString(KEY_USER_TOKEN,"");
    }

    public static void clearUserToken (Context context){
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.remove(KEY_USER_TOKEN);
        editor.apply();
    }
}
