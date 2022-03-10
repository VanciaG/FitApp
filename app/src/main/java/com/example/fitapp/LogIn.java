package com.example.fitapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class LogIn extends AppCompatActivity {

    private TextView signup_btn;
    private EditText mEmail, mPassword;
    private FirebaseAuth mAuth;
    private Button loginBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(1);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        getWindow().setStatusBarColor(Color.TRANSPARENT);
        setContentView(R.layout.activity_log_in);

        loginBtn = findViewById(R.id.login_btn);
        mEmail = findViewById(R.id.login_email);
        mPassword = findViewById(R.id.login_pass);
        signup_btn = findViewById(R.id.signup);

        mAuth=FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() != null){
            finish();
            return;
        }

        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LogIn.this, Register.class);
                startActivity(intent);
                finish();
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });



    }
}