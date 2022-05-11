package com.example.fitapp;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Month;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.time.LocalDate;

public class ProgressFragment extends Fragment {
    private String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
    HashMap<String,String> hashMap = new LinkedHashMap<>();
    HashMap<String,String> sterg = new LinkedHashMap<>();
    Map<Date, String> map = new TreeMap<>();
    private ListView listView;
    private GraphView graphView;
    private LineGraphSeries<DataPoint> series;
    private Spinner spinner;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_progress, container, false);

        listView = view.findViewById(R.id.listEntries);
        graphView = view.findViewById(R.id.graph);
        spinner = view.findViewById(R.id.spinner);
        //spinner.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);

        String[] date_range = getResources().getStringArray(R.array.progress_graph);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, date_range);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        showWeights(userID);

        graphView.getViewport().setXAxisBoundsManual(true);
        graphView.getViewport().setMinX(0);
        graphView.getViewport().setMaxX(3);
        graphView.getViewport().setScrollable(true);


        spinner.setSelection(Adapter.NO_SELECTION, true);

        LocalDate currentdate = LocalDate.now();
        Month currentMonth = currentdate.getMonth();
        Log.d("ceva","luna " + currentMonth);

        return view;
    }

    private void showWeights(String userID){

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat formatter1 = new SimpleDateFormat("dd/MM");
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("measurements").child(userID);
        databaseReference.keepSynced(true);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() != null) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        String kg = String.valueOf(dataSnapshot.child("weight").getValue());
                        String dateString = String.valueOf(dataSnapshot.child("date").getValue());
                        Date date = null;
                        try {
                            date = formatter.parse(dateString);
                        } catch (ParseException e) {
                            Log.d("Exception: ", e.getMessage());
                        }
                        map.put(date, kg);
                    }

                    for (Map.Entry<Date, String> entry : map.entrySet()) {
                        String i = formatter.format(entry.getKey());
                        String b = formatter1.format(entry.getKey());
                        hashMap.put(i, entry.getValue());
                        sterg.put(b, entry.getValue());
                    }


                    Log.d("ceva", "streg" + sterg);

                    //nu merge daca il pun inafara functiei
                    List<HashMap<String,String>> listItems = new ArrayList<>();
                    SimpleAdapter adapter = new SimpleAdapter(getActivity(), listItems, R.layout.list_item,
                            new String[]{"First Line", "Second Line"},
                            new int[]{R.id.weights, R.id.dates});

                    Iterator it = hashMap.entrySet().iterator();
                    while(it.hasNext()){
                        HashMap<String,String> resultsMap = new HashMap<>();
                        Map.Entry pair = (Map.Entry)it.next();
                        resultsMap.put("First Line", pair.getValue().toString() + " kg");
                        resultsMap.put("Second Line", pair.getKey().toString());
                        listItems.add(resultsMap);

                    }
                    listView.setAdapter(adapter);

                    series= new LineGraphSeries<>(getDataPoint());
                    graphView.addSeries(series);



                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Something went wrong!", Toast.LENGTH_LONG).show();
            }
        });
    }

    /*public void onStart(){
        super.onStart();

    }*/

    private DataPoint[] getDataPoint(){
        /*for (Map.Entry<String, String> entry : dates.entrySet()) {
            int i = entry.getKey();
            int b = entry.getKey();
            DataPoint[] dp = new DataPoint[]{new DataPoint(i, b)};

        }*/

        DataPoint[] dp = new DataPoint[]{
                new DataPoint(0,86),
                new DataPoint(1,75),
                new DataPoint(2,81),
                new DataPoint(3,80),
                new DataPoint(6,73),
                new DataPoint(9,82),
                new DataPoint(12,75)
        };
        return dp;
    }

    private void fct(){
        spinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("ceva", "intra");
                String text = adapterView.getSelectedItem().toString();

                Log.d("ceva", "aicc " + text);
                switch (text) {
                    case "1 Week":
                        Toast.makeText(getActivity(), "1 Week", Toast.LENGTH_LONG).show();
                        break;
                    case "1 Month":
                        Toast.makeText(getActivity(), "1 Month", Toast.LENGTH_LONG).show();
                        break;
                    case "2 Months":
                        Toast.makeText(getActivity(), "2 Months", Toast.LENGTH_LONG).show();
                        break;
                    case "1 Year":
                        Toast.makeText(getActivity(), "1 Year", Toast.LENGTH_LONG).show();
                        break;
                }
            }
        });
    }

}