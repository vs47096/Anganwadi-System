package com.example.vinamra.anganwadi_supervisor;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dmallcott.dismissibleimageview.DismissibleImageView;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Vinamra on 2/26/2018.
 */

public class HelperDetailsVerifyActivity extends AppCompatActivity {

    private CircleImageView avatarView;
    private Switch avatarSwitch,nameSwitch,idProofSwitch;
    private TextView nameTextView,helperIdVerifyTextView;
    public DismissibleImageView idProofVerifyImageView;
    private Button verifyBtn,reviewBtn,rejectBtn,dismissRejectDialogBtn,doneRejectDialogBtn;
    private final int sdk = android.os.Build.VERSION.SDK_INT;
    private StorageReference httspRef;
    private AlertDialog.Builder viewImageDialogBuilder,rejectReasonDialogBuilder;
    private AlertDialog viewImageDialog,rejectReasonDialog;
    private View viewImageDialogView,rejectReasonDialogView;
    private FloatingActionButton closeViewImageDialogBtn;
    private ImageView dialogImageView;
    private String helperid,rejectReason,helperProofUrl,helperAvatarUrl;
    private EditText rejectReasonEditTxt;
    private Dialog markAllCorrectDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helpersdetailsverifylayout);
        initialize();
    }

    @SuppressLint("LongLogTag")
    private void initialize() {

        /*******************************************View Image Dialog*************************************/
        viewImageDialogView=getLayoutInflater().inflate(R.layout.alertdialoglayout_viewimage,null);
        dialogImageView= (ImageView) viewImageDialogView.findViewById(R.id.dialogImageView);
        closeViewImageDialogBtn= (FloatingActionButton) viewImageDialogView.findViewById(R.id.closeViewImageDialogBtn);
        closeViewImageDialogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewImageDialog.dismiss();
            }
        });
        viewImageDialogBuilder =new AlertDialog.Builder(HelperDetailsVerifyActivity.this);
        viewImageDialogBuilder.setView(viewImageDialogView);
        viewImageDialogBuilder.setCancelable(true);
        viewImageDialog=viewImageDialogBuilder.create();
        /*****************************************************************************************************************************/

        /****************************************Reject Reason Dialog*************************************/
        rejectReasonDialogView=getLayoutInflater().inflate(R.layout.alertdialoglayout_rejectreason,null);
        //dismissRejectDialogBtn,doneRejectDialogBtn
        rejectReasonEditTxt= (EditText) rejectReasonDialogView.findViewById(R.id.rejectReasonEditTxt);
        dismissRejectDialogBtn= (Button) rejectReasonDialogView.findViewById(R.id.dismissRejectDialogBtn);
        dismissRejectDialogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rejectReasonDialog.dismiss();
            }
        });
        doneRejectDialogBtn= (Button) rejectReasonDialogView.findViewById(R.id.doneRejectDialogBtn);
        doneRejectDialogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveRejectionReasonToDatabase();
            }
        });

        rejectReasonDialogBuilder =new AlertDialog.Builder(HelperDetailsVerifyActivity.this);
        rejectReasonDialogBuilder.setView(rejectReasonDialogView);
        rejectReasonDialogBuilder.setCancelable(false);
        rejectReasonDialog=rejectReasonDialogBuilder.create();
        /*****************************************************************************************************************************/

        idProofVerifyImageView = (DismissibleImageView) findViewById(R.id.idProofVerifyImageView);
        avatarView= (CircleImageView) findViewById(R.id.avatarVerifyView);
        avatarView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Picasso.get()
                        .load(helperAvatarUrl)
                        .placeholder(R.drawable.ic_profile)
                        .error(R.drawable.error)
                        .into(dialogImageView);
                viewImageDialog.show();
            }
        });
        avatarSwitch= (Switch) findViewById(R.id.avatarSwitch);
        avatarSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    setGreenBoundary(avatarView);
                    buttonView.setText(R.string.correct);

                }
                else if(!isChecked)
                {
                    setRedBoundary(avatarView);
                    buttonView.setText(R.string.review);
                }
            }
        });
        nameSwitch= (Switch) findViewById(R.id.nameSwitch);
        nameSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    setGreenBoundary(nameTextView);
                    buttonView.setText(R.string.correct);

                }
                else if(!isChecked)
                {
                    setRedBoundary(nameTextView);
                    buttonView.setText(R.string.review);
                }
            }
        });
        idProofSwitch= (Switch) findViewById(R.id.idProofSwitch);
        idProofSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    setGreenBoundary(idProofVerifyImageView);
                    buttonView.setText(R.string.correct);
                }
                else if(!isChecked)
                {
                    setRedBoundary(idProofVerifyImageView);
                    buttonView.setText(R.string.review);
                }
            }
        });
        nameTextView= (TextView) findViewById(R.id.nameTextVerifyView);
        helperIdVerifyTextView=(TextView)findViewById(R.id.helperIdVerify);
        verifyBtn= (Button) findViewById(R.id.verifyBtn);
        verifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyHelper();
            }
        });
        reviewBtn= (Button) findViewById(R.id.reviewBtn);
        reviewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reviewHelper();
            }
        });
        rejectBtn= (Button) findViewById(R.id.rejectBtn);
        rejectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rejectHelper();
            }
        });

        /********************Getting Intent Data*************************/
        Bitmap bitmap=getIntent().getParcelableExtra("avatarimage");
        avatarView.setImageBitmap(bitmap);
        Bundle bundle=getIntent().getExtras();
        helperid = bundle.getString("helperId");
        String helperName=bundle.getString("helperName");
        helperAvatarUrl=bundle.getString("helperAvatarUrl");
        Log.d("HelperDetailsVerifyACtivity",helperAvatarUrl);
        helperProofUrl = bundle.getString("helperProofUrl");
        Log.d("HelperDetailsVerify",helperProofUrl);
        /*****************************************************************/

        /**************Setting Defaults of Views*************************/
        helperIdVerifyTextView.setText(helperid);
        nameTextView.setText(helperName);
        avatarSwitch.setChecked(false);
        nameSwitch.setChecked(false);
        idProofSwitch.setChecked(false);
        Picasso.get()
                .load(helperProofUrl)
                .placeholder(R.drawable.loading)
                .error(R.drawable.error)
                .into(idProofVerifyImageView);
        /*****************************************************************/
    }

    private void saveRejectionReasonToDatabase() {
        String rejectionReason= String.valueOf(rejectReasonEditTxt.getText());
        if(rejectionReason.equals(""))
        {
            rejectReasonEditTxt.setError("Reason must be specified");
        }
        else
        {
            if(isNetworkConnected(HelperDetailsVerifyActivity.this))
            {
                final ProgressDialog p2 = new ProgressDialog(HelperDetailsVerifyActivity.this);
                p2.setCancelable(false);
                p2.setMessage("Saving Rejection Reason");
                p2.show();
                String saveRejectionReasonUrl = "https://aproject2018.000webhostapp.com/loginFiles/supervisor/saverejectionreason.php?helperuserid="+helperid+"&rejectionreason="+rejectionReason;
                StringRequest saveRejectionReasonRequest = new StringRequest(Request.Method.GET, saveRejectionReasonUrl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        p2.dismiss();
                        saveRejectionReasonParseJSON(response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        p2.dismiss();
                        if(isNetworkConnected (HelperDetailsVerifyActivity.this))
                        {
                            Toast.makeText(HelperDetailsVerifyActivity.this,"Sorry,Check your Internet Connection",Toast.LENGTH_LONG).show();
                        }
                    }
                });
                RequestQueue requestQueue = Volley.newRequestQueue(HelperDetailsVerifyActivity.this);
                requestQueue.add(saveRejectionReasonRequest);
            }
            else if(!isNetworkConnected(HelperDetailsVerifyActivity.this))
            {
                Toast.makeText(HelperDetailsVerifyActivity.this,"Check your Internet Connection, Please",Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void saveRejectionReasonParseJSON(String response) {
        try {
            JSONObject mainObject = new JSONObject(response);
            JSONArray records = mainObject.getJSONArray("data");
            JSONObject jsonObject = records.getJSONObject(0);
            String saveStatus = jsonObject.getString("done");
            if(saveStatus.equals("true"))
            {
                Toast.makeText(HelperDetailsVerifyActivity.this,"Okay, Rejection reason Saved",Toast.LENGTH_SHORT).show();
                updateVerificationStatus("rejected");

            }
            else if(saveStatus.equals("duplicateid"))
            {
                Toast.makeText(HelperDetailsVerifyActivity.this,"Okay, Rejection reason already saved for this helper",Toast.LENGTH_SHORT).show();
            }
            else if(saveStatus.equals("false"))
            {
                Toast.makeText(HelperDetailsVerifyActivity.this,"Error while saving Rejection Reason",Toast.LENGTH_SHORT).show();
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void rejectHelper() {
            rejectReasonDialog.show();
    }

    private void reviewHelper() {
        if(isNetworkConnected(HelperDetailsVerifyActivity.this))
        {
            String verificationStatus = null;
            //avatarSwitch,nameSwitch,idProofSwitch
            if(idProofSwitch.isChecked())
            {
                if(nameSwitch.isChecked() || avatarSwitch.isChecked())
                {
                    verificationStatus="AVATAR_ID_REVIEW";
                }
                else
                {
                    verificationStatus="ID_REVIEW";
                }
            }
            else if(nameSwitch.isChecked() || avatarSwitch.isChecked())
            {
                verificationStatus="AVATAR_REVIEW";
            }
            updateVerificationStatus(verificationStatus);
        }
        else if(!isNetworkConnected(HelperDetailsVerifyActivity.this))
        {
            Toast.makeText(HelperDetailsVerifyActivity.this,"Check your Internet Connection, Please",Toast.LENGTH_SHORT).show();
        }
    }

    private void verifyHelper() {
        if(!nameSwitch.isChecked() || !avatarSwitch.isChecked() || !idProofSwitch.isChecked())
        {
            AlertDialog.Builder markAllCorrectDialogBuilder= new AlertDialog.Builder(HelperDetailsVerifyActivity.this);
            markAllCorrectDialogBuilder.setMessage(R.string.mark_all_fields_correct);
            markAllCorrectDialogBuilder.setPositiveButton(R.string.okay, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                markAllCorrectDialog.dismiss();
                }
            });
            markAllCorrectDialog=markAllCorrectDialogBuilder.create();
            markAllCorrectDialog.show();
        }
        else
        {
            if(isNetworkConnected(HelperDetailsVerifyActivity.this))
            {
                updateVerificationStatus("verified");

            }
            else if(!isNetworkConnected(HelperDetailsVerifyActivity.this))
            {
                Toast.makeText(HelperDetailsVerifyActivity.this,"Check your Internet Connection, Please",Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void updateVerificationStatus(String verificationStatusValue)
    {
        final ProgressDialog p = new ProgressDialog(HelperDetailsVerifyActivity.this);
        p.setCancelable(false);
        if(verificationStatusValue.equals("verified"))
        {
            p.setMessage("Marking Helper Verified");
        }
        else
        {
            p.setMessage("Updating Verification Status");
        }
        p.show();
        String updateVerificationStatus = "http://aproject2018.000webhostapp.com/loginFiles/supervisor/updateverificationstatus.php?helperuserid="+helperid+"&verificationstatus="+verificationStatusValue;
        StringRequest verificationStatusUpdateRequest = new StringRequest(Request.Method.GET, updateVerificationStatus, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                p.dismiss();
                verificationStatusParseJSON(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                p.dismiss();
                if(isNetworkConnected (HelperDetailsVerifyActivity.this))
                {
                    Toast.makeText(HelperDetailsVerifyActivity.this,"Sorry,Check your Internet Connection",Toast.LENGTH_LONG).show();
                }
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(HelperDetailsVerifyActivity.this);
        requestQueue.add(verificationStatusUpdateRequest);
    }


    private void verificationStatusParseJSON(String response) {
        try {
            JSONObject mainObject = new JSONObject(response);
            JSONArray records = mainObject.getJSONArray("data");
            JSONObject jsonObject = records.getJSONObject(0);
            String updatestatus = jsonObject.getString("done");
            final Snackbar snackbar;
            if(updatestatus.equals("true"))
            {
                Toast.makeText(HelperDetailsVerifyActivity.this,"Verification Status Updated",Toast.LENGTH_SHORT).show();
                finish();  //Close this Verifying Helper Activity
                //refresh list view
            }
            else if(updatestatus.equals("false"))
            {
                Toast.makeText(HelperDetailsVerifyActivity.this,"Verification Status Not Updated",Toast.LENGTH_SHORT).show();
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setGreenBoundary(View view)
    {
        if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackgroundDrawable( getResources().getDrawable(R.drawable.greenrect_viewborder) );
        } else {
            view.setBackground( getResources().getDrawable(R.drawable.greenrect_viewborder));
        }
    }

    private void setRedBoundary(View view)
    {
        if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackgroundDrawable( getResources().getDrawable(R.drawable.redrect_viewborder) );
        } else {
            view.setBackground( getResources().getDrawable(R.drawable.redrect_viewborder));
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
