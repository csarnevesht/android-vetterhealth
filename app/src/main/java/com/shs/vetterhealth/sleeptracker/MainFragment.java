package com.shs.vetterhealth.sleeptracker;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.FirebaseException;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.shs.vetterhealth.R;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by kartikey on 1/24/2016.
 */
public class MainFragment extends Fragment {

    private OnEventSelectedListener callback;

    public interface OnEventSelectedListener {
        public void onEventSelected(Observation o);
    }



    private ArrayAdapter<Observation> adapter;
    ArrayList<Observation> list = new ArrayList<Observation>();

    public MainFragment () {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView =  inflater.inflate(R.layout.sleeptracker_mainlist_view, container, false);

//        GetObservations go = new GetObservations();
//        go.execute();



        Button button = (Button) rootView.findViewById(R.id.addItemButton);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });



        //controller
        adapter = new ArrayAdapter<Observation>(
                getActivity(), R.layout.sleeptracker_list_item, R.id.txtItem, list);



        AdapterView listView = (AdapterView)rootView.findViewById(R.id.mainListView);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Observation o = (Observation) parent.getItemAtPosition(position);

                ((OnEventSelectedListener) getActivity()).onEventSelected(o);
            }
        });

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("sleeptracker");
        Query q = mDatabase.orderByKey();


        adapter.clear();
        q.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {


                Map<String, Object> m = (Map<String, Object>) dataSnapshot.getValue();
                Observation o = new Observation();

                o.title = m.get("title").toString();
                o.count = m.get("count").toString();
                o.comment = m.get("comment").toString();
                o.date = m.get("date").toString();

                Log.v("DEBUGGING", o.toString());

                adapter.add(o);
//                adapter.sort(new Comparator<Observation>() {
//                    @Override
//                    public int compare(Observation observation, Observation t1) {
//                        if(observation.date.compareTo(t1.date) > 0) return -1;
//                        else if(observation.date.compareTo(t1.date) < 0) return 1;
//                        else return 0;
//                    }
//                });

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


    public void showDialog() {
        FragmentManager fragmentManager = getFragmentManager();
        RecordingFragment rf = new RecordingFragment();

        FragmentTransaction transaction = fragmentManager.beginTransaction();
//        DialogFragment df = rf;
//
//        df.show(transaction,"dialog");

        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.add(android.R.id.content, rf).addToBackStack(null).commit();
    }



}
