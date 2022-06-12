package com.example.fitapp;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;

import android.view.WindowManager;
import android.widget.TextView;


public class ResultBiologicalAge extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

        setContentView(R.layout.activity_result_biological_age);

        TextView result=(TextView)findViewById(R.id.textResult);

        Bundle b = getIntent().getExtras();
        int score= b.getInt("score");
        int age= b.getInt("age");

        int biologicalAge = score + age;

        result.setText(String.valueOf(biologicalAge));



    }

}
