package com.spencerstudios.buttondesigner;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

class Utils {

    private static SharedPreferences prefs;
    private static SharedPreferences.Editor editor;

    static void setDimensionPrefs(Context context, String KEY, int dimen) {
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        editor = prefs.edit();
        editor.putInt(KEY, dimen).apply();
    }

    static void setColorPrefs(Context context, String KEY, String color) {
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        editor = prefs.edit();
        editor.putString(KEY, color).apply();
    }

    static String getColorPrefs(Context context, String KEY) {
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(KEY, "#626262");
    }

    static int getDimensionPrefs(Context context, String KEY) {
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getInt(KEY, 0);
    }

    static void setBooleanPrefs(Context context, String KEY, boolean b) {
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        editor.putBoolean(KEY, b).apply();
    }

    static boolean getBooleanPrefs(Context context, String KEY) {
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean(KEY, false);
    }
}

