package com.example.fitapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;


import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class AlertSetting extends AppCompatActivity {
    private ListView listView;
    private ArrayList<String> hours = new ArrayList<>();
    private ArrayAdapter<String> items;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private SwitchCompat switchCompat;
    private String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
    SimpleDateFormat f24Hours = new SimpleDateFormat("HH:mm");
    SimpleDateFormat f12Hours = new SimpleDateFormat("hh:mm aa");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        setContentView(R.layout.activity_alert_setting);

        listView = findViewById(R.id.listView);
        switchCompat = findViewById(R.id.switch_btn);

        createNotificationChannel();

        String value = "";
        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
             value = bundle.getString("cod");
        }

        if(value.equals("x")){
            hours = bundle.getStringArrayList("arrayHours");
            for (String hour : hours) {
                String newValue = hour.toUpperCase(Locale.ROOT);
                hours.set(hours.indexOf(hour), newValue);
            }
            items = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1 ,hours);
            listView.setAdapter(items);
            saveData();
        }else {
                loadData();
                if(hours == null){
                }else {
                    items = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, hours);
                    listView.setAdapter(items);
                }
        }

        SharedPreferences prefs = getSharedPreferences(userID, MODE_PRIVATE);
        switchCompat.setChecked(prefs.getBoolean("value", true));

        switchCompat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(switchCompat.isChecked()){
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putBoolean("value", true);
                    editor.apply();
                    switchCompat.setChecked(true);

                    for (String h : hours) {
                        String time = "";
                        try {
                            time = f24Hours.format(f12Hours.parse(h));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        String[] parts = time.split(":");
                        int hour = Integer.parseInt(parts[0]);
                        int minute = Integer.parseInt(parts[1]);
                        Log.d("ceva", " ora = " + time + hour + minute);
                        setAlarm(hour,minute);
                    }
                } else{
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putBoolean("value", false);
                    editor.apply();
                    switchCompat.setChecked(false);
                    cancelAlarm();
                }
            }
        });
    }

    private void setAlarm(int h, int m){
        final int id = (int) System.currentTimeMillis();

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, h);
        calendar.set(Calendar.MINUTE, m);
        calendar.set(Calendar.SECOND, 0);

        long alarmStartTime = calendar.getTimeInMillis();

        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this, id, intent, 0);

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, alarmStartTime, AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    private void cancelAlarm(){
        Intent intent = new Intent(this, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

        if(alarmManager == null){
            alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        }

        alarmManager.cancel(pendingIntent);
        Toast.makeText(AlertSetting.this, "Alarm canceled!", Toast.LENGTH_SHORT).show();

    }


    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "appWaterReminderChannel";
            String description = "Channel For Alarm Manager";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("water", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void saveData(){
        SharedPreferences prefs = getSharedPreferences(userID, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(hours);
        editor.putString("list", json);
        editor.apply();
    }

    private void loadData(){
        SharedPreferences prefs = getSharedPreferences(userID, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = prefs.getString("list", null);
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        hours = gson.fromJson(json, type);
    }
}