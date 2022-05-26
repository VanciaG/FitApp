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
    private String childKey, weight, height, gender, neck, waist, hips, age;
    private EditText heightM, genderM, neckM, waistM, hipsM, currentWeight, idealWeight, weightType, bmi, bodyFat, bodyFatMass, bodyFatType, leanBodyMass;
    private static final int INCH_PER_FEET = 12;
    private static final int MIN_HEIGHT_FEET = 5;
    private static final double KG_PER_EVERY_INCH = 2.3;
    private static final double KG_MEN = 50.0;
    private static final double KG_WOMEN = 45.5;

    int feetPart;
    double inchesPart, BMI, BFP, BFM, LBM;


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
        bmi = findViewById(R.id.bmi);
        weightType = findViewById(R.id.weightType);
        bodyFat = findViewById(R.id.bodyFat);
        bodyFatType = findViewById(R.id.bodyFatType);
        bodyFatMass = findViewById(R.id.bodyFatMass);
        leanBodyMass = findViewById(R.id.leanBodyMass);

        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        getWeight(userID);
        getData(userID);

        calculateIdealWeightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calculateIdealBodyWeight();
                calculateBodyMassIndex();
                calculateBodyFatPercentageAndMass();
                calculateLeanBodyMass();
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
        BMI = weightBMI * 10000 / ( heightBMI * heightBMI) ;

        bmi.setText(String.valueOf(Math.floor(BMI * 10) / 10.0));

        if(BMI < 18.5)
        {
            currentWeight.setTextColor(Color.parseColor("#0000FF"));
            weightType.setTextColor(Color.parseColor("#0000FF"));
            weightType.setText(R.string.underweight);
        }
        else if(BMI < 25)
        {
            currentWeight.setTextColor(Color.parseColor("#00D100"));
            weightType.setText(R.string.normal);
            weightType.setTextColor(Color.parseColor("#00D100"));
        }
        else if(BMI < 30)
        {
            currentWeight.setTextColor(Color.parseColor("#FFA500"));
            weightType.setTextColor(Color.parseColor("#FFA500"));
            weightType.setText(R.string.overweight);
        }
        else
        {
            currentWeight.setTextColor(Color.parseColor("#FF0000"));
            weightType.setTextColor(Color.parseColor("#FF0000"));
            weightType.setText(R.string.obese);
        }



    }

    private void calculateBodyFatPercentageAndMass(){

        double ageBFP = Double.parseDouble(age);
        double weightBFP = Double.parseDouble(weight);
        int g = 0;

        if(gender.equals("Female")) g = 1;

        BFP = (0.503 * ageBFP) + (10.689 * g) + (3.172 * BMI) - (0.026 * BMI * BMI) +
                (0.181 * BMI * g) - (0.02 * BMI * ageBFP) - (0.005 * BMI * BMI * g) +
                (0.00021 * BMI * BMI * ageBFP) - 44.988;

        BFM = BFP / 100 * weightBFP;

        bodyFatMass.setText(String.valueOf(Math.floor(BFM * 10) / 10.0));
        bodyFat.setText(String.valueOf(Math.floor(BFP * 10) / 10.0));

        if( g==0 ){

            if(BFP > 2 && BFP < 5 )
            {
                bodyFatType.setText(R.string.essentialFat);
                bodyFatType.setTextColor(Color.parseColor("#0000FF"));
            }
            else if(BFP < 13)
            {
                bodyFatType.setText(R.string.athletes);
                bodyFatType.setTextColor(Color.parseColor("#00D100"));
            }
            else if(BFP < 17)
            {
                bodyFatType.setText(R.string.fitness);
                bodyFatType.setTextColor(Color.parseColor("#127E0B"));
            }
            else if(BFP < 24)
            {
                bodyFatType.setText(R.string.average);
                bodyFatType.setTextColor(Color.parseColor("#FFA500"));
            }
            else {
                bodyFatType.setText(R.string.obese);
                bodyFatType.setTextColor(Color.parseColor("#FF0000"));
            }

        }else {

            if(BFP > 10 && BFP < 13 )
            {
                bodyFatType.setText(R.string.essentialFat);
                bodyFatType.setTextColor(Color.parseColor("#0000FF"));
            }
            else if(BFP < 20)
            {
                bodyFatType.setText(R.string.athletes);
                bodyFatType.setTextColor(Color.parseColor("#00D100"));
            }
            else if(BFP < 24)
            {
                bodyFatType.setText(R.string.fitness);
                bodyFatType.setTextColor(Color.parseColor("#127E0B"));
            }
            else if(BFP < 31)
            {
                bodyFatType.setText(R.string.average);
                bodyFatType.setTextColor(Color.parseColor("#FFA500"));
            }
            else {
                bodyFatType.setText(R.string.obese);
                bodyFatType.setTextColor(Color.parseColor("#FF0000"));
            }
        }

    }

    private void calculateLeanBodyMass(){

        double weightLBM = Double.parseDouble(weight);

        LBM = weightLBM - BFM;

        leanBodyMass.setText(String.valueOf(Math.floor(LBM * 10) / 10.0));
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
                    age = String.valueOf(snapshot.child("age").getValue());
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