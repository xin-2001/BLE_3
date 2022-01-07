package com.noahliu.ble_example;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.noahliu.ble_example.Controller.Database;
import com.noahliu.ble_example.Controller.DeviceInfoActivity;
import com.noahliu.ble_example.Module.Enitiy.ScannedData;

import java.util.ArrayList;

public class Result_Activity extends AppCompatActivity {
    private TextView max;
    private TextView avg;
    private EditText name;
    private Button save;
    private String Name_text;
    private String Max,Avg;
    private SQLiteDatabase db;
    private Database _DB;
    private ListView list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);


        _DB=new Database(this);
        db=_DB.getWritableDatabase();

        max=findViewById(R.id.max);
        avg=findViewById(R.id.avg);
        name=findViewById(R.id.name);
        save=findViewById(R.id.save);
        list=findViewById(R.id.list);

        Intent i=getIntent();
        Max=i.getStringExtra("MAX");
        Avg=i.getStringExtra("AVG");
        //max.setText("Max:"+Max);
        //avg.setText("Avg:"+Avg);
        max.setText(" ");
        avg.setText(" ");
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Name_text=name.getText().toString();
                ContentValues cv=new ContentValues();
                //cv.put("_id",1);
                cv.put("_name",Name_text);
                cv.put("_max",Max);
                cv.put("_avg",Avg);
                db.insert("TB1",null,cv);
                Toast.makeText(getApplicationContext(), "新增成功", Toast.LENGTH_SHORT).show();
                db.close();
                SSQL();
                /*Intent i=new Intent(Result_Activity.this, DeviceInfoActivity.class);
                startActivity(i);*/

            }
        });


    }
    public void SSQL(){
        db=_DB.getReadableDatabase();
        Cursor c=db.query("TB1",new String[]{"_name","_max","_avg"},null,null,null,null,null);
        ArrayList<String> arr=new ArrayList<>();
        c.moveToFirst();
        for(int i=0;i<c.getCount();i++){
            arr.add(c.getString(0)+" Max:"+c.getString(1)+" Avg:"+c.getString(2));
            c.moveToNext();
        }
        final ArrayAdapter SA=new ArrayAdapter(this, android.R.layout.simple_list_item_1,arr);
        //Toast.makeText(getApplicationContext(), SA.toString(), Toast.LENGTH_SHORT).show();
        list.setAdapter(SA);
    }
}