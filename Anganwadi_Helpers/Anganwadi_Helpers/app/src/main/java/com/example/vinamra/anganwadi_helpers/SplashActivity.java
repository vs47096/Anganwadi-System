package com.example.vinamra.anganwadi_helpers;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Vinamra on 3/15/2018.
 */

public class SplashActivity extends AppCompatActivity {
    private Handler handler;
    private Runnable r;
    private Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        TitanicTextView tv = (TitanicTextView) findViewById(R.id.titanic_tv);

        // set fancy typeface
        tv.setTypeface(Typefaces.get(this, "Satisfy-Regular.ttf"));

        // start animation
        new Titanic().start(tv);

        /**************Check already logged in or not************/
        SharedPreferences loginPref = getApplicationContext().getSharedPreferences("login_preferences", Context.MODE_PRIVATE);
        if(!loginPref.contains("helperId"))
        {
            i=new Intent(SplashActivity.this, LoginActivity.class);
        }
        else
        {
            i=new Intent(SplashActivity.this, HelperHomeActivity.class);
        }
        /********************Check part end*********************/


        handler = new Handler();

        r = new Runnable() {
            @Override
            public void run() {
                startActivity(i);
                finish();
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        moveForward();
    }

    private void moveForward() {
        handler.removeCallbacks(r);
        handler.postDelayed(r, 3200);
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(r);
    }
}
