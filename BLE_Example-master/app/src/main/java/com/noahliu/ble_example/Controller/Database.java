package com.noahliu.ble_example.Controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;

public class Database extends SQLiteOpenHelper{
    private static final String DB_name="DB1";
    private static final String TB_name="TB1";
    private static final String TB2_name="TB2";
    private static final int Version=1;

    public Database (Context context){
        super(context,DB_name,null,Version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS "+TB_name+
                "(_id INTEGER PRIMARY KEY AUTOINCREMENT " +
                ",_name VARCHAR(20)"+
                ",_number VARCHAR(20)"+
                ",_max VARCHAR(20)"+
                ",_min VARCHAR(20)"+
                ",_avg VARCHAR(20))");
        db.execSQL("CREATE TABLE IF NOT EXISTS "+TB2_name+
                "(_id INTEGER PRIMARY KEY AUTOINCREMENT " +
                ",_name VARCHAR(20)" +
                ",_number VARCHAR(20)"+
                ",_gender VARHAR(20)"+
                ",_birthday VARCHAR(20)"+
                ",_height VARCHAR(20)"+
                ",_weight VARCHAR(20)"+
                ",_hand VARCHAR(20))");
        ContentValues cv=new ContentValues();
        //cv.put("_id",1);
        cv.put("_name","123");
        cv.put("_number","123");
        db.insert("TB1",null,cv);
        db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS users");
        onCreate(db);
    }
}



