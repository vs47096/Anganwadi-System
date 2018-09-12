package com.example.vinamra.anganwadi_supervisor;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Vinamra on 10/4/2017.
 */

public class HomeActivity extends AppCompatActivity {

    private Button newHelperBtn, showUnverifiedHelpersListBtn,logOutBtn;
    private TextView supervisor_name,anganwadi_location;
    private String supervisorId,supervisorName;
    private Button showWorkingHelpersListBtn,showCurrentStock;
    private Button showStockBills;
    private Button showRejectedHelpers;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initialize();
    }

    private void initialize() {
        supervisor_name= (TextView) findViewById(R.id.supervisor_name);
        anganwadi_location= (TextView) findViewById(R.id.anganwadi_location);

        newHelperBtn= (Button) findViewById(R.id.newHelperBtn);
        newHelperBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNewHelperCreateActivity();
            }
        });
        showUnverifiedHelpersListBtn = (Button) findViewById(R.id.showUnverifiedHelpersListBtn);
        showUnverifiedHelpersListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUnverifiedHelpersListActivity();
            }
        });
        showWorkingHelpersListBtn =(Button) findViewById(R.id.showWorkingHelpersListBtn);
        showWorkingHelpersListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWorkingHelpersListActivity();
            }
        });
        showCurrentStock=(Button) findViewById(R.id.showCurrentStock);
        showCurrentStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCurrentStockActivity();
            }
        });
        showStockBills=(Button) findViewById(R.id.showStockBills);
        showStockBills.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openStockBillsActivity();
            }
        });
        showRejectedHelpers=(Button) findViewById(R.id.showRejectedHelpers);
        showRejectedHelpers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRejectedHelpersListActivity();
            }
        });
        logOutBtn= (Button) findViewById(R.id.logOutBtn);
        logOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //clear shared preferences
                logoutUser();
            }
        });


        SharedPreferences supervisorPref=getSharedPreferences("supervisor_preferences", Context.MODE_PRIVATE);
        supervisorId=supervisorPref.getString("supervisorid",null);
        supervisorName=supervisorPref.getString("supervisorname",null);

        /*Bundle b=getIntent().getExtras();
        String anganwadiname=b.getString("anganwadiname",null);*/

        supervisor_name.setText("Hello Mr. "+supervisorName+"!");
        anganwadi_location.setText("Ajmer, Rajasthan");

    }

    private void openRejectedHelpersListActivity() {
        Intent i=new Intent(HomeActivity.this,RejectedHelpersActivity.class);
        startActivity(i);
    }

    private void openStockBillsActivity() {
        Intent i=new Intent(HomeActivity.this,StockBillsActivity.class);
        startActivity(i);
    }

    private void openCurrentStockActivity() {
        Intent i=new Intent(HomeActivity.this,CurrentStockActivity.class);
        startActivity(i);
    }

    private void openWorkingHelpersListActivity() {
        Intent i=new Intent(HomeActivity.this,WorkingHelpersListActivity.class);
        startActivity(i);
    }

    private void openUnverifiedHelpersListActivity() {
        Intent i=new Intent(HomeActivity.this,UnverifiedHelpersListActivity.class);
        startActivity(i);
    }

    public void openNewHelperCreateActivity() {
        Intent i=new Intent(HomeActivity.this,NewHelperActivity.class);
        startActivity(i);
    }

    /**
     * Clear session details
     * */
    public void logoutUser(){
        // Clearing all data from Shared Preferences
        SharedPreferences supervisorPref = getApplicationContext().getSharedPreferences("supervisor_preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor supervisorPrefEditor = supervisorPref.edit();
        supervisorPrefEditor.clear();
        supervisorPrefEditor.commit();

        // After logout redirect user to Loing Activity
        Intent i = new Intent(HomeActivity.this, LoginActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        startActivity(i);
        finish();
    }

}
