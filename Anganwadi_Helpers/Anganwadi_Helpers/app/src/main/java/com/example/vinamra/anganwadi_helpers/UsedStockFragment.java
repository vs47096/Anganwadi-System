package com.example.vinamra.anganwadi_helpers;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dd.processbutton.iml.ActionProcessButton;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import nl.dionsegijn.steppertouch.StepperTouch;

/**
 * Created by Vinamra on 3/29/2018.
 */

public class UsedStockFragment extends Fragment {
    private ArrayList<String> arrayListSpinnerData;
    private ArrayList<String> dummyStringArrayList;
    private ActionProcessButton proceedBtn;
    private FloatingActionButton addStockItemRowFabBtn;
    private FloatingActionButton removeStockItemRowFabBtn;
    private ListView stockItemsListView;
    private HashMap<Integer, String> stockItemsHashMap;
    private StockItemsListViewAdapter stockItemsListViewAdapter;
    private String stockData;
    private String date=null,time=null, meal =null,todaysmenu=null;
    FoodRecordActivity parentActivity;
    ProgressDialog p;
    private ViewPager viewPager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_usedstock,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        parentActivity= (FoodRecordActivity) getActivity();
        viewPager=(ViewPager) getActivity().findViewById(R.id.foodRecordViewPager);

        p = new ProgressDialog(getActivity());
        p.setCancelable(false);

        arrayListSpinnerData= new ArrayList<String>();
        dummyStringArrayList=new ArrayList<String>();
        dummyStringArrayList.add("Hello");//dummy string to add row
        proceedBtn=(ActionProcessButton) view.findViewById(R.id.stockItemEntryproceedBtn);
        proceedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            saveMenuAndUpdateStock();
            }
        });
        addStockItemRowFabBtn=(FloatingActionButton) view.findViewById(R.id.addStockItemRowFabBtn);
        removeStockItemRowFabBtn=(FloatingActionButton) view.findViewById(R.id.removeStockItemRowFabBtn);
        stockItemsListView=(ListView) view.findViewById(R.id.stockItemsListView);

        addStockItemRowFabBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addRow();
            }
        });
        removeStockItemRowFabBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeRow();
            }
        });
        getStockItems();

    }

    /**************************Function for getting Stock Items*******************************/
    private void getStockItems()
    {
        p.setMessage("Getting Stock Items List");
        p.show();
        String getStockItemsUrl = "http://aproject2018.000webhostapp.com/helpertask/getStockItems.php";
        StringRequest request = new StringRequest(Request.Method.GET, getStockItemsUrl, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response) {
                p.dismiss();
                parseStockItemsJSON(response);

            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error) {
                p.dismiss();
                Toast.makeText(getActivity(),"Sorry,unable to get data.Check if you are connected to Internet", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(request);
    }

    private void parseStockItemsJSON(String response) {
        stockItemsHashMap = new HashMap<Integer, String>();
        try {
            JSONObject mainObject = new JSONObject(response);
            JSONArray stockItems = mainObject.getJSONArray("data");
            for (int i = 0; i < stockItems.length(); i++) {
                JSONObject jsonObject = stockItems.getJSONObject(i);
                String values = jsonObject.getString("values");

                String [] stockItemsParts=values.split("-");
                Integer stockItemId= Integer.valueOf(stockItemsParts[0]);
                String stockItemName= stockItemsParts[1];

                stockItemsHashMap.put(stockItemId,stockItemName);
            }

            convertHashMapStockItemToArraYlist();
            stockItemsListViewAdapter=new StockItemsListViewAdapter(getContext(),arrayListSpinnerData,dummyStringArrayList);
            stockItemsListView.setAdapter(stockItemsListViewAdapter);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }
    /************************************Function End***************************************/

    /**************************Function for Hash Map to Array List*************************/
    public void convertHashMapStockItemToArraYlist()
    {
        for(Map.Entry<Integer, String> entry: stockItemsHashMap.entrySet()) {
            arrayListSpinnerData.add(entry.getKey()+" - "+String.valueOf(entry.getValue()));
        }

    }
    /************************************Function End***************************************/

    /*********************************Function for Adding Row*******************************/
    public void addRow()
    {
        dummyStringArrayList.add("Hello");
        stockItemsListViewAdapter.notifyDataSetChanged();
    }
    /************************************Function End***************************************/

    /*********************************Function for Removing Row*******************************/
    public void removeRow()
    {
        if(stockItemsListView.getChildCount()>1)
        {
            dummyStringArrayList.remove(dummyStringArrayList.size()-1);
            stockItemsListViewAdapter.notifyDataSetChanged();
        }
    }
    /************************************Function End***************************************/

    /*********************************Function for Saving Menu******************************/
    private void saveMenuAndUpdateStock() {
        date=parentActivity.getDate();
        time=parentActivity.getTime();
        meal=parentActivity.getMeal();
        todaysmenu=parentActivity.getMenu();

        p.setMessage("Saving Menu");
        p.show();
        String saveMenuUrl = "http://aproject2018.000webhostapp.com/helpertask/saveMenu.php?date="+date+"&time="+time+"&meal="+meal+"&todaysmenu="+todaysmenu;
        StringRequest updateRegStatusRequest = new StringRequest(Request.Method.GET, saveMenuUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //chngHelperPwdBtn.setProgress(0);
                saveMenuParseJSON(response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //chngHelperPwdBtn.setProgress(0);
                p.dismiss();
                if(isNetworkConnected (getActivity()))
                {
                    Toast.makeText(getActivity(),"Sorry,Check your Internet Connection",Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(getActivity(),"Sorry, Some Error occured while updating registration status",Toast.LENGTH_SHORT).show();

                }
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(updateRegStatusRequest);
    }

    private void saveMenuParseJSON(String response) {
        try {
            JSONObject mainObject = new JSONObject(response);
            JSONArray records = mainObject.getJSONArray("data");
            JSONObject jsonObject = records.getJSONObject(0);
            String updatestatus = jsonObject.getString("done");
            if(updatestatus.equals("true"))
            {
                //registration status update success

                Toast.makeText(getActivity(),"Menu saved",Toast.LENGTH_SHORT).show();
                updateStock();

            }
            else if(updatestatus.equals("false"))
            {
                //registration status update failed

                Toast.makeText(getActivity(),"Menu not saved",Toast.LENGTH_SHORT).show();
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /************************************Function End***************************************/

    /*********************************Function for Updating stock***************************/
    private void updateStock() {
        if(isNetworkConnected(getContext()))
        {
            createJSONAndConvertToString();
            //viewPager.setCurrentItem(1, true);
        }
        else if (!isNetworkConnected(getContext()))
        {
            Toast.makeText(getContext(),"Check your Internet Connection",Toast.LENGTH_SHORT).show();
        }

    }
    /************************************Function End***************************************/

    /****************************Function for Creating JSON file of Input Data****************/
    private void createJSONAndConvertToString() {
        try {

            JSONObject jsonObj = new JSONObject();
            JSONArray jsonArr = new JSONArray();
            for(int i=0;i<stockItemsListView.getChildCount();i++)
            {
                View view=stockItemsListView.getChildAt(i);
                SearchableSpinner searchableSpinner=(SearchableSpinner) view.findViewById(R.id.stockItemsLSpinner);
                String selectedStockString= String.valueOf(searchableSpinner.getSelectedItem());
                String stringParts[]=new String[2];
                stringParts=selectedStockString.split("-");
                String stockItemId=stringParts[0];
                StepperTouch stockItemQuantityStepper=(StepperTouch) view.findViewById(R.id.stockItemQuantity);
                String stockItemQuantity= String.valueOf(stockItemQuantityStepper.stepper.getValue());

                JSONObject obj = new JSONObject();
                obj.put(stockItemId,stockItemQuantity);

                jsonArr.put(obj);

            }
            jsonObj.put("data", jsonArr);
            stockData=jsonObj.toString();
            sendStockDataJSONToDatabase();

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    /**************************************************Function End*************************************************************/

    /**************************************Function for Sending Stock JSON to Database******************************************/
    private void sendStockDataJSONToDatabase() {
        p.setMessage("Updating Stock");
        String updateStockDataUrl = "http://aproject2018.000webhostapp.com/helpertask/updateStockDataByJson.php?action=stocksubtract&stockjsondata="+stockData;
        StringRequest updateRegStatusRequest = new StringRequest(Request.Method.GET, updateStockDataUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //chngHelperPwdBtn.setProgress(0);
                p.dismiss();
                updateStockDataJSON(response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //chngHelperPwdBtn.setProgress(0);
                p.dismiss();
                if(isNetworkConnected (getActivity()))
                {
                    Toast.makeText(getActivity(),"Sorry,Check your Internet Connection",Toast.LENGTH_LONG).show();

                }
                else
                {
                    Toast.makeText(getActivity(),"Sorry, Some Error occured while updating registration status",Toast.LENGTH_SHORT).show();

                }
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(updateRegStatusRequest);
    }

    private void updateStockDataJSON(String response) {
        try {
            JSONObject mainObject = new JSONObject(response);
            JSONArray records = mainObject.getJSONArray("data");
            JSONObject jsonObject = records.getJSONObject(0);
            String updatestatus = jsonObject.getString("done");
            if(updatestatus.equals("true"))
            {
                //registration status update success

                Toast.makeText(getActivity(),"Stock Data Updated",Toast.LENGTH_SHORT).show();
                viewPager.setCurrentItem(2,true);

            }
            else if(updatestatus.equals("false"))
            {
                //registration status update failed

                Toast.makeText(getActivity(),"Stock Data failed to Update",Toast.LENGTH_SHORT).show();
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**************************************************Function End*************************************************************/

    /**************************************Function for Checking Connection to Internet******************************************/
    public boolean isNetworkConnected (Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null)
        {
            NetworkInfo netInfos = connectivityManager.getActiveNetworkInfo();
            if(netInfos != null)
            {
                return netInfos.isConnected();
            }
        }
        return false;
    }
    /**************************************************Function End*************************************************************/

}
