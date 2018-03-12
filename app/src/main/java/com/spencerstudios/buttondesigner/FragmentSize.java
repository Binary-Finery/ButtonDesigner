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

public class FragmentSize extends Fragment implements SeekBar.OnSeekBarChangeListener {

    View view;
    private TextView tvWidth, tvHeight;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (view == null) {

            view = inflater.inflate(R.layout.frag_3, container, false);

            Switch switchWidth = view.findViewById(R.id.switch_size_width);
            Switch switchHeight = view.findViewById(R.id.switch_size_height);
            AppCompatSeekBar seekWidth = view.findViewById(R.id.seekbar_size_width);
            AppCompatSeekBar seekHeight = view.findViewById(R.id.seekbar_size_height);
            tvWidth = view.findViewById(R.id.tv_size_width_dp);
            tvHeight = view.findViewById(R.id.tv_size_height_dp);

            seekWidth.setMax(Utils.getDimensionPrefs(getActivity(), "screen_width_in_dp"));
            seekHeight.setMax(Utils.getDimensionPrefs(getActivity(), "screen_width_in_dp"));

            seekHeight.setOnSeekBarChangeListener(this);
            seekWidth.setOnSeekBarChangeListener(this);

            switchWidth.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    Utils.setBooleanPrefs(getActivity(), "switch_width", b);
                }
            });

            switchHeight.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    Utils.setBooleanPrefs(getActivity(), "switch_height", b);
                }
            });

            switchWidth.setChecked(Utils.getBooleanPrefs(getActivity(), "switch_width"));
            switchHeight.setChecked(Utils.getBooleanPrefs(getActivity(), "switch_height"));

            seekWidth.setProgress(Utils.getDimensionPrefs(getActivity(), "size_width"));
            tvWidth.setText(String.format(Locale.getDefault(), "%d DP", Utils.getDimensionPrefs(getActivity(), "size_width")));
            seekHeight.setProgress(Utils.getDimensionPrefs(getActivity(), "size_height"));
            tvHeight.setText(String.format(Locale.getDefault(), "%d DP", Utils.getDimensionPrefs(getActivity(), "size_height")));
        }
        return view;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

        switch (seekBar.getId()) {

            case R.id.seekbar_size_width:
                tvWidth.setText(String.format(Locale.getDefault(), "%d DP", i));
                Utils.setDimensionPrefs(getActivity(), "size_width", i);
                break;

            case R.id.seekbar_size_height:
                tvHeight.setText(String.format(Locale.getDefault(), "%d DP", i));
                Utils.setDimensionPrefs(getActivity(), "size_height", i);
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

