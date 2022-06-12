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

import java.util.regex.Pattern;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class Register extends AppCompatActivity {
    private TextView login_btn;
    private EditText mUserName, mEmail, mPassword, mConfirmPassword;
    private FirebaseAuth mAuth;
    private Button registerBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(1);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

        setContentView(R.layout.activity_register);

        mUserName = findViewById(R.id.user_name);
        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.password);
        mConfirmPassword = findViewById(R.id.confirm_pass);
        registerBtn = findViewById(R.id.signup_btn);
        login_btn = findViewById(R.id.login);

        mAuth=FirebaseAuth.getInstance();
        /*if(mAuth.getCurrentUser() != null){
            finish();
            return;
        }*/



        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Register.this, LogIn.class);
                startActivity(intent);
                finish();
            }
        });



        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    String username = mUserName.getText().toString().trim();
                    String email = mEmail.getText().toString().trim();
                    String password = mPassword.getText().toString().trim();
                    String confirmPass = mConfirmPassword.getText().toString().trim();



                    if(TextUtils.isEmpty(username)){
                        mUserName.setError("Please enter username");
                        mUserName.requestFocus();
                        return;
                    }

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

                    if(TextUtils.isEmpty(confirmPass)){
                        mConfirmPassword.setError("Please confirm password");
                        mConfirmPassword.requestFocus();
                        return;
                    } else{
                        if(!password.equals(confirmPass)){
                            mConfirmPassword.setError("Password does not match");
                            mConfirmPassword.requestFocus();
                            return;
                        }
                    }

                    String passHash = BCrypt.withDefaults().hashToString(12, password.toCharArray());

                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                User user = new User (username, email , passHash);
                                FirebaseDatabase.getInstance().getReference("users")
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                            user.sendEmailVerification();
                                            Toast.makeText(Register.this, "Registered successfully. Please check your email to verify your account!", Toast.LENGTH_LONG).show();
                                        }else{
                                            Toast.makeText(Register.this, "Authentication failed!", Toast.LENGTH_LONG).show();

                                        }
                                    }
                                });
                            }else{
                                Toast.makeText(Register.this, "Authentication failed!", Toast.LENGTH_LONG).show();

                            }
                        }
                    });

            }
        });


    }

}