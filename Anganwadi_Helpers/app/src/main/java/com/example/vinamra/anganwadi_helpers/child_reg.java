package com.example.vinamra.anganwadi_helpers;

import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.bluehomestudio.steps.CircleImageSteps;

public class child_reg extends AppCompatActivity {
  CircleImageSteps imgsteps;
  Button next;
  android.support.v4.app.FragmentTransaction fragmentTransaction;
  FrameLayout frame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_reg);
        imgsteps= (CircleImageSteps) findViewById(R.id.cis_steps);
        next= (Button) findViewById(R.id.next);
        frame= (FrameLayout) findViewById(R.id.frame);
        imgsteps.addSteps(R.drawable.ic_basic,R.drawable.ic_img,R.drawable.ic_finish);
        next.setVisibility(View.GONE);
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame,new child_basic(),null);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int fragments = getSupportFragmentManager().getBackStackEntryCount();
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                if(fragments==1){
                    imgsteps.nextStep();
                    fragmentTransaction.replace(R.id.frame,new child_img(),null);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }else {
                    imgsteps.nextStep();
                    fragmentTransaction.replace(R.id.frame,new child_reg_complete(),null);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
              //  next.setVisibility(View.GONE);

            }
        });
    }
}
