package com.shs.vetterhealth.therapy;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shs.vetterhealth.R;

import java.util.ArrayList;
import java.util.List;

public class SignUp extends AppCompatActivity implements View.OnClickListener{
    private Button buttonSignUp;
    EditText editTextName;
    private EditText editTextEmail;
    private EditText editTextPassword;
    EditText editTextBranch;
    private TextView textViewLogIn;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    DatabaseReference mTherapyUsers;

    //ListView listViewTherapyUsers;

    List<TherapyUser> therapyUserList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.therapy_activity_sign_up);
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        buttonSignUp = (Button)findViewById(R.id.buttonSignUp);
        editTextEmail = (EditText)findViewById(R.id.editTextEmail);
        editTextPassword = (EditText)findViewById(R.id.editTextPassword);
        editTextBranch = (EditText)findViewById(R.id.editTextBranch);
        editTextName = (EditText)findViewById(R.id.editTextName);
        textViewLogIn = (TextView)findViewById(R.id.textViewLogIn);

        mTherapyUsers = FirebaseDatabase.getInstance().getReference().child("TherapyUsers");
        //listViewTherapyUsers = (ListView) findViewById(R.id.listViewTherapyUsers);
        therapyUserList = new ArrayList<>();

        buttonSignUp.setOnClickListener(this);
        textViewLogIn.setOnClickListener(this);
    }
    public void registerUser(){
        final String email = editTextEmail.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Email Cannot Be Empty", Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Password Cannot Be Empty", Toast.LENGTH_LONG).show();
            return;
        }
        progressDialog.setMessage("Getting you Registered");
        progressDialog.show();
        firebaseAuth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    // The User is registered.
                    if(task.isSuccessful()){
                        Toast.makeText(SignUp.this,"Registered Successfully", Toast.LENGTH_SHORT).show();
                        /*
                        Intent intent = new Intent(SignUp.this,MainActivity.class);
                        intent.putExtra("key1",email);
                        intent.putExtra("key2",password);
                        startActivity(intent);
                        */
                    }else{
                        Toast.makeText(SignUp.this,"Failed to Register", Toast.LENGTH_SHORT).show();
                    }
                }
            });
    }


    protected void addTherapyMember(){
        String name = editTextName.getText().toString().trim();
        String mailId = editTextEmail.getText().toString().trim();
        //String number = editTextScore.getText().toString().trim();
        String number="0";
        //float no=0;
        //displayScore = calcScore(no);
        //textViewAverage.setText(String.valueOf(displayScore));


        if(!TextUtils.isEmpty(name)){   //not empty store it in database.
            String id = mTherapyUsers.push().getKey();

            //creating the therapyUser object
            TherapyUser therapyUser = new TherapyUser(id,name,mailId,number);

            //Saving therapyUser
            mTherapyUsers.child(id).setValue(therapyUser);

            //setting edittext to blank again
            editTextName.setText("");

            Toast.makeText(this,"Therapy Member Added in database!", Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(this, "You should enter a name", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        if(v == buttonSignUp){
            registerUser();
            addTherapyMember();
        }
        if(v == textViewLogIn){
            finish();
        }
    }
}
