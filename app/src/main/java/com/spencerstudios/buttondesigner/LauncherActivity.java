package com.spencerstudios.buttondesigner;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import spencerstudios.com.bungeelib.Bungee;

public class LauncherActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private final GradientDrawable.Orientation[] ORIENTATION = Constants.GRADIENT_ORIENTATIONS();
    private Button button;
    private SharedPreferences prefs;
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        button = findViewById(R.id.button_preview);
        ViewPager viewPager = findViewById(R.id.container);
        TabLayout tabLayout = findViewById(R.id.tabs);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.registerOnSharedPreferenceChangeListener(this);

        Configuration configuration = context.getResources().getConfiguration();
        Utils.setDimensionPrefs(context, "screen_width_in_dp", configuration.screenWidthDp);

        if (prefs.getBoolean("initial_launch", true)) {
            Constants.DEFAULTS(this);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("initial_launch", false).apply();
        }

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(sectionsPagerAdapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));

        applyButtonProperties();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        applyButtonProperties();
    }

    private int convertDpToPx(int dp) {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        return (int) ((dp * displayMetrics.density) + .5);
    }

    public void onBackPressed() {
        prefs.unregisterOnSharedPreferenceChangeListener(this);
        finishAffinity();
        Bungee.inAndOut(context);
    }

    private void applyButtonProperties() {

        GradientDrawable drawable = new GradientDrawable();

        //set button corner radii...
        if (Utils.getBooleanPrefs(context, "switch_corner")) {
            drawable.setCornerRadii(new float[]{
                    convertDpToPx(Utils.getDimensionPrefs(context, "corner_tl")), convertDpToPx(Utils.getDimensionPrefs(context, "corner_tl")), convertDpToPx(Utils.getDimensionPrefs(context, "corner_tr")), convertDpToPx(Utils.getDimensionPrefs(context, "corner_tr")), convertDpToPx(Utils.getDimensionPrefs(context, "corner_br")), convertDpToPx(Utils.getDimensionPrefs(context, "corner_br")), convertDpToPx(Utils.getDimensionPrefs(context, "corner_bl")), convertDpToPx(Utils.getDimensionPrefs(context, "corner_bl"))});
        } else {
            int globalCornerRadius = Utils.getDimensionPrefs(context, "corner_all");
            drawable.setCornerRadii(new float[]{convertDpToPx(globalCornerRadius), convertDpToPx(globalCornerRadius), convertDpToPx(globalCornerRadius), convertDpToPx(globalCornerRadius), convertDpToPx(globalCornerRadius), convertDpToPx(globalCornerRadius), convertDpToPx(globalCornerRadius), convertDpToPx(globalCornerRadius)});
        }

        //set button border width and color...
        int sw = Utils.getDimensionPrefs(this, "stroke_width");
        drawable.setStroke(convertDpToPx(sw), Color.parseColor(Utils.getColorPrefs(context, "stroke_color")));

        //set button width and height...
        int w = Utils.getDimensionPrefs(context, "size_width"), h = Utils.getDimensionPrefs(context, "size_height");
        button.setLayoutParams(new LinearLayout.LayoutParams(Utils.getBooleanPrefs(context, "switch_width") ? LinearLayout.LayoutParams.WRAP_CONTENT : convertDpToPx(w), Utils.getBooleanPrefs(context, "switch_height") ? LinearLayout.LayoutParams.WRAP_CONTENT : convertDpToPx(h)));

        //set button text...
        button.setAllCaps(Utils.getBooleanPrefs(context, "all_caps"));
        button.setText(Utils.getColorPrefs(context, "button_text"));

        //set button text size in sp...
        button.setTextSize(TypedValue.COMPLEX_UNIT_SP, Utils.getDimensionPrefs(context, "text_size"));

        //set text color..
        button.setTextColor(Color.parseColor(Utils.getColorPrefs(context, "text_color")));

        //set typeface
        button.setTypeface(Typeface.create(Utils.getColorPrefs(context, "typeface"), Typeface.NORMAL));

        //set button color(s) and gradient type...
        boolean hasGradient = Utils.getBooleanPrefs(context, "use_gradient");
        boolean isRadial = Utils.getBooleanPrefs(context, "use_radial");
        boolean isThreeColors = Utils.getBooleanPrefs(context, "use_three_colors");

        if (hasGradient) {

            if (isRadial) {

                drawable.setGradientType(GradientDrawable.RADIAL_GRADIENT);
                drawable.setGradientCenter((float) Utils.getDimensionPrefs(context, "center_x") / 100, (float) Utils.getDimensionPrefs(context, "center_y") / 100);
                drawable.setGradientRadius(convertDpToPx(Utils.getDimensionPrefs(context, "radius")));

                if (isThreeColors) {
                    int[] colors = {Color.parseColor(Utils.getColorPrefs(context, "color1")), Color.parseColor(Utils.getColorPrefs(context, "color2")), Color.parseColor(Utils.getColorPrefs(context, "color3"))};
                    drawable.setColors(colors);
                } else {
                    int[] colors = {Color.parseColor(Utils.getColorPrefs(context, "color1")), Color.parseColor(Utils.getColorPrefs(context, "color2"))};
                    drawable.setColors(colors);
                }
            } else {
                drawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
                drawable.setOrientation(ORIENTATION[Utils.getDimensionPrefs(context, "angel")]);
                if (isThreeColors) {
                    int[] colors = {Color.parseColor(Utils.getColorPrefs(context, "color1")), Color.parseColor(Utils.getColorPrefs(context, "color2")), Color.parseColor(Utils.getColorPrefs(context, "color3"))};
                    drawable.setColors(colors);
                } else {
                    int[] colors = {Color.parseColor(Utils.getColorPrefs(context, "color1")), Color.parseColor(Utils.getColorPrefs(context, "color2"))};
                    drawable.setColors(colors);
                }
            }
        } else drawable.setColor(Color.parseColor(Utils.getColorPrefs(context, "color1")));
        button.setBackground(drawable);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_done) {
            startActivity(new Intent(context, GenerateDrawableXMLActivity.class));
            Bungee.split(this);
        }
        if (item.getItemId() == R.id.action_save) {
            Utils.saveButton(context);
            Toast.makeText(context, "Button designed saved", Toast.LENGTH_LONG).show();
        }

        if (item.getItemId() == R.id.action_saved_list) {
            startActivity(new Intent(context, SavedDesignsActivity.class));
            Bungee.split(context);
        }
        return super.onOptionsItemSelected(item);
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new FragmentColor();
                case 1:
                    return new FragmentSize();
                case 2:
                    return new FragmentBorder();
                case 3:
                    return new FragmentCorners();
                case 4:
                    return new FragmentText();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 5;
        }
    }

}
