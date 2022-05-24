package com.example.fitapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;


public class ListAdapter extends ArrayAdapter<ExerciseModel> {

    private Context context;
    private int resource;

    public ListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<ExerciseModel> objects) {
        super(context, resource, objects);
        this.context=context;
        this.resource=resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        convertView = layoutInflater.inflate(resource, parent, false);

        TextView name = convertView.findViewById(R.id.titleE);
        TextView reps = convertView.findViewById(R.id.reps);
        ImageView image = convertView.findViewById(R.id.imageW);

        name.setText(getItem(position).getName());
        reps.setText(getItem(position).getReps());
        image.setImageResource(getItem(position).getImage());

        return convertView;
    }
}
