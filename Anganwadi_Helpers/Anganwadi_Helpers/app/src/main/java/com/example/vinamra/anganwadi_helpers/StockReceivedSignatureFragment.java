package com.example.vinamra.anganwadi_helpers;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dd.processbutton.iml.ActionProcessButton;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import is.arontibo.library.ElasticDownloadView;

/**
 * Created by Vinamra on 3/22/2018.
 */

public class StockReceivedSignatureFragment extends Fragment {
    private static final String TAG ="SignatureFragment" ;
    private ActionProcessButton doneSignatureBtn;
    private Button openSignatureActivityBtn;
    private static final int HELPER_SIGN = 1234, DP_SIGN =5678;
    private Uri helperSignatureFileUri,deliveryPersonSignatureFileUri;
    private TextView deliveryPersonSignatureCheckedTextView,helperSignatureCheckedTextView;
    private MaterialEditText deliveryPersonNameEditText;
    private String deliveryPersonName;
    private StorageReference mStorageRef;
    private Uri[] downloadUrl;
    private NewStockUpdateActivity parentActivity;
    private ProgressDialog progressDialog;
    private String helperId,helperSignatureFileName,deliveryPersonSignatureFileName;
    Drawable checkedDrawable;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_stockreceivedsignature,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences regPref = getContext().getSharedPreferences("registration_preferences", Context.MODE_PRIVATE);
        helperId=regPref.getString("helperId",null);

        mStorageRef = FirebaseStorage.getInstance().getReference();

        checkedDrawable = getResources().getDrawable(R.drawable.checked);

        parentActivity= (NewStockUpdateActivity) getActivity();
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Saving Data To database");
        progressDialog.setCancelable(false);

        downloadUrl=new Uri[2];

        deliveryPersonNameEditText=(MaterialEditText) view.findViewById(R.id.deliveryPersonNameEditText);
        deliveryPersonSignatureCheckedTextView=(TextView) view.findViewById(R.id.deliveryPersonSignatureCheckedTextView);
        helperSignatureCheckedTextView=(TextView) view.findViewById(R.id.helperSignatureCheckedTextView);
        openSignatureActivityBtn=(Button) view.findViewById(R.id.openSignatureActivityBtn);
        openSignatureActivityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(String.valueOf(openSignatureActivityBtn.getText()).equalsIgnoreCase(getString(R.string.delivery_person_signature)))
                {
                    openSignatureActivityForDeliveryPersonSign();
                }
                else if(String.valueOf(openSignatureActivityBtn.getText()).equalsIgnoreCase(getString(R.string.helper_signature)))
                {
                    openSignatureActivityForHelperSign();
                }

            }
        });
        doneSignatureBtn=(ActionProcessButton) view.findViewById(R.id.doneSignatureBtn);
        doneSignatureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadSignatures();
            }
        });
        createFileName();

    }

    private void createFileName()
    {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        helperSignatureFileName="helpersignature_"+timeStamp;
        deliveryPersonSignatureFileName="deliverypsignature_"+timeStamp;
    }

    private void openSignatureActivityForDeliveryPersonSign() {
            Intent intent=new Intent(getActivity(),SignatureActivity.class);
            startActivityForResult(intent,DP_SIGN);
    }

    private void openSignatureActivityForHelperSign() {
            Intent intent=new Intent(getActivity(),SignatureActivity.class);
            startActivityForResult(intent, HELPER_SIGN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK)
        {
            Bundle result = data.getExtras();
            if (requestCode == DP_SIGN)
            {
                deliveryPersonSignatureFileUri=Uri.parse(result.getString("signature"));
                deliveryPersonSignatureCheckedTextView.setCompoundDrawablesWithIntrinsicBounds(null, null, checkedDrawable, null);
                openSignatureActivityBtn.setText(R.string.helper_signature);
            }
            else if(requestCode == HELPER_SIGN)
            {
                helperSignatureFileUri=Uri.parse(result.getString("signature"));
                helperSignatureCheckedTextView.setCompoundDrawablesWithIntrinsicBounds(null, null, checkedDrawable, null);
                openSignatureActivityBtn.setText(R.string.signatures_done);
                openSignatureActivityBtn.setClickable(false);
                doneSignatureBtn.setVisibility(View.VISIBLE);
                doneSignatureBtn.setClickable(true);
            }
        }
    }

    /**************************************Function for Uploading Signatures******************************************/
    private void uploadSignatures() {
        deliveryPersonName= String.valueOf(deliveryPersonNameEditText.getText());
        if(!deliveryPersonName.equals(""))
        {
            final int[] imageUploadedCounter = new int[1];
            Log.d(TAG,"All fine");
            if(isNetworkConnected (getActivity()))
            {
                imageUploadedCounter[0] = 0;
                final StorageMetadata metadata = new StorageMetadata.Builder()
                        .setContentType("image/jpeg")
                        .build();
                final UploadTask[] uploadTask=new UploadTask[2];

                uploadTask[0] = mStorageRef.child("Anganwadi_Helper_images/stock_received_signature/"+ helperSignatureFileName +".jpg").putFile(helperSignatureFileUri, metadata);

                View alertLayoutView=getActivity().getLayoutInflater().inflate(R.layout.alertdialoglayoutforupload,null);
                AlertDialog.Builder alert=new AlertDialog.Builder(getContext());
                alert.setView(alertLayoutView);
                alert.setCancelable(false);
                final FloatingActionButton doneFabBtn= (FloatingActionButton) alertLayoutView.findViewById(R.id.doneFabBtn);
                doneFabBtn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#43A047")));
                final ElasticDownloadView et= (ElasticDownloadView) alertLayoutView.findViewById(R.id.elastic_download_view);
                final AlertDialog dialog = alert.create();
                doneFabBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        doneFabBtn.setVisibility(View.INVISIBLE);
                        Toast.makeText(getContext(),"All pics Uploaded, Thank You ;-)",Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.show();
                et.startIntro();
                et.setProgress(0);

                uploadTask[0].addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                    }
                }).addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(getContext(), "Upload is Paused, Check Internet Connection", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        et.fail();
                        doneFabBtn.setVisibility(View.VISIBLE);

                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Handle successful uploads on complete
                        downloadUrl[0] = taskSnapshot.getMetadata().getDownloadUrl();
                        et.setProgress(50.00f);
                        ++imageUploadedCounter[0];
                        uploadTask[1] = mStorageRef.child("Anganwadi_Helper_images/stock_received_signature/"+ deliveryPersonSignatureFileName +".jpg").putFile(deliveryPersonSignatureFileUri, metadata);
                        uploadTask[1].addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                            }
                        }).addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {
                                Toast.makeText(getContext(), "Upload is Paused, Check Internet Connection", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                et.fail();
                                doneFabBtn.setVisibility(View.VISIBLE);

                            }
                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                downloadUrl[1] = taskSnapshot.getMetadata().getDownloadUrl();
                                et.setProgress(100.00f);
                                ++imageUploadedCounter[0];
                                et.success();
                                doneFabBtn.setVisibility(View.VISIBLE);
                                saveSignaturePicsUrlToDatabase();
                            }
                        });
                    }
                });
            }
            else
            {
                Toast.makeText(getContext(),"Check your Internet Connection",Toast.LENGTH_LONG).show();
            }
        }
        else if(deliveryPersonName.equals(""))
        {
            deliveryPersonNameEditText.setError("Name please");
        }

    }
    /**************************************************Function End*************************************************************/

    /**************************************Function for Saving Stock Item Pics to Database******************************************/
    private void saveSignaturePicsUrlToDatabase() {

        if(isNetworkConnected(getContext()))
        {
            progressDialog.show();
            /*Push data to mysql database with avatar url specfying helper`s id*/
            String saveHelperSignaturePicLinkUrl="http://aproject2018.000webhostapp.com/helpertask/saveSignaturePicsUrl.php?billno="+parentActivity.getBillno()+"&signingperson=helper&helperid="+helperId+"&fileurl="+downloadUrl[0];
            StringRequest request = new StringRequest(Request.Method.GET, saveHelperSignaturePicLinkUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    urlSaveParseJSONResponse1(response);
                    //registrationLoginBtn.setProgress(0);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    if(!isNetworkConnected(getActivity()))
                    {
                        Toast.makeText(getActivity(),"Sorry,Check your Internet Connection",Toast.LENGTH_LONG).show();
                        //registrationLoginBtn.setProgress(0);
                    }
                }
            });

            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            requestQueue.add(request);
        }
        else if(!isNetworkConnected(getContext()))
        {
            Toast.makeText(getContext(),"Check your Internet Connection",Toast.LENGTH_SHORT).show();
        }

    }

    private void urlSaveParseJSONResponse1(String response) {
        try
        {
            JSONObject mainObject = new JSONObject(response);
            JSONArray records = mainObject.getJSONArray("data");

            JSONObject jsonObject = records.getJSONObject(0);
            String saveStatus = jsonObject.getString("done");
            if(saveStatus.equals("true"))
            {
                String saveDeliveryPersonSignaturePicLinkUrl="http://aproject2018.000webhostapp.com/helpertask/saveSignaturePicsUrl.php?billno="+parentActivity.getBillno()+"&signingperson=deliveryperson&deliverypname="+deliveryPersonName+"&fileurl="+downloadUrl[1];
                StringRequest request = new StringRequest(Request.Method.GET, saveDeliveryPersonSignaturePicLinkUrl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        urlSaveParseJSONResponse2(response);
                        progressDialog.dismiss();
                        //registrationLoginBtn.setProgress(0);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        if(!isNetworkConnected(getActivity()))
                        {
                            Toast.makeText(getActivity(),"Sorry,Check your Internet Connection",Toast.LENGTH_LONG).show();
                            //registrationLoginBtn.setProgress(0);
                        }
                    }
                });

                RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
                requestQueue.add(request);
            }
            else if(saveStatus.equals("false"))
            {
                Toast.makeText(getActivity(),"Error while saving proof url in database",Toast.LENGTH_SHORT).show();
            }

        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void urlSaveParseJSONResponse2(String response) {
        try
        {
            JSONObject mainObject = new JSONObject(response);
            JSONArray records = mainObject.getJSONArray("data");

            JSONObject jsonObject = records.getJSONObject(0);
            String saveStatus = jsonObject.getString("done");
            if(saveStatus.equals("true"))
            {
                Toast.makeText(getActivity(),"Signatures Urls saved",Toast.LENGTH_SHORT).show();
                getActivity().finish();

            }
            else if(saveStatus.equals("false"))
            {
                Toast.makeText(getActivity(),"Error while saving signature urls in database",Toast.LENGTH_SHORT).show();
            }

        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**************************************************Function End*************************************************************/

    /**************************************Function for Checking Connection to Internet******************************************/
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
    /**************************************************Function End*************************************************************/

}
