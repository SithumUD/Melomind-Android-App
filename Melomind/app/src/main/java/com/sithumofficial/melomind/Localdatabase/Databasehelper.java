package com.sithumofficial.melomind.Localdatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class Databasehelper extends SQLiteOpenHelper {

    private Context context;
    private static final String DATABASE_NAME = "Melomindlocalstorage.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "speech_library";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_SPEAKERS = "speakers";
    private static final String COLUMN_CATEGORY = "category";
    private static final String COLUMN_COVER_IMAGE_URL = "coverimageurl";
    private static final String COLUMN_DURATION = "duration";
    private static final String COLUMN_FILE_PATH = "filepath";

    public Databasehelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query =
                "CREATE TABLE " + TABLE_NAME +
                        "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_TITLE + " TEXT, " +
                        COLUMN_SPEAKERS + " TEXT, " +
                        COLUMN_CATEGORY + " TEXT, " +
                        COLUMN_DURATION + " TEXT, " +
                        COLUMN_COVER_IMAGE_URL + " TEXT, " +
                        COLUMN_FILE_PATH + " TEXT);";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void addSpeache(String title,String speakers,String category,String duration,String coverimage,String filepath){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_TITLE, title);
        cv.put(COLUMN_SPEAKERS, speakers);
        cv.put(COLUMN_CATEGORY, category);
        cv.put(COLUMN_DURATION, duration);
        cv.put(COLUMN_COVER_IMAGE_URL, coverimage);
        cv.put(COLUMN_FILE_PATH, filepath);
        long result = db.insert(TABLE_NAME,null, cv);
        if (result == -1){
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Added Successfully", Toast.LENGTH_SHORT).show();
        }
    }

    public Cursor readAllData(){
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

}
