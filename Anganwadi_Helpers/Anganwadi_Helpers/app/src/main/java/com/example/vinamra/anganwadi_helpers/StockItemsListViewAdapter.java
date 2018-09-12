package com.example.vinamra.anganwadi_helpers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;

import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.util.ArrayList;

import nl.dionsegijn.steppertouch.StepperTouch;

/**
 * Created by Vinamra on 3/24/2018.
 */

class StockItemsListViewAdapter extends BaseAdapter{
    Context context;
    ArrayList<String> stockItemsArrayList;
    ArrayList<String> dummyStringArrayList;

    public StockItemsListViewAdapter(Context context, ArrayList<String> stockItemsArrayList, ArrayList<String> dummyStringArrayList) {
        this.context = context;
        this.stockItemsArrayList = stockItemsArrayList;
        this.dummyStringArrayList = dummyStringArrayList;
    }

    @Override
    public int getCount() {
        return dummyStringArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {

        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.listviewelementlayout_stockitemsadd,viewGroup,false);

        SearchableSpinner stockItemsLSpinner=(SearchableSpinner) view.findViewById(R.id.stockItemsLSpinner);
        StepperTouch stockItemQuantity=(StepperTouch) view.findViewById(R.id.stockItemQuantity);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item, stockItemsArrayList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        stockItemsLSpinner.setAdapter(adapter);

        stockItemQuantity.stepper.setMin(0);

        return view;

    }
}
