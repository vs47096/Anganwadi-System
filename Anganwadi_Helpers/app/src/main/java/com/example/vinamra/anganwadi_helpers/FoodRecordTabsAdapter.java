package com.example.vinamra.anganwadi_helpers;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Vinamra on 3/29/2018.
 */

public class FoodRecordTabsAdapter extends FragmentPagerAdapter {
    public FoodRecordTabsAdapter(FragmentManager fm) {
        super(fm);
    }

    public Fragment getItem(int position) {
        if(position==0){
            return new EnterMenuFragment();
        }else if(position==1){
            return new UsedStockFragment();
        }else if(position==2){
            return new FoodPicsFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if(position==0){
            return "Enter Menu";
        }else if(position==1){
            return "Used Stock";
        }
        else if(position==2){
            return "Meal Pics";
        }
        return super.getPageTitle(position);
    }
}
