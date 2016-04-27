package com.example.ahmed.movies;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ahmed on 4/21/2016.
 */
public class Database extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "favorit.dp" ;
    public static final String TABLE_NAME = "film_table";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "IMAGE";
    public static final String COL_3 = "DESCRIPTION";
    public static final String COL_4 = "DATE";
    public static final String COL_5 = "TITLE";
    public static final String COL_6 = "BACKGROUND";
    public static final String COL_7 = "VOTE";

    public Database(Context context) {
        super(context, DATABASE_NAME, null, 6);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + "( ID INTEGER PRIMARY KEY AUTOINCREMENT, IMAGE TEXT, DESCRIPTION TEXT, DATE TEXT, TITLE TEXT, BACKGROUND TEXT, VOTE TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);

    }
    public boolean insertData(String id, String image, String description, String date, String title, String background, String vote)
    {
        SQLiteDatabase dp=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(COL_1, id);
        contentValues.put(COL_2, image);
        contentValues.put(COL_3, description);
        contentValues.put(COL_4, date);
        contentValues.put(COL_5, title);
        contentValues.put(COL_6, background);
        contentValues.put(COL_7, vote );
        Long result =  dp.insert(TABLE_NAME, null, contentValues);
        if(-1 == result)
            return  false;
        else
            return true;
    }
    public Cursor getAllData()
    {
        SQLiteDatabase dp=this.getWritableDatabase();
        Cursor res=dp.rawQuery("select * from "+ TABLE_NAME, null);
        return res;
    }

    public Integer deleteData(String id)
    {
        SQLiteDatabase dp=this.getWritableDatabase();
        return dp.delete(TABLE_NAME, "ID= ?", new String[]{id});
    }



}
