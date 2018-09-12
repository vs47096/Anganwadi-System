package com.example.vinamra.anganwadi_supervisor;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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
 * Created by Vinamra on 2/26/2018.
 */

public class UnverifiedHelpersListActivity extends AppCompatActivity {
    private ListView unverifiedHelpersListView;
    private String TAG="Unverified Helper list Activty";
    private ArrayList<Helpers> helpersList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unverifiedhelperslist);
        initialize();
    }

    private void initialize() {
        unverifiedHelpersListView = (ListView) findViewById(R.id.unverifiedHelpersList);
        unverifiedHelpersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                TextView helperIdVerifyView= (TextView) view.findViewById(R.id.helperIdVerifyView);
                TextView helperAvatarUrlTextView= (TextView) view.findViewById(R.id.helperAvatarUrl);
                TextView nameTextVerifyView= (TextView) view.findViewById(R.id.nameTextVerifyView);
                TextView helperProofUrlVerifyView= (TextView) view.findViewById(R.id.helperProofUrlVerifyView);
                view.findViewById(R.id.avatar_imageVerifyView).buildDrawingCache();
                Bitmap bmp=view.findViewById(R.id.avatar_imageVerifyView).getDrawingCache();
                String helperId= helperIdVerifyView.getText().toString();
                String helperAvatarUrl=helperAvatarUrlTextView.getText().toString();
                String helperName= nameTextVerifyView.getText().toString();
                String helperProofUrl= helperProofUrlVerifyView.getText().toString();

                Intent i=new Intent(UnverifiedHelpersListActivity.this,HelperDetailsVerifyActivity.class);
                i.putExtra("avatarimage",bmp);
                i.putExtra("helperId",helperId);
                i.putExtra("helperAvatarUrl",helperAvatarUrl);
                i.putExtra("helperName",helperName);
                i.putExtra("helperProofUrl",helperProofUrl);
                startActivity(i);

            }
        });
        if(isNetworkConnected(UnverifiedHelpersListActivity.this))
        {
            getUnverifiedHelpersData();
        }
        else
        {
            Toast.makeText(UnverifiedHelpersListActivity.this,"Sorry,Check your Internet Connection",Toast.LENGTH_LONG).show();
        }

    }


    private void getUnverifiedHelpersData() {
        final ProgressDialog p = new ProgressDialog(this);
        p.setCancelable(false);
        p.setMessage("Getting Unverified Helpers List");
        p.show();
        String url = "https://aproject2018.000webhostapp.com/loginFiles/supervisor/getunverifiedhelperslist.php";
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response) {
                Log.d(TAG,"Got JSON response");
                p.dismiss();
                parseUnverifiedHelpersJSON(response);

            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error) {
                p.dismiss();
                Toast.makeText(UnverifiedHelpersListActivity.this,"Sorry,unable to get data.Check if you are connected to Internet", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue queue = Volley.newRequestQueue(UnverifiedHelpersListActivity.this);
        queue.add(request);
    }

    private void parseUnverifiedHelpersJSON(String response) {

        helpersList = new ArrayList<Helpers>();
        try {
            JSONObject mainObject = new JSONObject(response);
            JSONArray contacts = mainObject.getJSONArray("data");
            for (int i = 0; i < contacts.length(); i++) {
                JSONObject jsonObject = contacts.getJSONObject(i);
                String helperId = jsonObject.getString("helperid");
                String firstname = jsonObject.getString("firstname");
                String middlename = jsonObject.getString("middlename");
                String surname = jsonObject.getString("surname");
                String photourl = jsonObject.getString("photourl");

                /********************Replacing /helper_proof/ with %2fhelper_proof%2f*******************/
                photourl=photourl.replace("/helper_avatar/","%2Fhelper_avatar%2F");
                /********************************Replacing Task End************************************/

                String photourltoken = jsonObject.getString("photourltoken");
                String photourlComplete=photourl+"&token"+photourltoken;
                String idproofurl = jsonObject.getString("idproofurl");

                /********************Replacing /helper_proof/ with %2fhelper_proof%2f*******************/
                idproofurl=idproofurl.replace("/helper_proof/","%2fhelper_proof%2f");
                /********************************Replacing Task End************************************/

                String idproofurltoken = jsonObject.getString("idproofurltoken");
                String idproofurlComplete=idproofurl+"&token"+idproofurltoken;


                Log.d(TAG,photourlComplete);
                Log.d(TAG,idproofurlComplete);

                Helpers helper = new Helpers();
                helper.setHelperid(helperId);
                helper.setHelpername(firstname+middlename+surname);
                helper.setPhotourl(photourlComplete);
                helper.setIdproofurl(idproofurlComplete);


                helpersList.add(helper);
                Log.d(TAG,"Helper added");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        UnverifiedHelpersListAdpater adapter=new UnverifiedHelpersListAdpater(this, helpersList);
        unverifiedHelpersListView.setAdapter(adapter);
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
