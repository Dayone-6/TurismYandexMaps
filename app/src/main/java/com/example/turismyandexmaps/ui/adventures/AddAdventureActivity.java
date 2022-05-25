package com.example.turismyandexmaps.ui.adventures;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.turismyandexmaps.MainActivity;
import com.example.turismyandexmaps.R;

import java.io.IOException;

public class AddAdventureActivity extends Activity {
    private Bitmap icon = null;
    private String icon_path = null;
    private ImageView imagePick;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_adventure_activity);
        Button button = findViewById(R.id.upload);
        imagePick = findViewById(R.id.image_pick);
        imagePick.setOnClickListener(v -> {
            Intent imagePick = new Intent(Intent.ACTION_PICK);
            imagePick.setType("image/*");
            startActivityForResult(imagePick, 1);
        });
        button.setOnClickListener(v -> {
            @SuppressLint("CutPasteId") String adv_title = ((TextView)findViewById(R.id.adventure_title)).getText().toString();
            @SuppressLint("CutPasteId") String adv_desc = ((TextView)findViewById(R.id.adventure_desc)).getText().toString();
            if(!adv_desc.equals("") && !adv_title.equals("") && icon_path != null) {
                @SuppressLint("CutPasteId") AdventuresFragment.Adventure new_adventure = new AdventuresFragment.Adventure(AddAdventureActivity.this.icon, ((TextView) findViewById(R.id.adventure_title)).getText().toString(), ((TextView) findViewById(R.id.adventure_desc)).getText().toString(), icon_path);
                MainActivity.new_adventures.add(new_adventure);
                AddAdventureActivity.super.onBackPressed();
            }else{
                Toast.makeText(getApplicationContext(), "Все поля должны быть заполнены", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Bitmap picked_image = null;
        ImageView btn = imagePick;
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Uri selectedImage = data.getData();
                try {
                    picked_image = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                btn.setImageBitmap(picked_image);
                icon = picked_image;
                icon_path = selectedImage.getPath();
            }
        }
    }
}
