package com.example.fitapp;


import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



public class WaterFragment extends Fragment implements View.OnClickListener{
    private AppCompatButton setWaterBtn, alertSettingBtn;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_water, container, false);

        setWaterBtn = view.findViewById(R.id.set_water_btn);
        alertSettingBtn = view.findViewById(R.id.alert_btn);

        setWaterBtn.setOnClickListener(this);
        alertSettingBtn.setOnClickListener(this);

        return view;
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.set_water_btn:
                startActivity(new Intent(getActivity(), SetMyWater.class));
                break;
            case R.id.alert_btn:
                Intent intent = new Intent(getActivity(), AlertSetting.class);
                intent.putExtra("cod", "y");
                startActivity(intent);
                break;
        }
    }
}