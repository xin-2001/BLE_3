package com.noahliu.ble_example;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class Result_Activity extends AppCompatActivity {
    private TextView max;
    private TextView min;
    private TextView avg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        max=findViewById(R.id.max_);
        min=findViewById(R.id.min_);
        avg=findViewById(R.id.avg_);

        Intent i=this.getIntent();
        String Max=i.getStringExtra("MAX");
        String Min=i.getStringExtra("MIN");
        String Avg=i.getStringExtra("AVG");
        max.append(Max);
        min.append(Min);
        avg.append(Avg);
    }
}