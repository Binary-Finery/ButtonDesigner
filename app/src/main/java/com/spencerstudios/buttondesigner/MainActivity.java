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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.pes.androidmaterialcolorpickerdialog.ColorPicker;
import com.pes.androidmaterialcolorpickerdialog.ColorPickerCallback;

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
    private CheckBox cbGradient, cbCenterColor;
    private ColorPicker cp;


    private boolean twoColorsSelected = false, singleColor = false, triColors = false, hasBorder = true, isVertical = false;

    private boolean solidColor = false;

    private List<String> dpList;

    private CheckBox cbBorder;

    private final GradientDrawable.Orientation[] ORIENTATION = {
            GradientDrawable.Orientation.LEFT_RIGHT,
            GradientDrawable.Orientation.TOP_BOTTOM
    };

    private Spinner angleSpinner;

    boolean useGradient = true;
    boolean linearSelected = true;
    boolean hasCenterColor = true;
    boolean vertical = false;

    private EditText etCx, etCy, etRad;

    private GradientDrawable shape;

    private String c1, c2, c3;

    Button preview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (getSupportActionBar() != null) getSupportActionBar().hide();

        findViews();



        shape = new GradientDrawable();

        rowType = (TableRow) findViewById(R.id.row_type);
        rowCX = (TableRow) findViewById(R.id.row_center_x);
        rowCY = (TableRow) findViewById(R.id.row_center_y);
        rowCen = (TableRow) findViewById(R.id.row_centre_color);
        rowEnd = (TableRow) findViewById(R.id.row_end_color);
        rowOrien = (TableRow) findViewById(R.id.row_orientation);
        rowIncludeCenterColor = (TableRow) findViewById(R.id.row_include_center);
        rowRadius = (TableRow) findViewById(R.id.row_radius);

        preview = (Button) findViewById(R.id.btn_preview);
        tvLinear = (TextView) findViewById(R.id.tv_linear);
        tvRadial = (TextView) findViewById(R.id.tv_rad);

        etCx = (EditText) findViewById(R.id.et_cx);
        etCy = (EditText) findViewById(R.id.et_cy);
        etRad = (EditText) findViewById(R.id.et_rad);

        cbCenterColor = (CheckBox) findViewById(R.id.cb_include_center);
        cbGradient = (CheckBox) findViewById(R.id.cb_gradient);

        tvHor = (TextView) findViewById(R.id.tv_horizontal);
        tvVert = (TextView) findViewById(R.id.tv_vertical);

        tvLinear.setOnClickListener(this);
        tvRadial.setOnClickListener(this);

        tvVert.setOnClickListener(this);
        tvHor.setOnClickListener(this);

        if (vertical) setBackground(tvVert, tvHor);
        else setBackground(tvHor, tvVert);

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

        cbGradient.setChecked(useGradient);

        cbGradient.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                useGradient = isChecked;
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

        //genXml = (Button) findViewById(R.id.btn_xml);

        //genXml.setOnClickListener(this);


        startColor.setOnClickListener(this);
        centerColor.setOnClickListener(this);
        endColor.setOnClickListener(this);

        displayMechanics();

        //applyButtonProperties();

        applySettings();
    }

    private void applyButtonProperties() {

        shape.setShape(GradientDrawable.RECTANGLE);

        if (useGradient && !linearSelected) {

            shape.setGradientType(GradientDrawable.RADIAL_GRADIENT);

            //shape.setOrientation(GradientDrawable.Orientation.LEFT_RIGHT);

            //float x = Float.parseFloat("0.".concat(etCx.getText().toString()));
            //float y = Float.parseFloat("0.".concat(etCy.getText().toString()));

            shape.setGradientCenter(.1f, 0.30f);

            //int r = Integer.parseInt(etRad.getText().toString());
            shape.setGradientRadius(convertDpToPx(50));

            String c1, c2 = null, c3;

            c1 = startColor.getText().toString();
            if (hasCenterColor) c2 = centerColor.getText().toString();
            c3 = endColor.getText().toString();

            if (hasCenterColor) {
                int[] c = {Color.parseColor("#000000"), Color.parseColor("#ffffff"), Color.parseColor("#000000")};
                shape.setColors(c);
            } else {
                int[] c = {Color.parseColor("#000000"), Color.parseColor("#ffffff")};
                shape.setColors(c);
            }
        }
        //shape.setOrientation(ORIENTATION[0]);

        shape.setSize(convertDpToPx(270), convertDpToPx(60));

        //shape.setOrientation(GradientDrawable.Orientation.RIGHT_LEFT);

        try {
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

            //shape.setColor(Color.parseColor(startColor.getText().toString()));

            //shape.setGradientCenter(0.10f, 0.50f);
            //shape.setGradientRadius(convertDpToPx(100));

            //twoColorsSelected = true;

            preview.setBackgroundDrawable(shape);

        } catch (IllegalArgumentException e) {
            Toast.makeText(getApplicationContext(), "Illegal args", Toast.LENGTH_LONG).show();
        }
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
        }
        if (v == tvRadial) {
            linearSelected = false;
            setBackground(tvRadial, tvLinear);
            displayMechanics();
        }

        if (v == tvHor) {
            setBackground(tvHor, tvVert);
            vertical = false;
        }
        if (v == tvVert) {
            setBackground(tvVert, tvHor);
            vertical = true;
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

            vis(rowType, 1);
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
            vis(rowType, 0);
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

        shape  = new GradientDrawable();
        if (useGradient) {

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
        }else {
            c1 = startColor.getText().toString();
            shape.setColor(Color.parseColor(c1));
        }

        if (!linearSelected) {

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
        }

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
}



