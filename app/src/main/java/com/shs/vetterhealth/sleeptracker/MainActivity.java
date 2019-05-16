package com.shs.vetterhealth.sleeptracker;

import android.os.Bundle;

import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.shs.vetterhealth.R;

public class MainActivity extends AppCompatActivity implements MainFragment.OnEventSelectedListener {

    private ArrayAdapter<String> adapter;
    MainFragment mf = new MainFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sleeptracker_activity_main);

        FrameLayout fl1 = (FrameLayout) findViewById(R.id.containerLeft);
        FrameLayout fl2 = (FrameLayout) findViewById(R.id.containerRight);

        Log.v("Checking framelayout1", fl1.toString());
        Log.v("Checking framelayout2", fl2.toString());

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();


        ft.add(R.id.containerLeft, new SummaryFragment());
        ft.add(R.id.containerRight, new MainFragment());
        ft.commit();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if(getFragmentManager().getBackStackEntryCount() != 0) {
                    getFragmentManager().popBackStack();
                }else {
                    NavUtils.navigateUpFromSameTask(this);
                }

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        if(getFragmentManager().getBackStackEntryCount() != 0) {
            getFragmentManager().popBackStack();
        }else {
            super.onBackPressed();
        }

    }

    public void onEventSelected(Observation o){

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();



        DetailFragment detail = new DetailFragment();

        Bundle bundle = new Bundle();
        bundle.putString("title",o.title);
        bundle.putString("count",o.count);
        bundle.putString("comment",o.comment);
        bundle.putString("date", o.date);

        detail.setArguments(bundle);



            ft.replace(R.id.containerLeft, new MainFragment()).addToBackStack(null);
            ft.replace(R.id.containerRight, detail).addToBackStack(null);



        ft.commit();

    }
}
