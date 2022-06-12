package com.example.fitapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class BiologicalAgeCalculator extends AppCompatActivity {
    List<QuestionBiologicalAge> quesList;
    int score=0;
    int qid=0;
    QuestionBiologicalAge currentQ;
    TextView txtQuestion;
    RadioButton rda, rdb, rdc, rdd;
    Button butNext;
    String age;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(1);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

        setContentView(R.layout.activity_biological_age_calculator);
        DBHelper db=new DBHelper(this);

        quesList=db.getAllQuestions();
        currentQ=quesList.get(qid);

        txtQuestion=(TextView)findViewById(R.id.question);
        rda=(RadioButton)findViewById(R.id.option1);
        rdb=(RadioButton)findViewById(R.id.option2);
        rdc=(RadioButton)findViewById(R.id.option3);
        rdd=(RadioButton)findViewById(R.id.option4);
        butNext=(Button)findViewById(R.id.next);

        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        getAge(userID);

        setQuestionView();

        butNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioGroup grp=(RadioGroup)findViewById(R.id.radioGroup1);

                RadioButton answer=(RadioButton)findViewById(grp.getCheckedRadioButtonId());

                grp.clearCheck();

                String ans = answer.getText().toString();

                if(currentQ.getOPT1().equals(ans))
                {
                    score = score + 2;
                    Log.d("score", "Your score1 " + score);

                }else if (currentQ.getOPT2().equals(ans)){
                    score = score + 1;
                    Log.d("score", "Your score2 " + score);
                }else if(currentQ.getOPT3().equals(ans)){
                    Log.d("score", "Your score3 " + score);
                }else{
                    score = score - 1;
                    Log.d("score", "Your score4 " + score);
                }

                if(qid < 8){
                    Log.d("score", "intra " + qid);
                    currentQ=quesList.get(qid);
                    setQuestionView();

                }else{
                    Log.d("score", "intraaaa " + score);
                    Intent intent = new Intent(BiologicalAgeCalculator.this, ResultBiologicalAge.class);
                    int a = Integer.parseInt(age);
                    Bundle b = new Bundle();
                    b.putInt("score", score); //Your score
                    b.putInt("age", a);
                    intent.putExtras(b); //Put your score to your next Intent
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    private void setQuestionView()
    {
        txtQuestion.setText(currentQ.getQUESTION());
        rda.setText(currentQ.getOPT1());
        rdb.setText(currentQ.getOPT2());
        rdc.setText(currentQ.getOPT3());
        rdd.setText(currentQ.getOPT4());
        qid++;
    }

    private void getAge(String userID){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userID);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() != null) {
                    age = String.valueOf(snapshot.child("age").getValue());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(BiologicalAgeCalculator.this, "Something went wrong!", Toast.LENGTH_LONG).show();
            }
        });
    }


}
