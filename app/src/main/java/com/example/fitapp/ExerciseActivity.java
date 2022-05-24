package com.example.fitapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class ExerciseActivity extends AppCompatActivity {

    private TextView description, name;
    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(1);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

        setContentView(R.layout.activity_exercise);

        description = findViewById(R.id.descriptionE);
        image = findViewById(R.id.imageE);
        name = findViewById(R.id.exercise_name);

        //imaginile nu vin de la workouts class

        Intent intent = this.getIntent();

        if(intent != null){

            String name_exercise = intent.getStringExtra("name");
            String desc= intent.getStringExtra("description");
            int image_exercise = intent.getIntExtra("image_exercise", 0);
            image.setImageResource(image_exercise);
            description.setText(desc);
            name.setText(name_exercise);

        }

    }
}