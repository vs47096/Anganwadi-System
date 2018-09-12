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

public class StockBillsAdapter extends BaseAdapter {
    Context context;
    ArrayList<StockBills> stockBillsList;

    public StockBillsAdapter(Context context, ArrayList<StockBills> stockBillsList) {
        this.context = context;
        this.stockBillsList = stockBillsList;
    }
    @Override
    public int getCount() {
        return stockBillsList.size();
    }

    @Override
    public Object getItem(int position) {
        return stockBillsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.stockbillslistelementlayout,parent,false);

        TextView billnoTxtView= (TextView) view.findViewById(R.id.billnoTxtView);
        TextView billdatetimeTxtView= (TextView) view.findViewById(R.id.billdatetimeTxtView);
        TextView billreceivedbyTxtView= (TextView) view.findViewById(R.id.billreceivedbyTxtView);
        TextView billtimeTxtView= (TextView) view.findViewById(R.id.billtimeTxtView);
        TextView billpicurlTxtView= (TextView) view.findViewById(R.id.billpicurlTxtView);
        TextView billitemspic1urlTxtView= (TextView) view.findViewById(R.id.billitemspic1urlTxtView);
        TextView billitemspic2urlTxtView= (TextView) view.findViewById(R.id.billitemspic2urlTxtView);
        TextView helperidTxtView= (TextView) view.findViewById(R.id.helperidTxtView);
        TextView helpersignatureurlTxtView= (TextView) view.findViewById(R.id.helpersignatureurlTxtView);
        TextView deliverypersonnameTxtView= (TextView) view.findViewById(R.id.deliverypersonnameTxtView);
        TextView deliverypsignurlTxtView= (TextView) view.findViewById(R.id.deliverypsignurlTxtView);

        billnoTxtView.setText(stockBillsList.get(position).getBillno());
        billdatetimeTxtView.setText(stockBillsList.get(position).getBilldate());
        billreceivedbyTxtView.setText(stockBillsList.get(position).getHelpername());
        billtimeTxtView.setText(stockBillsList.get(position).getBilltime());
        billpicurlTxtView.setText(stockBillsList.get(position).getBillpicurl());
        billitemspic1urlTxtView.setText(stockBillsList.get(position).getBillitemspic1url());
        billitemspic2urlTxtView.setText(stockBillsList.get(position).getBillitemspic2url());
        helperidTxtView.setText(stockBillsList.get(position).getHelperidwhoreceived());
        helpersignatureurlTxtView.setText(stockBillsList.get(position).getHelpersignatureurl());
        deliverypersonnameTxtView.setText(stockBillsList.get(position).getDeliverypersonname());
        deliverypsignurlTxtView.setText(stockBillsList.get(position).getDeliverysignatureurl());

        return view;
    }

}
