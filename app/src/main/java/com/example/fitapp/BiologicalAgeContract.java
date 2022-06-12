package com.example.fitapp;

import android.provider.BaseColumns;

public class BiologicalAgeContract {

    public static class Entry implements BaseColumns {
        public static final String TABLE_QUEST = "quest";
        // tasks Table Columns names
        public static final String KEY_ID = "id";
        public static final String KEY_QUES = "question";
        //public static final String KEY_ANSWER = "answer"; //correct option
        public static final String KEY_OPT1= "opt1"; //option a
        public static final String KEY_OPT2= "opt2"; //option b
        public static final String KEY_OPT3= "opt3"; //option c
        public static final String KEY_OPT4= "opt4"; //option d
    }
}
