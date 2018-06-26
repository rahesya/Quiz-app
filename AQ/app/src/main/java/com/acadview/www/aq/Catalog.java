package com.acadview.www.aq;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class Catalog extends AppCompatActivity {

    int backcounter=0;
    BottomNavigationView bottomNavigationView;

    private FirebaseAuth fbAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        fbAuth = FirebaseAuth.getInstance();

        bottomNavigationView = (BottomNavigationView)findViewById(R.id.navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment =null;
                switch(item.getItemId()){
                    case R.id.action_category:
                        selectedFragment = CategoryFragment.newInstance();
                        break;
                    case R.id.action_Ranking:
                        selectedFragment = RankingFragment.newInstance();
                        break;
                }
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, selectedFragment);
                transaction.commit();
                return true;
            }
        });

        setDefaultFragment();

    }

    private void setDefaultFragment(){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, CategoryFragment.newInstance());
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        ++backcounter;
        Toast.makeText(getApplicationContext(),"To exit the app press the back",Toast.LENGTH_LONG).show();
        if(backcounter>=2){
            backcounter=0;
            finish();
            System.exit(0);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Toast.makeText(this,"You clicked "+item.toString(),Toast.LENGTH_LONG).show();

        switch(item.getItemId()){
            case R.id.setting:{
                break;
            }
            case R.id.logout:{
                Intent logout =new Intent(Catalog.this,Login.class);
                signOut();
                Bundle dataSend = new Bundle();
                dataSend.putInt("Logout",1);
                logout.putExtras(dataSend);
                startActivity(logout);
                break;
            }
            case R.id.myprofile:{
                Intent profile= new Intent(Catalog.this,MyProfile.class);
                startActivity(profile);
                break;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    public void signOut() {
        fbAuth.signOut();
    }

}
