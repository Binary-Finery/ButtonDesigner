package com.spencerstudios.buttondesigner;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatSeekBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import java.util.Locale;


public class FragmentCorners extends Fragment implements SeekBar.OnSeekBarChangeListener {

    private TextView tvAll, tvTL, tvTR, tvBL, tvBR;
    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (view == null) {

            view = inflater.inflate(R.layout.frag_2, container, false);

            Switch switchCorners = view.findViewById(R.id.switch_corners);

            AppCompatSeekBar cornerAll = view.findViewById(R.id.corners_all);
            AppCompatSeekBar cornerTopLeft = view.findViewById(R.id.corners_top_left);
            AppCompatSeekBar cornerTopRight = view.findViewById(R.id.corners_top_right);
            AppCompatSeekBar cornerBottomLeft = view.findViewById(R.id.corners_bottom_left);
            AppCompatSeekBar cornerBottomRight = view.findViewById(R.id.corners_bottom_right);

            tvAll = view.findViewById(R.id.tv_all_corners_dp);
            tvTL = view.findViewById(R.id.tv_top_left_dp);
            tvTR = view.findViewById(R.id.tv_top_right_dp);
            tvBL = view.findViewById(R.id.tv_bottom_left_dp);
            tvBR = view.findViewById(R.id.tv_bottom_right_dp);


            cornerAll.setOnSeekBarChangeListener(this);
            cornerTopLeft.setOnSeekBarChangeListener(this);
            cornerTopRight.setOnSeekBarChangeListener(this);
            cornerBottomLeft.setOnSeekBarChangeListener(this);
            cornerBottomRight.setOnSeekBarChangeListener(this);

            cornerAll.setProgress(Utils.getDimensionPrefs(getContext(), "corner_all"));
            cornerTopLeft.setProgress(Utils.getDimensionPrefs(getContext(), "corner_tl"));
            cornerTopRight.setProgress(Utils.getDimensionPrefs(getContext(), "corner_tr"));
            cornerBottomLeft.setProgress(Utils.getDimensionPrefs(getContext(), "corner_bl"));
            cornerBottomRight.setProgress(Utils.getDimensionPrefs(getContext(), "corner_br"));

            switchCorners.setChecked(Utils.getBooleanPrefs(getContext(), "switch_corner"));

            switchCorners.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    Utils.setBooleanPrefs(getContext(), "switch_corner", b);
                }
            });
        }
        return view;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

        switch (seekBar.getId()) {

            case R.id.corners_all:
                tvAll.setText(String.format(Locale.getDefault(), "%d dp", i));
                Utils.setDimensionPrefs(getContext(), "corner_all", i);
                break;

            case R.id.corners_top_left:
                tvTL.setText(String.format(Locale.getDefault(), "%d dp", i));
                Utils.setDimensionPrefs(getContext(), "corner_tl", i);
                break;

            case R.id.corners_top_right:
                tvTR.setText(String.format(Locale.getDefault(), "%d dp", i));
                Utils.setDimensionPrefs(getContext(), "corner_tr", i);
                break;

            case R.id.corners_bottom_left:
                tvBL.setText(String.format(Locale.getDefault(), "%d dp", i));
                Utils.setDimensionPrefs(getContext(), "corner_bl", i);
                break;

            case R.id.corners_bottom_right:
                tvBR.setText(String.format(Locale.getDefault(), "%d dp", i));
                Utils.setDimensionPrefs(getContext(), "corner_br", i);
                break;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}

