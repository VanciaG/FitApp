package com.example.fitapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Measurements extends AppCompatActivity {

    private Button addMeasurementsBtn;
    private TextView backPersonalInfoBtn, dateW, dateH, dateNeck, dateWaist, dateHips, weight, height, neck, waist, hips;
    private String childKey = new String();
    public Bundle bundle = new Bundle();

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
        dateNeck = findViewById(R.id.date3);
        dateWaist = findViewById(R.id.date4);
        dateHips = findViewById(R.id.date5);
        weight = findViewById(R.id.weightM);
        height = findViewById(R.id.heightM);
        neck = findViewById(R.id.neck);
        waist = findViewById(R.id.waist);
        hips = findViewById(R.id.hips);

        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("measurements").child(userID);

        lastUpdate(userID);

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

    private void lastUpdate(String userID){
        HashMap<String, Date> map = new HashMap<>();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("measurements").child(userID);
        databaseReference.keepSynced(true);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() != null) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        String k = dataSnapshot.getKey();
                        String dateString = String.valueOf(dataSnapshot.child("date").getValue());

                        Date date = null;
                        try {
                            date = format.parse(dateString);
                        } catch (ParseException e) {
                            Log.d("Exception: ", e.getMessage());
                        }

                        map.put(k, date);

                    }

                    Date latest = Collections.max(map.values());

                    for (Map.Entry<String, Date> entry : map.entrySet()) {
                        if ((entry.getValue()).compareTo(latest) == 0) {
                            childKey = entry.getKey();
                        }
                    }

                    showData(userID);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Measurements.this, "Something went wrong!", Toast.LENGTH_LONG).show();
            }
        });
    }


    private void showData(String userID){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("measurements").child(userID);
        databaseReference.keepSynced(true);
        Query query = databaseReference.orderByKey().equalTo(childKey);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    String date = String.valueOf(dataSnapshot.child("date").getValue());
                    String weightM = String.valueOf(dataSnapshot.child("weight").getValue());
                    String heightM = String.valueOf(dataSnapshot.child("height").getValue());
                    String body_neck = String.valueOf(dataSnapshot.child("neck").getValue());
                    String body_waist = String.valueOf(dataSnapshot.child("waist").getValue());
                    String body_hips = String.valueOf(dataSnapshot.child("hips").getValue());

                    dateW.setText(date);
                    dateH.setText(date);
                    dateNeck.setText(date);
                    dateWaist.setText(date);
                    dateHips.setText(date);
                    weight.setText(weightM);
                    height.setText(heightM);
                    neck.setText(body_neck);
                    waist.setText(body_waist);
                    hips.setText(body_hips);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Measurements.this, "Something went wrong!", Toast.LENGTH_LONG).show();
            }
        });
    }


}