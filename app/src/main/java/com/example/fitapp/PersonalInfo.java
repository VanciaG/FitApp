package com.example.fitapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PersonalInfo extends AppCompatActivity implements View.OnClickListener{
    private TextView backBtn;
    private Button savePersonalInfo;
    private EditText name, age;
    private RadioGroup gender, activityLevel;
    private RadioButton radioButtonGenderSelected, radioButtonActivitySelected;
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
        //name = findViewById(R.id.name);
        age = findViewById(R.id.age);
        gender = findViewById(R.id.profile_gender);
        activityLevel = findViewById(R.id.activity_level);

        backBtn.setOnClickListener(this);
        savePersonalInfo.setOnClickListener(this);

    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_personal_info:
                Toast.makeText(PersonalInfo.this, "Please verify your email address!", Toast.LENGTH_LONG).show();
                finish();
                break;
            case R.id.saveBtn_personalInfo:
                Toast.makeText(PersonalInfo.this, "intra!", Toast.LENGTH_LONG).show();
                String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                addData(userID);
                //showData(userID);
                //startActivity(new Intent(this, ProfileFragment.class));
                finish();
                break;
        }
    }

    private void addData(String userID){
        Toast.makeText(PersonalInfo.this, "aic!", Toast.LENGTH_LONG).show();
        //String username = name.getText().toString().trim();
        String user_age = age.getText().toString().trim();
        int selectedGenderID = gender.getCheckedRadioButtonId();
        radioButtonGenderSelected = findViewById(selectedGenderID);
        int selectedActivityID = activityLevel.getCheckedRadioButtonId();
        radioButtonActivitySelected = findViewById(selectedActivityID);
        String textGender = radioButtonGenderSelected.getText().toString();
        String textActivity = radioButtonActivitySelected.getText().toString();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");
        databaseReference.child(userID).child("age").setValue(user_age);
        databaseReference.child(userID).child("gender").setValue(textGender);
        databaseReference.child(userID).child("activity").setValue(textActivity);

    }


    /*private void showData(String userID){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        databaseReference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User readWriteUserDetails = snapshot.getValue(User.class);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }*/

}