package com.example.vinamra.anganwadi_supervisor;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class List_of_doctor extends AppCompatActivity {
    RecyclerView recyclerView;
    class_for_dr_list obj;
    List<class_for_dr_list> list=new ArrayList<>();
    String url="https://aproject2018.000webhostapp.com/module_jeetu/selectAllDr.php";
    Adapter_for_dr_list adpter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_doctor);
        recyclerView= (RecyclerView) findViewById(R.id.reecycle);
        callServer();
    }

    private void callServer() {
        final ProgressDialog loading=ProgressDialog.show(List_of_doctor.this,"Fetching Details....","please wait",false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loading.dismiss();
                 //Toast.makeText(List_of_doctor.this,response,Toast.LENGTH_LONG).show();
                try {
                    JSONArray jsonArray1=new JSONArray(response);
                    int l=0;
                    while (jsonArray1.length()>0){
                        JSONObject jsonObject= jsonArray1.getJSONObject(l);
                         obj=new class_for_dr_list(jsonObject.getString("name"),jsonObject.getString("org"),jsonObject.getString("date"),
                                                     jsonObject.getString("childdata"));
                        list.add(obj);
                        adpter.notifyDataSetChanged();
                        l++;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //  circleLoadingView.stopFailure();
                //  circleLoadingView.setVisibility(View.GONE);
                loading.dismiss();
                Toast.makeText(List_of_doctor.this,error.toString(),Toast.LENGTH_LONG).show();
                if (error instanceof com.android.volley.TimeoutError) {
                    Toast.makeText(getApplicationContext(), "Time Out Error", Toast.LENGTH_SHORT).show();
                } else if (error instanceof com.android.volley.NoConnectionError) {
                    Toast.makeText(getApplicationContext(), "No Connection Error", Toast.LENGTH_SHORT).show();
                } else if (error instanceof com.android.volley.AuthFailureError) {
                    Toast.makeText(getApplicationContext(), "Authentication Failure Error", Toast.LENGTH_SHORT).show();
                } else if (error instanceof com.android.volley.NetworkError) {
                    Toast.makeText(getApplicationContext(), "Network Error", Toast.LENGTH_SHORT).show();
                } else if (error instanceof com.android.volley.ServerError) {
                    Toast.makeText(getApplicationContext(), "Server Error", Toast.LENGTH_SHORT).show();
                } else if (error instanceof com.android.volley.ParseError) {
                    Toast.makeText(getApplicationContext(), "JSON Parse Error", Toast.LENGTH_SHORT).show();
                }
            }
        });
        MySingleton.getInstance(List_of_doctor.this).addToRequestQueue(stringRequest);
        adpter=new Adapter_for_dr_list(list,this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adpter);
    }
}
