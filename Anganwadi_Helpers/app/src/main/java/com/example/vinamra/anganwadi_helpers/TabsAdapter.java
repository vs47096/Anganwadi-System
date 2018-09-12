package com.example.vinamra.anganwadi_helpers;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Vinamra on 10/5/2017.
 */

public class TabsAdapter extends FragmentPagerAdapter {
    public TabsAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if(position==0){
            return new RegistrationLoginFragment();
        }else if(position==1){
            return new ChangePasswordFragment();
        }else if(position==2){
            return new PhotoNameFragment();
        }else if(position==3){
            return new ContactFragment();
        }else if(position==4){
            return new UploadIdProofFragment();
        }else if(position==5){
            return new RegistrationStatusFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 6;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if(position==0){
            return "Login";
        }else if(position==1){
            return "Password Change";
        }else if(position==2){
            return "Photo and Name";
        }else if(position==3){
            return "Contact Details";
        }else if(position==4){
            return "ID Proof";
        }else if(position==5){
            return "Registration Status";
        }
        return super.getPageTitle(position);
    }
}
