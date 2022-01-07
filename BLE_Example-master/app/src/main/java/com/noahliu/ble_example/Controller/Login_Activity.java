package com.noahliu.ble_example.Controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.noahliu.ble_example.Module.Enitiy.ScannedData;
import com.noahliu.ble_example.R;

public class Login_Activity extends AppCompatActivity {
    private Button login;
    private Button register;
    private Button back_button;
    private TextView check_pass_text;
    private EditText check_pass_edit;
    private EditText account;
    private EditText password;
    private SQLiteDatabase SQL_db;
    private Database _DB;
    private TextView account_err;
    private TextView password_err;
    private TextView check_pass_err;

    public static final String INTENT_KEY = "GET_DEVICE";
    private ScannedData selectedDevice;

    private String user,pass,check_pass;
    private int reg=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        selectedDevice = (ScannedData) getIntent().getSerializableExtra(INTENT_KEY);

        login=findViewById(R.id.login_button);
        register=findViewById(R.id.register_button);
        back_button=findViewById(R.id.back_button);
        check_pass_text=findViewById(R.id.check_pass_text);
        check_pass_edit=findViewById(R.id.check_pass_edit);
        account=findViewById(R.id.account_edit);
        password=findViewById(R.id.password_edit);
        account_err=findViewById(R.id.account_error);
        password_err=findViewById(R.id.password_error);
        check_pass_err=findViewById(R.id.check_pass_error);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(reg==0) {
                    check_pass_edit.setVisibility(View.VISIBLE);
                    check_pass_edit.setVisibility(View.VISIBLE);
                    login.setVisibility(View.INVISIBLE);
                    back_button.setVisibility(View.VISIBLE);
                    reg++;
                }else{
                    user=account.getText().toString();
                    pass=password.getText().toString();
                    check_pass=check_pass_edit.getText().toString();
                    if(user==""){
                        account_err.setText("請輸入帳號");
                    }
                    if(pass==""){
                        password.setText("請輸入密碼");
                    }
                    if(check_pass==""){
                        check_pass_edit.setText("請再次輸入密碼");
                    }
                    if(user!=""&&pass!=""&&check_pass!=""){
                          //走訪user是否有被用過
                            //沒有->確認pass==check_pass

                    }
                }

            }
        });
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check_pass_edit.setVisibility(View.INVISIBLE);
                check_pass_edit.setVisibility(View.INVISIBLE);
                login.setVisibility(View.VISIBLE);
                back_button.setVisibility(View.INVISIBLE);
                reg=0;
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user=account.getText().toString();
                pass=password.getText().toString();
                if(user==""){
                    account_err.setText("請輸入帳號");
                }
                if(pass==""){
                    password.setText("請輸入密碼");
                }
                if(user!=""&&pass!=""){
                    //確認user是否註冊過
                      //確認pass是否正確
                    Intent i=new Intent(Login_Activity.this,DeviceInfoActivity.class);
                    i.putExtra(DeviceInfoActivity.INTENT_KEY,selectedDevice);
                    startActivity(i);
                }


            }
        });
    }
}