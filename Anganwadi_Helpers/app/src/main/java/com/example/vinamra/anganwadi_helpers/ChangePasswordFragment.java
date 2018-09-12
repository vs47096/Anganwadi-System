package com.example.vinamra.anganwadi_helpers;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
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

public class ChangePasswordFragment extends Fragment {
    private MaterialEditText helperNewPwd1EditText, helperNewPwd2EditText;
    private ActionProcessButton chngHelperPwdBtn;
    private FrameLayout registrationActivityParentLayout;
    String helperId, newHelperPass;
    private ViewPager viewPager;
    public boolean viewDisabled=false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_changepassword,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        viewPager=(ViewPager) getActivity().findViewById(R.id.viewPager);
        registrationActivityParentLayout = (FrameLayout) getActivity().findViewById(R.id.RegistrationActivityParentLayout);
        helperNewPwd1EditText = (MaterialEditText) view.findViewById(R.id.helperNewPwd1);
        helperNewPwd2EditText = (MaterialEditText) view.findViewById(R.id.helperNewPwd2);
        chngHelperPwdBtn = (ActionProcessButton) view.findViewById(R.id.chngHelperPwd);
        chngHelperPwdBtn.setMode(ActionProcessButton.Mode.PROGRESS);
        chngHelperPwdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Bundle b=getIntent().getExtras();
                helperId=b.getString("uniqueHelperId");*/
                //helperId ="checkid4";
                SharedPreferences regPref = getContext().getSharedPreferences("registration_preferences", Context.MODE_PRIVATE);
                helperId=regPref.getString("helperId",null);
                updateHelperPassword(helperId);
            }
        });
        super.onViewCreated(view, savedInstanceState);

        //Check what Parent Activity Tells About View States
        /*RegistrationActivity parentActivity= (RegistrationActivity) getActivity();
        if(parentActivity.getFragmentViewStates(1)==0)
        {
            disableFragmentViews();
        }*/
    }
    private void updateHelperPassword(String helperuniqueid) {
        String helperNewPwd1= String.valueOf(helperNewPwd1EditText.getText());
        String helperNewPwd2= String.valueOf(helperNewPwd2EditText.getText());

        if(helperNewPwd1.equals("") || helperNewPwd2.equals(""))
        {
            if(helperNewPwd1.equals(""))
                helperNewPwd1EditText.setError("Enter New Password");

            if(helperNewPwd2.equals(""))
                helperNewPwd2EditText.setError("Enter Again Please");
        }
        else if(helperNewPwd1.length()<8 || helperNewPwd1.length()>32)
        {
            final Snackbar snackbar = Snackbar.make(registrationActivityParentLayout, "Password shoule be more than 8 and less than 32 charcaters", Snackbar.LENGTH_LONG);
            snackbar.setAction("DISMISS", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    snackbar.dismiss();
                }
            }).show();
        }
        else if(!helperNewPwd1.matches(".*[A-Z]+.*") || !helperNewPwd1.matches(".*[@ $ _ #]+.*"))
        {
            final Snackbar snackbar = Snackbar.make(registrationActivityParentLayout, "Password must contain 1 capital letter and 1 special character", Snackbar.LENGTH_LONG);
            snackbar.setAction("DISMISS", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    snackbar.dismiss();
                }
            }).show();
        }
        else if(!helperNewPwd2.equals(helperNewPwd1))
        {
            final Snackbar snackbar = Snackbar.make(registrationActivityParentLayout, "Password not matched", Snackbar.LENGTH_LONG);
            snackbar.setAction("DISMISS", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    snackbar.dismiss();
                }
            });
            helperNewPwd1EditText.setError("Both should be same");
            helperNewPwd2EditText.setError("Both should be same");
            snackbar.show();
        }
        else if(helperNewPwd2.equals(helperNewPwd1))
        {
            final ProgressDialog p = new ProgressDialog(getActivity());
            p.setMessage("Updating your password");
            newHelperPass =helperNewPwd2;
            chngHelperPwdBtn.setMode(ActionProcessButton.Mode.ENDLESS);
            chngHelperPwdBtn.setProgress(1);
            p.show();
            String chngPwdUrl = "http://aproject2018.000webhostapp.com/loginFiles/helper/changehelperpassword.php?helperuserid="+helperuniqueid+"&newhelperpwd="+ newHelperPass;
            StringRequest chngPwdrequest = new StringRequest(Request.Method.GET, chngPwdUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    p.dismiss();
                    parseJSON(response);
                    //chngHelperPwdBtn.setProgress(0);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    p.dismiss();
                    if(isNetworkConnected (getActivity()))
                    {
                        Toast.makeText(getActivity(),"Sorry,Check your Internet Connection",Toast.LENGTH_LONG).show();
                        chngHelperPwdBtn.setProgress(0);
                    }
                    else
                    {
                        Toast.makeText(getActivity(),"Sorry,Some error occured while updating password",Toast.LENGTH_SHORT).show();
                        chngHelperPwdBtn.setProgress(0);
                    }
                }
            });
            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            requestQueue.add(chngPwdrequest);
        }
    }

    private void parseJSON(String response) {
        try {
            JSONObject mainObject = new JSONObject(response);
            JSONArray records = mainObject.getJSONArray("data");

            JSONObject jsonObject = records.getJSONObject(0);
            String updatestatus = jsonObject.getString("done");
            final Snackbar snackbar;
            if(updatestatus.equals("true"))
            {
                //password update done
                    /*String uniqueHelperId = jsonObject.getString("unique_id");
                    Intent i;
                    i.putExtra("uniqueHelperId",uniqueHelperId);
                    startActivity(i);
                    finish();*/
                Toast.makeText(getActivity(),"Password Changed",Toast.LENGTH_SHORT).show();

                String updateRegStatusUrl = "https://aproject2018.000webhostapp.com/loginFiles/helper/updateregstatus.php?helperuserid="+helperId+"&registrationstatus=PASS_UPDATED";
                StringRequest updateRegStatusRequest = new StringRequest(Request.Method.GET, updateRegStatusUrl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        chngHelperPwdBtn.setProgress(0);
                        updateRegStatusJSON(response);

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        chngHelperPwdBtn.setProgress(0);
                        if(isNetworkConnected (getActivity()))
                        {
                            Toast.makeText(getActivity(),"Sorry,Check your Internet Connection",Toast.LENGTH_LONG).show();

                        }
                        else
                        {
                            Toast.makeText(getActivity(),"Sorry, Some Error occured while updating registration status",Toast.LENGTH_SHORT).show();

                        }
                    }
                });
                RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
                requestQueue.add(updateRegStatusRequest);
            }
            else if(updatestatus.equals("false"))
            {
                //password update failed
                chngHelperPwdBtn.setProgress(0);
                Toast.makeText(getActivity(),"Password not changed",Toast.LENGTH_SHORT).show();
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void updateRegStatusJSON(String response) {
        try {
            JSONObject mainObject = new JSONObject(response);
            JSONArray records = mainObject.getJSONArray("data");

            JSONObject jsonObject = records.getJSONObject(0);
            String updatestatus = jsonObject.getString("done");
            if(updatestatus.equals("true"))
            {
                //registration status update success

                Toast.makeText(getActivity(),"Registration Status Updated",Toast.LENGTH_SHORT).show();

                //Move to next Fragment
                viewPager.setCurrentItem(2, true);
            }
            else if(updatestatus.equals("false"))
            {
                //registration status update failed

                Toast.makeText(getActivity(),"Registration Status failed to Update",Toast.LENGTH_SHORT).show();
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
