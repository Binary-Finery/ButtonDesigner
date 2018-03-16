package com.spencerstudios.buttondesigner;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;

import spencerstudios.com.bungeelib.Bungee;

public class ButtonsAdapter extends BaseAdapter {

    private ArrayList<Model> btnList;
    private LayoutInflater layoutInflater;
    private Context context;
    private final GradientDrawable.Orientation[] ORIENTATION = Constants.GRADIENT_ORIENTATIONS();

    ButtonsAdapter(Context context, ArrayList<Model> btnList) {
        this.btnList = btnList;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
    }

    @Override
    public int getCount() {
        return btnList.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    private static class Holder {
        Button btnPreview;
        ImageView ivOpen;
        ImageView ivDel;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        Holder holder;

        if (view == null) {

            view = layoutInflater.inflate(R.layout.button_item, null);
            holder = new Holder();
            holder.btnPreview = view.findViewById(R.id.button_preview);
            holder.ivOpen = view.findViewById(R.id.iv_open);
            holder.ivDel = view.findViewById(R.id.iv_delete);

            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }

        holder.ivDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final AlertDialog deleteDialog = new AlertDialog.Builder(context).create();
                deleteDialog.setTitle("Delete This Design");
                deleteDialog.setMessage("Are you sure?");
                deleteDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Yes, Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int val) {
                        Utils.deleteItem(context, i);
                        btnList.remove(i);
                        notifyDataSetChanged();
                    }
                });

                deleteDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteDialog.dismiss();
                    }
                });
                deleteDialog.show();
            }
        });

        holder.ivOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //border...
                Utils.setDimensionPrefs(context, "stroke_width", btnList.get(i).getStrokeWidth());
                Utils.setColorPrefs(context, "stroke_color", btnList.get(i).getStrokeColor());
                //corners...
                Utils.setDimensionPrefs(context, "corner_all", btnList.get(i).getCornerAll());
                Utils.setBooleanPrefs(context, "switch_corner", btnList.get(i).isSwitchCorner());
                //size...
                Utils.setBooleanPrefs(context, "switch_width", btnList.get(i).isSwitchWidth());
                Utils.setDimensionPrefs(context, "size_width", btnList.get(i).getSizeWidth());
                Utils.setBooleanPrefs(context, "switch_height", btnList.get(i).isSwitchHeight());
                Utils.setDimensionPrefs(context, "size_height", btnList.get(i).getSizeHeight());
                //color...
                Utils.setBooleanPrefs(context, "use_gradient", btnList.get(i).isUseGradient());
                Utils.setBooleanPrefs(context, "use_radial", btnList.get(i).isUseRadial());
                Utils.setBooleanPrefs(context, "use_three_colors", btnList.get(i).isUseThreeColors());
                Utils.setDimensionPrefs(context, "angel", btnList.get(i).getAngle());
                Utils.setColorPrefs(context, "color1", btnList.get(i).getColorOne());
                Utils.setColorPrefs(context, "color2", btnList.get(i).getColorTwo());
                Utils.setColorPrefs(context, "color3", btnList.get(i).getColorThree());
                Utils.setDimensionPrefs(context, "center_x", btnList.get(i).getCenterX());
                Utils.setDimensionPrefs(context, "center_y", btnList.get(i).getCenterY());
                Utils.setDimensionPrefs(context, "radius", btnList.get(i).getRadius());
                //text...
                Utils.setColorPrefs(context, "button_text", btnList.get(i).getText());
                Utils.setDimensionPrefs(context, "text_size", btnList.get(i).getTextSize());
                Utils.setColorPrefs(context, "text_color", btnList.get(i).getTextColor());
                Utils.setBooleanPrefs(context, "all_caps", btnList.get(i).isAllCaps());
                Utils.setColorPrefs(context, "typeface", btnList.get(i).getTypeFace());

                context.startActivity(new Intent(context, LauncherActivity.class));
                ((Activity) context).finish();
                Bungee.split(context);

            }
        });

        GradientDrawable drawable = new GradientDrawable();

        //set button corner radii...
        if (btnList.get(i).isSwitchCorner()) {
            drawable.setCornerRadii(new float[]{
                    convertDpToPx(btnList.get(i).getTopLeft()),
                    convertDpToPx(btnList.get(i).getTopLeft()),
                    convertDpToPx(btnList.get(i).getTopRight()),
                    convertDpToPx(btnList.get(i).getTopRight()),
                    convertDpToPx(btnList.get(i).getBottomRight()),
                    convertDpToPx(btnList.get(i).getBottomRight()),
                    convertDpToPx(btnList.get(i).getBottomLeft()),
                    convertDpToPx(btnList.get(i).getBottomLeft())
            });
        } else {
            int globalCornerRadius = btnList.get(i).getCornerAll();
            drawable.setCornerRadii(new float[]{convertDpToPx(globalCornerRadius), convertDpToPx(globalCornerRadius), convertDpToPx(globalCornerRadius), convertDpToPx(globalCornerRadius), convertDpToPx(globalCornerRadius), convertDpToPx(globalCornerRadius), convertDpToPx(globalCornerRadius), convertDpToPx(globalCornerRadius)});
        }

        //set button border width and color...
        drawable.setStroke(convertDpToPx(btnList.get(i).getStrokeWidth()), Color.parseColor(btnList.get(i).getStrokeColor()));

        //set button width and height...
        int w = btnList.get(i).getSizeWidth(), h = btnList.get(i).getSizeHeight();
        holder.btnPreview.setLayoutParams(new LinearLayout.LayoutParams(btnList.get(i).isSwitchWidth() ? LinearLayout.LayoutParams.WRAP_CONTENT : convertDpToPx(w), btnList.get(i).isSwitchHeight() ? LinearLayout.LayoutParams.WRAP_CONTENT : convertDpToPx(h)));

        //set button text...
        holder.btnPreview.setAllCaps(btnList.get(i).isAllCaps());
        holder.btnPreview.setText(btnList.get(i).getText());

        //set button text size in sp...
        holder.btnPreview.setTextSize(TypedValue.COMPLEX_UNIT_SP, btnList.get(i).getTextSize());

        //set text color..
        holder.btnPreview.setTextColor(Color.parseColor(btnList.get(i).getTextColor()));

        //set typeface
        holder.btnPreview.setTypeface(Typeface.create(btnList.get(i).getTypeFace(), Typeface.NORMAL));

        //set button color(s) and gradient type...
        boolean hasGradient = btnList.get(i).isUseGradient();
        boolean isRadial = btnList.get(i).isUseRadial();
        boolean isThreeColors = btnList.get(i).isUseThreeColors();

        if (hasGradient) {

            if (isRadial) {

                drawable.setGradientType(GradientDrawable.RADIAL_GRADIENT);
                drawable.setGradientCenter((float) btnList.get(i).getCenterX() / 100, (float) btnList.get(i).getCenterY() / 100);
                drawable.setGradientRadius(convertDpToPx(btnList.get(i).getRadius()));

                if (isThreeColors) {
                    int[] colors = {Color.parseColor(Utils.getColorPrefs(context, "color1")), Color.parseColor(Utils.getColorPrefs(context, "color2")), Color.parseColor(Utils.getColorPrefs(context, "color3"))};
                    drawable.setColors(colors);
                } else {
                    int[] colors = {Color.parseColor(Utils.getColorPrefs(context, "color1")), Color.parseColor(Utils.getColorPrefs(context, "color2"))};
                    drawable.setColors(colors);
                }
            } else {
                drawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
                drawable.setOrientation(ORIENTATION[btnList.get(i).getAngle()]);
                if (isThreeColors) {
                    int[] colors = {Color.parseColor(btnList.get(i).getColorOne()), Color.parseColor(btnList.get(i).getColorTwo()), Color.parseColor(btnList.get(i).getColorThree())};
                    drawable.setColors(colors);
                } else {
                    int[] colors = {Color.parseColor(btnList.get(i).getColorOne()), Color.parseColor(btnList.get(i).getColorTwo())};
                    drawable.setColors(colors);
                }
            }
        } else drawable.setColor(Color.parseColor(btnList.get(i).getColorOne()));

        holder.btnPreview.setBackground(drawable);

        return view;
    }

    private int convertDpToPx(int dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return (int) ((dp * displayMetrics.density) + .5);
    }
}
