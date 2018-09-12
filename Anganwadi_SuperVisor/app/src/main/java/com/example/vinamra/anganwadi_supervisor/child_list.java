package com.example.vinamra.anganwadi_supervisor;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
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
import java.util.Map;

public class child_list extends AppCompatActivity {
    RecyclerView recyclerView;
    String url="https://aproject2018.000webhostapp.com/module_jeetu/list_of_child.php";
    adapter_for_child_list mAdapter;
    List<child_reg_list_class> list=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_list);
        recyclerView= (RecyclerView) findViewById(R.id.recycle);
        callServer();
    }
    private void callServer() {
        final ProgressDialog loading=ProgressDialog.show(child_list.this,"Fetching Details....","please wait",false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loading.dismiss();
                //   Toast.makeText(List_for_tracking.this,response,Toast.LENGTH_LONG).show();
                try {
                    JSONArray jsonArray1=new JSONArray(response);
                    int l=0;
                    while (jsonArray1.length()>0){
                        JSONObject jsonObject= jsonArray1.getJSONObject(l);
                        child_reg_list_class object=new child_reg_list_class(jsonObject.getString("id"),jsonObject.getString("name"),jsonObject.getString("fname"),jsonObject.getString("mname"),jsonObject.getString("dob"),jsonObject.getString("weight"));
                        list.add(object);
                        mAdapter.notifyDataSetChanged();
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
                Toast.makeText(child_list.this,error.toString(),Toast.LENGTH_LONG).show();
                if (error instanceof com.android.volley.TimeoutError) {
                    Toast.makeText(getApplicationContext(), "Time Out Error", Toast.LENGTH_SHORT).show();
                } else if (error instanceof com.android.volley.NoConnectionError) {
                    Toast.makeText(getApplicationContext(), "No Connection Error", Toast.LENGTH_SHORT).show();
                } else if (error instanceof AuthFailureError) {
                    Toast.makeText(getApplicationContext(), "Authentication Failure Error", Toast.LENGTH_SHORT).show();
                } else if (error instanceof com.android.volley.NetworkError) {
                    Toast.makeText(getApplicationContext(), "Network Error", Toast.LENGTH_SHORT).show();
                } else if (error instanceof com.android.volley.ServerError) {
                    Toast.makeText(getApplicationContext(), "Server Error", Toast.LENGTH_SHORT).show();
                } else if (error instanceof com.android.volley.ParseError) {
                    Toast.makeText(getApplicationContext(), "JSON Parse Error", Toast.LENGTH_SHORT).show();
                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("result", "0");
                return params;
            }};
        MySingleton.getInstance(child_list.this).addToRequestQueue(stringRequest);
        mAdapter=new adapter_for_child_list(this,list);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
    }
}
