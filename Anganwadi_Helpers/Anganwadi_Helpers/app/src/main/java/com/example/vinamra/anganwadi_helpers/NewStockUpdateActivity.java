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

public class NewStockUpdateActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    public String date;
    public String time;

    public String getBillno() {
        return billno;
    }

    public void setBillno(String billno) {
        this.billno = billno;
    }

    public String billno;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newstockupdate);
        initialize();
    }

    private void initialize() {
        viewPager = (ViewPager) findViewById(R.id.stockUpdateViewPager);
        toolbar = (Toolbar) findViewById(R.id.stockUpdateToolbar);
        tabLayout = (TabLayout) findViewById(R.id.stockUpdateTabLayout);

        toolbar.setTitle("Stock Update Form");
        toolbar.setTitleTextColor(Color.WHITE);

        setSupportActionBar(toolbar);

        NewStockTabsAdapter adapter = new NewStockTabsAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    public void setDateTime(String date,String time)
    {
        this.date=date;
        this.time=time;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }
}
