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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class LogIn extends AppCompatActivity {

    private TextView signup_btn, resetPass_btn;
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
        resetPass_btn = findViewById(R.id.resetPass);

        mAuth=FirebaseAuth.getInstance();
        /*if(mAuth.getCurrentUser() != null){
            finish();
            return;
        }*/

        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LogIn.this, Register.class);
                startActivity(intent);
                finish();
            }
        });

        resetPass_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LogIn.this,ForgotPassword.class);
                startActivity(intent);
                finish();
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();

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

                if(TextUtils.isEmpty(password)){
                    mPassword.setError("Password is required");
                    mPassword.requestFocus();
                    return;
                }

                if (password.length() <6){
                    mPassword.setError("Password must be at least 6 characters");
                    mPassword.requestFocus();
                    return;
                }

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                    if(user.isEmailVerified()) {
                                        startActivity(new Intent(LogIn.this, MainScreen.class));
                                    }else{
                                        Toast.makeText(LogIn.this, "Please verify your email address!", Toast.LENGTH_LONG).show();
                                    }
                                }else{
                                    Toast.makeText(LogIn.this, "Your credentials are incorrect!", Toast.LENGTH_LONG).show();

                                }
                            }
                        });

            }
        });



    }
}