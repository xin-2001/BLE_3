package com.noahliu.ble_example.Controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;

public class Login_Database extends SQLiteOpenHelper{
    private static final String DB_name="DB1";
    private static final String TB_name="TB1";
    private static final int Version=1;

    public Login_Database (Context context){
        super(context,DB_name,null,Version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS "+TB_name+
                "(_id INTEGER PRIMARY KEY AUTOINCREMENT " +
                ",_name VARCHAR(20)" +
                ",_password VARCHAR(20))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS users");
        onCreate(db);
    }
}
