package com.example.turismyandexmaps.ui.maps;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;

public class InfoActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getIntent().getIntExtra("Layout", 0));
    }
}
