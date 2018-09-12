package com.example.vinamra.anganwadi_helpers;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
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
    private MaterialEditText helperUserIdTextBox,helperPasswordTextBox;
    private ActionProcessButton helperBtnSignIn,helperBtnSignUp;
    private LinearLayout loginActivityParentLayout;
    private String helperID,helperPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        helperUserIdTextBox= (MaterialEditText) findViewById(R.id.userIdLoginTxtBox);
        helperPasswordTextBox= (MaterialEditText) findViewById(R.id.passwordLoginTxtBox);
        helperBtnSignIn = (ActionProcessButton) findViewById(R.id.helperBtnSignIn);
        helperBtnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                authCheck();
            }
        });
        helperBtnSignUp = (ActionProcessButton) findViewById(R.id.helperBtnSignUp);
        helperBtnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });
        helperBtnSignIn.setMode(ActionProcessButton.Mode.PROGRESS);
        helperBtnSignUp.setMode(ActionProcessButton.Mode.PROGRESS);
    }

    public void register() {
        startActivity(new Intent(LoginActivity.this,RegistrationActivity.class));
    }

    private void authCheck()
    {
        String userid= String.valueOf(helperUserIdTextBox.getText());
        String pass= String.valueOf(helperPasswordTextBox.getText());
        if(userid.equals("") && pass.equals(""))
        {
            helperUserIdTextBox.setError("Enter UserID");
            helperPasswordTextBox.setError("Enter password");
        }
        if(userid.equals(""))
        {
            helperUserIdTextBox.setError("Enter UserID");
        }
        else if(pass.equals(""))
        {
            helperPasswordTextBox.setError("Enter password");
        }
        else
        {
            helperBtnSignIn.setMode(ActionProcessButton.Mode.ENDLESS);
            helperBtnSignIn.setProgress(1);
            String url = "https://aproject2018.000webhostapp.com/loginFiles/helper/helperlogincheck.php?helperuserid="+userid+"&helperpwd="+pass;
            StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    parseJSON(response);
                    helperBtnSignIn.setProgress(0);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if(!isNetworkConnected(LoginActivity.this))
                    {
                        Toast.makeText(LoginActivity.this,"Sorry,Check your Internet Connection",Toast.LENGTH_LONG).show();
                        helperBtnSignIn.setProgress(0);
                    }
                }
            });

            RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
            requestQueue.add(request);
        }
    }

    private void parseJSON(String response) {
        try {

            JSONObject mainObject = new JSONObject(response);
            JSONArray records = mainObject.getJSONArray("data");

            JSONObject jsonObject = records.getJSONObject(0);
            String userexist = jsonObject.getString("userexist");
            if(userexist.equals("true"))
            {
                String errormsg = jsonObject.getString("errormsg");
                if(errormsg.equals("no"))
                {

                    //*****Credentials Matched*****//
                    final String helperId = jsonObject.getString("unique_id");
                    String registration_completed = jsonObject.getString("registration_completed");
                    final String registration_status = jsonObject.getString("registration_status");
                    String verification_status = jsonObject.getString("verification_status");
                    String helper_logged_in = jsonObject.getString("helper_logged_in");
                    Toast.makeText(LoginActivity.this,"Your unique_id is "+helperId,Toast.LENGTH_SHORT).show();

                    if(registration_completed.equals("yes"))
                    {
                        if(verification_status.equals("verified"))
                        {
                            Intent intent=new Intent(LoginActivity.this,HelperHomeActivity.class);
                            SharedPreferences loginPref = getApplicationContext().getSharedPreferences("login_preferences", Context.MODE_PRIVATE);
                            SharedPreferences.Editor loginPrefEditor = loginPref.edit();
                            loginPrefEditor.putString("helperId", helperId);
                            if(loginPrefEditor.commit())
                            {
                                Toast.makeText(LoginActivity.this,"Login Shared Preferences are set",Toast.LENGTH_SHORT).show();
                            }
                            else if(!loginPrefEditor.commit())
                            {
                                Toast.makeText(LoginActivity.this,"Login Shared Preferences are not set",Toast.LENGTH_SHORT).show();
                            }
                            //loginPrefEditor.apply();
                            startActivity(intent);
                            finish();

                        }
                        else if(verification_status.equals("not_verified"))
                        {
                            AlertDialog.Builder notVerifiedDialogBox = new AlertDialog.Builder(this);
                            notVerifiedDialogBox.setTitle("Verification Awaited");
                            notVerifiedDialogBox.setMessage("Supervisor has not verified your application \nTill then,Please sit back and relax :)");
                            notVerifiedDialogBox.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });
                            notVerifiedDialogBox.show();
                        }
                        else if(verification_status.equals("AVATAR_REVIEW"))
                        {
                            AlertDialog.Builder reviewDialogBox = new AlertDialog.Builder(this);
                            reviewDialogBox.setTitle("Review Application");
                            reviewDialogBox.setMessage("Please Upload your photo, Again");
                            reviewDialogBox.setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                    Intent intent=new Intent(LoginActivity.this,RegistrationActivity.class);
                                    intent.putExtra("fragment_number",2);
                                    startActivity(intent);
                                }
                            });
                            reviewDialogBox.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });

                        }
                        else if(verification_status.equals("ID_REVIEW"))
                        {
                            AlertDialog.Builder reviewDialogBox = new AlertDialog.Builder(this);
                            reviewDialogBox.setTitle("Review Application");
                            reviewDialogBox.setMessage("Please Upload your ID Proof, Again");
                            reviewDialogBox.setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                    Intent intent=new Intent(LoginActivity.this,RegistrationActivity.class);
                                    intent.putExtra("fragment_number",4);
                                    startActivity(intent);
                                }
                            });
                            reviewDialogBox.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });
                        }
                        else if(verification_status.equals("AVATAR_ID_REVIEW"))
                        {
                            AlertDialog.Builder reviewDialogBox = new AlertDialog.Builder(this);
                            reviewDialogBox.setTitle("Review Application");
                            reviewDialogBox.setMessage("Please Upload your Photo and ID Proof, Again");
                            reviewDialogBox.setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                    Intent intent=new Intent(LoginActivity.this,RegistrationActivity.class);
                                    intent.putExtra("fragment_number",2);
                                    startActivity(intent);
                                }
                            });
                            reviewDialogBox.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });
                        }
                        else if(verification_status.equals("REJECTED"))
                        {
                            AlertDialog.Builder rejectedDialogBox = new AlertDialog.Builder(this);
                            rejectedDialogBox.setTitle("Application Rejected");
                            rejectedDialogBox.setMessage("Sorry, Your Application is rejected by supervisor");
                            rejectedDialogBox.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });
                        }
                    }
                    else if(registration_completed.equals("no"))
                    {
                        //Incomplete Registration
                        AlertDialog.Builder incompleteRegistrationDialogBox = new AlertDialog.Builder(this);
                        incompleteRegistrationDialogBox.setTitle("Incomplete Registration");
                        incompleteRegistrationDialogBox.setMessage("Your Registration is incomplete \n Would you like to complete it ? ");
                        incompleteRegistrationDialogBox.setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();

                                SharedPreferences regPref = getApplicationContext().getSharedPreferences("registration_preferences", Context.MODE_PRIVATE);
                                SharedPreferences.Editor refPrefEditor = regPref.edit();
                                refPrefEditor.putString("helperId", helperId);
                                if(refPrefEditor.commit())
                                {
                                  Toast.makeText(LoginActivity.this,"Registration Shared Preferences are set",Toast.LENGTH_SHORT).show();
                                }
                                else if(!refPrefEditor.commit())
                                {
                                    Toast.makeText(LoginActivity.this,"Registration Shared Preferences are not set",Toast.LENGTH_SHORT).show();
                                }
                                //refPrefEditor.apply();

                                Intent intent=new Intent(LoginActivity.this,RegistrationActivity.class);
                                if(registration_status.equals("PASS_UPDATED"))
                                {
                                    //Photo Fragment
                                    intent.putExtra("fragment_number",2);
                                }
                                else if(registration_status.equals("PHOTO_PROVIDED"))
                                {
                                    //Contact Fragment
                                    intent.putExtra("fragment_number",3);
                                }
                                else if(registration_status.equals("CONTACT_PROVIDED"))
                                {
                                    //ID Proof Fragment
                                    intent.putExtra("fragment_number",4);
                                }
                                else
                                {
                                    //Change Password Fragment
                                    intent.putExtra("fragment_number",1);
                                }
                                startActivity(intent);
                            }
                        });

                        incompleteRegistrationDialogBox.setNegativeButton("Later", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                        incompleteRegistrationDialogBox.show();
                    }

                }
                else if(errormsg.equals("password wrong"))
                {
                    //*****Password Incorrect*****//
                    helperBtnSignIn.setProgress(0);
                    Toast.makeText(LoginActivity.this,"Your password is wrong",Toast.LENGTH_SHORT).show();
                }
            }
            else if(userexist.equals("false"))
            {
                //*****User ID is incorrect*****//
                helperBtnSignIn.setProgress(0);
                Toast.makeText(LoginActivity.this,"User ID is incorrect",Toast.LENGTH_SHORT).show();
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
