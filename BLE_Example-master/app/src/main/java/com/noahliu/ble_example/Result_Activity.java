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
import com.noahliu.ble_example.Controller.Login_Activity;
import com.noahliu.ble_example.Controller.MainActivity;
import com.noahliu.ble_example.Module.Enitiy.ScannedData;

import java.util.ArrayList;

public class Result_Activity extends AppCompatActivity {
    private TextView max;
    private TextView avg;
    private TextView min;
    private TextView renew;
    private EditText name;
    private Button save;
    private String Name_text;
    private String Max,Avg,user_name,Min;
    private SQLiteDatabase db;
    private Database _DB;
    private ListView list;
    public static final String INTENT_KEY = "GET_DEVICE";
    private ScannedData selectedDevice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        selectedDevice = (ScannedData) getIntent().getSerializableExtra(INTENT_KEY);


        _DB=new Database(this);
        db=_DB.getWritableDatabase();

        max=findViewById(R.id.max);
        avg=findViewById(R.id.avg);
        min=findViewById(R.id.min);
        name=findViewById(R.id.name);
        save=findViewById(R.id.save);
        list=findViewById(R.id.list);
        renew=findViewById(R.id.renew);

        Intent i=getIntent();
        Max=i.getStringExtra("MAX");
        Avg=i.getStringExtra("AVG");
        Min=i.getStringExtra("MIN");
        user_name=i.getStringExtra("user_name");

        max.setText("MAX："+Max);
        avg.setText("AVG："+Avg);
        min.setText("MIN："+Min);
        renew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Result_Activity.this, Login_Activity.class);
                intent.putExtra(DeviceInfoActivity.INTENT_KEY,selectedDevice);
                startActivity(intent);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Name_text=name.getText().toString();
                ContentValues cv=new ContentValues();
                //cv.put("_id",1);
                cv.put("_name",user_name);
                cv.put("_max",Max);
                cv.put("_min",Min);
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
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }//禁止上一頁
    public void SSQL(){
        db=_DB.getReadableDatabase();
        Cursor c=db.query("TB1",new String[]{"_name","_max","_min","_avg"},null,null,null,null,null);
        ArrayList<String> arr=new ArrayList<>();
        c.moveToFirst();
        for(int i=0;i<c.getCount();i++){
            arr.add(c.getString(0)+" Max:"+c.getString(1)+" Min:"+c.getString(2)+" Avg:"+c.getString(3));
            c.moveToNext();
        }
        final ArrayAdapter SA=new ArrayAdapter(this, android.R.layout.simple_list_item_1,arr);
        //Toast.makeText(getApplicationContext(), SA.toString(), Toast.LENGTH_SHORT).show();
        list.setAdapter(SA);
    }
}