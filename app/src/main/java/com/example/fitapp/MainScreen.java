package com.example.fitapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.TextView;


import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MainScreen extends AppCompatActivity {
    private FirebaseAuth mAuth;
    Bundle bundle = new Bundle();
    BottomNavigationView bottomNavigationView;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        getWindow().setStatusBarColor(Color.TRANSPARENT);

        setContentView(R.layout.activity_main_screen);

        bottomNavigationView = findViewById(R.id.bottomNavigator);

        mAuth= FirebaseAuth.getInstance();

        if(!mAuth.getCurrentUser().isEmailVerified()){
            startActivity(new Intent(MainScreen.this, OnboardingScreen.class));
            finish();
            return;
        }

        String userID = mAuth.getCurrentUser().getUid();
        showUserName(userID);

        //name = bundle.getString("user_name");
        //bundle.putString("username", name);


        loadFragment(new HomeFragment());

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        loadFragment(new HomeFragment());
                        break;
                    case R.id.workouts:
                        loadFragment(new WorkoutsFragment());
                        break;
                    case R.id.water:
                        loadFragment(new WaterFragment());
                        break;
                    case R.id.profile:
                        ProfileFragment profileFragment = new ProfileFragment();
                        profileFragment.setArguments(bundle);
                        //loadFragment(new ProfileFragment());
                        loadFragment(profileFragment);
                        break;
                }
                return true;
            }
        });
    }

    private void loadFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.body_container, fragment).commit();
    }

    private void showUserName(String userID){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userID);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String username = String.valueOf(snapshot.child("userName").getValue());
                bundle.putString("username", username);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //Toast.makeText(ProfileFragment.this, "Something went wrong!", Toast.LENGTH_LONG).show();
            }
        });
    }

}