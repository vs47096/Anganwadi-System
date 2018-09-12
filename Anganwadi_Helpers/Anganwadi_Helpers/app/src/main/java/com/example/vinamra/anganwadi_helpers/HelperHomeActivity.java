package com.example.vinamra.anganwadi_helpers;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by Vinamra on 10/25/2017.
 */

public class HelperHomeActivity extends AppCompatActivity {
    private Button logOutBtn,newStockUpdateBtn,foodRecordBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_helperhome);
        super.onCreate(savedInstanceState);
        initialize();
    }

    private void initialize() {
        logOutBtn= (Button) findViewById(R.id.logOutBtn);
        logOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //clear shared preferences
                logoutUser();
            }
        });
        newStockUpdateBtn= (Button) findViewById(R.id.newStockUpdateBtn);
        newStockUpdateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isNetworkConnected(HelperHomeActivity.this))
                {
                    startActivity(new Intent(HelperHomeActivity.this,NewStockUpdateActivity.class));
                }
                else if(!isNetworkConnected(HelperHomeActivity.this))
                {
                    Toast.makeText(HelperHomeActivity.this,"Sorry,Check your Internet Connection",Toast.LENGTH_SHORT).show();
                }

            }
        });
        foodRecordBtn= (Button) findViewById(R.id.foodRecordBtn);
        foodRecordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(isNetworkConnected(HelperHomeActivity.this))
                {
                    startActivity(new Intent(HelperHomeActivity.this,FoodRecordActivity.class));
                }
                else if(!isNetworkConnected(HelperHomeActivity.this))
                {
                    Toast.makeText(HelperHomeActivity.this,"Sorry,Check your Internet Connection",Toast.LENGTH_SHORT).show();
                }
            }
        });
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
        Intent i = new Intent(HelperHomeActivity.this, LoginActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        startActivity(i);
        finish();
    }
    public boolean isNetworkConnected (Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null)
        {
            NetworkInfo netInfos = connectivityManager.getActiveNetworkInfo();
            if(netInfos != null)
            {
                return netInfos.isConnected();
            }
        }
        return false;
    }
}
