package com.example.fitapp;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProfileFragment extends Fragment {
    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;
    private TextView userNameProfile;
    private AppCompatButton personalInfoBtn, measurementsBtn, log_outBtn, delete_Btn;
    private String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        personalInfoBtn = view.findViewById(R.id.personal_info_btn);
        measurementsBtn = view.findViewById(R.id.measurements_btn);
        log_outBtn = view.findViewById(R.id.logout);
        delete_Btn = view.findViewById(R.id.delete);
        userNameProfile = view.findViewById(R.id.userName);

        userNameProfile.setText(this.getArguments().getString("username"));

        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();


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

        delete_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlertDialog();
                //startActivity(new Intent(getActivity(), OnboardingScreen.class));
                //getActivity().finish();

            }
        });
        return view;
    }


    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Delete User Account?");
        builder.setMessage("Do you really want to delete your profile and related data?");

        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                deleteUserData(userID);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });

        AlertDialog alertDialog = builder.create();

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.red));
            }
        });
        alertDialog.show();
    }

    private void deleteUser(FirebaseUser firebaseUser) {
        firebaseUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    mAuth.signOut();
                    startActivity(new Intent(getActivity(), OnboardingScreen.class));
                    getActivity().finish();
                } else {
                    Toast.makeText(getActivity(), "Something went wrong!", Toast.LENGTH_LONG).show();

                }
            }
        });
    }

    private void deleteUserData(String userID){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("measurements").child(userID);
        databaseReference.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                deleteUser(firebaseUser);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "Something went wrong!", Toast.LENGTH_LONG).show();
            }
        });

    }

}