package com.example.vinamra.anganwadi_supervisor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Vinamra on 4/5/2018.
 */

public class RejectedHelpersAdapter extends BaseAdapter {
    Context context;
    ArrayList<RejectedHelpers> rejectedHelpersArrayList;

    public RejectedHelpersAdapter(Context context, ArrayList<RejectedHelpers> rejectedHelpersArrayList) {
        this.context = context;
        this.rejectedHelpersArrayList = rejectedHelpersArrayList;
    }
    @Override
    public int getCount() {
        return rejectedHelpersArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return rejectedHelpersArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.rejecthelperslistelementlayout,parent,false);

        TextView rejectedHelperidTxtView= (TextView) view.findViewById(R.id.rejectedHelperidTxtView);
        TextView rejectedHelperNameTxtView= (TextView) view.findViewById(R.id.rejectedHelperNameTxtView);
        TextView rejectionReasonTxtView= (TextView) view.findViewById(R.id.rejectionReasonTxtView);

        rejectedHelperidTxtView.setText(rejectedHelpersArrayList.get(position).getHelperid());
        rejectedHelperNameTxtView.setText(rejectedHelpersArrayList.get(position).getHelpername());
        rejectionReasonTxtView.setText(rejectedHelpersArrayList.get(position).getRejectionreason());

        return view;
    }
}
