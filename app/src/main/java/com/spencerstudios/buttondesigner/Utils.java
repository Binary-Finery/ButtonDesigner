package com.spencerstudios.buttondesigner;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

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

    static ArrayList<Model> buttonList(Context context) {

        ArrayList<Model> temp;

        SharedPreferences sp = context.getSharedPreferences("saved_buttons", 0);
        String content = sp.getString("buttons", "");
        Gson gson = new Gson();

        if (content.isEmpty()) temp = new ArrayList<>();
        else {
            Type type = new TypeToken<ArrayList<Model>>() {
            }.getType();
            temp = gson.fromJson(content, type);
        }

        return temp;
    }

    static void saveButton(Context context) {
        ArrayList<Model> buttons = buttonList(context);
        SharedPreferences sp = context.getSharedPreferences("saved_buttons", 0);
        SharedPreferences.Editor editor = sp.edit();

        buttons.add(0, new Model(
                Utils.getDimensionPrefs(context, "stroke_width"),
                Utils.getColorPrefs(context, "stroke_color"),
                Utils.getDimensionPrefs(context, "corner_all"),
                Utils.getDimensionPrefs(context, "corner_tl"),
                Utils.getDimensionPrefs(context, "corner_tr"),
                Utils.getDimensionPrefs(context, "corner_bl"),
                Utils.getDimensionPrefs(context, "corner_br"),
                Utils.getBooleanPrefs(context, "switch_corner"),
                Utils.getBooleanPrefs(context, "switch_width"),
                Utils.getDimensionPrefs(context, "size_width"),
                Utils.getBooleanPrefs(context, "switch_height"),
                Utils.getDimensionPrefs(context, "size_height"),
                Utils.getBooleanPrefs(context, "use_gradient"),
                Utils.getBooleanPrefs(context, "use_radial"),
                Utils.getBooleanPrefs(context, "use_three_colors"),
                Utils.getDimensionPrefs(context, "angel"),
                Utils.getColorPrefs(context, "color1"),
                Utils.getColorPrefs(context, "color2"),
                Utils.getColorPrefs(context, "color3"),
                Utils.getDimensionPrefs(context, "center_x"),
                Utils.getDimensionPrefs(context, "center_y"),
                Utils.getDimensionPrefs(context, "radius"),
                Utils.getColorPrefs(context, "button_text"),
                Utils.getDimensionPrefs(context, "text_size"),
                Utils.getColorPrefs(context, "text_color"),
                Utils.getBooleanPrefs(context, "all_caps"),
                Utils.getColorPrefs(context, "typeface")
        ));

        Gson gson = new Gson();
        String db = gson.toJson(buttons);
        editor.putString("buttons", db).apply();
    }

    static void deleteItem(Context context, int position) {
        ArrayList<Model> temp = buttonList(context);
        SharedPreferences sp = context.getSharedPreferences("saved_buttons", 0);
        SharedPreferences.Editor editor = sp.edit();
        temp.remove(position);
        Gson gson = new Gson();
        String db = gson.toJson(temp);
        editor.putString("buttons", db).apply();
    }
}

