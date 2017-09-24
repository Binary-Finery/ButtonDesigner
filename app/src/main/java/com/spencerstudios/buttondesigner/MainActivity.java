package com.spencerstudios.buttondesigner;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TableRow;

import com.pes.androidmaterialcolorpickerdialog.ColorPicker;
import com.pes.androidmaterialcolorpickerdialog.ColorPickerCallback;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;
import static android.view.View.OnClickListener;
import static android.view.View.VISIBLE;

public class MainActivity extends AppCompatActivity implements OnClickListener, TextWatcher, CompoundButton.OnCheckedChangeListener {

    private DisplayMetrics displayMetrics;

    private int select = 0;
    private TableRow rowOrien, rowCX, rowCY, rowIncludeCenterColor, rowRadius;
    private CheckBox cbCenterColor;
    private Switch switchGradient, switchRadial, switchCorners;
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

    private int[] tabIDs = {R.id.tab1, R.id.tab2, R.id.tab3, R.id.tab4}, fabIDs = {R.id.f1, R.id.f2, R.id.f3, R.id.fab_txt_color, R.id.fab_border_color};
    private Button[] tabs = new Button[tabIDs.length];

    private Button preview;
    private FloatingActionButton[] fabs = new FloatingActionButton[fabIDs.length];
    private String[] fc = {"#F44336", "#4CAF50", "#3F51B5", "#FFFFFF", "#e91e63"};

    private int[] layoutIDs = {R.id.color_layout, R.id.border_layout, R.id.corners_layout, R.id.size_layout};
    private LinearLayout[] linearLayouts = new LinearLayout[layoutIDs.length];

    private EditText btnWidth, btnHeight;

    private int[] cornerIDs = {R.id.et_all_corners, R.id.et_top_left, R.id.et_top_right, R.id.et_bottom_left, R.id.et_bottom_right};
    private EditText[] etCorners = new EditText[cornerIDs.length];

    private int[] switchIDs = {R.id.switch_corners, R.id.switch_height_wrap, R.id.switch_width_wrap};
    private Switch[] switches = new Switch[switchIDs.length];

    private EditText etBtnText, etTextSize;

    private int tl, tr, bl, br;

    private LinearLayout previewBox;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();

        angleList = new ArrayList<>();

        previewBox = (LinearLayout) findViewById(R.id.preview_container);

        for (int i = 0; i < linearLayouts.length; i++) {
            linearLayouts[i] = (LinearLayout) findViewById(layoutIDs[i]);
        }

        linearLayouts[1].setVisibility(GONE);
        linearLayouts[2].setVisibility(GONE);
        linearLayouts[3].setVisibility(GONE);

        for (int i = 0; i < tabs.length; i++) {
            tabs[i] = (Button) findViewById(tabIDs[i]);
            tabs[i].setOnClickListener(this);
        }
        for (int i = 0; i < fabs.length; i++) {
            fabs[i] = (FloatingActionButton) findViewById(fabIDs[i]);
            fabs[i].setOnClickListener(this);
            fabs[i].setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(fc[i])));
        }

        for (int i = 0; i < etCorners.length; i++) {
            etCorners[i] = (EditText) findViewById(cornerIDs[i]);
            etCorners[i].addTextChangedListener(this);
        }

        for (int i = 0; i < switches.length; i++) {
            switches[i] = (Switch) findViewById(switchIDs[i]);
            switches[i].setOnCheckedChangeListener(this);
        }

        int x = 0;
        for (int i = 0; i < 7; i++) {
            angleList.add(x + " degrees");
            x += 45;
        }

        btnHeight = (EditText) findViewById(R.id.et_btn_height);
        btnWidth = (EditText) findViewById(R.id.et_btn_width);

        btnHeight.addTextChangedListener(this);
        btnWidth.addTextChangedListener(this);

        etBtnText = (EditText) findViewById(R.id.et_btn_txt);
        etTextSize = (EditText) findViewById(R.id.et_btn_text_size);

        etTextSize.addTextChangedListener(this);
        etBtnText.addTextChangedListener(this);

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

                if (select != 3) {
                    fabs[select].setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(s)));
                    fc[select] = s;
                    applySettings();
                } else if (select == 3) {
                    fabs[select].setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(s)));
                    preview.setTextColor(Color.parseColor(s));
                } else if (select == 4) {
                    fabs[select].setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(s)));
                    fc[select] = s;
                    applySettings();
                }
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

        preview.setTextSize(TypedValue.COMPLEX_UNIT_SP, 40);

        tabs[0].setBackgroundResource(R.drawable.new_select);
        tabs[0].setTextColor(Color.WHITE);
        setTabs(1, 2, 3);
        changeTab(tabSelected, 0);
    }


    @Override
    public void onClick(View v) {

        if (v == tabs[0]) {
            tabs[0].setBackgroundResource(R.drawable.new_select);
            tabs[0].setTextColor(Color.WHITE);
            setTabs(1, 2, 3);
            changeTab(tabSelected, 0);
            tabSelected = 0;
        }
        if (v == tabs[1]) {
            tabs[1].setBackgroundResource(R.drawable.new_select);
            tabs[1].setTextColor(Color.WHITE);
            setTabs(0, 2, 3);
            changeTab(tabSelected, 1);
            tabSelected = 1;

        }
        if (v == tabs[2]) {
            tabs[2].setBackgroundResource(R.drawable.new_select);
            tabs[2].setTextColor(Color.WHITE);
            setTabs(0, 1, 3);
            changeTab(tabSelected, 2);

            tabSelected = 2;
        }
        if (v == tabs[3]) {
            tabs[3].setBackgroundResource(R.drawable.new_select);
            tabs[3].setTextColor(Color.WHITE);
            setTabs(0, 1, 2);
            changeTab(tabSelected, 3);
            tabSelected = 3;
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
        if (v == fabs[3]) {
            select = 3;
            cp.show();
        }
        if (v == fabs[4]) {
            select = 4;
            cp.show();
        }
    }

    private void setTabs(int... i) {
        for (int x : i) {
            tabs[x].setBackgroundColor(Color.WHITE);
            tabs[x].setTextColor(Color.BLACK);
        }
    }

    private void changeTab(int current, int next) {
        linearLayouts[current].setVisibility(GONE);
        linearLayouts[next].setVisibility(VISIBLE);
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

        if (switches[0].isChecked()) {

            tl = validateEdits(etCorners[1]);
            tr = validateEdits(etCorners[2]);
            bl = validateEdits(etCorners[3]);
            br = validateEdits(etCorners[4]);
        } else {
            int x = validateEdits(etCorners[0]);
            tl = x;
            tr = x;
            bl = x;
            br = x;
        }

        shape.setCornerRadii(new float[]{
                convertDpToPx(tl), //top left
                convertDpToPx(tl), //top left
                convertDpToPx(tr), //top right
                convertDpToPx(tr), //top right
                convertDpToPx(br), //bottom right
                convertDpToPx(br), //bottom right
                convertDpToPx(bl), //bottom left
                convertDpToPx(bl)  //bottom left
        });

        shape.setStroke(convertDpToPx(3), Color.parseColor(fc[4]));

        preview.setBackgroundDrawable(shape);
    }

    private int convertDpToPx(int dp) {
        displayMetrics = getResources().getDisplayMetrics();
        return (int) ((dp * displayMetrics.density) + .5);
    }

    private int pxToDp(int px) {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

        if (tabSelected != 3) applySettings();
        else setButtonSize();
    }

    @Override
    public void afterTextChanged(Editable s) {
    }

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

    private void setButtonSize() {

        int iw = validateEdits(btnWidth);
        int ih = validateEdits(btnHeight);

        preview.setLayoutParams(new LinearLayout.LayoutParams(switches[1].isChecked() ? LinearLayout.LayoutParams.WRAP_CONTENT : convertDpToPx(ih), switches[2].isChecked() ? LinearLayout.LayoutParams.WRAP_CONTENT : convertDpToPx(iw)));
        String txt = etBtnText.getText().toString();
        preview.setText(txt);

        String ts = etTextSize.getText().toString();
        int its = 0;
        if (TextUtils.isEmpty(ts)) its = 14;
        else its = Integer.parseInt(ts);
        preview.setTextSize(TypedValue.COMPLEX_UNIT_SP, its);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int id = buttonView.getId();

        if (id == R.id.switch_corners) {
            applySettings();
        } else if (id == R.id.switch_height_wrap) {
            setButtonSize();
        } else if (id == R.id.switch_width_wrap) {
            setButtonSize();
        }
    }

    private int validateEdits(EditText et) {
        String s = et.getText().toString();
        if (TextUtils.isEmpty(s))
            s = "0";
        return Integer.parseInt(s);
    }
}



