package com.example.fitapp;

import androidx.annotation.NonNull;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.HashMap;

public class AddMeasurements extends AppCompatActivity implements View.OnClickListener {
    private EditText date, weight, height, neck, waist, hips;
    private DatePickerDialog picker;
    private Button saveBtn;
    private TextView backBtn;
    private String user_weight, user_height, user_neck, user_waist, user_hips, dateMeasurements;
    private boolean k = true;
    private String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

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
        neck = findViewById(R.id.bust);
        waist = findViewById(R.id.waist);
        hips = findViewById(R.id.hips);
        saveBtn = findViewById(R.id.saveBtn_personalInfo);
        backBtn = findViewById(R.id.back_personal_info);

        date.setOnClickListener(this);
        saveBtn.setOnClickListener(this);
        backBtn.setOnClickListener(this);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.date:
                setDate();
                break;
            case R.id.saveBtn_personalInfo:
                addData();
                startActivity(new Intent(AddMeasurements.this, Measurements.class));
                finish();
                break;
            case R.id.back_personal_info:
                startActivity(new Intent(this, Measurements.class));
                break;
        }
    }

    private void setDate(){
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

    private void addData(){
        dateMeasurements = date.getText().toString();
        user_weight = weight.getText().toString().trim();
        user_height = height.getText().toString().trim();
        user_neck = neck.getText().toString().trim();
        user_waist = waist.getText().toString().trim();
        user_hips = hips.getText().toString().trim();

        HashMap<String, Object> map = new HashMap<>();
        map.put("date", dateMeasurements);
        map.put("weight", user_weight);
        map.put("height", user_height);
        map.put("neck", user_neck);
        map.put("waist", user_waist);
        map.put("hips", user_hips);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("measurements").child(userID);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String dateString = String.valueOf(dataSnapshot.child("date").getValue());

                    if (dateMeasurements.equals(dateString)) {
                        String key = dataSnapshot.getKey();
                        databaseReference.child(key).updateChildren(map);
                        k = false;
                    }
                }

                if(k){
                    databaseReference.push().updateChildren(map);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AddMeasurements.this, "Something went wrong!", Toast.LENGTH_LONG).show();
            }
        });
    }
}