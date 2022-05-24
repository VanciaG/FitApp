package com.example.fitapp;

public class ExerciseModel {

    int image;
    String name, reps, desc;


    public ExerciseModel(int image, String name, String reps, String desc) {
        this.image = image;
        this.name = name;
        this.reps = reps;
        this.desc = desc;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReps() {
        return reps;
    }

    public void setReps(String reps) {
        this.reps = reps;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
