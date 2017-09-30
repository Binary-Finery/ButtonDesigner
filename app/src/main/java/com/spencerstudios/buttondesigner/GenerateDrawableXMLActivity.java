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

        Intent get = getIntent();

        tv.setText(drawableHighLightingFactory(get.getStringExtra("xml")));
        tvBtn.setText(buttonHighlightingFactory(get.getStringExtra("button")));
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
        if (item.getItemId() == R.id.action_share)
            shareXML();
        else finish();
        return super.onOptionsItemSelected(item);
    }

    private void shareXML() {
        Intent get = getIntent();
        xml = drawableHighLightingFactory(get.getStringExtra("xml")).toString();
        button = buttonHighlightingFactory(get.getStringExtra("button")).toString();
        String body = xml + "\n\n-----------\n\n" + button;
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "button designer xml");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, body);
        startActivity(Intent.createChooser(sharingIntent, "Share via... "));
    }
}
