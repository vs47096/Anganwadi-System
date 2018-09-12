package com.example.vinamra.anganwadi_supervisor;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.AdapterView;
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
 * Created by Vinamra on 4/5/2018.
 */

public class StockBillsActivity extends AppCompatActivity {
    private ListView stockBillsListView;
    private ImageView iv;
    private TextView searchStockEditText;
    private AnimatedVectorDrawable searchToBar;
    private AnimatedVectorDrawable barToSearch;
    private float offset;
    private Interpolator interp;
    private int duration;
    private boolean expanded = false;
    ArrayList<StockBills> stockBillsList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stockbills);
        initialize();
    }



    private void initialize() {
        iv = (ImageView) findViewById(R.id.search);
        stockBillsListView =(ListView)findViewById(R.id.stockBillsListView);
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

        if(isNetworkConnected(StockBillsActivity.this))
        {
            getStockBills();
        }
        else
        {
            Toast.makeText(StockBillsActivity.this,"Sorry,Check your Internet Connection",Toast.LENGTH_LONG).show();
        }
        stockBillsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
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


                Intent i = new Intent(StockBillsActivity.this,StockBillViewActivity.class);
                i.putExtra("billno",billnoTxtView.getText().toString());
                i.putExtra("billdate",billdatetimeTxtView.getText().toString());
                i.putExtra("helpername",billreceivedbyTxtView.getText().toString());
                i.putExtra("billtime",billtimeTxtView.getText().toString());
                i.putExtra("billpicurl",billpicurlTxtView.getText().toString());
                i.putExtra("billitems1picurl",billitemspic1urlTxtView.getText().toString());
                i.putExtra("billitems2picurl",billitemspic2urlTxtView.getText().toString());
                i.putExtra("helperid",helperidTxtView.getText().toString());
                i.putExtra("helpersignatureurl",helpersignatureurlTxtView.getText().toString());
                i.putExtra("deliverypname",deliverypersonnameTxtView.getText().toString());
                i.putExtra("deliverypsign",deliverypsignurlTxtView.getText().toString());
                startActivity(i);

            }
        });
    }

    private void getStockBills() {
        final ProgressDialog p = new ProgressDialog(this);
        p.setMessage("Getting Stock Bill Details");
        p.show();
        String url = "http://aproject2018.000webhostapp.com/supervisorTasks/getStockBillRecords.php";
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response) {
                p.dismiss();
                parseStockBillsJSON(response);
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error) {
                p.dismiss();
                Toast.makeText(StockBillsActivity.this,"Data can`t be retrieved", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue queue = Volley.newRequestQueue(StockBillsActivity.this);
        queue.add(request);
    }

    private void parseStockBillsJSON(String response) {
        stockBillsList = new ArrayList<StockBills>();
        try {
            JSONObject mainObject = new JSONObject(response);
            JSONArray contacts = mainObject.getJSONArray("data");
            for (int i = 0; i < contacts.length(); i++) {
                JSONObject jsonObject = contacts.getJSONObject(i);
                String billno = jsonObject.getString("billno");
                String billdate = jsonObject.getString("billdate");
                String billtime = jsonObject.getString("billtime");

                String billpicurl = jsonObject.getString("billpicurl");
                /********************Replacing /helper_proof/ with %2fhelper_proof%2f*******************/
                billpicurl=billpicurl.replace("/stock_bills/","%2Fstock_bills%2F");
                /********************************Replacing Task End************************************/
                String billpicurltoken = jsonObject.getString("billpicurltoken");
                String billpicurlComplete=billpicurl+"&token"+billpicurltoken;

                String billitemspic1url = jsonObject.getString("billitemspic1url");
                /********************Replacing /helper_proof/ with %2fhelper_proof%2f*******************/
                billitemspic1url=billitemspic1url.replace("/stock_items_pics/","%2Fstock_items_pics%2F");
                /********************************Replacing Task End************************************/
                String billitemspic1urltoken = jsonObject.getString("billitemspic1urltoken");
                String billitemspic1urlcomplete=billitemspic1url+"&token"+billitemspic1urltoken;

                String billitemspic2url = jsonObject.getString("billitemspic2url");
                /********************Replacing /helper_proof/ with %2fhelper_proof%2f*******************/
                billitemspic2url=billitemspic2url.replace("/stock_items_pics/","%2Fstock_items_pics%2F");
                /********************************Replacing Task End************************************/
                String billitemspic2urltoken = jsonObject.getString("billitemspic2urltoken");
                String billitemspic2urlcomplete=billitemspic2url+"&token"+billitemspic2urltoken;

                String helperidwhoreceived = jsonObject.getString("helperidwhoreceived");
                String firstname = jsonObject.getString("firstname");
                String middlename = jsonObject.getString("middlename");
                String surname = jsonObject.getString("surname");

                String helpersignatureurl = jsonObject.getString("helpersignatureurl");
                /********************Replacing /helper_proof/ with %2fhelper_proof%2f*******************/
                helpersignatureurl=helpersignatureurl.replace("/stock_received_signature/","%2Fstock_received_signature%2F");
                /********************************Replacing Task End************************************/
                String helpersignatureurltoken = jsonObject.getString("helpersignatureurltoken");
                String helpersignatureurlcomplete=helpersignatureurl+"&token"+helpersignatureurltoken;

                String deliverypersonname = jsonObject.getString("deliverypersonname");

                String deliverysignatureurl = jsonObject.getString("deliverysignatureurl");
                /********************Replacing /helper_proof/ with %2fhelper_proof%2f*******************/
                deliverysignatureurl=deliverysignatureurl.replace("/stock_received_signature/","%2Fstock_received_signature%2F");
                /********************************Replacing Task End************************************/
                String deliverysignatureurltoken = jsonObject.getString("deliverysignatureurltoken");
                String deliverysignatureurlcomplete=deliverysignatureurl+"&token"+deliverysignatureurltoken;

                StockBills stockBills = new StockBills();
                stockBills.setBillno(billno);
                stockBills.setBilldate(billdate);
                stockBills.setBilltime(billtime);
                stockBills.setBillpicurl(billpicurlComplete);
                stockBills.setBillitemspic1url(billitemspic1urlcomplete);
                stockBills.setBillitemspic2url(billitemspic2urlcomplete);
                stockBills.setHelperidwhoreceived(helperidwhoreceived);
                stockBills.setHelpername(firstname+" "+middlename+" "+surname);
                stockBills.setHelpersignatureurl(helpersignatureurlcomplete);
                stockBills.setDeliverypersonname(deliverypersonname);
                stockBills.setDeliverysignatureurl(deliverysignatureurl);

                stockBillsList.add(stockBills);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        StockBillsAdapter adapter=new StockBillsAdapter(this,stockBillsList);
        stockBillsListView.setAdapter(adapter);
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
