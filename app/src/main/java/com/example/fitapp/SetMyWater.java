package com.example.fitapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TimePicker;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SetMyWater extends AppCompatActivity {
    private EditText myWeight, defaultQuantity, suggestedQuantity, wakeUpTime, sleepTime;
    private Button calculateBtn, setMyWater;
    private RadioGroup weather;
    private RadioButton radioButtonWeatherSelected;
    private String weight, gender, activityLevel, childKey, textWeather, quantity;
    int t1Hour, t1Minute, t2Hour, t2Minute, water = 0, default_quantity = 200;
    private Date date1, date2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(1);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        setContentView(R.layout.activity_set_my_water);

        myWeight = findViewById(R.id.my_weight);
        defaultQuantity = findViewById(R.id.default_quantity);
        suggestedQuantity = findViewById(R.id.suggested_quantity);
        wakeUpTime = findViewById(R.id.time1);
        sleepTime = findViewById(R.id.time2);
        weather = findViewById(R.id.weather_type);
        calculateBtn = findViewById(R.id.calculate);
        setMyWater = findViewById(R.id.saveBtn_water);

        defaultQuantity.setText(String.valueOf(default_quantity));

        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        getWeight(userID);
        getData(userID);
        readWeather();

        wakeUpTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(SetMyWater.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                                t1Hour = i;
                                t1Minute = i1;
                                String time = t1Hour + ":" + t1Minute;
                                SimpleDateFormat f24Hours = new SimpleDateFormat("HH:mm");
                                try {
                                     date1= f24Hours.parse(time);
                                     SimpleDateFormat f12Hours = new SimpleDateFormat("hh:mm aa");
                                     wakeUpTime.setText(f12Hours.format(date1));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, 12, 0, false);
                timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                timePickerDialog.updateTime(t1Hour,t1Minute);
                timePickerDialog.show();
            }
        });


        sleepTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(SetMyWater.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                                t2Hour = i;
                                t2Minute = i1;
                                String time = t2Hour + ":" + t2Minute;
                                SimpleDateFormat f24Hours = new SimpleDateFormat("HH:mm");
                                try {
                                    date2= f24Hours.parse(time);
                                    SimpleDateFormat f12Hours = new SimpleDateFormat("hh:mm aa");
                                    sleepTime.setText(f12Hours.format(date2));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, 12, 0, false);
                timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                timePickerDialog.updateTime(t2Hour,t2Minute);
                timePickerDialog.show();
            }
        });

        calculateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calculateWater(gender, weight, activityLevel);
            }
        });

        setMyWater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quantity = defaultQuantity.getText().toString().trim();
                default_quantity = Integer.parseInt(quantity);
                int a = (int) Math.round((double) water / default_quantity); //de cate ori
                long diff = Math.abs(date2.getTime() - date1.getTime());
                long diffMinutes = (diff / (60 * 1000));
                int b = (int) Math.round((double) diffMinutes / a); //minute

                Log.d("ceva", "de cate ori = " + a);
                Log.d("ceva", "diferenta intre ore in min = " + diffMinutes);
                Log.d("ceva", "tot la cate min = " + b);
                Log.d("ceva", "d1 = " + date1);
                Log.d("ceva", "d2 = " + date2);


                ArrayList<String> hours = new ArrayList<>();
                SimpleDateFormat f24Hours = new SimpleDateFormat("HH:mm");
                SimpleDateFormat f12Hours = new SimpleDateFormat("hh:mm aa");
                Calendar cal = Calendar.getInstance();
                cal.setTime(date1);
                for (int i=0; i < a; i++) {
                    cal.add(Calendar.MINUTE, b);
                    String newTime = f12Hours.format(cal.getTime());
                    hours.add(newTime);
                    //Log.d("ceva", "d1 = " + newTime);
                    try {
                        date1 = f24Hours.parse(newTime);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }


                //aici trebuie sa fac partea de trimitere de notificari


                Intent intent = new Intent(SetMyWater.this, AlertSetting.class);
                intent.putExtra("arrayHours", hours);
                intent.putExtra("cod", "x");
                startActivity(intent);
                finish();

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
                     myWeight.setText(weight);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SetMyWater.this, "Something went wrong!", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getWeight(String userID){
        HashMap<String, Date> map = new HashMap<>();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("measurements").child(userID);
        databaseReference.keepSynced(true);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

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
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SetMyWater.this, "Something went wrong!", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getData(String userID){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userID);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                gender = String.valueOf(snapshot.child("gender").getValue());
                activityLevel = String.valueOf(snapshot.child("activityLevel").getValue());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SetMyWater.this, "Something went wrong!", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void readWeather(){
        Log.d("ceva", "aici" );
        int selectedWeatherID = weather.getCheckedRadioButtonId();
        radioButtonWeatherSelected = findViewById(selectedWeatherID);
        Log.d("ceva", "aici1" );
        //textWeather = radioButtonWeatherSelected.getText().toString();
        //String textWeather = radioButtonWeatherSelected.getText().toString();
        //Log.d("ceva", "aici2" + textWeather);
       // Log.d("ceva", "aici2" + textWeather);

        /*if(textWeather.equals("Hot")){
            radioButtonWeatherSelected = findViewById(R.id.radio_hot);
        } else if (textWeather.equals("Cold")) {
            radioButtonWeatherSelected = findViewById(R.id.radio_cold);
        }*/
    }


    private void calculateWater (String gender, String weight, String activityLevel){
        if(gender.equals("Female")){
            if(activityLevel.equals("Sedentary") || activityLevel.equals("Lightly Active")) {
                    water = Integer.parseInt(weight) * 38;
            }else {
                    water = Integer.parseInt(weight) * 40;
            }
        } else {
            if(activityLevel.equals("Sedentary") || activityLevel.equals("Lightly Active")) {
                water = Integer.parseInt(weight) * 43;
            }else {
                water = Integer.parseInt(weight) * 45;
            }
        }
        suggestedQuantity.setText(String.valueOf(water));
    }

}

