package com.example.vinamra.anganwadi_helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Created by Vinamra on 10/4/2017.
 */

public class RegistrationActivity extends AppCompatActivity {
    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;
    LinearLayout expanded_image_Parent;
    ImageView expanded_image;
    private int fragmentNumber;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        initializeViews();
    }

    private void initializeViews() {
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        expanded_image_Parent= (LinearLayout) findViewById(R.id.expanded_image_Parent);
        expanded_image_Parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideExpandedImage();
            }
        });
        expanded_image= (ImageView) findViewById(R.id.expanded_image);
        expanded_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideExpandedImage();
            }
        });

        toolbar.setTitle("Registration Form");
        toolbar.setTitleTextColor(Color.WHITE);

        setSupportActionBar(toolbar);

        RegistrationTabsAdapter adapter = new RegistrationTabsAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        if(getIntent().hasExtra("fragment_number"))
        {
            fragmentNumber=getIntent().getExtras().getInt("fragment_number",0);
            viewPager.setCurrentItem(fragmentNumber,true);
        }

    }

    private void hideExpandedImage() {
        findViewById(R.id.appbar).setVisibility(View.VISIBLE);
        expanded_image.setVisibility(View.INVISIBLE);
        expanded_image_Parent.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences regPref = getApplicationContext().getSharedPreferences("registration_preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor refPrefEditor = regPref.edit();
        refPrefEditor.clear();
        refPrefEditor.apply();
    }
}
