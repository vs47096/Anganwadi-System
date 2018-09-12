package com.example.vinamra.anganwadi_helpers;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dd.processbutton.iml.ActionProcessButton;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vinamra on 10/5/2017.
 */

public class ContactFragment extends Fragment {
    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    private MaterialEditText registrationContactTxtBox,registrationAltContactTxtBox,otpTxtBox;
    private ActionProcessButton contactfragmentBtn;
    private AlertDialog.Builder otpWaitingAlertBuild, inputOtpAlertBuild;
    private AlertDialog otpWaitingAlert, inputOtpAlert;
    private View otpWaitingAlertView, inputOtpAlertView;
    private String TAG="Contact Fragment", otpSessionId,contactNo,contactAltNo,helperId,contactSaveURL;
    private AVLoadingIndicatorView avi;
    private RoundCornerProgressBar otpWaitingProgressBar;
    private FloatingActionButton resendOtpBtn,otpDoneBtn,otpInputDialogCloseBtn;
    private TextView timer;
    private CountDownTimer otpWaitingCountDown;
    private boolean numberVerifiedFlag=false;
    private ViewPager viewPager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_contact,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewPager=(ViewPager) getActivity().findViewById(R.id.viewPager);

        SharedPreferences regPref = getContext().getSharedPreferences("registration_preferences", Context.MODE_PRIVATE);
        helperId=regPref.getString("helperId",null);

        registrationContactTxtBox= (MaterialEditText) view.findViewById(R.id.registrationContact);
        registrationAltContactTxtBox= (MaterialEditText) view.findViewById(R.id.registrationAltContact);
        contactfragmentBtn= (ActionProcessButton) view.findViewById(R.id.contactfragmentBtn);
        contactfragmentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //check if number already exist in database or not
                checkNumberAlreadyExist();
                    //requestOtp();

            }
        });

        otpWaitingAlertView=getActivity().getLayoutInflater().inflate(R.layout.alertdialoglayoutforotpwaiting,null);
        avi= (AVLoadingIndicatorView) otpWaitingAlertView.findViewById(R.id.avi);
        otpWaitingProgressBar= (RoundCornerProgressBar) otpWaitingAlertView.findViewById(R.id.otpWaitingProgressBar);
        timer= (TextView) otpWaitingAlertView.findViewById(R.id.timer);
        otpWaitingAlertBuild =new AlertDialog.Builder(getContext());
        otpWaitingAlertBuild.setView(otpWaitingAlertView);
        otpWaitingAlertBuild.setCancelable(false);
        otpWaitingAlert=otpWaitingAlertBuild.create();

        inputOtpAlertView=getActivity().getLayoutInflater().inflate(R.layout.alertlayoutforenteringotp,null);
        otpTxtBox=(MaterialEditText)inputOtpAlertView.findViewById(R.id.otpTxtBox);
        otpDoneBtn= (FloatingActionButton) inputOtpAlertView.findViewById(R.id.otpDoneBtn);
        otpDoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateOtp();
            }
        });
        inputOtpAlertBuild =new AlertDialog.Builder(getContext());
        inputOtpAlertBuild.setCancelable(false);
        inputOtpAlertBuild.setView(inputOtpAlertView);
        inputOtpAlert=inputOtpAlertBuild.create();

        resendOtpBtn= (FloatingActionButton) inputOtpAlertView.findViewById(R.id.resendOtpBtn);
        resendOtpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //recall request otp function
                inputOtpAlert.dismiss();
                requestOtp();
            }
        });

        otpInputDialogCloseBtn= (FloatingActionButton) inputOtpAlertView.findViewById(R.id.otpInputDialogCloseBtn);
        otpInputDialogCloseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputOtpAlert.dismiss();
            }
        });

        //Check what Parent Activity Tells About View States
        /*RegistrationActivity parentActivity= (RegistrationActivity) getActivity();
        if(parentActivity.getFragmentViewStates(3)==0)
        {
            disableFragmentViews();
        }*/
    }

    private void checkNumberAlreadyExist() {
        contactNo= String.valueOf(registrationContactTxtBox.getText());
        contactAltNo= String.valueOf(registrationAltContactTxtBox.getText());
        if(contactNo.equals(""))
        {
            //Contact number not provided
            registrationContactTxtBox.setError("Enter your contact number Please");
        }
        else if(contactNo.equals(contactAltNo))
        {
            registrationAltContactTxtBox.setText("");
            Toast.makeText(getActivity(),"Both Numbers same",Toast.LENGTH_LONG).show();
            checkNumberAlreadyExist();

        }
        else if(!contactNo.equals(""))
        {
            final ProgressDialog p = new ProgressDialog(getActivity());
            p.setMessage("Checking if number already present");
            p.show();
            String checkAlreadyExistUrl = null;

            //Contact number provided

            if(contactAltNo.equals(""))
            {
                ////Alt Contact number not provided
                //Check if contact no already exist in database or not
                checkAlreadyExistUrl="https://aproject2018.000webhostapp.com/loginFiles/helper/updateandcheckcontactdetails.php?helperuserid="+helperId+"&action=checkifalreadyexist&contact="+contactNo;
            }
            else if(!contactAltNo.equals(""))
            {
                ////Alt Contact number provided
                //Check if contact no and alternate contact no already exist in database or not

                checkAlreadyExistUrl="https://aproject2018.000webhostapp.com/loginFiles/helper/updateandcheckcontactdetails.php?helperuserid="+helperId+"&action=checkifalreadyexist&contact="+contactNo+"&altcontact="+contactAltNo;

            }


            Log.d(TAG,"Checking if number already exist or not");
            StringRequest request = new StringRequest(Request.Method.GET, checkAlreadyExistUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    p.dismiss();
                    alreadyExistparseJSON(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    p.dismiss();
                    if(!isNetworkConnected(getActivity()))
                    {
                        Toast.makeText(getActivity(),"Sorry,Check your Internet Connection",Toast.LENGTH_LONG).show();
                    }
                }
            });

            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            requestQueue.add(request);

        }

    }

    private void alreadyExistparseJSON(String response) {
        try {
            JSONObject mainObject = new JSONObject(response);
            JSONArray records = mainObject.getJSONArray("data");

            JSONObject jsonObject = records.getJSONObject(0);
            String contactexist = jsonObject.getString("contactexist");
            String altcontactexist = jsonObject.getString("altcontactexist");

            if(contactexist.equals("true"))
            {
                registrationContactTxtBox.setError("This number belongs to some other helper");
            }
            else if(contactexist.equals("false"))
            {

                if(altcontactexist.equals("true"))
                {
                    registrationAltContactTxtBox.setError("This number belongs to some other helper");
                }
                else if(altcontactexist.equals("false"))
                {
                    requestOtp();
                    contactSaveURL="https://aproject2018.000webhostapp.com/loginFiles/helper/updateandcheckcontactdetails.php?helperuserid="+helperId+"&action=savecontact&contact="+contactNo+"&altcontact="+contactAltNo;
                }
                else if(altcontactexist.equals("none"))
                {
                    requestOtp();
                    contactSaveURL="https://aproject2018.000webhostapp.com/loginFiles/helper/updateandcheckcontactdetails.php?helperuserid="+helperId+"&action=savecontact&contact="+contactNo;
                }
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void requestOtp() {
        //API_KEY-ecf60505-efb6-11e7-a328-0200cd936042
        if(!checkAndRequestPermissions())
        {
            Toast.makeText(getActivity(),"Permission required for detecting OTP",Toast.LENGTH_LONG).show();
        }
        else if(checkAndRequestPermissions())
        {

            final ProgressDialog p = new ProgressDialog(getActivity());
            p.setMessage("Requesting for OTP verification");
            p.setCancelable(false);
            p.show();
            String url = "https://2factor.in/API/V1/ecf60505-efb6-11e7-a328-0200cd936042/SMS/+91"+contactNo+"/AUTOGEN";
            Log.d(TAG,"https://2factor.in/API/V1/ecf60505-efb6-11e7-a328-0200cd936042/SMS/+91"+contactNo+"/AUTOGEN");
            StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    p.dismiss();
                    otpRequestParseJSON(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    p.dismiss();
                    if(!isNetworkConnected(getActivity()))
                    {
                        Toast.makeText(getActivity(),"Sorry,Check your Internet Connection",Toast.LENGTH_LONG).show();
                    }
                }
            });

            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            requestQueue.add(request);
        }
    }

    private void otpRequestParseJSON(String response) {
        try {

            JSONObject mainObject = new JSONObject(response);
            String otpRequestStatus = mainObject.getString("Status");
            if(otpRequestStatus.equals("Success"))
            {
                otpSessionId = mainObject.getString("Details");
                otpWaitingAlert.show();
                avi.smoothToShow();
                otpWaitingProgressBar.setProgress(0);
                otpWaitingCountDown=new CountDownTimer(30000,1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        timer.setText("0:"+String.format("%02d",(int)millisUntilFinished/1000));
                        otpWaitingProgressBar.setProgress((30000-(float)millisUntilFinished)/30000*100);
                    }

                    @Override
                    public void onFinish() {
                        otpWaitingAlert.dismiss();
                        inputOtpAlert.show();
                    }
                }.start();
            }
            else
            {
                Toast.makeText(getActivity(),"OTP request failed",Toast.LENGTH_SHORT).show();
            }

        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void validateOtp() {
        Toast.makeText(getActivity(),"Done Button pressed",Toast.LENGTH_LONG).show();
        String otpVerificationCode= String.valueOf(otpTxtBox.getText());
        if(otpVerificationCode.equals(""))
        {
            otpTxtBox.setError("Enter OTP please");
        }
        else{
            //https://2factor.in/API/V1/{api_key}/SMS/VERIFY/{session_id}/{otp_entered_by_user}
            String url = "https://2factor.in/API/V1/ecf60505-efb6-11e7-a328-0200cd936042/SMS/VERIFY/"+otpSessionId+"/"+otpVerificationCode;
            Log.d(TAG,"https://2factor.in/API/V1/ecf60505-efb6-11e7-a328-0200cd936042/SMS/VERIFY/"+otpSessionId+"/"+otpVerificationCode);
            StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d(TAG,"Received Validation Response");
                    otpVaildateParseJSON(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d(TAG,"Received Error in Validation");
                    if(!isNetworkConnected(getActivity()))
                    {
                        Toast.makeText(getActivity(),"Sorry,Check your Internet Connection",Toast.LENGTH_LONG).show();
                    }
                    else{
                        otpTxtBox.setError("OTP is incorrect");
                    }
                }
            });
            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            requestQueue.add(request);
        }
    }

    private void otpVaildateParseJSON(String response) {
        try {
            Log.d(TAG,"Entered parse2 json");
            JSONObject mainObject = new JSONObject(response);
            String otpRequestStatus = mainObject.getString("Status");
            if(otpRequestStatus.equals("Success"))
            {
                Log.d(TAG,"OTP validation success");
                String matchingStatus = mainObject.getString("Details");
                if(matchingStatus.equals("OTP Matched"))
                {
                    Log.d(TAG,"OTP matched");
                    inputOtpAlert.dismiss();
                    Toast.makeText(getActivity(),"Phone Number Verified",Toast.LENGTH_LONG).show();
                    registrationContactTxtBox.setFocusable(false);
                    registrationContactTxtBox.setClickable(true);
                    registrationContactTxtBox.setFloatingLabelText("Number verified");
                    registrationContactTxtBox.setHelperTextAlwaysShown(false);

                    numberVerifiedFlag=true;
                    //disable verificaion btn
                    contactfragmentBtn.setEnabled(false);
                    Log.d(TAG,"jst b4 saveContact Data function call");
                    saveContactData();
                    Log.d(TAG,"After saveContact Data function call");
                }
            }
            else
            {
                Log.d(TAG,"OTP validation failure");
                Toast.makeText(getActivity(),"OTP verification failed",Toast.LENGTH_SHORT).show();
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void saveContactData() {
        Log.d(TAG,"Entered saveContactData");
        Log.d(TAG,contactSaveURL);
        final ProgressDialog p = new ProgressDialog(getActivity());
        p.setMessage("Saving Contact Data");
        p.setCancelable(false);
        p.show();
        StringRequest saveContactDataRequest = new StringRequest(Request.Method.GET, contactSaveURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //chngHelperPwdBtn.setProgress(0);
                p.dismiss();
                saveContactDataJSONParse(response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                p.dismiss();
                //chngHelperPwdBtn.setProgress(0);
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
        requestQueue.add(saveContactDataRequest);
    }

    private void saveContactDataJSONParse(String response) {

        try {
            JSONObject mainObject = new JSONObject(response);
            JSONArray records = mainObject.getJSONArray("data");

            JSONObject jsonObject = records.getJSONObject(0);
            String savestatus = jsonObject.getString("done");
            if(savestatus.equals("true"))
            {
                //contact data save success

                Toast.makeText(getActivity(),"Contact Info saved",Toast.LENGTH_SHORT).show();
                updateRegStatus();

            }
            else if(savestatus.equals("false"))
            {
                //contact data save failed

                Toast.makeText(getActivity(),"Failed in saving Contact Info",Toast.LENGTH_SHORT).show();
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void updateRegStatus()
    {
        String updateRegStatusUrl = "https://aproject2018.000webhostapp.com/loginFiles/helper/updateregstatus.php?helperuserid="+helperId+"&registrationstatus=CONTACT_PROVIDED";
        StringRequest updateRegStatusRequest = new StringRequest(Request.Method.GET, updateRegStatusUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //chngHelperPwdBtn.setProgress(0);
                updateRegStatusJSON(response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //chngHelperPwdBtn.setProgress(0);
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
                /*UploadIdProofFragment fragment=new UploadIdProofFragment();
                fragment.enableFragmentViews();
                disableFragmentViews();*/
                viewPager.setCurrentItem(4, true);

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

    private  boolean checkAndRequestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {

            int receiveSMS = ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.RECEIVE_SMS);

            int readSMS = ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.READ_SMS);
            List<String> listPermissionsNeeded = new ArrayList<>();

            if (receiveSMS != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(android.Manifest.permission.RECEIVE_SMS);
            }
            if (readSMS != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(android.Manifest.permission.READ_SMS);
            }
            if (!listPermissionsNeeded.isEmpty()) {
                ActivityCompat.requestPermissions(getActivity(), listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),
                        REQUEST_ID_MULTIPLE_PERMISSIONS);
                return false;
            }
            return true;
        }
        else
        {
            return true;
        }

    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase("otp")) {
                final String message = intent.getStringExtra("message");
                otpWaitingAlert.dismiss();
                otpWaitingCountDown.cancel();
                inputOtpAlert.show();
                otpTxtBox.setText(message);
            }
        }
    };

    @Override
    public void onResume() {
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(receiver, new IntentFilter("otp"));
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(receiver);
    }



}
