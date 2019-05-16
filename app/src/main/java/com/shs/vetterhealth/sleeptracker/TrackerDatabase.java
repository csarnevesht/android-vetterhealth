package com.shs.vetterhealth.sleeptracker;

import android.util.Log;


import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by kartikey on 1/24/2016.
 */
public class TrackerDatabase {

    ArrayList<Observation> list = new ArrayList<Observation>();

    public ArrayList<Observation> getData() {
        final ArrayList<Observation> list = new ArrayList<Observation>();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("sleeptracker");


//        Firebase.setAndroidContext(new MainActivity());
//
//        Firebase ref = new Firebase("https://redclonefb.firebaseio.com/");




        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {


                Map<String, Object> m = (Map<String, Object>) dataSnapshot.getValue();
                Observation o = new Observation();

                o.title = m.get("title").toString();
                o.count = m.get("count").toString();
                o.comment = m.get("comment").toString();
                o.date = m.get("date").toString();

                Log.v("DEBUGGING", o.toString());

                list.add(o);



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

        return list;

    }
}
