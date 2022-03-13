package com.example.fitapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {
    private EditText mEmail;
    private TextView mBack;
    private Button resetPassword;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(1);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        getWindow().setStatusBarColor(Color.TRANSPARENT);

        setContentView(R.layout.activity_forgot_password);

        mEmail = findViewById(R.id.resetpass_email);
        resetPassword = findViewById(R.id.resetpass_btn);
        mBack = findViewById(R.id.back);

        mAuth=FirebaseAuth.getInstance();

        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetPassword();

            }
        });

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent (ForgotPassword.this, LogIn.class));
                finish();
            }
        });


    }



    public void resetPassword(){
        String email = mEmail.getText().toString().trim();

        if(!TextUtils.isEmpty(email)){
            if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                mEmail.setError("Please provide valid email");
                mEmail.requestFocus();
                return;
            }

        } else{
            mEmail.setError("Email is required");
            mEmail.requestFocus();
            return;
        }

        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(ForgotPassword.this, "Check your email to reset your password", Toast.LENGTH_LONG).show();

                }else{
                    Toast.makeText(ForgotPassword.this, "Try again! Something went wrong!", Toast.LENGTH_LONG).show();
                }

                Intent intent = new Intent(ForgotPassword.this, LogIn.class);
                startActivity(intent);
                finish();
            }
        });

    }


}