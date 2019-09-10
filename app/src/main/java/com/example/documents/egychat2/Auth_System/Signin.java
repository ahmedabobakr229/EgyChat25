package com.example.documents.egychat2.Auth_System;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.documents.egychat2.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Signin extends AppCompatActivity {
    Button signout;
    private FirebaseAuth mAuth;
    TextView username;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);

        mAuth = FirebaseAuth.getInstance();
        signout = findViewById(R.id.signout);
        username = findViewById(R.id.tvname);

        if (mAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(getApplicationContext(),MainActivity.class));

        }
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null){
            username.setText("Welcome ," + user.getDisplayName());
        }

        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                finish();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
    }
}
