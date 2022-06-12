package com.example.fitapp;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;


public class ProgressFragment extends Fragment {
    private String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private HashMap<String,String> hashMap = new LinkedHashMap<>();
    //HashMap<String,String> sterg = new LinkedHashMap<>();
    private Map<Date, String> map = new TreeMap<>();
    private ListView listView;
    private GraphView graphView;
    private LineGraphSeries<DataPoint> series;
    //private Spinner spinner;
    private TextView progress_month;
    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_progress, container, false);

        listView = view.findViewById(R.id.listEntries);
        graphView = view.findViewById(R.id.graph);
        progress_month = view.findViewById(R.id.progress_month);

        /*spinner.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
        String[] date_range = getResources().getStringArray(R.array.progress_graph);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, date_range);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(Adapter.NO_SELECTION, true);*/

        readWeights(userID);

        graphView.getViewport().setXAxisBoundsManual(true);

        graphView.getViewport().setScrollable(true);

        return view;
    }

    private void readWeights(String userID){
        //SimpleDateFormat formatter1 = new SimpleDateFormat("dd/MM");
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("measurements").child(userID);
        databaseReference.keepSynced(true);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
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
                        //String b = formatter1.format(entry.getKey());
                        hashMap.put(i, entry.getValue());
                        //sterg.put(b, entry.getValue());
                    }

                        showWeights();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Something went wrong!", Toast.LENGTH_LONG).show();
            }
        });
    }


    private void showWeights(){
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

        showGraph();

    }



    private void showGraph(){
        SimpleDateFormat formatter1 = new SimpleDateFormat("dd.MM");
        ArrayList<DataPoint> dp = new ArrayList<>();
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        Calendar cal3 = Calendar.getInstance();
        cal1.setTime(new Date());
        //cal3.add(Calendar.MONTH, -2); //din ultima luna
        int b;
        double x =0 ;
        Date date = new Date();
        boolean s = true;




        for (Map.Entry<Date, String> entry : map.entrySet()) {
            //date = entry.get(map.keySet().toArray()[0]);
            //Log.d("ceva", "t" + t);
            cal2.setTime(entry.getKey());
            if(cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)) {
                if(cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH)) {
                    if(s) {
                        date = cal2.getTime();
                        s = false;
                    }
                    String g = formatter1.format(cal2.getTime());
                    x= Double.parseDouble(g);
                    //Log.d("ceva", " x " + date);
                    b = Integer.parseInt(entry.getValue());
                    dp.add(new DataPoint(x,b));
                    //Log.d("ceva", " x " + dp);
                }
            }
        }

        cal3.setTime(date);
        int day = cal3.get(Calendar.DAY_OF_MONTH);

        String month = "Progress in " + cal1.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
        progress_month.setText(month);
        graphView.getViewport().setMinX(day);
        graphView.getViewport().setMaxX(x);

        DataPoint[] d = new DataPoint[dp.size()];
        d = dp.toArray(d);
        series= new LineGraphSeries<>(d);
        graphView.addSeries(series);

        /*graphView.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter(){
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if(isValueX){
                    return formatter1.format(new Date ((long)value));
                }else{
                    return super.formatLabel(value,isValueX);
                }
            }
        });*/




    }

    /*private void fct(){
        spinner.setOnItemClickListener((adapterView, view, i, l) -> {
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
        });
    }*/

}