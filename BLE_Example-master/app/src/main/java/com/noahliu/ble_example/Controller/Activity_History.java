package com.noahliu.ble_example.Controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.noahliu.ble_example.Module.Enitiy.ScannedData;
import com.noahliu.ble_example.R;

public class Activity_History extends AppCompatActivity {
    private TextView name;
    private Button back;
    private ListView list;
    private String us_name,us_number;

    public static final String INTENT_KEY = "GET_DEVICE";
    private ScannedData selectedDevice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        selectedDevice = (ScannedData) getIntent().getSerializableExtra(INTENT_KEY);
        Intent i=getIntent();
        us_name=i.getStringExtra("user_name");
        us_number=i.getStringExtra("user_number");

        name=findViewById(R.id.text_name);
        back=findViewById(R.id.back_button);
        list=findViewById(R.id.list);

        name.setText(us_name);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Activity_History.this.finish();//返回上一頁
                /*Intent i = new Intent(Activity_History.this, Activity_User.class);
                i.putExtra(DeviceInfoActivity.INTENT_KEY, selectedDevice);
                i.putExtra("user_name", us_name);
                i.putExtra("user_number", us_number);
                startActivity(i);*/
            }
        });
    }

}