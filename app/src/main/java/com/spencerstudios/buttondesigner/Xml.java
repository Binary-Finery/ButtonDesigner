package com.spencerstudios.buttondesigner;

public class Xml {

    //gradient colors...
    public static String grad_color_start = "";
    public static String grad_center_color = "";
    public static String grad_color_end = "";

    //gradient angle...

    //radius...
    public static String radius_bottom_left = "";
    public static String radius_bottom_right = "";
    public static String radius_top_left = "";
    public static String radius_top_right = "";

    //closing tags...
    static final String closing_tag = "/>\n\n";
    static final String final_closing_tag = "</shape>";

    public static final String DRAWABLE_XML = "res/drawable/button_background.xml";

    public static String header = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
            "<shape xmlns:android=\"http://schemas.android.com/apk/res/android\" \n" +
            "    android:shape=\"rectangle\">\n";

    public static String solidColor = "\t<solid\n\t\tandroid:color=";

    public static String gradientHead = "\t<gradient";



}
