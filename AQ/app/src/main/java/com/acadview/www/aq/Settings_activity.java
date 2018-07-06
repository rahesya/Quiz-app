package com.acadview.www.aq;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import java.util.Set;

public class Settings_activity extends AppCompatActivity {

    Switch soundswitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_activity);
        soundswitch = (Switch) findViewById(R.id.soundswitch);

        if (Common.student) {
            if (Login.getsharedpreference(Common.currentuser.getUsername(),Login.userdetails)=="true"){
                soundswitch.setChecked(true);
            }
            else
                soundswitch.setChecked(false);
                soundswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            Login.setsharedpreference(Common.currentuser.getUsername(), "true", Login.userdetails);
                            Toast.makeText(Settings_activity.this,"Sound ON",Toast.LENGTH_SHORT).show();
                        } else {
                            Login.setsharedpreference(Common.currentuser.getUsername(), "false", Login.userdetails);
                            Toast.makeText(Settings_activity.this,"Sound OFF",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
          else {
            if (Login.getsharedpreference(Common.currentadmin.getUsername(),Login.admindetails)=="true"){
                soundswitch.setChecked(true);
            }
            else {
                soundswitch.setChecked(false);
            }
                soundswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            Login.setsharedpreference(Common.currentadmin.getUsername(), "true", Login.admindetails);
                            Toast.makeText(Settings_activity.this,"Sound ON",Toast.LENGTH_SHORT).show();
                        } else {
                            Login.setsharedpreference(Common.currentadmin.getUsername(), "false", Login.admindetails);
                            Toast.makeText(Settings_activity.this,"Sound OFF",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

        }


    }