package com.example.vinamra.anganwadi_supervisor;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

/**
 * Created by Vinamra on 10/4/2017.
 */

public class NewHelperActivity extends AppCompatActivity {

    private MaterialEditText firstNameTxtBox,surNameTxtBox,userIdTxtBox,passwordTxtBox;
    private String firstName,surName, helperuserId, helperpassword;
    private Button createHelperAccountButton,generate_id_pass;
    private LinearLayout newHelperParentLayout;
    private int availableidcheckpass=1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newhelper);
        initializeView();
    }

    private void initializeView() {
        newHelperParentLayout = (LinearLayout) findViewById(R.id.newHelperParentLayout);
        firstNameTxtBox = (MaterialEditText) findViewById(R.id.firstName);
        surNameTxtBox = (MaterialEditText) findViewById(R.id.surName);
        userIdTxtBox = (MaterialEditText) findViewById(R.id.userId);
        userIdTxtBox.setFocusable(false);
        userIdTxtBox.setClickable(true);
        userIdTxtBox.setFloatingLabelText("Generated Helper User ID");
        passwordTxtBox = (MaterialEditText) findViewById(R.id.password);
        passwordTxtBox.setFocusable(false);
        passwordTxtBox.setClickable(true);
        passwordTxtBox.setFloatingLabelText("Generated Helper Password");
        generate_id_pass = (Button) findViewById(R.id.generate_id_pass);
        generate_id_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firstName = String.valueOf(firstNameTxtBox.getText());
                surName = String.valueOf(surNameTxtBox.getText());
                checkAvailabilityOfID(firstName);
            }
        });
        createHelperAccountButton= (Button) findViewById(R.id.createHelperAccountButton);
        createHelperAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newhelpertempdatastorage();
            }
        });
    }

    private void checkAvailabilityOfID(String generatedUserID) {

        if(generatedUserID.equals(""))
        {
            firstNameTxtBox.setError("Enter Name");
        }
        else
        {
            final ProgressDialog p = new ProgressDialog(NewHelperActivity.this);
            p.setMessage("Generating ID and Password");
            p.setCancelable(false);
            p.setCanceledOnTouchOutside(false);
            p.show();
            String url;
            url = "https://aproject2018.000webhostapp.com/loginFiles/supervisor/checkAvailabilityOfID.php?helpergenerateduserid="+generatedUserID;
            StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    p.dismiss();
                    checkAvailabilityOfIdparseJSON(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    p.dismiss();
                    if(isNetworkConnected (NewHelperActivity.this))
                    {
                        Toast.makeText(NewHelperActivity.this,"Sorry,Check your Internet Connection",Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        Toast.makeText(NewHelperActivity.this,"Sorry,Something Happened",Toast.LENGTH_SHORT).show();
                    }
                }
            });
            RequestQueue requestQueue = Volley.newRequestQueue(NewHelperActivity.this);
            requestQueue.add(request);
        }
    }


    private void newhelpertempdatastorage() {

        helperuserId = String.valueOf(userIdTxtBox.getText());
        helperpassword = String.valueOf(passwordTxtBox.getText());

        if(firstName.equals(""))
        {
            firstNameTxtBox.setError("Enter Name");
        }
        else
        {
            final ProgressDialog p = new ProgressDialog(NewHelperActivity.this);
            p.setMessage("Creating Helper Account");
            p.setCancelable(false);
            p.setCanceledOnTouchOutside(false);
            p.show();
            String url;
            if(surName.equals(""))
            {
                url = "https://aproject2018.000webhostapp.com/loginFiles/supervisor/newhelpertempdatastorage.php?helperuserid="+ helperuserId +"&helperfirstname="+firstName+"&helperpwd="+ helperpassword +"";
                Log.d("NewHelperActivity",url);
            }
            else
            {
                url = "https://aproject2018.000webhostapp.com/loginFiles/supervisor/newhelpertempdatastorage.php?helperuserid="+ helperuserId +"&helperfirstname="+firstName+"&helpersurname="+surName+"&helperpwd="+ helperpassword +"";
                Log.d("NewHelperActivity",url);
            }
            StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    p.dismiss();
                    saveHelperTempDataparseJSON(response);
                    Log.d("NewHelperActivity","Successful Response");
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    p.dismiss();
                    Log.d("NewHelperActivity","Error Response");
                    if(isNetworkConnected (NewHelperActivity.this))
                    {
                        Toast.makeText(NewHelperActivity.this,"Sorry,Check your Internet Connection",Toast.LENGTH_LONG).show();
                        Log.d("NewHelperActivity","No internet");
                    }
                    else
                    {
                        Toast.makeText(NewHelperActivity.this,"Sorry,Something Happened",Toast.LENGTH_SHORT).show();
                        Log.d("NewHelperActivity","Some error");
                    }
                }
            });

            RequestQueue requestQueue = Volley.newRequestQueue(NewHelperActivity.this);
            requestQueue.add(request);
        }
    }

    private void checkAvailabilityOfIdparseJSON(String response) {
        try
        {
            JSONObject mainObject = new JSONObject(response);
            JSONArray records = mainObject.getJSONArray("data");

            JSONObject jsonObject = records.getJSONObject(0);
            String status = jsonObject.getString("available");

            if(status.equals("true"))
            {
                Toast.makeText(NewHelperActivity.this,"UserID is generated",Toast.LENGTH_LONG).show();
                String helperid = jsonObject.getString("helperid");
                userIdTxtBox.setText(helperid);
                String helperTempPassword=generateRandomPassword();
                passwordTxtBox.setText(helperTempPassword);
                availableidcheckpass=1;
            }
            else if(status.equals("false"))
            {
                ++availableidcheckpass;
                if(!surName.equals(""))
                {
                    if(availableidcheckpass==2 )
                    {
                        checkAvailabilityOfID(firstName+surName);
                    }
                    else if(availableidcheckpass==3)
                    {
                        int random = (int )(Math.random() * 10 + 1);
                        checkAvailabilityOfID(firstName+random);
                    }
                    else if(availableidcheckpass==4)
                    {
                        int random = (int )(Math.random() * 100 + 1);
                        checkAvailabilityOfID(firstName+random);
                    }
                    else if(availableidcheckpass==5)
                    {
                        int random = (int )(Math.random() * 1000 + 1);
                        checkAvailabilityOfID(firstName+random);
                    }
                }
                else if(surName.equals(""))
                {
                    if(availableidcheckpass==2)
                    {
                    int random = (int )(Math.random() * 10 + 1);
                    checkAvailabilityOfID(firstName+random);
                    }
                    else if(availableidcheckpass==3)
                    {
                    int random = (int )(Math.random() * 100 + 1);
                    checkAvailabilityOfID(firstName+random);
                    }
                    else if(availableidcheckpass==4)
                    {
                    int random = (int )(Math.random() * 1000 + 1);
                    checkAvailabilityOfID(firstName+random);
                    }
                }
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String generateRandomPassword() {

            String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
            StringBuilder salt = new StringBuilder();
            Random rnd = new Random();
            while (salt.length() < 8)
            { // length of the random string.
                int index = (int) (rnd.nextFloat() * SALTCHARS.length());
                salt.append(SALTCHARS.charAt(index));
            }
            String saltStr = salt.toString();
            return saltStr;
    }

    private void saveHelperTempDataparseJSON(String response) {
        try
        {
            JSONObject mainObject = new JSONObject(response);
            JSONArray records = mainObject.getJSONArray("data");

            JSONObject jsonObject = records.getJSONObject(0);
            String status = jsonObject.getString("status");

            if(status.equals("success"))
            {
                Toast.makeText(NewHelperActivity.this,"Success",Toast.LENGTH_LONG).show();
                createHelperAccountButton.setClickable(false);
                createHelperAccountButton.setText(R.string.id_created);
            }
            else if(status.equals("duplicateid"))
            {
                Toast.makeText(NewHelperActivity.this,"Duplicate ID",Toast.LENGTH_LONG).show();
            }
            else if(status.equals("unknownerror"))
            {
                Toast.makeText(NewHelperActivity.this,"Something happened under the hood, Sorry",Toast.LENGTH_LONG).show();
            }
            else if(status.equals("blankentries"))
            {
                Toast.makeText(NewHelperActivity.this,"Blank fields are not allowed",Toast.LENGTH_LONG).show();
            }

        }
        catch (JSONException e) {
            e.printStackTrace();
        }

    }

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
}
