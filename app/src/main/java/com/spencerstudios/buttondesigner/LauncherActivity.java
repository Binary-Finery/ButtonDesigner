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

public class LauncherActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private Button button;
    private SharedPreferences prefs;
    private Context ctx = this;
    private final GradientDrawable.Orientation[] ORIENTATION = {GradientDrawable.Orientation.LEFT_RIGHT, GradientDrawable.Orientation.BL_TR, GradientDrawable.Orientation.BOTTOM_TOP, GradientDrawable.Orientation.BR_TL, GradientDrawable.Orientation.RIGHT_LEFT, GradientDrawable.Orientation.TR_BL, GradientDrawable.Orientation.TOP_BOTTOM};

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

        Configuration configuration = ctx.getResources().getConfiguration();
        Utils.setDimensionPrefs(ctx, "screen_width_in_dp", configuration.screenWidthDp);

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

    private int convertDpToPx(int dp) {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        return (int) ((dp * displayMetrics.density) + .5);
    }

    public void onBackPressed() {
        prefs.unregisterOnSharedPreferenceChangeListener(this);
        finishAffinity();
    }

    private void applyButtonProperties() {

        GradientDrawable drawable = new GradientDrawable();

        //set button corner radii...
        if (Utils.getBooleanPrefs(ctx, "switch_corner")) {
            drawable.setCornerRadii(new float[]{
                    convertDpToPx(Utils.getDimensionPrefs(ctx, "corner_tl")), convertDpToPx(Utils.getDimensionPrefs(ctx, "corner_tl")), convertDpToPx(Utils.getDimensionPrefs(ctx, "corner_tr")), convertDpToPx(Utils.getDimensionPrefs(ctx, "corner_tr")), convertDpToPx(Utils.getDimensionPrefs(ctx, "corner_br")), convertDpToPx(Utils.getDimensionPrefs(ctx, "corner_br")), convertDpToPx(Utils.getDimensionPrefs(ctx, "corner_bl")), convertDpToPx(Utils.getDimensionPrefs(ctx, "corner_bl"))});
        } else {
            int globalCornerRadius = Utils.getDimensionPrefs(ctx, "corner_all");
            drawable.setCornerRadii(new float[]{convertDpToPx(globalCornerRadius), convertDpToPx(globalCornerRadius), convertDpToPx(globalCornerRadius), convertDpToPx(globalCornerRadius), convertDpToPx(globalCornerRadius), convertDpToPx(globalCornerRadius), convertDpToPx(globalCornerRadius), convertDpToPx(globalCornerRadius)});
        }

        //set button border width and color...
        int sw = Utils.getDimensionPrefs(ctx, "stroke_width");
        drawable.setStroke(convertDpToPx(sw), Color.parseColor(Utils.getColorPrefs(ctx, "stroke_color")));

        //set button width and height...
        int w = Utils.getDimensionPrefs(ctx, "size_width"), h = Utils.getDimensionPrefs(ctx, "size_height");
        button.setLayoutParams(new LinearLayout.LayoutParams(Utils.getBooleanPrefs(ctx, "switch_width") ? LinearLayout.LayoutParams.WRAP_CONTENT : convertDpToPx(w), Utils.getBooleanPrefs(ctx, "switch_height") ? LinearLayout.LayoutParams.WRAP_CONTENT : convertDpToPx(h)));

        //set button text...
        button.setAllCaps(Utils.getBooleanPrefs(ctx, "all_caps"));
        button.setText(Utils.getColorPrefs(ctx, "button_text"));

        //set button text size in sp...
        button.setTextSize(TypedValue.COMPLEX_UNIT_SP, Utils.getDimensionPrefs(ctx, "text_size"));

        //set text color..
        button.setTextColor(Color.parseColor(Utils.getColorPrefs(ctx, "text_color")));

        //set typeface
        button.setTypeface(Typeface.create(Utils.getColorPrefs(ctx, "typeface"), Typeface.NORMAL));

        //set button color(s) and gradient type...
        boolean hasGradient = Utils.getBooleanPrefs(ctx, "use_gradient");
        boolean isRadial = Utils.getBooleanPrefs(ctx, "use_radial");
        boolean isThreeColors = Utils.getBooleanPrefs(ctx, "use_three_colors");

        if (hasGradient) {

            if (isRadial) {

                drawable.setGradientType(GradientDrawable.RADIAL_GRADIENT);
                drawable.setGradientCenter((float) Utils.getDimensionPrefs(ctx, "center_x") / 100, (float) Utils.getDimensionPrefs(ctx, "center_y") / 100);
                drawable.setGradientRadius(convertDpToPx(Utils.getDimensionPrefs(ctx, "radius")));

                if (isThreeColors) {
                    int[] colors = {Color.parseColor(Utils.getColorPrefs(ctx, "color1")), Color.parseColor(Utils.getColorPrefs(ctx, "color2")), Color.parseColor(Utils.getColorPrefs(ctx, "color3"))};
                    drawable.setColors(colors);
                } else {
                    int[] colors = {Color.parseColor(Utils.getColorPrefs(ctx, "color1")), Color.parseColor(Utils.getColorPrefs(ctx, "color2"))};
                    drawable.setColors(colors);
                }
            } else {
                drawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
                drawable.setOrientation(ORIENTATION[Utils.getDimensionPrefs(ctx, "angel")]);
                if (isThreeColors) {
                    int[] colors = {Color.parseColor(Utils.getColorPrefs(ctx, "color1")), Color.parseColor(Utils.getColorPrefs(ctx, "color2")), Color.parseColor(Utils.getColorPrefs(ctx, "color3"))};
                    drawable.setColors(colors);
                } else {
                    int[] colors = {Color.parseColor(Utils.getColorPrefs(ctx, "color1")), Color.parseColor(Utils.getColorPrefs(ctx, "color2"))};
                    drawable.setColors(colors);
                }
            }
        } else drawable.setColor(Color.parseColor(Utils.getColorPrefs(ctx, "color1")));
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
            startActivity(new Intent(ctx, GenerateDrawableXMLActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
}
