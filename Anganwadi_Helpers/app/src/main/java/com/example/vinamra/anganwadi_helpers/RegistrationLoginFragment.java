package com.example.vinamra.anganwadi_helpers;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
 * Created by Vinamra on 10/22/2017.
 */

public class RegistrationLoginFragment extends Fragment {
    private MaterialEditText helperIdEditText, helperPwdEditText;
    private ActionProcessButton registrationLoginBtn;
    private ViewPager viewPager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_registrationlogin,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        viewPager=(ViewPager) getActivity().findViewById(R.id.viewPager);
        helperIdEditText = (MaterialEditText) view.findViewById(R.id.helperId);
        helperPwdEditText = (MaterialEditText) view.findViewById(R.id.helperPwd);
        registrationLoginBtn = (ActionProcessButton) view.findViewById(R.id.registrationLogin);
        registrationLoginBtn.setMode(ActionProcessButton.Mode.PROGRESS);
        registrationLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authCheck();
            }
        });
        super.onViewCreated(view, savedInstanceState);

        //Check what Parent Activity Tells About View States
        /*RegistrationActivity parentActivity= (RegistrationActivity) getActivity();
        if(parentActivity.getFragmentViewStates(0)==0)
        {
            disableFragmentViews();
        }*/
    }
    private void authCheck()
    {
        String helperId= String.valueOf(helperIdEditText.getText());
        String helperPass= String.valueOf(helperPwdEditText.getText());
        if(helperId.equals(""))
        {
            helperIdEditText.setError("Enter UserID");

        }
        else if(helperPass.equals(""))
        {
            helperPwdEditText.setError("Enter password");
        }
        else
        {
            registrationLoginBtn.setMode(ActionProcessButton.Mode.ENDLESS);
            registrationLoginBtn.setProgress(1);
            String url = "https://aproject2018.000webhostapp.com/loginFiles/helper/registrationlogincredentialscheck.php?helperuserid="+helperId+"&helperpwd="+helperPass;
            StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    parseJSON(response);
                    registrationLoginBtn.setProgress(0);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if(!isNetworkConnected(getActivity()))
                    {
                        Toast.makeText(getActivity(),"Sorry,Check your Internet Connection",Toast.LENGTH_LONG).show();
                        registrationLoginBtn.setProgress(0);
                    }
                }
            });

            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            requestQueue.add(request);
        }
    }

    private void parseJSON(String response) {
        try
        {

            JSONObject mainObject = new JSONObject(response);
            JSONArray records = mainObject.getJSONArray("data");

            JSONObject jsonObject = records.getJSONObject(0);
            String userexist = jsonObject.getString("userexist");
            if(userexist.equals("true")) {

                String errormsg = jsonObject.getString("errormsg");
                if (errormsg.equals("no"))
                {
                    //*****Registration Login Successful*****//
                    String helperId = jsonObject.getString("unique_id");
                    String registration_completed = jsonObject.getString("registration_completed");
                    String registration_status = jsonObject.getString("registration_status");
                    String verification_status = jsonObject.getString("verification_status");
                    String helper_logged_in = jsonObject.getString("helper_logged_in");

                    SharedPreferences regPref = getContext().getSharedPreferences("registration_preferences", Context.MODE_PRIVATE);
                    SharedPreferences.Editor refPrefEditor = regPref.edit();
                    refPrefEditor.putString("helperId", helperId);
                    if (refPrefEditor.commit()) {
                        Toast.makeText(getContext(), "Registration Shared Preferences are set", Toast.LENGTH_SHORT).show();
                    } else if (!refPrefEditor.commit()) {
                        Toast.makeText(getContext(), "Registration Shared Preferences are not set", Toast.LENGTH_SHORT).show();
                    }
                    //refPrefEditor.apply();

                    /**************Test Code************************/

                    if (registration_completed.equals("yes")) {
                        if (verification_status.equals("verified")) {
                            AlertDialog.Builder dialogBox = new AlertDialog.Builder(getContext());
                            dialogBox.setTitle("Login Please");
                            dialogBox.setMessage("Your registration is completed and verified by supervisor \n Please Login :)");
                            dialogBox.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                    getActivity().finish();
                                }
                            });
                            dialogBox.show();
                        } else if (verification_status.equals("not_verified")) {
                            AlertDialog.Builder notVerifiedDialogBox = new AlertDialog.Builder(getContext());
                            notVerifiedDialogBox.setTitle("Verification Awaited");
                            notVerifiedDialogBox.setMessage("Supervisor has not verified your application \nTill then,Please sit back and relax :)");
                            notVerifiedDialogBox.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                    getActivity().finish();
                                }
                            });
                            notVerifiedDialogBox.show();
                        } else if (verification_status.equals("AVATAR_REVIEW")) {
                            AlertDialog.Builder reviewDialogBox = new AlertDialog.Builder(getContext());
                            reviewDialogBox.setTitle("Make changes");
                            reviewDialogBox.setMessage("Please Upload your Photo, Again");
                            reviewDialogBox.setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                    viewPager.setCurrentItem(2, true);
                                }
                            });

                        } else if (verification_status.equals("ID_REVIEW")) {
                            AlertDialog.Builder reviewDialogBox = new AlertDialog.Builder(getContext());
                            reviewDialogBox.setTitle("Make changes");
                            reviewDialogBox.setMessage("Please Upload your ID Proof, Again");
                            reviewDialogBox.setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                    viewPager.setCurrentItem(4, true);
                                }
                            });
                        } else if (verification_status.equals("AVATAR_ID_REVIEW")) {
                            AlertDialog.Builder reviewDialogBox = new AlertDialog.Builder(getContext());
                            reviewDialogBox.setTitle("Make changes");
                            reviewDialogBox.setMessage("Please Upload your Photo and ID Proof, Again");
                            reviewDialogBox.setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                    viewPager.setCurrentItem(2, true);
                                }
                            });
                        } else if (verification_status.equals("REJECTED")) {
                            AlertDialog.Builder rejectedDialogBox = new AlertDialog.Builder(getContext());
                            rejectedDialogBox.setTitle("Application Rejected");
                            rejectedDialogBox.setMessage("Your application has been rejected /n Please contact supervisor");
                            rejectedDialogBox.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                    getActivity().finish();
                                }
                            });
                        }
                    }
                    else if (registration_completed.equals("no"))
                    {
                        if (registration_status.equals("PASS_UPDATED")) {
                            //Photo Fragment
                            viewPager.setCurrentItem(2, true);
                        } else if (registration_status.equals("PHOTO_PROVIDED")) {
                            //Contact Fragment
                            viewPager.setCurrentItem(3, true);
                        } else if (registration_status.equals("CONTACT_PROVIDED")) {
                            //ID Proof Fragment
                            viewPager.setCurrentItem(4, true);
                        } else {
                            //Change Password Fragment
                            viewPager.setCurrentItem(1, true);
                        }

                    }
                    /*************************************************/

                    Toast.makeText(getActivity(), "Your unique_id is " + helperId, Toast.LENGTH_SHORT).show();
                }
                else if (errormsg.equals("password wrong"))
                {
                    //*****Password Incorrect*****//
                    registrationLoginBtn.setProgress(0);
                    Toast.makeText(getActivity(), "Your password is wrong", Toast.LENGTH_SHORT).show();
                }
            }
            else if(userexist.equals("false"))
            {
                //*****User ID is incorrect*****//
                registrationLoginBtn.setProgress(0);
                Toast.makeText(getActivity(),"User ID is incorrect",Toast.LENGTH_SHORT).show();
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
