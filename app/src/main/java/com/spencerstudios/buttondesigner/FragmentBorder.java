package com.spencerstudios.buttondesigner;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatSeekBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.pes.androidmaterialcolorpickerdialog.ColorPicker;
import com.pes.androidmaterialcolorpickerdialog.ColorPickerCallback;

import java.util.Locale;

public class FragmentBorder extends Fragment {

    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (view == null) {

            view = inflater.inflate(R.layout.frag_1, container, false);

            AppCompatSeekBar strokeWidth = view.findViewById(R.id.stroke_width);
            final TextView tvStrokeWidth = view.findViewById(R.id.tv_stroke_width);
            final Button strokeColor = view.findViewById(R.id.stroke_color);

            strokeColor.setBackgroundColor(Color.parseColor(Utils.getColorPrefs(getContext(), "stroke_color")));

            final ColorPicker colorPicker = new ColorPicker(getActivity(), 50, 50, 50);
            colorPicker.setCallback(new ColorPickerCallback() {
                @Override
                public void onColorChosen(int color) {
                    String hex = String.format("#%06X", (0xFFFFFF & color));
                    strokeColor.setBackgroundColor(Color.parseColor(hex));
                    Utils.setColorPrefs(getActivity(), "stroke_color", hex);
                    colorPicker.dismiss();
                }
            });

            strokeWidth.setMax(25);
            int sw = Utils.getDimensionPrefs(getContext(), "stroke_width");
            strokeWidth.setProgress(sw);
            tvStrokeWidth.setText(String.format(Locale.getDefault(), "%d DP", sw));
            strokeColor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    colorPicker.show();
                }
            });

            strokeWidth.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                    tvStrokeWidth.setText(String.format(Locale.getDefault(), "%d dp", i));
                    Utils.setDimensionPrefs(getContext(), "stroke_width", i);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
        }
        return view;
    }
}
