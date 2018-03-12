package com.spencerstudios.buttondesigner;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GenerateDrawableXMLActivity extends AppCompatActivity {

    private String[] colors = {"#673ab7", "#673ab7", "#E91E63", "#E91E63", "#E91E63", "#E91E63", "#4caf50"};
    private String[] format = {"(\"([^\"]*)\")", "\"", "gradient\n", "stroke\n", "corners\n", "solid\n", "\t\tandroid"};
    private Pattern[] patterns = new Pattern[format.length];
    private Matcher[] matchers = new Matcher[format.length];
    private String xml, button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_drawable_xml);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        TextView tv = (TextView) findViewById(R.id.tv_xml);
        TextView tvBtn = (TextView) findViewById(R.id.tv_btn);

        tv.setText(drawableHighLightingFactory(drawableXml()));
        tvBtn.setText(buttonHighlightingFactory(buttonXml()));
    }

    private SpannableStringBuilder drawableHighLightingFactory(String in) {
        SpannableStringBuilder sb = new SpannableStringBuilder(in);
        for (int i = 0; i < format.length; i++) {
            patterns[i] = Pattern.compile(format[i], Pattern.CASE_INSENSITIVE);
            matchers[i] = patterns[i].matcher(in);
            while (matchers[i].find())
                sb.setSpan(new ForegroundColorSpan(Color.parseColor(colors[i])), matchers[i].start(), matchers[i].end(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        }
        int sst = in.indexOf("<shape");
        int sen = in.indexOf("</shape>");
        int xm = in.indexOf("<?xml");
        sb.setSpan(new ForegroundColorSpan(Color.parseColor("#E91E63")), sst + 1, sst + 6, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        sb.setSpan(new ForegroundColorSpan(Color.parseColor("#E91E63")), sen + 2, sen + 7, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        sb.setSpan(new ForegroundColorSpan(Color.parseColor("#E91E63")), xm + 2, xm + 5, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        return sb;
    }

    private SpannableStringBuilder buttonHighlightingFactory(String in) {
        SpannableStringBuilder sb = new SpannableStringBuilder(in);
        int head = in.indexOf("<Button");
        sb.setSpan(new ForegroundColorSpan(Color.parseColor("#E91E63")), head + 1, head + 7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        Pattern p1 = Pattern.compile("\t\tandroid:");
        Matcher m1 = p1.matcher(in);
        Pattern p2 = Pattern.compile("(\"([^\"]*)\")");
        Matcher m2 = p2.matcher(in);
        Pattern p3 = Pattern.compile("\"");
        Matcher m3 = p3.matcher(in);
        while (m1.find())
            sb.setSpan(new ForegroundColorSpan(Color.parseColor("#4caf50")), m1.start(), m1.end() - 1, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        while (m2.find())
            sb.setSpan(new ForegroundColorSpan(Color.parseColor("#673ab7")), m2.start(), m2.end(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        while (m3.find())
            sb.setSpan(new ForegroundColorSpan(Color.parseColor("#673ab7")), m3.start(), m3.end(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        return sb;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.share_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_share) shareXML();
        else finish();
        return super.onOptionsItemSelected(item);
    }

    private void shareXML() {
        xml = String.valueOf(drawableHighLightingFactory(drawableXml()));
        button = String.valueOf(buttonHighlightingFactory(buttonXml()));
        String body = xml + "\n\n-----------\n\n" + button;
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "button designer xml");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, body);
        startActivity(Intent.createChooser(sharingIntent, "Share via... "));
    }

    private String drawableXml() {

        StringBuilder builder = new StringBuilder();

        builder.append(Xml.header.concat("\n"));

        if (!Utils.getBooleanPrefs(this, "use_gradient")) {
            builder.append("\t<solid\n\t\tandroid:color=\"" + Utils.getColorPrefs(this, "color1") + "\"" + Xml.closing_tag);
        } else {
            builder.append("\t<gradient");

            if (Utils.getBooleanPrefs(this, "use_three_colors")) {
                builder.append("\n\t\tandroid:startColor=\"" + Utils.getColorPrefs(this, "color1") + "\"\n");
                builder.append("\t\tandroid:centerColor=\"" + Utils.getColorPrefs(this, "color2") + "\"\n");
                builder.append("\t\tandroid:endColor=\"" + Utils.getColorPrefs(this, "color3") + "\"\n");
            } else {
                builder.append("\n\t\tandroid:startColor=\"" + Utils.getColorPrefs(this, "color1") + "\"\n");
                builder.append("\t\tandroid:endColor=\"" + Utils.getColorPrefs(this, "color2") + "\"\n");
            }

            if (!Utils.getBooleanPrefs(this, "use_radial")) {
                builder.append("\t\tandroid:type=\"linear\"\n");
                int angel = Utils.getDimensionPrefs(this, "angel") * 45;
                builder.append("\t\tandroid:angle=\"" + angel + "\"" + Xml.closing_tag);
            } else {
                builder.append("\t\tandroid:type=\"radial\"\n");
                String scx = String.valueOf(Utils.getDimensionPrefs(this, "center_x")).concat("%");
                String scy = String.valueOf(Utils.getDimensionPrefs(this, "center_y")).concat("%");
                String srad = String.valueOf(Utils.getDimensionPrefs(this, "radius")).concat("dp");

                builder.append("\t\tandroid:centerX=\"" + scx + "\"\n");
                builder.append("\t\tandroid:centerY=\"" + scy + "\"\n");
                builder.append("\t\tandroid:gradientRadius=\"" + srad + "\"" + Xml.closing_tag);
            }
        }

        int borderWidth = Utils.getDimensionPrefs(this, "stroke_width");
        if (borderWidth > 0) {
            builder.append("\t<stroke\n");
            String sw = String.valueOf(borderWidth).concat("dp");
            builder.append("\t\tandroid:width=\"" + sw + "\"\n");
            builder.append("\t\tandroid:color=\"" + Utils.getColorPrefs(this, "stroke_color") + "\"" + Xml.closing_tag);
        }

        if (Utils.getBooleanPrefs(this, "switch_corner")) {

            builder.append("\t<corners\n");
            builder.append("\t\tandroid:topLeftRadius=\"" + Utils.getDimensionPrefs(this, "corner_tl") + "dp\"\n");
            builder.append("\t\tandroid:topRightRadius=\"" + Utils.getDimensionPrefs(this, "corner_tr") + "dp\"\n");
            builder.append("\t\tandroid:bottomLeftRadius=\"" + Utils.getDimensionPrefs(this, "corner_bl") + "dp\"\n");
            builder.append("\t\tandroid:bottomRightRadius=\"" + Utils.getDimensionPrefs(this, "corner_br") + "dp\"" + Xml.closing_tag);

        } else {
            if (Utils.getDimensionPrefs(this, "corner_all") > 0) {
                String ac = String.valueOf(Utils.getDimensionPrefs(this, "corner_all")).concat("dp\"");
                builder.append("\t<corners\n\t\tandroid:radius=\"" + ac + Xml.closing_tag);

            }
        }
        builder.append(Xml.final_closing_tag);

        return builder.toString();
    }

    private String buttonXml() {
        StringBuilder btnBuild = new StringBuilder();

        btnBuild.append("\t<Button\n\t\tandroid:id=\"@+id/button\"\n");
        if (Utils.getBooleanPrefs(this, "switch_width")) {
            btnBuild.append("\t\tandroid:layout_width=\"wrap_content\"\n");
        } else {
            btnBuild.append("\t\tandroid:layout_width=\"" + Utils.getDimensionPrefs(this, "size_width") + "dp\"\n");
        }
        if (Utils.getBooleanPrefs(this, "switch_height")) {
            btnBuild.append("\t\tandroid:layout_height=\"wrap_content\"\n");
        } else {
            btnBuild.append("\t\tandroid:layout_height=\"" + Utils.getDimensionPrefs(this, "size_height") + "dp\"\n");
        }
        btnBuild.append("\t\tandroid:background=\"@drawable/button_background\"\n");
        if (Utils.getColorPrefs(this, "button_text").length() > 0) {
            btnBuild.append("\t\tandroid:text=\"" + Utils.getColorPrefs(this, "button_text") + "\"\n");
        }
        if (Utils.getDimensionPrefs(this, "text_size") > 0) {
            btnBuild.append("\t\tandroid:textSize=\"" + Utils.getDimensionPrefs(this, "text_size") + "sp\"\n");
        }
        if (!Utils.getBooleanPrefs(this, "all_caps")) {
            btnBuild.append("\t\tandroid:textAllCaps=\"false\"\n");
        }
        if (!Utils.getColorPrefs(this, "typeface").equals(Constants.FONT_FAMILIES[0])) {
            String tf = Utils.getColorPrefs(this, "typeface");
            btnBuild.append("\t\tandroid:fontFamily=\"" + tf + "\"\n");
        }

        btnBuild.append("\t\tandroid:textColor=\"" + Utils.getColorPrefs(this, "text_color") + "\"" + Xml.closing_tag);

        return btnBuild.toString();
    }
}
