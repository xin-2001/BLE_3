package com.noahliu.ble_example.Controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.widget.Toast;

public class Login_Database extends SQLiteOpenHelper{
    private static final String DB_name="DB1";
    private static final String TB_name="TB2";
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
        ContentValues cv=new ContentValues();
        //cv.put("_id",1);
        cv.put("_name","abc");
        cv.put("_password","abc");
        db.insert("TB2",null,cv);
        db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS users");
        onCreate(db);
    }
}
