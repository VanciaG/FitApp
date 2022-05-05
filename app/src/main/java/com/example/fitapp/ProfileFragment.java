package com.example.fitapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileFragment extends Fragment {
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private TextView userNameProfile;
    private CircleImageView imageProfile;
    private AppCompatButton personalInfoBtn, measurementsBtn, log_outBtn;
    private static final int REQ_CAMERA_CODE = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        personalInfoBtn = view.findViewById(R.id.personal_info_btn);
        measurementsBtn = view.findViewById(R.id.measurements_btn);
        log_outBtn = view.findViewById(R.id.logout);
        userNameProfile = view.findViewById(R.id.userName);
        imageProfile = view.findViewById(R.id.profile_image);

        userNameProfile.setText(this.getArguments().getString("username"));

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

        /*imageProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean pick = true;
                if(pick == true){
                    if(!checkCameraPermission()){
                        requestCameraPermission();
                    } else PickImage();
                }else{
                    if(!checkStoragePermission()){
                        requestStoragePermission();
                    } else PickImage();
                }

            }
        });*/


        return view;
    }

    /*private boolean checkCameraPermission(){
        boolean res1 = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA)==PackageManager.PERMISSION_GRANTED;
        boolean res2 = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED;
        return res1 && res2;
    }

    private boolean checkStoragePermission(){
        boolean res2 = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED;
        return res2;
    }

    private void requestCameraPermission(){
        ;
    }*/


}