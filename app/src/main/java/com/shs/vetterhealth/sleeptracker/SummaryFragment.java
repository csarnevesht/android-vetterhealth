package com.shs.vetterhealth.sleeptracker;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.fragment.app.Fragment;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shs.vetterhealth.R;

import java.util.Map;

/**
 * Created by kartikey on 1/24/2016.
 */
public class SummaryFragment extends Fragment {

    double sleepTime = 0.0;
    double avgSleepTime = 0.0;
    long childrenCount = 1;

    public SummaryFragment () {

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView =  inflater.inflate(R.layout.sleeptracker_summary_view, container, false);

        final TextView summary = (TextView)rootView.findViewById(R.id.summaryText);

        sleepTime = 0.0;
        avgSleepTime = 0.0;
        childrenCount = 0;


        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("sleeptracker");



        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {


                Map<String, Object> m = (Map<String, Object>) dataSnapshot.getValue();
                Observation o = new Observation();

                o.title = m.get("title").toString();
                o.count = m.get("count").toString();
                o.comment = m.get("comment").toString();
                o.date = m.get("date").toString();

                sleepTime = sleepTime + (Double.parseDouble(o.count));
                childrenCount++;
                avgSleepTime = (childrenCount == 0) ? sleepTime : sleepTime / childrenCount;


                summary.setText("You have slept an average of "+avgSleepTime+" hours.");


            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });

        return rootView;
    }
}
