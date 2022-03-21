package com.example.fitapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.google.firebase.auth.FirebaseAuth;


public class ProfileFragment extends Fragment {
    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        AppCompatButton personalInfoBtn = view.findViewById(R.id.personal_info_btn);
        AppCompatButton measurementsBtn = view.findViewById(R.id.measurements_btn);
        AppCompatButton log_outBtn = view.findViewById(R.id.logout);

        mAuth=FirebaseAuth.getInstance();

        personalInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), PersonalInfo.class));
            }
        });

        measurementsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), Measurements.class));
            }
        });

        log_outBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                startActivity(new Intent(getActivity(), OnboardingScreen.class));
                getActivity().finish();

            }
        });


        return view;
    }


}