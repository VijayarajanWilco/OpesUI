package com.wilco.opesui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;

public class StartGoal extends AppCompatActivity {

    private Toolbar toolbar;
    CheckBox agree;
    Button fb,google,createBtn;
    EditText emailEdit,pwdEdit;

    @SuppressLint("WrongConstant")
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_goal);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        fb = (Button) findViewById(R.id.fb);
        google = (Button) findViewById(R.id.google);

        emailEdit = (EditText) findViewById(R.id.emailEdit);
        pwdEdit= (EditText) findViewById(R.id.pwdEdit);
        createBtn = (Button) findViewById(R.id.createBtn);

        toolbar.setTitle("Start Goal");
        toolbar.setTextAlignment(Gravity.CENTER);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);



       RelativeLayout createLay = (RelativeLayout)findViewById(R.id.createLay) ;
        createLay.setEnabled(false);
        agree = (CheckBox) findViewById(R.id.agree);
        agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                createLay.setEnabled(true);

            }
        });

        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                launchFacebookApplication(v);

            }
        });
        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                launchGmail(v);
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void launchFacebookApplication(View view) {
        Intent launchFacebookApplication = getPackageManager().getLaunchIntentForPackage("com.facebook.katana");
        startActivity(launchFacebookApplication);
    }

    // Launch Google Chrome after clicking the button
    public void launchGmail(View view) {
        Intent launchGoogleChrome = getPackageManager().getLaunchIntentForPackage("com.google.android.gm");
        startActivity(launchGoogleChrome);

    }

}
