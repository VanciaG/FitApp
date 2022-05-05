package com.example.fitapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class PersonalInfo extends AppCompatActivity{
    private TextView backBtn, userNameProfile;
    private Button savePersonalInfo;
    private EditText name, age;
    private RadioGroup gender, activityLevel;
    private RadioButton radioButtonGenderSelected, radioButtonActivitySelected;
    private String username, textGender, user_age, textActivity;
    private Bundle bundle = new Bundle();
    //private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(1);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

        setContentView(R.layout.activity_personal_info);

        backBtn = findViewById(R.id.back_personal_info);
        savePersonalInfo = findViewById(R.id.saveBtn_personalInfo);
        name = findViewById(R.id.name);
        age = findViewById(R.id.age);
        gender = findViewById(R.id.profile_gender);
        activityLevel = findViewById(R.id.activity_level);


        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        showData(userID);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        savePersonalInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addData(userID);
                //ProfileFragment profileFragment = new ProfileFragment();
                //profileFragment.setArguments(bundle);
                finish();
            }
        });

    }

    private void addData(String userID){
        username = name.getText().toString().trim();
        user_age = age.getText().toString().trim();
        int selectedGenderID = gender.getCheckedRadioButtonId();
        radioButtonGenderSelected = findViewById(selectedGenderID);
        int selectedActivityID = activityLevel.getCheckedRadioButtonId();
        radioButtonActivitySelected = findViewById(selectedActivityID);
        textGender = radioButtonGenderSelected.getText().toString();
        textActivity = radioButtonActivitySelected.getText().toString();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");

        HashMap<String, Object> map = new HashMap<>();
        map.put("age", user_age);
        map.put("gender", textGender);
        map.put("activityLevel", textActivity);
        map.put("userName", username);

        databaseReference.child(userID).updateChildren(map);

    }


    private void showData(String userID){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userID);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                username = String.valueOf(snapshot.child("userName").getValue());
                user_age = String.valueOf(snapshot.child("age").getValue());
                textGender = String.valueOf(snapshot.child("gender").getValue());
                textActivity = String.valueOf(snapshot.child("activityLevel").getValue());

                if(user_age != null && !user_age.equals("null")){
                    age.setText(user_age);
                }else {
                    age.setText(" ");
                }

                name.setText(username);
                //userNameProfile.setText(username);
                //bundle.putString("user_name", username);



                if(textGender.equals("Male")){
                    radioButtonGenderSelected = findViewById(R.id.radio_male);
                } else{
                    radioButtonGenderSelected = findViewById(R.id.radio_female);
                }

                radioButtonGenderSelected.setChecked(true);


                if(textActivity.equals("Sedentary")){
                    radioButtonActivitySelected = findViewById(R.id.sedentary);
                } else if (textActivity.equals("Lightly Active")){
                    radioButtonActivitySelected = findViewById(R.id.lightly_active);
                }else if (textActivity.equals("Moderately Active")) {
                    radioButtonActivitySelected = findViewById(R.id.moderately_active);
                } else {
                    radioButtonActivitySelected = findViewById(R.id.very_active);
                }

                radioButtonActivitySelected.setChecked(true);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(PersonalInfo.this, "Something went wrong!", Toast.LENGTH_LONG).show();
            }
        });
    }

}