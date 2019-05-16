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
    static String baseScore,baseEmail,baseBranch="min";
    DatabaseReference databaseStudents;
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
        t2 = (TextView)findViewById(R.id.text_branch_avg_score);
        t3 = (TextView)findViewById(R.id.text_avg_score);

        databaseStudents = FirebaseDatabase.getInstance().getReference("students");
        databaseStudents.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //studentList.clear();
                sum=0;count=0;
                for(DataSnapshot studentSnapshot : dataSnapshot.getChildren()){
                    Student student = studentSnapshot.getValue(Student.class);
                    //studentList.add(student);
                    int cscore = Integer.parseInt(student.getStudentScore());

                    //if mailid from base class matches then update this student:
                    if(student.getStudentMailId().equals(baseEmail)){
                        updateScore(student.getStudentId(),student.getStudentName(),baseEmail,baseScore,baseBranch);
                        cscore = Integer.parseInt(baseScore);
                    }

                    if(cscore==0)
                        continue;
                    sum  = sum + cscore;
                    count++;
                    if(student.getStudentBranch().equals(baseBranch)){
                        sumb = sumb + cscore;
                        countb++;
                    }

                }
                //StudentList adapter = new StudentList(MainActivity.this, studentList);
                //listViewStudents.setAdapter(adapter);
                //calculating average

                avg = ((float)(sum))/count;
                avgb = ((float)(sumb))/countb;
                displayScore = calcScore(avg);
                displayScoreb = calcScore(avgb);

                t1.setText(String.valueOf(calcScore(Integer.parseInt(baseScore))));
                t2.setText(String.valueOf(displayScoreb));
                t3.setText(String.valueOf(calcScore(displayScore)));

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void updateScore(String id, String name, String mail, String score, String branch){

        DatabaseReference dr = FirebaseDatabase.getInstance().getReference("students").child(id);
        Student student = new Student(id,name,mail,score,branch);
        dr.setValue(student);
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
