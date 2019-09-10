package com.example.documents.egychat2.Auth_System;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.documents.egychat2.R;

public class MainPage extends AppCompatActivity {

    Button cret , logn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        cret = findViewById(R.id.create);
        logn = findViewById(R.id.login);

        cret.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainPage.this , MainActivity.class);
                startActivity(i);
            }
        });
        logn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainPage.this , Login.class);
                startActivity(i);
            }
        });
    }
}
