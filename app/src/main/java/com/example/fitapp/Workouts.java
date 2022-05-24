package com.example.fitapp;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

public class Workouts extends AppCompatActivity {

    private ListView listView;
    private TextView textViewCountDown, nameWorkout;
    private Button startBtn, resetBtn;
    private CountDownTimer countDownTimer;
    private boolean timerRunning;
    private static final long START_TIME = 45000;
    private long timeLeft = START_TIME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(1);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

        setContentView(R.layout.activity_workouts);

        listView = findViewById(R.id.listViewWorkout);
        textViewCountDown = findViewById(R.id.countdown_timer);
        nameWorkout = findViewById(R.id.nameW);
        startBtn = findViewById(R.id.start_btn);
        resetBtn = findViewById(R.id.reset_btn);

        String position = getIntent().getStringExtra("a");
        String name = getIntent().getStringExtra("name");

        String[] name_list1 = getResources().getStringArray(R.array.exercisesName_list1);
        String[] name_list2 = getResources().getStringArray(R.array.exercisesName_list2);
        String[] name_list3 = getResources().getStringArray(R.array.exercisesName_list3);
        String[] name_list4 = getResources().getStringArray(R.array.exercisesName_list4);
        String[] name_list5 = getResources().getStringArray(R.array.exercisesName_list5);
        String[] name_list6 = getResources().getStringArray(R.array.exercisesName_list6);
        String[] name_list7 = getResources().getStringArray(R.array.exercisesName_list7);
        String[] name_list8 = getResources().getStringArray(R.array.exercisesName_list8);
        String[] name_list9 = getResources().getStringArray(R.array.exercisesName_list9);

        String[] reps_list1 = getResources().getStringArray(R.array.reps_list1);
        String[] reps_list2 = getResources().getStringArray(R.array.reps_list2);
        String[] reps_list3 = getResources().getStringArray(R.array.reps_list3);
        String[] reps_list4 = getResources().getStringArray(R.array.reps_list4);
        String[] reps_list5 = getResources().getStringArray(R.array.reps_list5);
        String[] reps_list6 = getResources().getStringArray(R.array.reps_list6);
        String[] reps_list7 = getResources().getStringArray(R.array.reps_list7);
        String[] reps_list8 = getResources().getStringArray(R.array.reps_list8);
        String[] reps_list9 = getResources().getStringArray(R.array.reps_list9);

        String[] desc_list1 = getResources().getStringArray(R.array.desc_list1);
        String[] desc_list2 = getResources().getStringArray(R.array.desc_list2);
        String[] desc_list3 = getResources().getStringArray(R.array.desc_list3);
        String[] desc_list4 = getResources().getStringArray(R.array.desc_list4);
        String[] desc_list5 = getResources().getStringArray(R.array.desc_list5);
        String[] desc_list6 = getResources().getStringArray(R.array.desc_list6);
        String[] desc_list7 = getResources().getStringArray(R.array.desc_list7);
        String[] desc_list8 = getResources().getStringArray(R.array.desc_list8);
        String[] desc_list9 = getResources().getStringArray(R.array.desc_list9);

        ArrayList<ExerciseModel> exerciseModels1 = new ArrayList<>();
        ArrayList<ExerciseModel> exerciseModels2 = new ArrayList<>();
        ArrayList<ExerciseModel> exerciseModels3 = new ArrayList<>();
        ArrayList<ExerciseModel> exerciseModels4 = new ArrayList<>();
        ArrayList<ExerciseModel> exerciseModels5 = new ArrayList<>();
        ArrayList<ExerciseModel> exerciseModels6 = new ArrayList<>();
        ArrayList<ExerciseModel> exerciseModels7 = new ArrayList<>();
        ArrayList<ExerciseModel> exerciseModels8 = new ArrayList<>();
        ArrayList<ExerciseModel> exerciseModels9 = new ArrayList<>();

        int[] image_list1 ={R.drawable.jumping_jack, R.drawable.flat_knee_raise, R.drawable.single_leg_bridge,
                        R.drawable.high_knee_skips, R.drawable.bicycle_crunch};

        int[] image_list1_exercises ={R.drawable.jumping_jack1, R.drawable.flat_knee_raise1, R.drawable.single_leg_bridge,
                R.drawable.high_knee_skips1, R.drawable.bicycle_crunch1};

        int[] image_list2 ={R.drawable.superman, R.drawable.push_up_knee, R.drawable.bent_over_row,
                R.drawable.push_up, R.drawable.hammer_curl};

        int[] image_list2_exercises ={R.drawable.superman1, R.drawable.push_up_knee1, R.drawable.bent_over_row1,
                R.drawable.push_up, R.drawable.hammer_curl1};

        int[] image_list3 ={R.drawable.lunge, R.drawable.squat, R.drawable.straight_leg_deadlift,
                R.drawable.single_leg_bridge, R.drawable.squat_band};

        int[] image_list3_exercises ={R.drawable.lunge, R.drawable.squat1, R.drawable.straight_leg_deadlift1,
                R.drawable.single_leg_bridge, R.drawable.squat_band1};

        int[] image_list4 ={R.drawable.squat_dumbbell, R.drawable.deadlift_dumbbell, R.drawable.cross_body_crunch,
                R.drawable.push_up, R.drawable.flat_knee_raise};

        int[] image_list4_exercises ={R.drawable.squat_dumbbell1, R.drawable.deadlift_dumbbell1, R.drawable.cross_body_crunch1,
                R.drawable.push_up, R.drawable.flat_knee_raise1};

        for(int i=0; i<name_list1.length;i++){
            ExerciseModel exerciseModel = new ExerciseModel(image_list1[i], name_list1[i], reps_list1[i], desc_list1[i]);
            exerciseModels1.add(exerciseModel);

        }

        for(int i=0; i<name_list2.length;i++){
            ExerciseModel exerciseModel = new ExerciseModel(image_list2[i], name_list2[i], reps_list2[i], desc_list2[i]);
            exerciseModels2.add(exerciseModel);
        }

        for(int i=0; i<name_list3.length;i++){
            ExerciseModel exerciseModel = new ExerciseModel(image_list3[i], name_list3[i], reps_list3[i], desc_list3[i]);
            exerciseModels3.add(exerciseModel);
        }

        for(int i=0; i<name_list4.length;i++){
            ExerciseModel exerciseModel = new ExerciseModel(image_list4[i], name_list4[i], reps_list4[i], desc_list4[i]);
            exerciseModels4.add(exerciseModel);
        }

        for(int i=0; i<name_list5.length;i++){
            ExerciseModel exerciseModel = new ExerciseModel(image_list1[i], name_list5[i], reps_list5[i], desc_list5[i]);
            exerciseModels5.add(exerciseModel);
        }

        for(int i=0; i<name_list6.length;i++){
            ExerciseModel exerciseModel = new ExerciseModel(image_list1[i], name_list6[i], reps_list6[i], desc_list6[i]);
            exerciseModels6.add(exerciseModel);
        }

        for(int i=0; i<name_list7.length;i++){
            ExerciseModel exerciseModel = new ExerciseModel(image_list1[i], name_list7[i], reps_list7[i], desc_list7[i]);
            exerciseModels7.add(exerciseModel);
        }

        for(int i=0; i<name_list8.length;i++){
            ExerciseModel exerciseModel = new ExerciseModel(image_list1[i], name_list8[i], reps_list8[i], desc_list8[i]);
            exerciseModels8.add(exerciseModel);
        }

        for(int i=0; i<name_list9.length;i++){
            ExerciseModel exerciseModel = new ExerciseModel(image_list1[i], name_list9[i], reps_list9[i], desc_list9[i]);
            exerciseModels9.add(exerciseModel);
        }

        ListAdapter listAdapter = null;


        switch (position) {
            case "0":
                listAdapter = new ListAdapter(this, R.layout.list_item_workout, exerciseModels1);
                //listView.setAdapter(listAdapter);
                break;
            case "1":
                listAdapter = new ListAdapter(this, R.layout.list_item_workout, exerciseModels2);
                //listView.setAdapter(listAdapter);
                break;
            case "2":
                listAdapter = new ListAdapter(this, R.layout.list_item_workout, exerciseModels3);
                //listView.setAdapter(listAdapter);
                break;
            case "3":
                listAdapter = new ListAdapter(this, R.layout.list_item_workout, exerciseModels4);
                //listView.setAdapter(listAdapter);
                break;
            case "4":
                listAdapter = new ListAdapter(this, R.layout.list_item_workout, exerciseModels5);
                //listView.setAdapter(listAdapter);
                break;
            case "5":
                listAdapter = new ListAdapter(this, R.layout.list_item_workout, exerciseModels6);
                //listView.setAdapter(listAdapter);
                break;
            case "6":
                listAdapter = new ListAdapter(this, R.layout.list_item_workout, exerciseModels7);
                //listView.setAdapter(listAdapter);
                break;
            case "7":
                listAdapter = new ListAdapter(this, R.layout.list_item_workout, exerciseModels8);
                //listView.setAdapter(listAdapter);
                break;
            case "8":
                listAdapter = new ListAdapter(this, R.layout.list_item_workout, exerciseModels9);
                //listView.setAdapter(listAdapter);
                break;
        }
        listView.setAdapter(listAdapter);
        nameWorkout.setText(name);

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(timerRunning){
                    pauseTimer();
                }else {
                    startTimer();
                }
            }
        });

        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetTimer();
            }
        });

        updateCountDown();



        listView.setClickable(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(Workouts.this, ExerciseActivity.class);
                switch (position) {
                    case "0":
                        intent.putExtra("name", name_list1[i]);
                        intent.putExtra("description", desc_list1[i]);
                        intent.putExtra("image_exercise", image_list1_exercises[i]);
                        break;
                    case "1":
                        intent.putExtra("name", name_list2[i]);
                        intent.putExtra("description", desc_list2[i]);
                        intent.putExtra("image_exercise", image_list2_exercises[i]);
                        break;
                    case "2":
                        intent.putExtra("name", name_list3[i]);
                        intent.putExtra("description", desc_list3[i]);
                        intent.putExtra("image_exercise", image_list3_exercises[i]);
                        break;
                    case "3":
                        intent.putExtra("name", name_list4[i]);
                        intent.putExtra("description", desc_list4[i]);
                        intent.putExtra("image_exercise", image_list4_exercises[i]);
                        break;
                    case "4":
                        intent.putExtra("description", desc_list5[i]);
                        break;
                    case "5":
                        intent.putExtra("description", desc_list6[i]);
                        break;
                    case "6":
                        intent.putExtra("description", desc_list7[i]);
                        break;
                    case "7":
                        intent.putExtra("description", desc_list8[i]);
                        break;
                    case "8":
                        intent.putExtra("description", desc_list9[i]);
                        break;
                }

                startActivity(intent);
            }
        });


    }

    private void startTimer(){
        countDownTimer = new CountDownTimer(timeLeft, 1000) {
            @Override
            public void onTick(long l) {
                timeLeft = l;
                updateCountDown();
            }

            @Override
            public void onFinish() {
                timerRunning = false;
                startBtn.setText("Start");
                startBtn.setVisibility(View.INVISIBLE);
                resetBtn.setVisibility(View.VISIBLE);
            }
        }.start();
        timerRunning = true;
        startBtn.setText("pause");
        resetBtn.setVisibility(View.INVISIBLE);
    }

    private void pauseTimer(){
        countDownTimer.cancel();
        timerRunning = false;
        startBtn.setText("Start");
        resetBtn.setVisibility(View.VISIBLE);
    }

    private void resetTimer(){
        timeLeft = START_TIME;
        updateCountDown();
        resetBtn.setVisibility(View.INVISIBLE);
        startBtn.setVisibility(View.VISIBLE);
    }

    private void updateCountDown(){
        int minutes =(int) (timeLeft / 1000) /60;
        int seconds = (int) (timeLeft / 1000) %60;

        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);

        textViewCountDown.setText(timeLeftFormatted);
    }
}