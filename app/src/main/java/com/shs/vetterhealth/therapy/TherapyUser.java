package com.shs.vetterhealth.therapy;

import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class TherapyUser {
    String therapyUserName;
    String therapyUserId;
    String therapyUserMailId;
    String therapyUserScore;

    public TherapyUser(){

    }
    public TherapyUser(String therapyUserId, String therapyUserName, String therapyUserMailId, String therapyUserScore){
        this.therapyUserName = therapyUserName;
        this.therapyUserMailId = therapyUserMailId;
        this.therapyUserId = therapyUserId;
        this.therapyUserScore = therapyUserScore;
    }
    public String getTherapyUserMailId(){
        return therapyUserMailId;
    }
    public String getTherapyUserName(){
        return therapyUserName;
    }
    public String getTherapyUserId(){
        return therapyUserId;
    }
    public String getTherapyUserScore(){
        return therapyUserScore;
    }

}
