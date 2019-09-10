package com.example.documents.egychat2;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.documents.egychat2.Auth_System.MainPage;
import com.example.documents.egychat2.Settings_Pakage.Settings;
import com.example.documents.egychat2.Settings_Pakage.UsersActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class StartActivity extends AppCompatActivity {

   private Toolbar mToolbar;
   private FirebaseAuth mAuth;
   private ViewPager mViewPager;
   private SerctionPagerAdapter mSerctionPagerAdapter;
   private TabLayout mTabLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        mToolbar = findViewById(R.id.main_app_bar);
        mViewPager = findViewById(R.id.tab_pager);
        mTabLayout = findViewById(R.id.main_tabs);
            setSupportActionBar(mToolbar);
            getSupportActionBar().setTitle("EgyChat");
            mAuth = FirebaseAuth.getInstance();

            mSerctionPagerAdapter = new SerctionPagerAdapter(getSupportFragmentManager());
            mViewPager.setAdapter(mSerctionPagerAdapter);
            mTabLayout.setupWithViewPager(mViewPager);


    }
    private void sendToMain() {
        Intent i = new Intent(StartActivity.this,MainPage.class);
        startActivity(i);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
         super.onCreateOptionsMenu(menu);
         getMenuInflater().inflate(R.menu.main_menu , menu);
         return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
         super.onOptionsItemSelected(item);
            if (item.getItemId() == R.id.main_logout_btn){
                FirebaseAuth.getInstance().signOut();
                sendToMain();
            }else if (item.getItemId() == R.id.main_stng_btn){
                Intent i = new Intent(StartActivity.this,Settings.class);
                startActivity(i);
            } else if (item.getItemId() == R.id.main_all_btn) {
                Intent i = new Intent(StartActivity.this, UsersActivity.class);
                startActivity(i);
            }


        return true;
    }

    @Override
    protected void onStart() {

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser==null){
           sendToMain();
        }
        super.onStart();
    }
}
