package com.example.turismyandexmaps;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

public class StartActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_activity);
        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE
                , Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION
                , Manifest.permission.ACCESS_COARSE_LOCATION }, 1);
        Button con = findViewById(R.id.continue_btn);
        con.setOnClickListener(v -> {
            setContentView(R.layout.loading_screen);
            Intent i = new Intent(StartActivity.this, MainActivity.class);
            startActivity(i);
        });
        Button exit = findViewById(R.id.exit_btn);
        exit.setOnClickListener(v -> System.exit(0));
    }
}
