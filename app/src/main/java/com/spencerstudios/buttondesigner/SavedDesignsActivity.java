package com.spencerstudios.buttondesigner;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import spencerstudios.com.bungeelib.Bungee;

public class SavedDesignsActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_designs);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        ListView buttonList = findViewById(R.id.button_list_view);
        ArrayList<Model> btnList = Utils.buttonList(this);
        TextView empty = findViewById(android.R.id.empty);
        buttonList.setEmptyView(empty);

        ButtonsAdapter buttonsAdapter = new ButtonsAdapter(this, btnList);
        buttonList.setAdapter(buttonsAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.saved_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            Bungee.split(this);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Bungee.split(this);
    }

}
