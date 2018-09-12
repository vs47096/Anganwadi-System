package com.example.vinamra.anganwadi_supervisor;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Vinamra on 4/4/2018.
 */

public class CurrentStockActivity extends AppCompatActivity {
    private ListView currentStockListView;
    private ImageView iv;
    private TextView searchStockEditText;
    private AnimatedVectorDrawable searchToBar;
    private AnimatedVectorDrawable barToSearch;
    private float offset;
    private Interpolator interp;
    private int duration;
    private boolean expanded = false;
    ArrayList<Stock> stockList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currentstock);
        initialize();
    }

    private void initialize() {


        iv = (ImageView) findViewById(R.id.search);
        currentStockListView=(ListView)findViewById(R.id.currentStockListView);
        searchStockEditText = (TextView) findViewById(R.id.searchStockEditText);
        searchStockEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        searchToBar = (AnimatedVectorDrawable) getResources().getDrawable(R.drawable.anim_search_to_bar);
        barToSearch = (AnimatedVectorDrawable) getResources().getDrawable(R.drawable.anim_bar_to_search);
        interp = AnimationUtils.loadInterpolator(this, android.R.interpolator.linear_out_slow_in);
        duration = getResources().getInteger(R.integer.duration_bar);
        // iv is sized to hold the search+bar so when only showing the search icon, translate the
        // whole view left by half the difference to keep it centered
        offset = -71f * (int) getResources().getDisplayMetrics().scaledDensity;
        iv.setTranslationX(offset);

        if(isNetworkConnected(CurrentStockActivity.this))
        {
            getStock();
        }
        else
        {
            Toast.makeText(CurrentStockActivity.this,"Sorry,Check your Internet Connection",Toast.LENGTH_LONG).show();
        }
    }

    private void getStock() {
        final ProgressDialog p = new ProgressDialog(this);
        p.setMessage("Getting Stock Details");
        p.setCancelable(false);
        p.show();
        String url = "http://aproject2018.000webhostapp.com/supervisorTasks/getStock.php";
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response) {
                p.dismiss();
                parseStockJSON(response);
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error) {
                p.dismiss();
                Toast.makeText(CurrentStockActivity.this,"Data can`t be retrieved", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue queue = Volley.newRequestQueue(CurrentStockActivity.this);
        queue.add(request);
    }

    private void parseStockJSON(String response) {
        stockList = new ArrayList<Stock>();
        try {
            JSONObject mainObject = new JSONObject(response);
            JSONArray contacts = mainObject.getJSONArray("data");
            for (int i = 0; i < contacts.length(); i++) {
                JSONObject jsonObject = contacts.getJSONObject(i);
                String itemname = jsonObject.getString("itemname");
                String quantity = jsonObject.getString("quantity");

                Stock stock = new Stock();
                stock.setItemname(itemname);
                stock.setQuantity(quantity);

                stockList.add(stock);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        CurrentStockAdapter adapter=new CurrentStockAdapter(this,stockList);
        currentStockListView.setAdapter(adapter);
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void animate(View view) {

        if (!expanded) {
            iv.setImageDrawable(searchToBar);
            searchToBar.start();
            iv.animate().translationX(0f).setDuration(duration).setInterpolator(interp);
            searchStockEditText.animate().alpha(1f).setStartDelay(duration - 100).setDuration(100).setInterpolator(interp);
        } else {
            iv.setImageDrawable(barToSearch);
            barToSearch.start();
            iv.animate().translationX(offset).setDuration(duration).setInterpolator(interp);
            searchStockEditText.setAlpha(0f);
        }
        expanded = !expanded;
    }

    public boolean isNetworkConnected (Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo netInfos = connectivityManager.getActiveNetworkInfo();
            if (netInfos != null) {
                return netInfos.isConnected();
            }
        }
        return false;
    }
}
