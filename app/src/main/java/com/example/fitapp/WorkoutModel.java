package com.example.fitapp;

public class WorkoutModel {
    String workoutTitle;
    String workoutDescription;
    String workoutTime;
    String workoutLevel;
    String workoutEquipment;


    public WorkoutModel(String workoutTitle, String workoutDescription, String workoutTime, String workoutLevel, String workoutEquipment) {
        this.workoutTitle = workoutTitle;
        this.workoutDescription = workoutDescription;
        this.workoutTime = workoutTime;
        this.workoutLevel = workoutLevel;
        this.workoutEquipment = workoutEquipment;
    }

    public String getWorkoutTitle() {
        return workoutTitle;
    }

    public String getWorkoutDescription() {
        return workoutDescription;
    }

    public String getWorkoutTime() {
        return workoutTime;
    }

    public String getWorkoutLevel() {
        return workoutLevel;
    }

    public String getWorkoutEquipment() {
        return workoutEquipment;
    }
}
