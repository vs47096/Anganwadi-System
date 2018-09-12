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

public class CurrentStockAdapter  extends BaseAdapter {
    Context context;
    ArrayList<Stock> stockArrayList;

    public CurrentStockAdapter(Context context, ArrayList<Stock> stockArrayList) {
        this.context = context;
        this.stockArrayList = stockArrayList;
    }

    @Override
    public int getCount() {
        return stockArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return stockArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.stocklistelementlayout,parent,false);

        TextView itemname= (TextView) view.findViewById(R.id.itemnameTxtView);
        TextView itemquantity= (TextView) view.findViewById(R.id.itemquantityTxtView);

        itemname.setText(stockArrayList.get(position).getItemname());
        itemquantity.setText(stockArrayList.get(position).getQuantity()+" Kg");

        return view;
    }
}
