package com.spencerstudios.buttondesigner;

public class Xml {

    //gradient colors...
    public static String grad_color_start = "";
    public static String grad_center_color = "";
    public static String grad_color_end = "";

    //gradient angle...
    private static String grad_orientation = "";

    //stroke...
    private static String stroke_width = "";
    private static String stroke_color = "";

    //radius...
    public static String radius_bottom_left = "";
    public static String radius_bottom_right = "";
    public static String radius_top_left = "";
    public static String radius_top_right = "";

    //closing tags...
    static final String closing_tag = " />\n";
    static final String final_closing_tag = "\n</shape>";

    public static String header = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
            "<shape xmlns:android=\"http://schemas.android.com/apk/res/android\" \n" +
            "    android:shape=\"rectangle\">\n";

    public static String solidColor = "\t<solid\n\t\tandroid:color=";

    public static String gradientHead = "\t<gradient";

    public static String startColor = "\t\tandroid:startColor=";
    public static String centerColor = "\t\tandroid:centerColor=";
    public static String endColor = "\t\tandroid:endColor=";
    public static String gradientAngle = "\t\tandroid:angle=";

    public static String stroke = "\n\t<stroke\n";
    public static String strokeWidth = "\n\t\tandroid:width=";
    public static String strokeColor = "\t\tandroid:color=";

    public static String cornerRadius = "<corners\n" +
            "        android:bottomLeftRadius=" + radius_bottom_left + "\n" +
            "        android:bottomRightRadius=" + radius_bottom_right + "\n" +
            "        android:topLeftRadius=" + radius_top_left + "\n" +
            "        android:topRightRadius=" + radius_top_right + closing_tag;
}
