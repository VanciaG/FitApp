package com.example.fitapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Workouts_RecyclerViewAdapter extends RecyclerView.Adapter<Workouts_RecyclerViewAdapter.MyViewHolder> {

    Context context;
    ArrayList<WorkoutModel> workoutModels;

    public Workouts_RecyclerViewAdapter(Context context, ArrayList<WorkoutModel> workoutModels){
        this.context=context;
        this.workoutModels=workoutModels;
    }

    @NonNull
    @Override
    public Workouts_RecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_view_workout, parent, false);

        return new Workouts_RecyclerViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Workouts_RecyclerViewAdapter.MyViewHolder holder, int position) {
        holder.title.setText(workoutModels.get(position).getWorkoutTitle());
        holder.description.setText(workoutModels.get(position).getWorkoutDescription());
        holder.time.setText(workoutModels.get(position).getWorkoutTime());
        holder.level.setText(workoutModels.get(position).getWorkoutLevel());
        holder.equipment.setText(workoutModels.get(position).getWorkoutEquipment());
    }

    @Override
    public int getItemCount() {
        return workoutModels.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView title, description, time, level, equipment;
        ImageView imageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.titleW);
            description = itemView.findViewById(R.id.description);
            time = itemView.findViewById(R.id.minutes);
            level = itemView.findViewById(R.id.level);
            equipment = itemView.findViewById(R.id.equipment_type);
            imageView = itemView.findViewById(R.id.imageView2);
        }
    }
}
