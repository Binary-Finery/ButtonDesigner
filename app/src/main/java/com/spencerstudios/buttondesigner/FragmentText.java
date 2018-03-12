package com.spencerstudios.buttondesigner;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatSeekBar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.pes.androidmaterialcolorpickerdialog.ColorPicker;
import com.pes.androidmaterialcolorpickerdialog.ColorPickerCallback;

import java.util.Locale;

public class FragmentText extends Fragment {

    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (view == null) {
            view = inflater.inflate(R.layout.frag_5, container, false);

            AppCompatSeekBar seekTextSize = view.findViewById(R.id.seek_text_size);
            final TextView tvSp = view.findViewById(R.id.tv_text_size_sp);
            final Button btnTextColor = view.findViewById(R.id.btn_text_color);
            final Button btnTypeface = view.findViewById(R.id.btn_typeface);
            final EditText editText = view.findViewById(R.id.et_button_text);
            Switch allCaps = view.findViewById(R.id.switch_all_caps);

            btnTypeface.setText(Utils.getColorPrefs(getActivity(), "typeface"));

            allCaps.setChecked(Utils.getBooleanPrefs(getActivity(), "all_caps"));

            btnTypeface.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    PopupMenu menu = new PopupMenu(getActivity(), v);

                    for (String typeface : Constants.FONT_FAMILIES) {
                        menu.getMenu().add(typeface);
                    }
                    menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            String tf = item.getTitle().toString();
                            btnTypeface.setText(tf);
                            Utils.setColorPrefs(getActivity(), "typeface", tf);
                            return false;
                        }
                    });
                    menu.show();
                }
            });

            allCaps.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    Utils.setBooleanPrefs(getActivity(), "all_caps", b);
                }
            });

            editText.setText(Utils.getColorPrefs(getActivity(), "button_text"));
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    Utils.setColorPrefs(getActivity(), "button_text", editText.getText().toString());
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            seekTextSize.setProgress(Utils.getDimensionPrefs(getActivity(), "text_size"));
            tvSp.setText(String.format(Locale.getDefault(), "%d sp", Utils.getDimensionPrefs(getActivity(), "text_size")));

            btnTextColor.setBackgroundColor(Color.parseColor(Utils.getColorPrefs(getActivity(), "text_color")));

            final ColorPicker cp = new ColorPicker(getActivity(), 50, 50, 50);
            cp.setCallback(new ColorPickerCallback() {
                @Override
                public void onColorChosen(int color) {
                    String hex = String.format("#%06X", (0xFFFFFF & color));
                    btnTextColor.setBackgroundColor(Color.parseColor(hex));
                    Utils.setColorPrefs(getActivity(), "text_color", hex);
                    cp.dismiss();
                }
            });

            btnTextColor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cp.show();
                }
            });


            seekTextSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                    Utils.setDimensionPrefs(getActivity(), "text_size", i);
                    tvSp.setText(String.format(Locale.getDefault(), "%d sp", i));
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

