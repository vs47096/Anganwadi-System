package com.example.vinamra.anganwadi_helpers;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

/**
 * Created by Vinamra on 3/22/2018.
 */

public class FoodRecordActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private String date,time,menu,meal;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foodrecord);
        initialize();
    }

    private void initialize() {
        viewPager = (ViewPager) findViewById(R.id.foodRecordViewPager);
        toolbar = (Toolbar) findViewById(R.id.foodRecordToolbar);
        tabLayout = (TabLayout) findViewById(R.id.foodRecordTabLayout);

        toolbar.setTitle("Food Record Form");
        toolbar.setTitleTextColor(Color.WHITE);

        setSupportActionBar(toolbar);

        FoodRecordTabsAdapter adapter = new FoodRecordTabsAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    public void setDateTime(String date,String time)
    {
        this.date=date;
        this.time=time;

    }

    public void setMenu(String menu) {
        this.menu = menu;
    }

    public String getMeal() {
        return meal;
    }

    public void setMeal(String meal) {
        this.meal = meal;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getMenu() {
        return menu;
    }
}
