package com.example.vinamra.anganwadi_helpers;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dd.processbutton.iml.ActionProcessButton;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Vinamra on 3/29/2018.
 */

public class EnterMenuFragment extends Fragment {
    private ViewPager viewPager;
    private ActionProcessButton proceedBtn;
    private EditText stockItemsAddDateEditTxt;
    private EditText stockItemsAddTimeEditTxt;
    private EditText mealEditText;
    private MaterialEditText todaysMenuEditText;
    private String date=null,time=null, meal =null;
    FoodRecordActivity parentActivity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_entermenu,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        parentActivity = (FoodRecordActivity) getActivity();

        viewPager=(ViewPager) getActivity().findViewById(R.id.foodRecordViewPager);

        proceedBtn=(ActionProcessButton) view.findViewById(R.id.savemenuproceedBtn);
        proceedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            saveDataToVariableAndMoveToNextFragment();
            }
        });
        stockItemsAddDateEditTxt=(EditText) view.findViewById(R.id.menuDateEditText);
        stockItemsAddTimeEditTxt=(EditText) view.findViewById(R.id.menuTimeEditText);
        mealEditText =(EditText) view.findViewById(R.id.mealEditText);

        todaysMenuEditText=(MaterialEditText) view.findViewById(R.id.todaysMenuEditText);

        getServerDateTime();
    }

    /************************************Getting Server Date-Time**************************/
    private void getServerDateTime()
    {
        final ProgressDialog p = new ProgressDialog(getContext());
        p.setCancelable(false);
        p.setMessage("Getting Date and Time");
        p.show();
        String getServerDateTimeUrl = "http://aproject2018.000webhostapp.com/helpertask/getServerTimeDate.php";
        StringRequest request = new StringRequest(Request.Method.GET, getServerDateTimeUrl, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response) {
                p.dismiss();
                parseServerDateTimeJSON(response);

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

    private void parseServerDateTimeJSON(String response) {
        try {
            JSONObject mainObject = new JSONObject(response);
            JSONArray data = mainObject.getJSONArray("data");
            JSONObject jsonObject = data.getJSONObject(0);

                date=jsonObject.getString("date");
                time=jsonObject.getString("time");

            stockItemsAddDateEditTxt.setText(date);
            stockItemsAddTimeEditTxt.setText(time);

            String[] timeParts=time.split(":");
            int hour= Integer.parseInt(timeParts[0]);
            if(hour>=5 && hour<=11 )
            {
                meal ="Breakfast";
            }
            else if(hour>=12 && hour<=15 )
            {
                meal ="Lunch";
            }
            else if(hour>=16 && hour<=17 )
            {
                meal ="Snacks";
            }
            else if(hour>=18 && hour<=20 )
            {
                meal ="Dinner";
            }
            else if(hour>=21)
            {
                meal ="Midnight Meal";
            }
            mealEditText.setText(meal);

            parentActivity.setDateTime(date,time);
            parentActivity.setMeal(meal);

        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }
    /************************************Function End**************************************/

    /*********************************Saving Data to Variable*****************************/
    private void saveDataToVariableAndMoveToNextFragment() {
        String todaysMenu= String.valueOf(todaysMenuEditText.getText());
        if(todaysMenu.equals(""))
        {
           todaysMenuEditText.setError("Don`t leave it blank");
        }
        else if(!todaysMenu.equals(""))
        {
            parentActivity.setMenu(todaysMenu);
            viewPager.setCurrentItem(1,true);
        }
    }
    /************************************Function End**************************************/

}
