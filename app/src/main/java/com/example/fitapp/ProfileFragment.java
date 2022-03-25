package com.example.fitapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileFragment extends Fragment {
    private FirebaseAuth mAuth;
    private TextView userNameProfile;
    private CircleImageView imageProfile;
    private AppCompatButton personalInfoBtn, measurementsBtn, log_outBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

         personalInfoBtn = view.findViewById(R.id.personal_info_btn);
         measurementsBtn = view.findViewById(R.id.measurements_btn);
         log_outBtn = view.findViewById(R.id.logout);
         userNameProfile = view.findViewById(R.id.userName);
         imageProfile = view.findViewById(R.id.profile_image);

         mAuth=FirebaseAuth.getInstance();

        //String userID = mAuth.getCurrentUser().getUid();
        //showUserName(userID);

        userNameProfile.setText(this.getArguments().getString("username"));
        //userNameProfile.setText(this.getArguments().getString("user_name"));

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


        imageProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                startActivity(new Intent(getActivity(), OnboardingScreen.class));
                getActivity().finish();

            }
        });

        return view;
    }

    /*private void showUserName(String userID){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userID);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String username = String.valueOf(snapshot.child("userName").getValue());
                userNameProfile.setText(username);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //Toast.makeText(ProfileFragment.this, "Something went wrong!", Toast.LENGTH_LONG).show();
            }
        });
    }*/


}