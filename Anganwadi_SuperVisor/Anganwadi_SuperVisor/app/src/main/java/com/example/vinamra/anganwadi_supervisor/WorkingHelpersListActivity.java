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
 * Created by Vinamra on 2/28/2018.
 */

public class WorkingHelpersListActivity extends AppCompatActivity {
    private ListView workingHelpersListView;
    private String TAG="Verified Helper list Activty";
    private ArrayList<Helpers> helpersList;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workinghelperslist);
        initialize();
    }

    private void initialize() {
        workingHelpersListView= (ListView) findViewById(R.id.workingHelpersListView);
        workingHelpersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                TextView helperIdTxtView= (TextView) view.findViewById(R.id.helperId);
                TextView helperAvatarUrlTxtView= (TextView) view.findViewById(R.id.helperAvatarUrl);
                TextView nameTxtView= (TextView) view.findViewById(R.id.nameTextView);
                TextView helperProofUrlTxtView= (TextView) view.findViewById(R.id.helperProofUrl);
                TextView contact1TextView= (TextView) view.findViewById(R.id.contact1TextView);
                TextView contact2TextView= (TextView) view.findViewById(R.id.contact2TextView);
                view.findViewById(R.id.avatar_image).buildDrawingCache();
                Bitmap bmp=view.findViewById(R.id.avatar_image).getDrawingCache();
                String helperId= helperIdTxtView.getText().toString();
                String helperAvatarUrl= helperAvatarUrlTxtView.getText().toString();
                String helperName= nameTxtView.getText().toString();
                String helperProofUrl= helperProofUrlTxtView.getText().toString();
                String contact1 = contact1TextView.getText().toString();
                String contact2 = contact2TextView.getText().toString();

                Intent i=new Intent(WorkingHelpersListActivity.this,HelperDetailsViewActivity.class);
                i.putExtra("avatarimage",bmp);
                i.putExtra("helperId",helperId);
                i.putExtra("helperAvatarUrl",helperAvatarUrl);
                i.putExtra("helperName",helperName);
                i.putExtra("helperProofUrl",helperProofUrl);
                i.putExtra("contact1",contact1);
                i.putExtra("contact2",contact2);
                startActivity(i);

            }
        });
        if(isNetworkConnected(WorkingHelpersListActivity.this))
        {
            getWorkingHelpersData();
        }
        else
        {
            Toast.makeText(WorkingHelpersListActivity.this,"Sorry,Check your Internet Connection",Toast.LENGTH_LONG).show();
        }
    }

    private void getWorkingHelpersData() {
        final ProgressDialog p = new ProgressDialog(this);
        p.setMessage("Getting Helpers List");
        p.show();
        //final ArrayList<Helpers> helpersList = new ArrayList<Helpers>();
        String url = "https://aproject2018.000webhostapp.com/loginFiles/supervisor/getworkinghelperslist.php";
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response) {
                Log.d(TAG,"Got JSON response");
                p.dismiss();
                parseVerifiedHelpersJSON(response);
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error) {
                p.dismiss();
                Toast.makeText(WorkingHelpersListActivity.this,"Data can`t be retrieved", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue queue = Volley.newRequestQueue(WorkingHelpersListActivity.this);
        queue.add(request);

    }

    private void parseVerifiedHelpersJSON(String response) {

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

                String contact1 = jsonObject.getString("contact1");
                String contact2 = jsonObject.getString("contact2");

                Log.d(TAG,photourlComplete);
                Log.d(TAG,idproofurlComplete);

                Helpers helper = new Helpers();
                helper.setHelperid(helperId);
                helper.setHelpername(firstname+" "+middlename+" "+surname);
                helper.setPhotourl(photourlComplete);
                helper.setIdproofurl(idproofurlComplete);
                helper.setContact1(contact1);
                helper.setContact2(contact2);

                helpersList.add(helper);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        WorkingHelpersListAdapter adapter=new WorkingHelpersListAdapter(this,helpersList);
        workingHelpersListView.setAdapter(adapter);
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
