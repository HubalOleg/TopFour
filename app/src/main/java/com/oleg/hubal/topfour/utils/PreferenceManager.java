package com.oleg.hubal.topfour.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by User on 09.12.2016.
 */

public class PreferenceManager {
    private static final String PREFS_NAME = "com.oleg.hubal.topfour.PREFERENCES";
    private static final String KEY_AUTH_TOKEN = "KEY_AUTH_TOKEN";

    private static SharedPreferences mSharedPreferences;

    private static SharedPreferences getPref(Context context) {
        if (mSharedPreferences != null)
            return mSharedPreferences;
        else
            return context.getSharedPreferences(PREFS_NAME, 0);
    }

    public static String getToken(Context context) {
        mSharedPreferences = getPref(context);
        return mSharedPreferences.getString(KEY_AUTH_TOKEN, "");
    }

    public static void setToken(Context context, String token) {
        mSharedPreferences = getPref(context);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(KEY_AUTH_TOKEN, token);
        editor.apply();
    }
}
