package com.example.turismyandexmaps;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.annotation.Nullable;

import com.example.turismyandexmaps.ui.adventures.AdventuresFragment;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "adventures_db";
    public static final String TABLE_NAME = "adventures_table";
    public static final String ICON_COLUMN_NAME = "icon_path";
    public static final String TITLE_COLUMN_NAME = "title";
    public static final String DESC_COLUMN_NAME = "description";
    public static final int DATABASE_VERSION = 1;

    public DBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + "(" + TITLE_COLUMN_NAME + " " +
                "text primary key, " + ICON_COLUMN_NAME + " text, " + DESC_COLUMN_NAME + " text);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_NAME);
        onCreate(db);
    }

    public long insertLine(String title, String icon_path, String description){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DBHelper.TITLE_COLUMN_NAME, title);
        cv.put(DBHelper.ICON_COLUMN_NAME, icon_path);
        cv.put(DBHelper.DESC_COLUMN_NAME, description);
        return db.insert(DBHelper.TABLE_NAME, null, cv);
    }

    public ArrayList<AdventuresFragment.Adventure> getAllAdventures() {
        ArrayList<AdventuresFragment.Adventure> adventures = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        @SuppressLint("Recycle") Cursor c = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        c.moveToFirst();
        do{
            try {
                adventures.add(new AdventuresFragment.Adventure(MediaStore.Images.Media.getBitmap
                        (MainActivity.contentResolver, normalizeUri(Uri.parse(c.getString(1))))
                        , c.getString(0), c.getString(2), c.getString(1)));
            }catch (Exception e){
                e.printStackTrace();
                return new ArrayList<>();
            }
        }while (c.moveToNext());
        c.close();
        return adventures;
    }

    public void insertAll(ArrayList<AdventuresFragment.Adventure> data){
        for(AdventuresFragment.Adventure adventure : data){
            int count = 0;
            while(this.insertLine(adventure.title, adventure.picture_path, adventure.description) == -1 && count < 20){
                this.insertLine(adventure.title, adventure.picture_path, adventure.description);
                count++;
            };
        }
    }

    private Uri normalizeUri(Uri uri){
        return uri.getScheme() == null ? Uri.parse("file://" + uri.getPath().substring(1)) : uri;
    }
}
