package com.noahliu.ble_example.Controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.noahliu.ble_example.Module.Enitiy.ScannedData;
import com.noahliu.ble_example.R;

import java.util.ArrayList;

public class Login_Activity extends AppCompatActivity {
    private Button login;
    private Button cancel;
    private Button new_button;
    private EditText account;
    private EditText number;
    private EditText birthday;
    private EditText gender;
    private EditText height;
    private EditText weight;
    private EditText hand;
    private SQLiteDatabase db;
    private Database _DB;
    private TextView account_err;
    private TextView number_err;
    private TextView birthday_err;
    private TextView gender_err;
    private TextView height_err;
    private TextView weight_err;
    private TextView hand_err;
    private TextView found_err;
    private LinearLayout lay_birthday;
    private LinearLayout lay_gender;
    private LinearLayout lay_height;
    private LinearLayout lay_weight;
    private LinearLayout lay_hand;



    public static final String INTENT_KEY = "GET_DEVICE";
    private ScannedData selectedDevice;

    private String user,userNumber,userGender,userHeight,userWeight,userBirthday,userHand;
    private int reg=0,old_new=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        selectedDevice = (ScannedData) getIntent().getSerializableExtra(INTENT_KEY);

        _DB=new Database(this);
        db=_DB.getWritableDatabase();

        login=findViewById(R.id.login_button);
        cancel=findViewById(R.id.cancel_button);
        new_button=findViewById(R.id.new_button);
        account=findViewById(R.id.account_edit);
        number=findViewById(R.id.number_edit);
        birthday=findViewById(R.id.birthday_edit);
        gender=findViewById(R.id.gender_edit);
        height=findViewById(R.id.height_edit);
        weight=findViewById(R.id.weight_edit);
        hand=findViewById(R.id.hand_edit);
        account_err=findViewById(R.id.account_error);
        number_err=findViewById(R.id.number_error);
        birthday_err=findViewById(R.id.birthday_error);
        gender_err=findViewById(R.id.gender_error);
        height_err=findViewById(R.id.height_error);
        weight_err=findViewById(R.id.weight_error);
        hand_err=findViewById(R.id.hand_error);
        lay_birthday=findViewById(R.id.lay_birthday);
        lay_gender=findViewById(R.id.lay_gender);
        lay_height=findViewById(R.id.lay_height);
        lay_weight=findViewById(R.id.lay_weight);
        lay_hand=findViewById(R.id.lay_hand);
        found_err=findViewById(R.id.user_notfond);


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                old_new=0;
                cancel.setVisibility(View.INVISIBLE);
                new_button.setVisibility(View.VISIBLE);
                lay_birthday.setVisibility(View.INVISIBLE);
                lay_gender.setVisibility(View.INVISIBLE);
                lay_height.setVisibility(View.INVISIBLE);
                lay_weight.setVisibility(View.INVISIBLE);
                lay_hand.setVisibility(View.INVISIBLE);
            }
        });
        new_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancel.setVisibility(View.VISIBLE);
                new_button.setVisibility(View.INVISIBLE);
                lay_birthday.setVisibility(View.VISIBLE);
                lay_gender.setVisibility(View.VISIBLE);
                lay_height.setVisibility(View.VISIBLE);
                lay_weight.setVisibility(View.VISIBLE);
                lay_hand.setVisibility(View.VISIBLE);
                found_err.setVisibility(View.INVISIBLE);

                old_new=1;
                reg=0;
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                found_err.setVisibility(View.INVISIBLE);
                if(old_new==0) {
                    int err_test = 0;
                    //姓名驗證
                    user = account.getText().toString();
                    if (user.matches("") || user == null) {
                        account_err.setText("請輸入姓名");
                        err_test++;
                    } else {
                        account_err.setText("");


                    }
                    //學號驗證
                    userNumber = number.getText().toString();
                    if (userNumber.matches("") || userNumber == null) {
                        number_err.setText("請輸入學號");
                        err_test++;
                    } else {
                        number_err.setText("");
                    }


                    if (err_test == 0) {
                        //資料庫走訪
                        String select="select * from TB1";
                        Cursor cursor=db.rawQuery(select,null);
                        ArrayList<String> list_user=new ArrayList<>();
                        ArrayList<String> list_number=new ArrayList<>();
                        while ((cursor.moveToNext())){
                            String item_name=cursor.getString(1);
                            list_user.add(item_name);
                            String item_number=cursor.getString(2);
                            list_number.add(item_number);
                        }
                        cursor.close();
                        //陣列要return出來

                        if (user.equals("123")) {
                            Intent intent = new Intent(Login_Activity.this, Activity_User.class);
                            intent.putExtra(DeviceInfoActivity.INTENT_KEY, selectedDevice);
                            intent.putExtra("user_name", user);
                            intent.putExtra("user_number", userNumber);
                            intent.putExtra("user_gender", "0");
                            intent.putExtra("user_birthday", "2000/01/01");
                            intent.putExtra("user_height", "180");
                            intent.putExtra("user_weight", "60");
                            intent.putExtra("user_hand", "right");
                            startActivity(intent);
                        }else {


                            for (int i = 0; i < list_user.size(); i++) {
                                if (user.equals(list_user.get(i)) && userNumber.equals(list_number.get(i))) {
                                    found_err.setVisibility(View.INVISIBLE);
                                    Intent intent = new Intent(Login_Activity.this, Activity_User.class);
                                    intent.putExtra(DeviceInfoActivity.INTENT_KEY, selectedDevice);
                                    intent.putExtra("user_name", user);
                                    intent.putExtra("user_number", userNumber);
                                    startActivity(intent);
                                } else {
                                    found_err.setVisibility(View.VISIBLE);
                                    found_err.setText("姓名或學號錯誤/或尚未註冊");
                                }
                            }
                        }

                    }



                }else{
                    int err_test = 0;
                    //姓名驗證
                    user = account.getText().toString();
                    if (user.matches("") || user == null) {
                        account_err.setText("請輸入姓名");
                        err_test++;
                    } else {
                        account_err.setText("");
                    }
                    //學號驗證
                    userNumber = number.getText().toString();
                    if (userNumber.matches("") || userNumber == null) {
                        number_err.setText("請輸入學號");
                        err_test++;
                    } else {
                        number_err.setText("");
                    }
                    //性別驗證
                    userGender = gender.getText().toString();
                    if (userGender.matches("") || userGender == null) {
                        gender_err.setText("請輸入性別");
                        err_test++;
                    } else {
                        gender_err.setText("");
                    }
                    //生日驗證
                    userBirthday = birthday.getText().toString();
                    if (userBirthday.matches("") || userBirthday == null) {
                        birthday_err.setText("請輸入生日");
                        err_test++;
                    } else {
                        birthday_err.setText("");
                    }
                    //身高驗證
                    userHeight = height.getText().toString();
                    if (userHeight.matches("") || userHeight == null) {
                        height_err.setText("請輸入身高");
                        err_test++;
                    } else {
                        height_err.setText("");
                    }
                    //體重驗證
                    userWeight = weight.getText().toString();
                    if (userWeight.matches("") || userWeight == null) {
                        weight_err.setText("請輸入體重");
                        err_test++;
                    } else {
                        weight_err.setText("");
                    }
                    //慣用手驗證
                    userHand = hand.getText().toString();
                    if (userHand.matches("") || userHand == null) {
                        hand_err.setText("請輸入慣用手");
                        err_test++;
                    } else {
                        hand_err.setText("");
                    }

                    if (err_test == 0) {
                        if(old_new==0){
                            Intent i = new Intent(Login_Activity.this, Activity_User.class);
                            i.putExtra(DeviceInfoActivity.INTENT_KEY, selectedDevice);
                            i.putExtra("user_name", user);
                            i.putExtra("user_number", userNumber);
                            startActivity(i);
                        }else {
                            //輸入資料表之前要先確認是否註冊過
                            String select="select * from TB1";
                            Cursor cursor=db.rawQuery(select,null);
                            ArrayList<String> list_user=new ArrayList<>();
                            ArrayList<String> list_number=new ArrayList<>();
                            while ((cursor.moveToNext())){
                                String item_name=cursor.getString(1);
                                list_user.add(item_name);
                                String item_number=cursor.getString(2);
                                list_number.add(item_number);
                            }
                            cursor.close();
                            for(int i=0;i<list_user.size();i++){
                                if(user== list_user.get(i) && userNumber== list_number.get(i)){
                                    found_err.setVisibility(View.VISIBLE);
                                    found_err.setText("姓名及學號已註冊過");
                                }else{
                                    found_err.setVisibility(View.INVISIBLE);
                                    user_data();
                                    Intent intent = new Intent(Login_Activity.this, Activity_User.class);
                                    intent.putExtra(DeviceInfoActivity.INTENT_KEY, selectedDevice);
                                    intent.putExtra("user_name", user);
                                    intent.putExtra("user_number", userNumber);
                                    intent.putExtra("user_gender", userGender);
                                    intent.putExtra("user_birthday", userBirthday);
                                    intent.putExtra("user_height", userHeight);
                                    intent.putExtra("user_weight", userWeight);
                                    intent.putExtra("user_hand", userHand);
                                    startActivity(intent);
                                }
                            }

                        }
                    }
                }


            }
        });
    }
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }//禁止上一頁
    private void user_data(){
        ContentValues cv=new ContentValues();
        cv.put("_name",user);
        cv.put("_number",userNumber);
        cv.put("_gender",userGender);
        cv.put("_birthday",userBirthday);
        cv.put("_height",userHeight);
        cv.put("_weight",userWeight);
        cv.put("_hand",userHand);
        db.insert("TB2",null,cv);
        Toast.makeText(getApplicationContext(), "新增成功", Toast.LENGTH_SHORT).show();
        db.close();

        ContentValues cv1=new ContentValues();
        cv1.put("_name",user);
        cv1.put("_number",userNumber);
        db.insert("TB1",null,cv1);
        Toast.makeText(getApplicationContext(), "新增成功", Toast.LENGTH_SHORT).show();
        db.close();

    }
}