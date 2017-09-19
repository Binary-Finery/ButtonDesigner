package com.spencerstudios.buttondesigner;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.FloatRange;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.pes.androidmaterialcolorpickerdialog.ColorPicker;
import com.pes.androidmaterialcolorpickerdialog.ColorPickerCallback;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import static android.view.View.GONE;
import static android.view.View.OnClickListener;
import static android.view.View.VISIBLE;

public class MainActivity extends AppCompatActivity implements OnClickListener, TextWatcher {

    private Button btnPreview;
    private Button startColor, centerColor, endColor, borderColor, genXml;
    private TextView tvSolid, tvGrad, tvLinear, tvRadial, tvHor, tvVert;

    private DisplayMetrics displayMetrics;

    private int select = 1;
    private TableRow rowOrien, rowCen, rowEnd, rowType, rowCX, rowCY, rowIncludeCenterColor, rowRadius;
    private CheckBox cbCenterColor;
    private Switch switchGradient, switchRadial;
    private ColorPicker cp;


    private boolean twoColorsSelected = false, singleColor = false, triColors = false, hasBorder = true, isVertical = false;

    private boolean solidColor = false;

    private List<String> dpList;

    private CheckBox cbBorder;

    private final GradientDrawable.Orientation[] ORIENTATION = {
            GradientDrawable.Orientation.LEFT_RIGHT,
            GradientDrawable.Orientation.BL_TR,
            GradientDrawable.Orientation.BOTTOM_TOP,
            GradientDrawable.Orientation.BR_TL,
            GradientDrawable.Orientation.RIGHT_LEFT,
            GradientDrawable.Orientation.TR_BL,
            GradientDrawable.Orientation.TOP_BOTTOM
    };

    private Spinner angleSpinner;

    private boolean useGradient = true;
    boolean linearSelected = true;
    boolean hasCenterColor = true;
    boolean vertical = false;

    private EditText etCx, etCy, etRad;

    private GradientDrawable shape;

    private String c1, c2, c3;

    private ArrayList<String> angleList;

    private int angleSelected = 1, tabSelected = 0;

    private int [] tabIDs = {R.id.tab1, R.id.tab2, R.id.tab3, R.id.tab4, R.id.tab5};
    private Button [] tabs = new Button[tabIDs.length];

    Button preview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();

        angleList = new ArrayList<>();

        for (int i = 0 ; i < tabs.length ; i++){
            tabs[i] = (Button)findViewById(tabIDs[i]);
            tabs[i].setOnClickListener(this);
        }

        int x = 0;
        for (int i = 0; i < 7; i++) {
            angleList.add(x + " degrees");
            x += 45;
        }

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
        rowCen = (TableRow) findViewById(R.id.row_centre_color);
        rowEnd = (TableRow) findViewById(R.id.row_end_color);
        rowOrien = (TableRow) findViewById(R.id.row_orientation);
        rowIncludeCenterColor = (TableRow) findViewById(R.id.row_include_center);
        rowRadius = (TableRow) findViewById(R.id.row_radius);

        preview = (Button) findViewById(R.id.btn_preview);
        etCx = (EditText) findViewById(R.id.et_cx);
        etCy = (EditText) findViewById(R.id.et_cy);
        etRad = (EditText) findViewById(R.id.et_rad);

        cbCenterColor = (CheckBox) findViewById(R.id.cb_include_center);
        switchGradient = (Switch) findViewById(R.id.switch_gradient);
        switchRadial = (Switch)findViewById(R.id.switch_type) ;


        tvHor = (TextView) findViewById(R.id.tv_horizontal);
        tvVert = (TextView) findViewById(R.id.tv_vertical);

        cbCenterColor.setChecked(hasCenterColor);

        cbCenterColor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                hasCenterColor = isChecked;
                if (useGradient) {
                    if (isChecked) {
                        vis(rowCen, 1);
                        vis(rowEnd, 1);
                    } else {
                        vis(rowCen, 0);
                    }
                }
                applySettings();
            }
        });

        cp = new ColorPicker(MainActivity.this, 0, 0, 0);
        cp.setCallback(new ColorPickerCallback() {
            @Override
            public void onColorChosen(@ColorInt int color) {
                String s = String.format("#%06X", (0xFFFFFF & color));

                switch (select) {
                    case 1:
                        startColor.setBackgroundColor(Color.parseColor(s));
                        startColor.setText(s);
                        break;
                    case 2:
                        centerColor.setBackgroundColor(Color.parseColor(s));
                        centerColor.setText(s);
                        break;
                    case 3:
                        endColor.setBackgroundColor(Color.parseColor(s));
                        endColor.setText(s);
                        break;
                    case 4:
                        borderColor.setBackgroundColor(Color.parseColor(s));
                        borderColor.setText(s);
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

        startColor = (Button) findViewById(R.id.start_color);
        centerColor = (Button) findViewById(R.id.center_color);
        endColor = (Button) findViewById(R.id.end_color);

        startColor.addTextChangedListener(this);
        centerColor.addTextChangedListener(this);
        endColor.addTextChangedListener(this);

        etCx.addTextChangedListener(this);
        etCy.addTextChangedListener(this);
        etRad.addTextChangedListener(this);

        startColor.setOnClickListener(this);
        centerColor.setOnClickListener(this);
        endColor.setOnClickListener(this);

        displayMechanics();

        applySettings();
    }

    @Override
    public void onClick(View v) {

        if (v == startColor) {
            select = 1;
            cp.show();
        }
        if (v == centerColor) {
            select = 2;
            cp.show();
        }
        if (v == endColor) {
            select = 3;
            cp.show();
        }
        if (v == borderColor) {
            select = 4;
            cp.show();
        }
        if (v == genXml) {
            constructXml();
        }
        if (v == tvSolid) {
            solidColor = true;
            setBackground(tvSolid, tvGrad);
        }
        if (v == tvGrad) {
            solidColor = false;
            setBackground(tvGrad, tvSolid);
        }
        if (v == tvLinear) {
            linearSelected = true;
            setBackground(tvLinear, tvRadial);
            displayMechanics();
            applySettings();
        }
        if (v == tvRadial) {
            linearSelected = false;
            setBackground(tvRadial, tvLinear);

        }

        if (v == tvHor) {
            setBackground(tvHor, tvVert);
            vertical = false;
        }
        if (v == tvVert) {
            setBackground(tvVert, tvHor);
            vertical = true;
        }
        if (v==tabs[0]){
            tabs[0].setBackgroundResource(R.drawable.new_select);
            tabs[0].setTextColor(Color.RED);
            setTabs(1,2,3,4);
        }
        if (v==tabs[1]){
            tabs[1].setBackgroundResource(R.drawable.new_select);
            tabs[1].setTextColor(Color.RED);
            setTabs(0,2,3,4);
        }
        if (v==tabs[2]){
            tabs[2].setBackgroundResource(R.drawable.new_select);
            tabs[2].setTextColor(Color.RED);
            setTabs(0,1,3,4);
        }
        if (v==tabs[3]){
            tabs[3].setBackgroundResource(R.drawable.new_select);
            tabs[3].setTextColor(Color.RED);
            setTabs(0,1,2,4);
        }
        if (v==tabs[4]){
            tabs[4].setBackgroundResource(R.drawable.new_select);
            tabs[4].setTextColor(Color.RED);
            setTabs(0,1,2,3);
        }
    }

    private void setTabs(int... i){
        for (int x : i){
            tabs[x].setBackgroundColor(Color.WHITE);
            tabs[x].setTextColor(Color.BLACK);
        }
    }


    private void findViews() {
    }

    private void constructXml() {

        char q = '"';
        StringBuilder builder = new StringBuilder();

        builder.append(Xml.header.concat("\n"));
        if (singleColor) {
            String c = q + startColor.getText().toString() + q;
            builder.append(Xml.solidColor.concat(c).concat(Xml.closing_tag));
        } else {
            builder.append(Xml.gradientHead);
            if (twoColorsSelected) {
                String s = q + startColor.getText().toString() + q;
                String e = q + endColor.getText().toString() + q;
                builder.append("\n".concat(Xml.startColor.concat(s).concat("\n").concat(Xml.endColor).concat(e)));
            } else {
                String s = q + startColor.getText().toString() + q;
                String c = q + centerColor.getText().toString() + q;
                String e = q + endColor.getText().toString() + q;
                builder.append("\n".concat(Xml.startColor.concat(s).concat("\n").concat(Xml.centerColor.concat(c).concat("\n").concat(Xml.endColor.concat(e)))));
            }

        }

        if (hasBorder) {
            String bc = q + borderColor.getText().toString() + q;

            //builder.append(Xml.stroke.concat(Xml.strokeColor.concat(bc).concat(Xml.strokeWidth.concat(bw).concat(Xml.closing_tag))));
        }

        builder.append(Xml.final_closing_tag);

        //Intent intent = new Intent(MainActivity.this, GenerateDrawableXMLActivity.class);
        //intent.putExtra("xml", builder.toString());
        //startActivity(intent);
    }

    private void setBackground(TextView textView, TextView textView2) {

        textView.setBackgroundResource(R.drawable.bg_selected);
        textView2.setBackgroundResource(R.drawable.bg);
        textView.setTextColor(Color.WHITE);
        textView2.setTextColor(Color.LTGRAY);
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

            if (hasCenterColor) {
                vis(rowCen, 1);
                vis(rowEnd, 1);
            } else {
                vis(rowCen, 0);
                vis(rowEnd, 1);
            }
        } else {
            vis(rowOrien, 0);
            vis(rowCX, 0);
            vis(rowCY, 0);
            vis(rowCen, 0);
            vis(rowEnd, 0);
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
            c1 = startColor.getText().toString();
            shape.setColor(Color.parseColor(c1));
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

    private int toInt(EditText et) {
        return Integer.parseInt(et.getText().toString());
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        applySettings();
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    private void setButtonColors() {

        c1 = startColor.getText().toString();
        c2 = centerColor.getText().toString();
        c3 = endColor.getText().toString();

        if (hasCenterColor) {
            int[] c = {Color.parseColor(c1), Color.parseColor(c2), Color.parseColor(c3)};
            shape.setColors(c);
        } else {
            int[] c = {Color.parseColor(c1), Color.parseColor(c3)};
            shape.setColors(c);
        }

    }
}



