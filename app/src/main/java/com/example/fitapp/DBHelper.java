package com.example.fitapp;



//import static com.example.fitapp.BiologicalAgeContract.Entry.KEY_ANSWER;
import static com.example.fitapp.BiologicalAgeContract.Entry.KEY_ID;
import static com.example.fitapp.BiologicalAgeContract.Entry.KEY_OPT1;
import static com.example.fitapp.BiologicalAgeContract.Entry.KEY_OPT2;
import static com.example.fitapp.BiologicalAgeContract.Entry.KEY_OPT3;
import static com.example.fitapp.BiologicalAgeContract.Entry.KEY_OPT4;
import static com.example.fitapp.BiologicalAgeContract.Entry.KEY_QUES;
import static com.example.fitapp.BiologicalAgeContract.Entry.TABLE_QUEST;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "biologicalAgeQuestions";
    // tasks table name

    private SQLiteDatabase dbase;
    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        dbase=db;
        String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_QUEST +
                " (" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                KEY_QUES + " TEXT, " +
                KEY_OPT1 + " TEXT, " +
                KEY_OPT2 + " TEXT, " +
                KEY_OPT3 + " TEXT, " +
                KEY_OPT4 + " TEXT)";

        db.execSQL(sql);
        addQuestions();
        //db.close();
    }
    private void addQuestions()
    {
        QuestionBiologicalAge q1=new QuestionBiologicalAge("1. How's your weight?", "I’m overweight and need to lose 6kg or more", "I'm a little overweight—I'd like to lose 3kg", "I'm slim and have the right weight for my height", "I'm underweight");
        this.addQuestion(q1);
        QuestionBiologicalAge q2=new QuestionBiologicalAge("2. What's your activity level?","I do at least 150 minutes of aerobic exercise each week, and some strength training","I don't do much formal exercise but try to walk as much as I can","I do light exercise one hour a week","I do little exercise");
        this.addQuestion(q2);
        QuestionBiologicalAge q3=new QuestionBiologicalAge("3. Which of these best describes your diet most days?","Balanced, mostly based around fruit and vegetables, with some oily fish, small amounts of meat and very little sugar","I eat junk food maximum 4 times a month","I try to eat well but sometimes reach for junk food when I’m stressed or busy","My diet is not all it could be. I have a sweet tooth and live on convenience meals");
        this.addQuestion(q3);
        QuestionBiologicalAge q4=new QuestionBiologicalAge("4. Regardless of how much exercise you do, how much do you sit down daily?","I'm largely sedentary and sit for at least eight hours a day","I sit for a lot of the day but make an effort to get up and down every hour","I sit for three hours a day","I'm active. I don't have a sedentary job");
        this.addQuestion(q4);
        QuestionBiologicalAge q5=new QuestionBiologicalAge("5. Do you smoke?","No, I never have","No, but I used to","Yes, from time to time","Yes");
        this.addQuestion(q5);
        QuestionBiologicalAge q6=new QuestionBiologicalAge("6. How's your sleep?","I get seven or eight hours most nights and wake feeling refreshed","I get less than six hours","I get less than four hours","My sleep is all over the place. I lie in at weekends but sleep less during the week");
        this.addQuestion(q6);
        QuestionBiologicalAge q7=new QuestionBiologicalAge("7. Are you constantly under stress?","Yes, but I manage it with meditation, dance classes and chats with friends","No, although I have some short-term stress at work from time to time","Half the time I'm stressed","Yes and I find it overwhelming and it affects my mood a lot");
        this.addQuestion(q7);
        QuestionBiologicalAge q8=new QuestionBiologicalAge("8. How much alcohol do you drink?","One small glass a week","Less than 7 small glasses a week, with a few alcohol-free days","More than 7 small glasses a week, with a few alcohol-free days","I drink every day and have more than 14 small glasses a week");
        this.addQuestion(q8);


    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUEST);
        // Create tables again
        onCreate(db);
    }
    // Adding new question
    public void addQuestion(QuestionBiologicalAge quest) {
        //SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_QUES, quest.getQUESTION());
        //values.put(KEY_ANSWER, quest.getANSWER());
        values.put(KEY_OPT1, quest.getOPT1());
        values.put(KEY_OPT2, quest.getOPT2());
        values.put(KEY_OPT3, quest.getOPT3());
        values.put(KEY_OPT4, quest.getOPT4());
        // Inserting Row
        dbase.insert(TABLE_QUEST, null, values);
    }
    public List<QuestionBiologicalAge> getAllQuestions() {
        List<QuestionBiologicalAge> quesList = new ArrayList<QuestionBiologicalAge>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_QUEST;
        dbase=this.getReadableDatabase();
        Cursor cursor = dbase.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                QuestionBiologicalAge quest = new QuestionBiologicalAge();
                quest.setID(cursor.getInt(0));
                quest.setQUESTION(cursor.getString(1));
                //quest.setANSWER(cursor.getString(2));
                quest.setOPT1(cursor.getString(2));
                quest.setOPT2(cursor.getString(3));
                quest.setOPT3(cursor.getString(4));
                quest.setOPT4(cursor.getString(5));
                quesList.add(quest);
            } while (cursor.moveToNext());
        }
        // return quest list
        return quesList;
    }
    public int rowcount()
    {
        int row=0;
        String selectQuery = "SELECT  * FROM " + TABLE_QUEST;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        row=cursor.getCount();
        return row;
    }
}
