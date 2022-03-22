package com.example.fitapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Measurements extends AppCompatActivity {

    private Button addMeasurementsBtn;
    private TextView backPersonalInfoBtn, dateW, dateH, dateP;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(1);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        setContentView(R.layout.activity_measurements);

        addMeasurementsBtn = findViewById(R.id.addBtn);
        backPersonalInfoBtn = findViewById(R.id.back_personal_info);
        dateW = findViewById(R.id.date1);
        dateH = findViewById(R.id.date2);
        dateP = findViewById(R.id.date3);

        String date = getIntent().getStringExtra("date");
        dateW.setText(date);
        dateH.setText(date);
        dateP.setText(date);

        addMeasurementsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AddMeasurements.class));
                finish();
            }
        });

        backPersonalInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


}