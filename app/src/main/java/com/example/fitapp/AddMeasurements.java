package com.example.fitapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;

public class AddMeasurements extends AppCompatActivity {
    private EditText date, weight, height, bodyFatPercentage;
    private DatePickerDialog picker;
    private Button saveBtn;
    private String user_weight, user_height, user_body_fat, dateMeasurements;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(1);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        getWindow().setStatusBarColor(Color.TRANSPARENT);
        setContentView(R.layout.activity_add_measurements);

        date = findViewById(R.id.date);
        weight = findViewById(R.id.weight);
        height = findViewById(R.id.height);
        bodyFatPercentage = findViewById(R.id.body_fat);
        saveBtn = findViewById(R.id.saveBtn_personalInfo);

        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                picker = new DatePickerDialog(AddMeasurements.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        date.setText(day + "/" + (month + 1) + "/" + year);
                    }
                }, year, month, day);
                picker.show();
            }
        });


        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addData(userID);
                startActivity(new Intent(AddMeasurements.this, Measurements.class));
                finish();
            }
        });
    }


    private void addData(String userID){
        dateMeasurements = date.getText().toString();
        user_weight = weight.getText().toString().trim();
        user_height = height.getText().toString().trim();
        user_body_fat = bodyFatPercentage.getText().toString().trim();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("measurements");

        HashMap<String, Object> map = new HashMap<>();
        map.put("date", dateMeasurements);
        map.put("weight", user_weight);
        map.put("height", user_height);
        map.put("bodyFatPercentage", user_body_fat);

        databaseReference.child(userID).push().updateChildren(map);

    }
}