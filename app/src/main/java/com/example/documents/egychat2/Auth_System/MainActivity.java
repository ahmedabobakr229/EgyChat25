package com.example.documents.egychat2.Auth_System;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.documents.egychat2.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {


    EditText ed_name , ed_email , ed_pass;
    Button regi;
    private Toolbar mToolbar;
    private ProgressDialog mProgress;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ed_name = findViewById(R.id.name);
        ed_email = findViewById(R.id.email);
        ed_pass = findViewById(R.id.pass);
        regi = findViewById(R.id.regstr);
        mToolbar = findViewById(R.id.register_app_bar);

        mProgress = new ProgressDialog(this);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Create Account");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mAuth = FirebaseAuth.getInstance();

        regi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String getemail = ed_email.getText().toString().trim();
                String getname = ed_name.getText().toString().trim();
                String getpassword = ed_pass.getText().toString().trim();

                if (!TextUtils.isEmpty(getemail) || !TextUtils.isEmpty(getpassword)){
                    mProgress.setTitle("Registering User");
                    mProgress.setMessage("Wait while we create your account");
                    mProgress.setCanceledOnTouchOutside(false);
                    mProgress.show();
                    callsignup(getemail, getpassword, getname);
                }
            }
        });
    }

    private void callsignup(final String email, String password, final String getname) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>(){
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("Testing","Signup successful" + task.isSuccessful());

                        if (!task.isSuccessful()){
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            mProgress.dismiss();
                        }
                        else {

                            FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
                            String uid = current_user.getUid();

                            mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);

                            HashMap<String , String> userMap = new HashMap<>();
                            userMap.put("email", email);
                            userMap.put("name", getname);
                            userMap.put("status","Hi there");
                            userMap.put("image","default");
                            userMap.put("thumb_image","default");

                            mDatabase.setValue(userMap);


                        }
                        if (task.isSuccessful()){
                            userProfile();
                            Toast.makeText(MainActivity.this, "Created Account", Toast.LENGTH_SHORT).show();
                            Log.d("TESTING", "Created Account");
                            mProgress.hide();
                        }
                    }
                });

    }

    private void userProfile() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user!=null){
            UserProfileChangeRequest profileupdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(ed_name.getText().toString().trim()).build();

            user.updateProfile(profileupdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if (task.isSuccessful()){
                        Log.d("TESTING","User profile updated.");
                    }
                }
            });

        }
    }
}
