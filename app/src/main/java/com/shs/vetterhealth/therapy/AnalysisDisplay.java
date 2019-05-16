package com.shs.vetterhealth.therapy;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shs.vetterhealth.R;

public class AnalysisDisplay extends AppCompatActivity {

    static int sum=0,count=0,displayScore=0;
    static int sumb=0,countb=0,displayScoreb=0;
    static float avg,avgb;
    //get these values from previous activity
    static String baseScore,baseEmail;
    DatabaseReference mTherapyUsers;
    TextView t1,t2,t3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.therapy_activity_analysis_display);
        baseScore = getIntent().getStringExtra("SCORE");
        baseEmail = getIntent().getStringExtra("GLOEMAIL");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
    @Override
    protected void onStart() {
        super.onStart();

        t1 = (TextView)findViewById(R.id.text_user_score);
        t3 = (TextView)findViewById(R.id.text_avg_score);

        this.mTherapyUsers = FirebaseDatabase.getInstance().getReference().child("TherapyUsers");
        this.mTherapyUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //therapyUserList.clear();
                sum=0;count=0;
                for(DataSnapshot therapyUserSnapshot : dataSnapshot.getChildren()){
                    TherapyUser therapyUser = therapyUserSnapshot.getValue(TherapyUser.class);
                    //therapyUserList.add(therapyUser);
                    int cscore = Integer.parseInt(therapyUser.getTherapyUserScore());

                    //if mailid from base class matches then update this therapyUser:
                    if(therapyUser.getTherapyUserMailId().equals(baseEmail)){
                        updateScore(therapyUser.getTherapyUserId(), therapyUser.getTherapyUserName(),baseEmail, baseScore);
                        cscore = Integer.parseInt(baseScore);
                    }

                    if(cscore==0)
                        continue;
                    sum  = sum + cscore;
                    count++;
                }
                //TherapyUserList adapter = new TherapyUserList(MainActivity.this, therapyUserList);
                //listViewTherapyUsers.setAdapter(adapter);
                //calculating average

                avg = ((float)(sum))/count;
                avgb = ((float)(sumb))/countb;
                displayScore = calcScore(avg);

                t1.setText("Your score: " + String.valueOf(calcScore(Integer.parseInt(baseScore))));
                t3.setText("Average user score: " + String.valueOf(calcScore(displayScore)));

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void updateScore(String id, String name, String mail, String score){

        DatabaseReference mTherapyUsers = FirebaseDatabase.getInstance().getReference().child("TherapyUsers").child(id);
        TherapyUser therapyUser = new TherapyUser(id,name,mail,score);
        mTherapyUsers.setValue(therapyUser);
        Toast.makeText(this,"Score Recorded Successfully", Toast.LENGTH_LONG).show();

    }
    public void toApi(View v){
        Intent intent = new Intent(this,ApiAiTask.class);
        intent.putExtra("SCORE",baseScore);
        startActivity(intent);
    }
    public int calcScore(float no){
        return Math.round(100-(no*100/30));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (getFragmentManager().getBackStackEntryCount() != 0) {
                    getFragmentManager().popBackStack();
                } else {
                    NavUtils.navigateUpFromSameTask(this);
                }

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() != 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }
}
