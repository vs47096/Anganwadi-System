package com.example.vinamra.anganwadi_helpers;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Vinamra on 3/22/2018.
 */

public class NewStockTabsAdapter extends FragmentPagerAdapter {
    public NewStockTabsAdapter(FragmentManager fm) {
        super(fm);
    }

    public Fragment getItem(int position) {
        if(position==0){
            return new EnterStockItemsFragment();
        }else if(position==1){
            return new StockBillUploadFragment();
        }else if(position==2){
            return new StockItemsPicUploadFragment();
        }else if(position==3){
            return new StockReceivedSignatureFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if(position==0){
            return "Enter Items";
        }else if(position==1){
            return "Upload Bill";
        }else if(position==2){
            return "Upload Items Pic";
        }else if(position==3){
            return "Signature";
        }
        return super.getPageTitle(position);
    }
}
