package com.example.vinamra.anganwadi_helpers;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ScrollView;

import com.dd.processbutton.iml.ActionProcessButton;
import com.q42.android.scrollingimageview.ScrollingImageView;

/**
 * Created by Vinamra on 10/5/2017.
 */

public class RegistrationStatusFragment extends Fragment {

    private ActionProcessButton registrationStatusFragmentDoneBtn;
    //private ScrollingImageView scrolling_background,scrolling_foreground;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_registrationstatus,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        registrationStatusFragmentDoneBtn= (ActionProcessButton) view.findViewById(R.id.registrationStatusFragmentDoneBtn);
        registrationStatusFragmentDoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
    }
}
