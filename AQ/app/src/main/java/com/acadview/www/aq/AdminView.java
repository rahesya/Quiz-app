package com.acadview.www.aq;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminView extends AppCompatActivity {

    private DrawerLayout DrawerLayout;
    private NavigationView navigationView;
    AdminViewAdapter adapter;
    boolean listupdated=false;
    RecyclerView students;
    FirebaseDatabase database;
    DatabaseReference users,admin;
    LinearLayoutManager layoutManager;
    TextView adminname,txtsettings,txtlogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);

        adapter = new AdminViewAdapter(Common.StudentsList);

        adminname=(TextView)findViewById(R.id.Admin_Name);
        txtlogout=(TextView)findViewById(R.id.admin_logout);
        txtsettings = (TextView)findViewById(R.id.adminsettings);
        database = FirebaseDatabase.getInstance();
        users = database.getReference("Users");
        admin = database.getReference("Admins");

        students = (RecyclerView)findViewById(R.id.admin_recycler_view);
        layoutManager =new LinearLayoutManager(this);
        students.setItemAnimator(new DefaultItemAnimator());
        students.setLayoutManager(layoutManager);

        txtsettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSettings();
            }
        });
        txtlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onlogout();
            }
        });

        if (Common.StudentsList.size()>0){
            Common.StudentsList.clear();
        }

        users.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    User student  =postSnapshot.getValue(User.class);
                    Common.StudentsList.add(student);

                    adapter.notifyDataSetChanged();
                    students.setAdapter(adapter);
                }
                listupdated = true;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        DrawerLayout = findViewById(R.id.drawer_layout);

        navigationView = findViewById(R.id.nav_view);
        admin.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Admin admin = dataSnapshot.child(Common.currentadmin.getUsername()).getValue(Admin.class);
                adminname.setText("Welcome "+admin.getUsername());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

//        navigationView.setNavigationItemSelectedListener(
//                new NavigationView.OnNavigationItemSelectedListener() {
//                    @Override
//                    public boolean onNavigationItemSelected(MenuItem menuItem) {
//
//                        switch (menuItem.getItemId()){
//                            case R.id.adminlogout:{
//                                Intent logout =new Intent(AdminView.this,Login.class);
//                                Bundle dataSend = new Bundle();
//                                dataSend.putInt("Logout",2);
//                                logout.putExtras(dataSend);
//                                startActivity(logout);
//                                break;
//                            }
//                            case R.id.adminsettings:{
//
//                                break;
//                            }
//                            case R.id.home:{
//
//                                break;
//                            }
//                        }
//                        DrawerLayout.closeDrawers();
//                        return true;
//                    }
//                });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                DrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(AdminView.this,"You have to logout to exit",Toast.LENGTH_SHORT).show();
    }

    public void onlogout(){
        Intent logout =new Intent(AdminView.this,Login.class);
        Bundle dataSend = new Bundle();
        dataSend.putInt("Logout",2);
        logout.putExtras(dataSend);
        startActivity(logout);
    }

    public void onSettings(){
        Intent setting = new Intent(AdminView.this,Settings_activity.class);
        Common.student=false;
        startActivity(setting);
    }
}
