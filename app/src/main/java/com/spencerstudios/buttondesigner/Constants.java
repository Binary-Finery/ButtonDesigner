package com.spencerstudios.buttondesigner;

import android.content.Context;

class Constants {

    static final String[] FONT_FAMILIES = {
            "normal",
            "cursive",
            "casual",
            "monospace",
            "sans",
            "sans-serif",
            "sans-serif-condensed",
            "sans-serif-light",
            "sans-serif-smallcaps",
            "serif",
            "serif-monospace"
    };

    static void DEFAULTS(Context context) {

        //border...
        Utils.setDimensionPrefs(context, "stroke_width", 2);
        Utils.setColorPrefs(context, "stroke_color", "#323232");
        //corners...
        Utils.setDimensionPrefs(context, "corner_all", 45);
        Utils.setBooleanPrefs(context, "switch_corner", false);
        //size...
        Utils.setBooleanPrefs(context, "switch_width", false);
        Utils.setDimensionPrefs(context, "size_width", 200);
        Utils.setBooleanPrefs(context, "switch_height", true);
        Utils.setDimensionPrefs(context, "size_height", 100);
        //color...
        Utils.setBooleanPrefs(context, "use_gradient", true);
        Utils.setBooleanPrefs(context, "use_radial", false);
        Utils.setBooleanPrefs(context, "use_three_colors", true);
        Utils.setDimensionPrefs(context, "angel", 0);
        Utils.setColorPrefs(context, "color1", "#66BB6A");
        Utils.setColorPrefs(context, "color2", "#FFFFFF");
        Utils.setColorPrefs(context, "color3", "#336489");
        Utils.setDimensionPrefs(context, "center_x", 50);
        Utils.setDimensionPrefs(context, "center_y", 50);
        Utils.setDimensionPrefs(context, "radius", 100);
        //text...
        Utils.setColorPrefs(context, "button_text", "Button");
        Utils.setDimensionPrefs(context, "text_size", 24);
        Utils.setColorPrefs(context, "text_color", "#525252");
        Utils.setBooleanPrefs(context, "all_caps", false);
        Utils.setColorPrefs(context, "typeface", Constants.FONT_FAMILIES[0]);
    }
}
