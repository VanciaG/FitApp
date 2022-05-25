package com.example.fitapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
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

public class IdealMeasurements extends AppCompatActivity {

    private Button calculateIdealWeightBtn;
    private String childKey, weight, height, gender, neck, waist, hips;
    private EditText heightM, genderM, neckM, waistM, hipsM, currentWeight, idealWeight, bmiText;
    private static final int INCH_PER_FEET = 12;
    private static final int MIN_HEIGHT_FEET = 5;
    private static final double KG_PER_EVERY_INCH = 2.3;
    private static final double KG_MEN = 50.0;
    private static final double KG_WOMEN = 45.5;

    int feetPart;
    double inchesPart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(1);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

        setContentView(R.layout.activity_ideal_measurements);

        calculateIdealWeightBtn = findViewById(R.id.calculate);
        heightM = findViewById(R.id.height);
        genderM = findViewById(R.id.gender);
        neckM = findViewById(R.id.neck);
        waistM = findViewById(R.id.waist);
        hipsM = findViewById(R.id.hips);
        currentWeight = findViewById(R.id.weight);
        idealWeight = findViewById(R.id.idealWeight);
        bmiText = findViewById(R.id.bmi);

        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        getWeight(userID);
        getData(userID);

        calculateIdealWeightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calculateIdealBodyWeight();
                calculateBodyMassIndex();
            }
        });

    }

    private void convertCentimeterToFeetAndInches(){

        double dCentimeter = Double.parseDouble(height);
        feetPart = (int) Math.floor((dCentimeter / 2.54) / 12);
        inchesPart = (dCentimeter / 2.54) - (feetPart * 12);
    }

    private void calculateIdealBodyWeight(){
        int idealBodyWeight;
        convertCentimeterToFeetAndInches();

        if(gender.equals("Female")){
            idealBodyWeight =  (int) Math.round(KG_WOMEN + (feetPart - MIN_HEIGHT_FEET) * INCH_PER_FEET * KG_PER_EVERY_INCH + inchesPart * KG_PER_EVERY_INCH);
        }else{
            idealBodyWeight =  (int) Math.round(KG_MEN + (feetPart - MIN_HEIGHT_FEET) * INCH_PER_FEET * KG_PER_EVERY_INCH + inchesPart * KG_PER_EVERY_INCH);
        }

        idealWeight.setText(String.valueOf(idealBodyWeight));
    }

    private void calculateBodyMassIndex(){
        double heightBMI = Double.parseDouble(height);
        double weightBMI = Double.parseDouble(weight);
        double BMI = weightBMI * 10000 / ( heightBMI * heightBMI) ;

        if(BMI < 18.5)
        {
            currentWeight.setTextColor(Color.parseColor("#0000FF"));
            bmiText.setText("Underweight...Being underweight could be a sign you're not eating enough or you may be ill.");
        }
        else if(BMI < 25)
        {
            currentWeight.setTextColor(Color.parseColor("#00D100"));
            bmiText.setText("Normal...Keep up the good work!");
        }
        else if(BMI < 30)
        {
            currentWeight.setTextColor(Color.parseColor("#FFA500"));
            bmiText.setText("Overweight");
        }
        else
        {
            currentWeight.setTextColor(Color.parseColor("#FF0000"));
            bmiText.setText("Obese");
        }

    }

    private void getWeight(String userID){
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

                    showWeight(userID);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(IdealMeasurements.this, "Something went wrong!", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showWeight(String userID){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("measurements").child(userID);
        databaseReference.keepSynced(true);
        Query query = databaseReference.orderByKey().equalTo(childKey);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    weight = String.valueOf(dataSnapshot.child("weight").getValue());
                    height = String.valueOf(dataSnapshot.child("height").getValue());
                    neck = String.valueOf(dataSnapshot.child("neck").getValue());
                    waist = String.valueOf(dataSnapshot.child("waist").getValue());
                    hips = String.valueOf(dataSnapshot.child("hips").getValue());

                    currentWeight.setText(weight);
                    heightM.setText(height);
                    neckM.setText(neck);
                    waistM.setText(waist);
                    hipsM.setText(hips);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(IdealMeasurements.this, "Something went wrong!", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getData(String userID){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userID);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() != null) {
                    gender = String.valueOf(snapshot.child("gender").getValue());
                    genderM.setText(gender);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(IdealMeasurements.this, "Something went wrong!", Toast.LENGTH_LONG).show();
            }
        });
    }
}