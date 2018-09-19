package com.example.documents.egychat2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    private Toolbar toolbar;
    EditText loginEmail , loginPass;
    Button login_btn;
    private ProgressDialog LoginProgress;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        toolbar = (Toolbar)findViewById(R.id.login_app_bar);
        loginEmail = (EditText)findViewById(R.id.lgn_name);
        loginPass = (EditText)findViewById(R.id.lgn_pass);
        login_btn = (Button)findViewById(R.id.lgn_login);
        LoginProgress = new ProgressDialog(this);

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Email = loginEmail.getText().toString().trim();
                String Pass = loginPass.getText().toString().trim();
                if (!TextUtils.isEmpty(Email) || !TextUtils.isEmpty(Pass)){
                    LoginProgress.setTitle("Logging in");
                    LoginProgress.setMessage("Please wait while checking your information");
                    LoginProgress.setCanceledOnTouchOutside(false);
                    LoginProgress.show();
                    loginUser(Email , Pass);
                }
            }
        });

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Login");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void loginUser(String email, String pass) {

        mAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(Login.this, "sucsess.",
                                    Toast.LENGTH_SHORT).show();
                           LoginProgress.dismiss();
                           Intent i = new Intent(Login.this,StartActivity.class);
                           i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                           startActivity(i);
                           finish();
                            FirebaseUser user = mAuth.getCurrentUser();
                           // updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                           // Toast.makeText(Login.this, "Error", Toast.LENGTH_SHORT).show();
                            Toast.makeText(Login.this, "Please make sure your information is true.",
                                    Toast.LENGTH_LONG).show();
                            LoginProgress.hide();
                            //updateUI(null);
                        }

                        // ...
                    }
                });

    }
}
