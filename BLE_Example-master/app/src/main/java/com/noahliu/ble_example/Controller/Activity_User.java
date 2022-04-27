package com.noahliu.ble_example.Controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.noahliu.ble_example.Module.Enitiy.ScannedData;
import com.noahliu.ble_example.R;

import java.util.ArrayList;

public class Activity_User extends AppCompatActivity {
    private TextView name;
    private TextView number;
    private TextView gender;
    private TextView birthday;
    private TextView height;
    private TextView weight;
    private TextView hand;
    private Button history;
    private Button new_train;
    private String us_name,us_number,us_gender,us_birthday,us_height,us_weight,us_hand;

    public static final String INTENT_KEY = "GET_DEVICE";
    private ScannedData selectedDevice;

    private SQLiteDatabase db;
    private Database _DB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        selectedDevice = (ScannedData) getIntent().getSerializableExtra(INTENT_KEY);

        _DB=new Database(this);
        db=_DB.getWritableDatabase();


        name=findViewById(R.id.userName);
        number=findViewById(R.id.userNumber);
        gender=findViewById(R.id.userGender);
        birthday=findViewById(R.id.userBirthday);
        height=findViewById(R.id.userHeight);
        weight=findViewById(R.id.userWeight);
        hand=findViewById(R.id.userHand);
        history=findViewById(R.id.history_button);
        new_train=findViewById(R.id.new_button);

        Intent intent=getIntent();
        us_name=intent.getStringExtra("user_name");
        us_number=intent.getStringExtra("user_number");
        us_gender=intent.getStringExtra("user_gender");
        us_birthday=intent.getStringExtra("user_birthday");
        us_height=intent.getStringExtra("user_height");
        us_weight=intent.getStringExtra("user_weight");
        us_hand=intent.getStringExtra("user_hand");
        name.append(us_name);
        number.append(us_number);
        gender.append(us_gender);
        birthday.append(us_birthday);
        height.append(us_height);
        weight.append(us_weight);
        hand.append(us_hand);

        //String select="select * from TB1";
        /*String select="select * from TB1 where _name="+us_name+" and _number="+us_number;
        Cursor cursor=db.rawQuery(select,null);
        ArrayList<String> list_user=new ArrayList<>();
        ArrayList<String> list_number=new ArrayList<>();
        ArrayList<String> list_gender=new ArrayList<>();
        ArrayList<String> list_birthday=new ArrayList<>();
        ArrayList<String> list_height=new ArrayList<>();
        ArrayList<String> list_weight=new ArrayList<>();
        ArrayList<String> list_hand=new ArrayList<>();*/
        /*while(cursor.moveToNext()){
            name.append(cursor.getString(1));
            number.append(cursor.getString(2));
            gender.append(cursor.getString(6));
            birthday.append(cursor.getString(7));
            height.append(cursor.getString(8));
            weight.append(cursor.getString(9));
            hand.append(cursor.getString(10));
        }
        cursor.close();*/
        /*while ((cursor.moveToNext())){
            String item_name=cursor.getString(1);
            list_user.add(item_name);
            String item_number=cursor.getString(2);
            list_number.add(item_number);
            String item_gender=cursor.getString(6);
            list_gender.add(item_gender);
            String item_birthday=cursor.getString(7);
            list_birthday.add(item_birthday);
            String item_height=cursor.getString(8);
            list_height.add(item_height);
            String item_weight=cursor.getString(9);
            list_weight.add(item_weight);
            String item_hand=cursor.getString(10);
            list_hand.add(item_hand);
        }
        cursor.close();

        //要先走訪去判斷使用者在哪
        int k=0;
        for(int i=0;i<list_user.size();i++){
            if(us_name== list_user.get(i) && us_number== list_number.get(i)){
                k=i;
            }else{
                //印出錯誤訊息(找不到使用者)
            }
        }*/

/*
        name.append(list_user.get(k));
        number.append(list_number.get(k));
        gender.append(list_gender.get(k));
        birthday.append(list_birthday.get(k));
        height.append(list_height.get(k));
        weight.append(list_weight.get(k));
        hand.append(list_hand.get(k));
*/



        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Activity_User.this, Activity_History.class);
                i.putExtra(DeviceInfoActivity.INTENT_KEY, selectedDevice);
                i.putExtra("user_name", us_name);
                i.putExtra("user_number", us_number);
                startActivity(i);
            }
        });
        new_train.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Activity_User.this, DeviceInfoActivity.class);
                i.putExtra(DeviceInfoActivity.INTENT_KEY, selectedDevice);
                i.putExtra("user_name", us_name);
                i.putExtra("user_number", us_number);
                startActivity(i);
            }
        });



    }
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }//禁止上一頁

}