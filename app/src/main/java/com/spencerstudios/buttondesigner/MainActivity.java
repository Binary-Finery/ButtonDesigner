package com.spencerstudios.buttondesigner;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.pes.androidmaterialcolorpickerdialog.ColorPicker;
import com.pes.androidmaterialcolorpickerdialog.ColorPickerCallback;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;
import static android.view.View.OnClickListener;
import static android.view.View.VISIBLE;

public class MainActivity extends AppCompatActivity implements OnClickListener, TextWatcher {

    private DisplayMetrics displayMetrics;

    private int select = 0;
    private TableRow rowOrien, rowCX, rowCY, rowIncludeCenterColor, rowRadius;
    private CheckBox cbCenterColor;
    private Switch switchGradient, switchRadial;
    private ColorPicker cp;


    private boolean twoColorsSelected = false, singleColor = false, triColors = false, hasBorder = true, isVertical = false;

    private List<String> dpList;

    private CheckBox cbBorder;

    private final GradientDrawable.Orientation[] ORIENTATION = {GradientDrawable.Orientation.LEFT_RIGHT, GradientDrawable.Orientation.BL_TR, GradientDrawable.Orientation.BOTTOM_TOP, GradientDrawable.Orientation.BR_TL, GradientDrawable.Orientation.RIGHT_LEFT, GradientDrawable.Orientation.TR_BL, GradientDrawable.Orientation.TOP_BOTTOM};

    private Spinner angleSpinner;

    private boolean useGradient = true, linearSelected = true, hasCenterColor = true;

    private EditText etCx, etCy, etRad;

    private GradientDrawable shape;

    private ArrayList<String> angleList;

    private int angleSelected = 1, tabSelected = 0;

    private int[] tabIDs = {R.id.tab1, R.id.tab2, R.id.tab3, R.id.tab4, R.id.tab5}, fabIDs = {R.id.f1, R.id.f2, R.id.f3};
    private Button[] tabs = new Button[tabIDs.length];

    private Button preview;
    private FloatingActionButton[] fabs = new FloatingActionButton[fabIDs.length];
    private String[] fc = {"#F44336", "#4CAF50", "#3F51B5"};

    private int [] tableIDs = {R.id.table_color, R.id.table_size, R.id.border_layout};
    private LinearLayout [] tableLayouts = new LinearLayout[tableIDs.length];

    private EditText btnWidth, btnHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();

        angleList = new ArrayList<>();

        for (int i = 0 ; i  <  tableLayouts.length ; i ++){
            tableLayouts[i] = (LinearLayout) findViewById(tableIDs[i]);
        }

        tableLayouts[1].setVisibility(GONE);
        tableLayouts[2].setVisibility(GONE);

        for (int i = 0; i < tabs.length; i++) {
            tabs[i] = (Button) findViewById(tabIDs[i]);
            tabs[i].setOnClickListener(this);
        }
        for (int i = 0; i < fabs.length; i++) {
            fabs[i] = (FloatingActionButton) findViewById(fabIDs[i]);
            fabs[i].setOnClickListener(this);
            fabs[i].setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(fc[i])));
        }
        int x = 0;
        for (int i = 0; i < 7; i++) {
            angleList.add(x + " degrees");
            x += 45;
        }
        btnHeight = (EditText)findViewById(R.id.et_btn_height);
        btnWidth = (EditText)findViewById(R.id.et_btn_width);

        btnHeight.addTextChangedListener(this);
        btnWidth.addTextChangedListener(this);

        angleSpinner = (Spinner) findViewById(R.id.spinner_angle);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item, angleList);
        angleSpinner.setAdapter(adapter);
        angleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                angleSelected = position;
                applySettings();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        shape = new GradientDrawable();

        shape.setShape(GradientDrawable.RECTANGLE);

        rowCX = (TableRow) findViewById(R.id.row_center_x);
        rowCY = (TableRow) findViewById(R.id.row_center_y);
        rowOrien = (TableRow) findViewById(R.id.row_orientation);
        rowIncludeCenterColor = (TableRow) findViewById(R.id.row_include_center);
        rowRadius = (TableRow) findViewById(R.id.row_radius);

        preview = (Button) findViewById(R.id.btn_preview);
        etCx = (EditText) findViewById(R.id.et_cx);
        etCy = (EditText) findViewById(R.id.et_cy);
        etRad = (EditText) findViewById(R.id.et_rad);

        cbCenterColor = (CheckBox) findViewById(R.id.cb_include_center);
        switchGradient = (Switch) findViewById(R.id.switch_gradient);
        switchRadial = (Switch) findViewById(R.id.switch_type);
        cbCenterColor.setChecked(hasCenterColor);

        cbCenterColor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                hasCenterColor = isChecked;
                if (useGradient) fabs[2].setVisibility(isChecked ? View.VISIBLE : View.GONE);
                applySettings();
            }
        });

        cp = new ColorPicker(MainActivity.this, 0, 0, 0);
        cp.setCallback(new ColorPickerCallback() {
            @Override
            public void onColorChosen(@ColorInt int color) {
                String s = String.format("#%06X", (0xFFFFFF & color));
                fabs[select].setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(s)));
                fc[select] = s;
                applySettings();
                cp.dismiss();
            }
        });

        switchGradient.setChecked(useGradient);

        switchGradient.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                useGradient = isChecked;
                displayMechanics();
                applySettings();
            }
        });

        switchRadial.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                linearSelected = isChecked;
                displayMechanics();
                applySettings();
            }
        });

        etCx.addTextChangedListener(this);
        etCy.addTextChangedListener(this);
        etRad.addTextChangedListener(this);

        displayMechanics();
        applySettings();
    }


    @Override
    public void onClick(View v) {

        if (v == tabs[0]) {
            tabs[0].setBackgroundResource(R.drawable.new_select);
            tabs[0].setTextColor(Color.WHITE);
            setTabs(1, 2, 3, 4);
            changeTab(tabSelected, 0);
            tabSelected  = 0;
        }
        if (v == tabs[1]) {
            tabs[1].setBackgroundResource(R.drawable.new_select);
            tabs[1].setTextColor(Color.WHITE);
            setTabs(0, 2, 3, 4);
            changeTab(tabSelected, 1);
            tabSelected = 1;

        }
        if (v == tabs[2]) {
            tabs[2].setBackgroundResource(R.drawable.new_select);
            tabs[2].setTextColor(Color.WHITE);
            setTabs(0, 1, 3, 4);
            changeTab(tabSelected, 2);

            tabSelected = 2;
        }
        if (v == tabs[3]) {
            tabs[3].setBackgroundResource(R.drawable.new_select);
            tabs[3].setTextColor(Color.RED);
            setTabs(0, 1, 2, 4);
            tabSelected = 3;
        }
        if (v == tabs[4]) {
            tabs[4].setBackgroundResource(R.drawable.new_select);
            tabs[4].setTextColor(Color.parseColor("#E91E63"));
            setTabs(0, 1, 2, 3);
            tabSelected = 4;
        }

        if (v == fabs[0]) {
            select = 0;
            cp.show();
        }
        if (v == fabs[1]) {
            select = 1;
            cp.show();
        }
        if (v == fabs[2]) {
            select = 2;
            cp.show();
        }
    }

    private void setTabs(int... i) {
        for (int x : i) {
            tabs[x].setBackgroundColor(Color.WHITE);
            tabs[x].setTextColor(Color.BLACK);
        }
    }

    private void changeTab(int current, int next){
        tableLayouts[current].setVisibility(GONE);
        tableLayouts[next].setVisibility(VISIBLE);
    }

    private void displayMechanics() {
        if (useGradient) {

            vis(rowIncludeCenterColor, 1);
            vis(rowRadius, 1);

            if (linearSelected) {
                vis(rowCX, 0);
                vis(rowCY, 0);
                vis(rowOrien, 1);
                vis(rowRadius, 0);
            } else {
                vis(rowCX, 1);
                vis(rowCY, 1);
                vis(rowOrien, 0);
                vis(rowRadius, 1);
            }
        } else {
            vis(rowOrien, 0);
            vis(rowCX, 0);
            vis(rowCY, 0);
            vis(rowIncludeCenterColor, 0);
            vis(rowRadius, 0);
        }
    }

    private void vis(TableRow tr, int v) {
        boolean visible = v == 1;
        tr.setVisibility(visible ? VISIBLE : GONE);
    }

    private void applySettings() {

        shape = new GradientDrawable();

        if (useGradient && !linearSelected) {

            shape.setGradientType(GradientDrawable.RADIAL_GRADIENT);

            String sx = etCx.getText().toString(), sy = etCy.getText().toString(), sr = etRad.getText().toString();
            if (sx.length() < 1) sx = "0";
            if (sy.length() < 1) sy = "0";
            if (sr.length() < 1) sr = "0";

            float fx = Float.parseFloat(sx) / 100;
            float fy = Float.parseFloat(sy) / 100;
            int ir = Integer.parseInt(sr);

            shape.setGradientCenter(fx, fy);
            shape.setGradientRadius(convertDpToPx(ir));

            setButtonColors();

        } else if (useGradient && linearSelected) {
            shape.setGradientType(GradientDrawable.LINEAR_GRADIENT);
            shape.setOrientation(ORIENTATION[angleSelected]);
            setButtonColors();

        } else if (!useGradient) {
            shape.setColor(Color.parseColor(fc[0]));
        }

        shape.setCornerRadii(new float[]{
                convertDpToPx(14), //top left
                convertDpToPx(14), //top left
                convertDpToPx(14), //top right
                convertDpToPx(14), //top right
                convertDpToPx(14), //bottom lef
                convertDpToPx(14), //bottom left
                convertDpToPx(14), //bottom right
                convertDpToPx(14)  //bottom right
        });

        shape.setStroke(convertDpToPx(3), Color.parseColor("#929292"));

        preview.setBackgroundDrawable(shape);
    }

    private int convertDpToPx(int dp) {
        displayMetrics = getResources().getDisplayMetrics();
        return (int) ((dp * displayMetrics.density) + .5);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (tabSelected == 0){
            applySettings();
        }else if (tabSelected == 1){
            setButtonSize();
        }

    }
    @Override
    public void afterTextChanged(Editable s) {}

    private void setButtonColors() {
        if (hasCenterColor) {
            int[] c = {Color.parseColor(fc[0]), Color.parseColor(fc[1]), Color.parseColor(fc[2])};
            shape.setColors(c);
        } else {
            int[] c = {Color.parseColor(fc[0]), Color.parseColor(fc[1])};
            shape.setColors(c);
        }
    }

    private void findViews() {
    }

    private void constructXml() {

        char q = '"';
        StringBuilder builder = new StringBuilder();

        builder.append(Xml.header.concat("\n"));
        if (singleColor) {

            //builder.append(Xml.solidColor.concat(c).concat(Xml.closing_tag));
        } else {
            builder.append(Xml.gradientHead);
            if (twoColorsSelected) {

                //builder.append("\n".concat(Xml.startColor.concat(s).concat("\n").concat(Xml.endColor).concat(e)));
            } else {
                //builder.append("\n".concat(Xml.startColor.concat(s).concat("\n").concat(Xml.centerColor.concat(c).concat("\n").concat(Xml.endColor.concat(e)))));
            }

        }

        if (hasBorder) {
        }

        builder.append(Xml.final_closing_tag);

        //Intent intent = new Intent(MainActivity.this, GenerateDrawableXMLActivity.class);
        //intent.putExtra("xml", builder.toString());
        //startActivity(intent);
    }

    private void setButtonSize(){
        String w, h;

        w = btnWidth.getText().toString();
        h = btnHeight.getText().toString();

        if (w.length() < 1) w = "0";
        if (h.length() < 1) h = "0";

        int iw = Integer.parseInt(w);
        int ih = Integer.parseInt(h);

        preview.setLayoutParams(new LinearLayout.LayoutParams(
                convertDpToPx(iw), convertDpToPx(ih)));
    }
}



