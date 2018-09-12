package com.example.vinamra.anganwadi_supervisor;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
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

public class LoginActivity extends AppCompatActivity {
    private MaterialEditText supervisor_user,supervisor_pwd;
    private LinearLayout loginParentLayout;
    private ActionProcessButton btnSignIn;
    private String userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        supervisor_user= (MaterialEditText) findViewById(R.id.supervisor_user);
        supervisor_pwd= (MaterialEditText) findViewById(R.id.supervisor_pwd);
        loginParentLayout= (LinearLayout) findViewById(R.id.loginParentLayout);
        loginParentLayout.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus)
                {
                    hideKeyboard();
                }
            }
        });
        btnSignIn = (ActionProcessButton) findViewById(R.id.btnSignIn);
        btnSignIn.setMode(ActionProcessButton.Mode.PROGRESS);
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

    public void authCheck(View view) {
        hideKeyboard();
        userid= String.valueOf(supervisor_user.getText());
        String pass= String.valueOf(supervisor_pwd.getText());
        if(userid.equals("") && pass.equals(""))
        {
            supervisor_pwd.setError("Enter password");
            supervisor_user.setError("Enter SuperVisor ID");
        }
        if(userid.equals(""))
        {
            supervisor_user.setError("Enter SuperVisor ID");

        }
        else if(pass.equals(""))
        {
            supervisor_pwd.setError("Enter password");
        }
        else
        {
            btnSignIn.setMode(ActionProcessButton.Mode.ENDLESS);
            btnSignIn.setProgress(1);
            String url = "http://aproject2018.000webhostapp.com/loginFiles/supervisor/supervisorlogin.php?userid="+userid+"&pwd="+pass;
            StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    parseJSON(response);
                    btnSignIn.setProgress(0);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if(isNetworkConnected (LoginActivity.this))
                    {
                        Toast.makeText(LoginActivity.this,"Sorry,Check your Internet Connection",Toast.LENGTH_LONG).show();
                        btnSignIn.setProgress(0);
                    }
                    else
                    {
                        Toast.makeText(LoginActivity.this,"Sorry,Something Happened",Toast.LENGTH_SHORT).show();
                        btnSignIn.setProgress(0);
                    }

                }
            });

            RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
            requestQueue.add(request);
        }

    }

    private void hideKeyboard() {
        try  {
            InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        catch (Exception e) {

        }
    }

    private void parseJSON(String response) {
        try {

            JSONObject mainObject = new JSONObject(response);
            JSONArray records = mainObject.getJSONArray("data");

            JSONObject jsonObject = records.getJSONObject(0);
            String userexist = jsonObject.getString("userexist");
            final Snackbar snackbar;
            if(userexist.equals("true"))
            {
                String errormsg = jsonObject.getString("errormsg");
                if(errormsg.equals("no"))
                {
                    //all right
                    String supervisorName = jsonObject.getString("name");
                    //String anganwadiName = jsonObject.getString("anganwadiname");

                    SharedPreferences supervisorPref = getApplicationContext().getSharedPreferences("supervisor_preferences", Context.MODE_PRIVATE);
                    SharedPreferences.Editor supervisorPrefEditor = supervisorPref.edit();
                    supervisorPrefEditor.putString("supervisorid", userid);
                    supervisorPrefEditor.putString("supervisorname", supervisorName);
                    if(supervisorPrefEditor.commit())
                    {
                        Toast.makeText(LoginActivity.this,"Registration Shared Preferences are set",Toast.LENGTH_SHORT).show();
                    }
                    else if(!supervisorPrefEditor.commit())
                    {
                        Toast.makeText(LoginActivity.this,"Registration Shared Preferences are not set",Toast.LENGTH_SHORT).show();
                    }

                    Intent i=new Intent(LoginActivity.this,HomeActivity.class);
                    //i.putExtra("anganwadiname",anganwadiName);
                    startActivity(i);
                    finish();
                }
                else if(errormsg.equals("password wrong"))
                {
                    //password wrong
                    btnSignIn.setProgress(0);
                    snackbar = Snackbar.make(loginParentLayout, "Password entered is wrong", Snackbar.LENGTH_LONG);
                    snackbar.setAction("DISMISS", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            snackbar.dismiss();
                        }
                    });
                    snackbar.show();
                }
            }
            else if(userexist.equals("false"))
            {
                //no user exist
                btnSignIn.setProgress(0);
                snackbar = Snackbar.make(loginParentLayout, "No such user exists", Snackbar.LENGTH_LONG);
                snackbar.setAction("DISMISS", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        snackbar.dismiss();
                    }
                });
                snackbar.show();
            }

        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
