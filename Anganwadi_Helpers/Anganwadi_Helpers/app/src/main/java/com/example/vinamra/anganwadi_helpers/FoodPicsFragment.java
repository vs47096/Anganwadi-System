package com.example.vinamra.anganwadi_helpers;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import is.arontibo.library.ElasticDownloadView;

/**
 * Created by Vinamra on 3/29/2018.
 */

public class FoodPicsFragment extends Fragment {
    private ViewPager viewPager;
    private int picNumber=1;
    private Uri[] uplaodFileURI;
    private Uri[] downloadUrl;
    private ImageButton uploadStockBillBtn;
    private static final int REQUEST_CAMERA =1234 ;
    private String[] mCurrentPhotoPath;
    private String TAG="FoodPicsFragment";
    private String stockPicsImageName1=null,stockPicsImageName2=null;
    private View alertLayoutForIdProofView;
    private AlertDialog.Builder idProofViewAlertBuild;
    private AlertDialog idProofViewAlert;
    private FloatingActionButton idPhotoCorrectBtn;
    private FloatingActionButton idPhotoCrossBtn;
    private Bitmap[] imageBitmap;
    private ImageView alertDialogImageView;
    private StorageReference mStorageRef;
    private String[] imageFileName;
    private ActionProcessButton proceedUploadingStockItemsPicsButton;
    private String[] saveStockPicsLinkUrl;
    private FoodRecordActivity parentActivity;
    private ProgressDialog progressDialog;
    private String date=null,time=null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_stockitemspicupload,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewPager = (ViewPager) view.findViewById(R.id.stockItemsPicViewPager);
        StockItemsPicsAdapter adapter = new StockItemsPicsAdapter(getActivity().getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        // Create a storage reference from our app
        mStorageRef = FirebaseStorage.getInstance().getReference();

        parentActivity= (FoodRecordActivity) getActivity();
        date=parentActivity.getDate();
        time=parentActivity.getTime();

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Saving Data To database");
        progressDialog.setCancelable(false);

        /**************************Initializing FileName*************************/
        stockPicsImageName1="meal_pic1_"+date+"_"+time;
        stockPicsImageName2="meal_pic2_"+date+"_"+time;
        /***********************Initializing FileName End***********************/

        /**************************Initializing Arrays*************************/
        uplaodFileURI=new Uri[2];
        mCurrentPhotoPath=new String[2];
        imageBitmap=new Bitmap[2];
        imageFileName=new String[2];
        downloadUrl=new Uri[2];
        saveStockPicsLinkUrl=new String[2];

        /***********************Initializing Arrays End***********************/

        uploadStockBillBtn=(ImageButton) view.findViewById(R.id.clickStockItemsPicsBtn);
        uploadStockBillBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    checkPermissionsAndExecute(getContext(),2222);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        proceedUploadingStockItemsPicsButton=(ActionProcessButton) view.findViewById(R.id.proceedUploadingStockItemsPicsButton);
        proceedUploadingStockItemsPicsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPhotoAndUpload();
            }
        });

        alertLayoutForIdProofView=getActivity().getLayoutInflater().inflate(R.layout.alertdialoglayoutforidproofview,null);
        alertDialogImageView=(ImageView) alertLayoutForIdProofView.findViewById(R.id.alertDialogImageView);
        idProofViewAlertBuild =new AlertDialog.Builder(getContext());
        idProofViewAlertBuild.setView(alertLayoutForIdProofView);
        idProofViewAlertBuild.setCancelable(false);
        idProofViewAlert=idProofViewAlertBuild.create();
        idPhotoCorrectBtn= (FloatingActionButton) alertLayoutForIdProofView.findViewById(R.id.idPhotoCorrectBtn);
        idPhotoCorrectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalizePhoto();
            }
        });
        idPhotoCrossBtn= (FloatingActionButton) alertLayoutForIdProofView.findViewById(R.id.idPhotoCrossBtn);
        idPhotoCrossBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                idProofViewAlert.dismiss();
            }
        });
    }

    /**************************************Function for Checking Permission and Executing Code*********************************/
    private void checkPermissionsAndExecute(Context context, int code) throws IOException {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (code==2222)
            {
                if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                        || ActivityCompat.checkSelfPermission(context, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                {
                    String[] permissions = {android.Manifest.permission.CAMERA, android.Manifest.permission.READ_EXTERNAL_STORAGE};
                    requestPermissions(permissions, code);
                }
                else if(ActivityCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(context, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                {
                    cameraIntent();
                }
                else
                {
                    Toast.makeText(getContext(),"Some permission for camera may be missing",Toast.LENGTH_LONG);
                }
            }
        }
        else
        {
            if(code==2222)
            {
                cameraIntent();
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==2222)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED)
            {
                try
                {
                    cameraIntent();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            else
            {
                Toast.makeText(getContext(),"Permissions required for USING CAMERA are denied",Toast.LENGTH_LONG).show();
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    /**************************************************Function End*************************************************************/


    /************************************************Function for Camera Intent*************************************************/
    private void cameraIntent() throws IOException
    {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null)
        {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            }
            catch (IOException ex) {
                // Error occurred while creating the File
                return;
            }
            // Continue only if the File was successfully created
            if (photoFile != null)
            {
                Uri photoURI = FileProvider.getUriForFile(getActivity(), BuildConfig.APPLICATION_ID + ".provider", createImageFile());
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_CAMERA);
            }
        }
    }
    /**************************************************Function End*************************************************************/

    /**********************************************Function for Handling Intent Result*****************************************/
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK)
        {
            if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult();
        }
    }
    /**************************************************Function End*************************************************************/

    /**********************************************Function for Handling Image from Camera*****************************************/
    private void onCaptureImageResult() {
        //Bitmap bm = BitmapFactory.decodeFile(mCurrentPhotoPath);
        if(picNumber==1)
        {
            imageBitmap[0] = BitmapFactory.decodeFile(mCurrentPhotoPath[0]);
            uplaodFileURI[0] = Uri.fromFile(new File(mCurrentPhotoPath[0]));
            alertDialogImageView.setImageBitmap(imageBitmap[0]);
        }
        else if(picNumber==2)
        {
            imageBitmap[1] = BitmapFactory.decodeFile(mCurrentPhotoPath[1]);
            uplaodFileURI[1] = Uri.fromFile(new File(mCurrentPhotoPath[1]));
            alertDialogImageView.setImageBitmap(imageBitmap[1]);
        }
        idProofViewAlert.show();
    }
    /**************************************************Function End*************************************************************/

    /**************************************Function for creating File for storing Image******************************************/
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName=null;
        if(picNumber==1)
        {

            imageFileName = "meal_pic1_"+ timeStamp;
        }
        else  if(picNumber==2)
        {
            imageFileName = "meal_pic2_"+ timeStamp;
        }
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        // Save a file: path for use with ACTION_VIEW intents
        if(picNumber==1)
        {
            mCurrentPhotoPath[0] = image.getAbsolutePath();
        }
        else if(picNumber==2)
        {
            mCurrentPhotoPath[1] = image.getAbsolutePath();
        }
        return image;
    }
    /**************************************************Function End*************************************************************/

    /*****************************************Function for Adding Image to ViewPager******************************************/
    private void finalizePhoto() {
        if(picNumber==1)
        {
            imageBitmap[0] = BitmapFactory.decodeFile(mCurrentPhotoPath[0]);
            ImageViewFragment fragment=(ImageViewFragment)getActivity().getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.stockItemsPicViewPager + ":" + 0);
            ImageView im=(ImageView) fragment.getView().findViewById(R.id.fragmentImageView);
            im.setImageBitmap(imageBitmap[0]);
        }
        else if(picNumber==2)
        {
            imageBitmap[1] = BitmapFactory.decodeFile(mCurrentPhotoPath[1]);
            ImageViewFragment fragment=(ImageViewFragment)getActivity().getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.stockItemsPicViewPager + ":" + 1);
            ImageView im=(ImageView) fragment.getView().findViewById(R.id.fragmentImageView);
            im.setImageBitmap(imageBitmap[1]);
        }
        ++picNumber;
        idProofViewAlert.dismiss();
    }
    /**************************************************Function End*************************************************************/

    /**************************************Function for Checking name and Photo and Upload******************************************/
    private void checkPhotoAndUpload() {
        final int[] imageUploadedCounter = new int[1];
        Log.d(TAG,"All fine");
        if(isNetworkConnected (getActivity()))
        {
            imageUploadedCounter[0] =0;
            final StorageMetadata metadata = new StorageMetadata.Builder()
                    .setContentType("image/jpeg")
                    .build();
            final UploadTask[] uploadTask=new UploadTask[2];
            uploadTask[0] = mStorageRef.child("Anganwadi_Helper_images/meal_pics/"+ stockPicsImageName1 +".jpg").putFile(uplaodFileURI[0], metadata);

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
                    // Handle unsuccessful uploads
                    et.fail();
                    doneFabBtn.setVisibility(View.VISIBLE);

                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    downloadUrl[0] = taskSnapshot.getMetadata().getDownloadUrl();
                    et.setProgress(50.00f);
                    ++imageUploadedCounter[0];
                    uploadTask[1] = mStorageRef.child("Anganwadi_Helper_images/meal_pics/"+ stockPicsImageName2 +".jpg").putFile(uplaodFileURI[1], metadata);
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
                            saveStockItemsPicsUrlToDatabase();
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

    /**************************************************Function End*************************************************************/

    /**************************************Function for Saving Stock Item Pics to Database******************************************/
    private void saveStockItemsPicsUrlToDatabase() {

        if(isNetworkConnected(getContext()))
        {
            progressDialog.show();
            /*Push data to mysql database with avatar url specfying helper`s id*/
            saveStockPicsLinkUrl[0]="http://aproject2018.000webhostapp.com/helpertask/saveMealPicsUrl.php?date="+date+"&time="+time+"&filenumber=pic1&fileurl="+downloadUrl[0];
            StringRequest request = new StringRequest(Request.Method.GET, saveStockPicsLinkUrl[0], new Response.Listener<String>() {
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
                saveStockPicsLinkUrl[1]="http://aproject2018.000webhostapp.com/helpertask/saveMealPicsUrl.php?date="+date+"&time="+time+"&filenumber=pic2&fileurl="+downloadUrl[1];
                StringRequest request = new StringRequest(Request.Method.GET, saveStockPicsLinkUrl[1], new Response.Listener<String>() {
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
                Toast.makeText(getActivity(),"Pics URL saved successfully",Toast.LENGTH_SHORT).show();
                getActivity().finish();

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
