package com.otnam.kasirqiu;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MenuHeader extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_header);
        getSupportActionBar().hide();
    }
}