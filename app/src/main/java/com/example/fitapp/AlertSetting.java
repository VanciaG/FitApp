package com.example.fitapp;

import androidx.appcompat.app.AppCompatActivity;


import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class AlertSetting extends AppCompatActivity {
    private ListView listView;
    private ArrayList<String> hours = new ArrayList<>();
    private ArrayAdapter<String> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        setContentView(R.layout.activity_alert_setting);

        listView = findViewById(R.id.listView);

        loadData();
        String value = "";
        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
             value = bundle.getString("cod");
             //Log.d("ceva", "cod = " + value);
        }

        if(value.equals("x")){
            hours = bundle.getStringArrayList("arrayHours");
            items = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1 ,hours);
            listView.setAdapter(items);
            saveData();
        }else {
            loadData();
            items = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1 ,hours);
            listView.setAdapter(items);
        }
    }

    public void saveData(){
        SharedPreferences prefs = getSharedPreferences("shared pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(hours);
        editor.putString("list", json);
        editor.apply();
    }

    private void loadData(){
        SharedPreferences prefs = getSharedPreferences("shared pref", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = prefs.getString("list", null);
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        hours = gson.fromJson(json, type);
    }
}