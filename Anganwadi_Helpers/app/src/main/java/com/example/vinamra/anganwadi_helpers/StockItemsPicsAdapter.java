package com.example.vinamra.anganwadi_helpers;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Vinamra on 3/23/2018.
 */

public class StockItemsPicsAdapter extends FragmentPagerAdapter{

    public StockItemsPicsAdapter(FragmentManager fm) {
        super(fm);
    }

    public Fragment getItem(int position) {
        if(position==0){
            return new ImageViewFragment();
        }else if(position==1){
            return new ImageViewFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if(position==0){
            return "Stock Pic 1";
        }else if(position==1){
            return "Stock Pic 2";
        }
        return super.getPageTitle(position);
    }

}
