package com.example.fitapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


public class WorkoutsFragment extends Fragment {

    private ArrayList<WorkoutModel> workoutModels = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_workouts, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        setUpWorkoutModel();
        Workouts_RecyclerViewAdapter adapter = new Workouts_RecyclerViewAdapter(getContext(), workoutModels);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return view;
    }

    private void setUpWorkoutModel(){
        String[] workoutTitle = getResources().getStringArray(R.array.workouts_title);
        String[] workoutDescription = getResources().getStringArray(R.array.workouts_description);
        String[] workoutTime = getResources().getStringArray(R.array.workouts_time);
        String[] workoutLevel = getResources().getStringArray(R.array.workouts_level);
        String[] workoutEquipment = getResources().getStringArray(R.array.workouts_equipment);

        for (int i=0; i<workoutTitle.length; i++){
            workoutModels.add(new WorkoutModel(workoutTitle[i],
                    workoutDescription[i],
                    workoutTime[i],
                    workoutLevel[i],
                    workoutEquipment[i]));
        }

    }
}