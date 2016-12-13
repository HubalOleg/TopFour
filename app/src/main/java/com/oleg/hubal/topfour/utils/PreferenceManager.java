package com.oleg.hubal.topfour.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by User on 09.12.2016.
 */

public class PreferenceManager {
    private static final String PREFS_NAME = "com.oleg.hubal.topfour.PREFERENCES";

    private static final String KEY_AUTH_TOKEN = "KEY_AUTH_TOKEN";
    private static final String KEY_PLACES_CACHED = "KEY_PLACES_CACHED";
    private static final String KEY_LOCATION = "KEY_LOCATION";

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

    public static boolean isLocationPlacesCached(Context context) {
        mSharedPreferences = getPref(context);
        return mSharedPreferences.getBoolean(KEY_PLACES_CACHED, false);
    }

    public static void setLocationCashed(Context context, boolean isCached) {
        mSharedPreferences = getPref(context);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(KEY_PLACES_CACHED, isCached);
        editor.apply();
    }

    public static String getLocation(Context context) {
        mSharedPreferences = getPref(context);
        return mSharedPreferences.getString(KEY_LOCATION, "");
    }

    public static void setLocation(Context context, String location) {
        mSharedPreferences = getPref(context);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(KEY_LOCATION, location);
        editor.apply();
    }
}
