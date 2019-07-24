package com.example.tictactoe;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

class DatabaseHandler extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "HIGHSCORE.db";
    private static final String TABLE_NAME = " SCORECARD ";
    private static final String S_NO = "S_no";
    private static final String Score = "Score";


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String create_table = "CREATE TABLE " + TABLE_NAME + "("
                + "S_no INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "Score INTEGER" + ")";
        db.execSQL(create_table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME );

    }

    public boolean addScore(float values) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(Score, values);

        Log.d("stk","sql"+values);
        // Inserting Values
        long insert = db.insert(TABLE_NAME, null, contentValues);

        if (insert==-1){
            return false;
        }else {
            return true;
        }

    }

    public Cursor displayScore(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_NAME, null );
        return data;
    }

    public void clearall(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(" DELETE FROM " + TABLE_NAME );
        db.close();
    }

}
